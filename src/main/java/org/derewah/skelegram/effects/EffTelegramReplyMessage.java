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
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.CompletableFuture;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramReplyMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramReplyMessage.class,
                "reply to telegram message %telegrammessage% with %telegrammessage/string% [with bot %-string%]",
                "reply to telegram message %telegrammessage% with %telegrammessage/string% with markdown [with bot %-string%]");
    }

    private Expression<Message> replyTo;
    private Expression<Object> message;
    private Expression<String> exprBotUser;
    private boolean specifyBot = false;
    private boolean markdown = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        replyTo = (Expression<Message>) expr[0];
        message = (Expression<Object>) expr[1];
        specifyBot = parseResult.expr.contains(" with bot ");
        markdown = matchedPattern == 1;
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Reply Telegram Message effect outside of a Telegram event. Specify the username of the bot you are sending a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            exprBotUser = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message != null && replyTo != null && !message.getSingle(event).equals("")){
            SendMessage replyMessage = new SendMessage();
            if(markdown) {
                replyMessage.setParseMode("MARKDOWN");
            }
            if(message.getSingle(event) instanceof String) {
                replyMessage.setText((String) message.getSingle(event));
                replyMessage.setChatId(replyTo.getSingle(event).getChatId().toString());
            }else if (message.getSingle(event) instanceof Message){
                Message mess = (Message) message.getSingle(event);
                replyMessage.setChatId(replyTo.getSingle(event).getChatId().toString());
                replyMessage.setText(mess.getText());
                replyMessage.setMessageThreadId(mess.getMessageThreadId());
                replyMessage.setEntities(mess.getEntities());
                replyMessage.setReplyMarkup(mess.getReplyMarkup());
            }
            replyMessage.setReplyToMessageId(replyTo.getSingle(event).getMessageId());


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
                    CompletableFuture<Message> sent = Skelegram.getInstance().getTelegramSessions().getBot(botUser).executeAsync(replyMessage);
                    Skelegram.getInstance().getTelegramSessions().getBot(botUser).lastSent = sent.get();
                } catch (Exception e) {
                    Skript.error("Error sending message: " + e.getMessage());
                    Skript.error(e.toString());
                }
            } else {
                Skript.error("Could not find the bot to use. If outside of a telegram event, did you specify the username of the bot?");
            }
        } else{
            Skript.error("An empty message object can't be sent.");
        }

    }

    public String toString(Event event, boolean debug) {
        return "telegram reply " + message.toString(event, debug) + "to message " + replyTo.toString(event, debug);
    }

}
