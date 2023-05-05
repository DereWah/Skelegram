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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramEditMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramEditMessage.class,
                "edit telegram message %telegrammessage% to %telegrammessage/string% [with bot %-string%]");
    }

    private Expression<Message> replyTo;
    private Expression<Object> message;
    private Expression<String> botName;
    private boolean specifyBot = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        replyTo = (Expression<Message>) expr[0];
        message = (Expression<Object>) expr[1];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Edit Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            botName = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message != null && replyTo != null && !message.getSingle(event).equals("")){
            EditMessageText editMessage = null;
            if(message.getSingle(event) instanceof String) {
                editMessage = new EditMessageText((String) message.getSingle(event));
            }else if (message.getSingle(event) instanceof Message){
                Message mess = (Message) message.getSingle(event);
                editMessage = new EditMessageText(mess.getText());
                editMessage.setEntities(mess.getEntities());
                editMessage.setReplyMarkup(mess.getReplyMarkup());
            }
            editMessage.setChatId(replyTo.getSingle(event).getChatId());
            editMessage.setMessageId(replyTo.getSingle(event).getMessageId());
            try {
                if (!specifyBot){
                    if (event instanceof BridgeTelegramUpdateMessage) {
                        ((BridgeTelegramUpdateMessage) event).getClient().executeAsync(editMessage);
                    }else if(event instanceof BridgeTelegramUpdateCallbackQuery){
                        ((BridgeTelegramUpdateCallbackQuery) event).getClient().executeAsync(editMessage);
                    } else{
                        Skript.error("You're using the Send Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
                    }
                }else {
                    if (Skelegram.getInstance().getTelegramSessions().getBot(botName.getSingle(event)) != null) {
                        Skelegram.getInstance().getTelegramSessions().getBot(botName.getSingle(event)).executeAsync(editMessage);
                    }else{
                        Skript.error("Could not find a session with bot " + botName.getSingle(event) + ". Did you authenticate the bot?");
                    }

                }
            } catch (Exception e) {
                Skript.error(e.toString());
            }
        } else{
            Skript.error("An empty message object can't be sent.");
        }

    }

    public String toString(Event event, boolean debug) {
        return "telegram reply " + message.toString(event, debug) + "to message " + replyTo.toString(event, debug);
    }

}
