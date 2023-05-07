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
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.concurrent.CompletableFuture;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramSendMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramSendMessage.class,
                "send telegram message %string/telegrammessage% to %telegramuser/telegramchat/number% [with bot %-string%]");
    }

    private Expression<Object> exprTarget;
    private Expression<Object> message;
    private Expression<String> exprBotUser;

    private boolean specifyBot = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        message = (Expression<Object>) expr[0];
        exprTarget = (Expression<Object>) expr[1];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Send Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            exprBotUser = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message.getSingle(event) != null){

            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode("MARKDOWN");
            if(exprTarget.getSingle(event) instanceof User){
                sendMessage.setChatId(((User) exprTarget.getSingle(event)).getId());
            }else if (exprTarget.getSingle(event) instanceof Chat){
                sendMessage.setChatId(((Chat) exprTarget.getSingle(event)).getId());
            }else if (exprTarget.getSingle(event) instanceof Number){
                sendMessage.setChatId(((Number) exprTarget.getSingle(event)).longValue());
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
            String botUser = null;
            if (specifyBot) {
                botUser = exprBotUser.getSingle(event);
            } else if (event instanceof BridgeTelegramUpdateMessage) {
                botUser = ((BridgeTelegramUpdateMessage) event).getClient().getBotUsername();
            } else if (event instanceof BridgeTelegramUpdateCallbackQuery) {
                botUser = ((BridgeTelegramUpdateCallbackQuery) event).getClient().getBotUsername();
            }

            if (botUser != null) {
                try {
                    CompletableFuture<Message> sent = Skelegram.getInstance().getTelegramSessions().getBot(botUser).executeAsync(sendMessage);
                    Skelegram.getInstance().getTelegramSessions().getBot(botUser).lastSent = sent.get();
                } catch (Exception e) {
                    Skript.error("Error sending message: " + e.getMessage());
                }
            } else {
                Skript.error("Could not find the bot to use. If outside of a telegram event, did you specify the username of the bot?");
            }
        } else{
            Skript.error("An empty message object can't be sent.");
        }

    }

    public String toString(Event event, boolean debug) {
        return "telegram send message " + message.toString(event, debug) + " to user " + exprTarget.toString(event, debug);
    }

}
