package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramChatType extends SimplePropertyExpression<Chat, String> {

    static {
        register(ExprTelegramChatType.class, String.class, "type", "telegramchat");
    }


    @Override
    protected String getPropertyName() {
        return "type";
    }

    @Override
    public String convert(Chat chat){
        return chat.getType();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
