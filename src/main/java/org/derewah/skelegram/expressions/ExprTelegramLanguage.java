package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramLanguage extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprTelegramLanguage.class, String.class, "language", "telegramuser");
    }

    @Override
    public String convert(Object object) {
        if(object instanceof User) {
            return ((User) object).getLanguageCode();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "language";
    }
}
