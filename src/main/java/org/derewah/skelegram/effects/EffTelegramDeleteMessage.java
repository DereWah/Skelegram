package org.derewah.skelegram.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateCallbackQuery;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramDeleteMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramDeleteMessage.class,
                "delete telegram message %telegrammessage% [with bot %-string%]");
    }

    private Expression<Message> message;
    private Expression<String> exprBotUser;
    private boolean specifyBot = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        message = (Expression<Message>) expr[0];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Delete Telegram Message effect outside of a Telegram event. Specify the username of the bot you are deleting a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            exprBotUser = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message != null && !message.getSingle(event).equals("")){
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(message.getSingle(event).getMessageId());
            deleteMessage.setChatId(message.getSingle(event).getChatId());
            String botUser = null;
            if(specifyBot){
                botUser = exprBotUser.getSingle(event);
            }else if(event instanceof BridgeTelegramUpdateMessage){
                botUser = ((BridgeTelegramUpdateMessage) event).getClient().getBotUsername();
            }else if(event instanceof BridgeTelegramUpdateCallbackQuery){
                botUser = ((BridgeTelegramUpdateCallbackQuery) event).getClient().getBotUsername();
            }
            if (botUser != null) {
                try {
                    Skelegram.getInstance().getTelegramSessions().getBot(botUser).executeAsync(deleteMessage);
                } catch (Exception e) {
                    Skript.error("Error deleting message: " + e.getMessage());
                    Skript.error(e.toString());
                }
            } else {
                Skript.error("Could not find the bot to use. If outside of a telegram event, did you specify the username of the bot?");
            }
        } else{
            Skript.error("An empty message object can't be deleted.");
        }
    }

    public String toString(Event event, boolean debug) {
        return "telegram delete " + message.toString(event, debug);
    }
}
