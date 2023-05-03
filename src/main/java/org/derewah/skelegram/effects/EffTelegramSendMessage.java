package org.derewah.skelegram.effects;


import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static ch.njol.skript.Skript.registerEffect;


public class EffTelegramSendMessage extends AsyncEffect {

    static  {
        registerEffect(EffTelegramSendMessage.class,
                "send telegram message %string/telegrammessage% to %telegramuser/telegramchat/number%");
    }

    private Expression<Object> exprtarget;
    private Expression<Object> message;







    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        message = (Expression<Object>) expr[0];
        exprtarget = (Expression<Object>) expr[1];
        return true;
    }


    @Override
    protected void execute(Event event){
        if (message.getSingle(event) != null && event instanceof BridgeTelegramUpdateMessage){
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
                if (mess.getReplyToMessage() != null) {sendMessage.setReplyToMessageId(mess.getReplyToMessage().getMessageId());}
                sendMessage.setEntities(mess.getEntities());
                sendMessage.setReplyMarkup(mess.getReplyMarkup());
            }
            try {
                ((BridgeTelegramUpdateMessage) event).getClient().executeAsync(sendMessage);
            } catch (TelegramApiException e) {
                Skript.error(e.toString());
            }
        } else{
            Skript.error("The Send Telegram Message effect can only be used inside of a Telegram event!");
        }

    }






    public String toString(Event e, boolean debug) {
        return "telegram message " + message.toString(e, debug) + "to user " + exprtarget.toString(e, debug);
    }

}
