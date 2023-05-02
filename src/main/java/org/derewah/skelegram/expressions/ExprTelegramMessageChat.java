package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramMessageChat extends SimplePropertyExpression<Object, Chat> {

    static {
        register(ExprTelegramMessageChat.class, Chat.class, "chat", "telegrammessage");
    }


    @Override
    protected String getPropertyName() {
        return "chat";
    }

    @Override
    public Chat convert(Object object) {
        if (object instanceof Message){
            return ((Message) object).getChat();
        }else{
            return null;
        }
    }

    @Override
    public Class<? extends Chat> getReturnType() {
        return Chat.class;
    }
}
