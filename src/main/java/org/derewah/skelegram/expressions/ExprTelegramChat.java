package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramChat extends SimplePropertyExpression<Object, Chat> {

    static {
        register(ExprTelegramChat.class, Chat.class, "chat", "telegrammessage");
    }

    @Override
    public Chat convert(Object object) {
        if (object instanceof Message){
            return ((Message) object).getChat();
        }
        return null;
    }

    @Override
    public Class<? extends Chat> getReturnType() {
        return Chat.class;
    }

    @Override
    protected String getPropertyName() {
        return "chat";
    }
}
