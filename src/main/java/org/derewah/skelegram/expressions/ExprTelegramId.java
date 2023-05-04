package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramId extends SimplePropertyExpression<Object, Number> {

    static {
        register(ExprTelegramId.class, Number.class, "id", "telegramuser/telegramchat/telegrammessage/callbackquery");
    }

    @Override
    public Number convert(Object object) {
        if (object instanceof User) {
            return ((User) object).getId();
        } else if (object instanceof Chat) {
            return ((Chat) object).getId();
        } else if (object instanceof Message) {
            return ((Message) object).getMessageId();
        } else if (object instanceof CallbackQuery){
            return Integer.valueOf(((CallbackQuery) object).getId());
        }else{
            return null;
        }
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "id";
    }
}
