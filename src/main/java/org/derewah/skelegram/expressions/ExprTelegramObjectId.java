package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramObjectId extends SimplePropertyExpression<User, Number> {

    static {
        register(ExprTelegramObjectId.class, Number.class, "id", "telegramuser");
    }


    @Override
    protected String getPropertyName() {
        return "id";
    }

    @Override
    public Number convert(User user) {
        return user.getId();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
}
