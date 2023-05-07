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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramEditMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramEditMessage.class,
                "edit telegram message %telegrammessage% to %telegrammessage/string% [with bot %-string%]");
    }

    private Expression<Message> originalMessage;
    private Expression<Object> message;
    private Expression<String> exprBotUser;
    private boolean specifyBot = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        originalMessage = (Expression<Message>) expr[0];
        message = (Expression<Object>) expr[1];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Edit Telegram Message effect outside of a Telegram event. Specify the username of the bot you are editing a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            exprBotUser = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message != null && !message.getSingle(event).equals("") && originalMessage.getSingle(event) != null){
            EditMessageText editMessage = new EditMessageText();
            editMessage.setParseMode("MARKDOWN");
            if(message.getSingle(event) instanceof String) {
                editMessage.setText((String) message.getSingle(event));
            }else if (message.getSingle(event) instanceof Message){
                Message mess = (Message) message.getSingle(event);
                editMessage.setText(mess.getText());
                editMessage.setEntities(mess.getEntities());
                editMessage.setReplyMarkup(mess.getReplyMarkup());
            }
            editMessage.setChatId(originalMessage.getSingle(event).getChatId());
            editMessage.setMessageId(originalMessage.getSingle(event).getMessageId());


            String botUser = null;
            if(specifyBot){
                botUser = exprBotUser.getSingle(event);
            }else if(event instanceof BridgeTelegramUpdateMessage){
                botUser = ((BridgeTelegramUpdateMessage) event).getClient().getBotUsername();
            }else if(event instanceof BridgeTelegramUpdateCallbackQuery){
                ((BridgeTelegramUpdateCallbackQuery) event).getClient().getBotUsername();
            }
            if (botUser != null) {
                try {
                    Skelegram.getInstance().getTelegramSessions().getBot(botUser).executeAsync(editMessage);
                } catch (Exception e) {
                    Skript.error("Error editing message: " + e.getMessage());
                }
            } else {
                Skript.error("Could not find the bot to use. If outside of a telegram event, did you specify the username of the bot?");
            }
        } else{
            Skript.error("An empty message object can't be sent.");
        }

    }

    public String toString(Event event, boolean debug) {
        return "telegram reply " + message.toString(event, debug) + "to message " + originalMessage.toString(event, debug);
    }

}
