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
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageId;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.concurrent.CompletableFuture;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramCopyMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramCopyMessage.class,
                "copy telegram message %telegrammessage% to %telegramuser/telegramchat/number% [with bot %-string%]");
    }

    private Expression<Object> exprtarget;
    private Expression<Message> message;
    private Expression<String> exprBotUser;
    private boolean specifyBot = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        message = (Expression<Message>) expr[0];
        exprtarget = (Expression<Object>) expr[1];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Copy Telegram Message effect outside of a Telegram event. Specify the username of the bot you are copying a message from to use this effect here.");
            return false;
        }
        if (specifyBot){
            exprBotUser = (Expression<String>) expr[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message.getSingle(event) != null) {

            CopyMessage copyMessage = new CopyMessage();
            if (exprtarget.getSingle(event) instanceof User) {
                copyMessage.setChatId(((User) exprtarget.getSingle(event)).getId());
            } else if (exprtarget.getSingle(event) instanceof Chat) {
                copyMessage.setChatId(((Chat) exprtarget.getSingle(event)).getId());
            } else if (exprtarget.getSingle(event) instanceof Number) {
                copyMessage.setChatId(((Number) exprtarget.getSingle(event)).longValue());
            }

            if (message != null) {
                Message mess = (Message) message.getSingle(event);
                copyMessage.setFromChatId(mess.getChatId());
                copyMessage.setMessageId(mess.getMessageId());
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
                    CompletableFuture<MessageId> sent = Skelegram.getInstance().getTelegramSessions().getBot(botUser).executeAsync(copyMessage);
                } catch (Exception e) {
                    Skript.error("Error copying message: " + e.getMessage());
                    Skript.error(e.toString());
                }
            } else {
                Skript.error("Could not find the bot to use. If outside of a telegram event, did you specify the username of the bot?");
            }
        }
    }

    public String toString(Event event, boolean debug) {
        return "telegram copy message " + message.toString(event, debug) + " to user " + exprtarget.toString(event, debug);
    }

}
