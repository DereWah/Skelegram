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
import org.telegram.telegrambots.meta.api.objects.Message;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramReplyMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramReplyMessage.class,
                "reply to telegram message %telegrammessage% with %telegrammessage/string% [with bot %-string%]",
                "telegram reply to %telegrammessage% with %telegrammessage/string% [with bot %-string%]");
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
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class) && !specifyBot){
            Skript.error("You're using the Reply Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            botName = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message != null && replyTo != null){
            SendMessage replyMessage = new SendMessage();
            replyMessage.setChatId(replyTo.getSingle(event).getChatId());
            if(message.getSingle(event) instanceof String) {
                replyMessage.setText((String) message.getSingle(event));
            }else if (message.getSingle(event) instanceof Message){
                replyMessage.setReplyToMessageId(replyTo.getSingle(event).getMessageId());
                Message mess = (Message) message.getSingle(event);
                replyMessage.setText(mess.getText());
                replyMessage.setMessageThreadId(mess.getMessageThreadId());

                replyMessage.setEntities(mess.getEntities());
                replyMessage.setReplyMarkup(mess.getReplyMarkup());
            }
            try {
                if (!specifyBot){
                    if (event instanceof BridgeTelegramUpdateMessage) {
                        ((BridgeTelegramUpdateMessage) event).getClient().executeAsync(replyMessage);
                    } else{
                        Skript.error("You're using the Send Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
                    }
                }else {
                    if (Skelegram.getInstance().getTelegramSessions().getBot(botName.getSingle(event)) != null) {
                        Skelegram.getInstance().getTelegramSessions().getBot(botName.getSingle(event)).executeAsync(replyMessage);
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
