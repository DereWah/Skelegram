package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.derewah.skelegram.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramUsername extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprTelegramUsername.class, String.class, "username", "telegramuser/telegramchat/telegrambot");
    }

    @Override
    public String convert(Object object) {
        if(object instanceof User) {
            return ((User) object).getUserName();
        } else if(object instanceof Chat) {
            return ((Chat) object).getUserName();
        } else if(object instanceof TelegramBot){
            return ((TelegramBot) object).getBotUsername();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "username";
    }
}
