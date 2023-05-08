package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramFullName extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprTelegramFullName.class, String.class, "full name", "telegramuser/telegramchat");
    }

    @Override
    public String convert(Object object) {
        if(object instanceof User) {
            return ((User) object).getFirstName() + " " + ((User) object).getLastName();
        } else if(object instanceof Chat) {
            return ((Chat) object).getFirstName() + " " + ((Chat) object).getLastName();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "full name";
    }
}
