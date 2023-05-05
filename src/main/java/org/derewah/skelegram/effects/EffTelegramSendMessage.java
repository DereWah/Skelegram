package org.derewah.skelegram.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramSendMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramSendMessage.class,
                "send telegram message %string/telegrammessage% to %telegramuser/telegramchat/number% [with bot %-string%]");
    }

    private Expression<Object> exprtarget;
    private Expression<Object> message;
    private Expression<String> username;
    private boolean specifyBot = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        message = (Expression<Object>) expr[0];
        exprtarget = (Expression<Object>) expr[1];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class) && !specifyBot){
            Skript.error("You're using the Send Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            username = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message.getSingle(event) != null){

            SendMessage sendMessage = new SendMessage();
            if(exprtarget.getSingle(event) instanceof User){
                sendMessage.setChatId(((User) exprtarget.getSingle(event)).getId());
            }else if (exprtarget.getSingle(event) instanceof Chat){
                sendMessage.setChatId(((Chat) exprtarget.getSingle(event)).getId());
            }else if (exprtarget.getSingle(event) instanceof Number){
                sendMessage.setChatId(((Number) exprtarget.getSingle(event)).longValue());
            }
            if(message.getSingle(event) instanceof String) {
                sendMessage.setText((String) message.getSingle(event));
            }else if (message.getSingle(event) instanceof Message){
                Message mess = (Message) message.getSingle(event);

                sendMessage.setText(mess.getText());
                sendMessage.setMessageThreadId(mess.getMessageThreadId());
                if (mess.getReplyToMessage() != null) {
                    sendMessage.setReplyToMessageId(mess.getReplyToMessage().getMessageId());
                }
                sendMessage.setEntities(mess.getEntities());
                sendMessage.setReplyMarkup(mess.getReplyMarkup());
            }
            try {
                if (!specifyBot){
                    if (event instanceof BridgeTelegramUpdateMessage) {
                        ((BridgeTelegramUpdateMessage) event).getClient().executeAsync(sendMessage);
                    } else{
                        Skript.error("You're using the Send Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
                    }
                }else {
                    if (Skelegram.getInstance().getTelegramSessions().getBot(username.getSingle(event)) != null) {
                        Skelegram.getInstance().getTelegramSessions().getBot(username.getSingle(event)).executeAsync(sendMessage);
                    }else{
                        Skript.error("Could not find a session with bot " + username.getSingle(event) + ". Did you authenticate the bot?");
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
        return "telegram message " + message.toString(event, debug) + "to user " + exprtarget.toString(event, debug);
    }

}
