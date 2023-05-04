package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramSender extends SimplePropertyExpression<Object, User> {

    static {
        register(ExprTelegramSender.class, User.class, "sender", "telegrammessage/callbackquery");
    }

    @Override
    public User convert(Object object) {
        if(object instanceof Message){
            return ((Message) object).getFrom();
        }else if(object instanceof CallbackQuery){
            return ((CallbackQuery) object).getFrom();
        }
        return null;
    }

    @Override
    public Class<? extends User> getReturnType() {
        return User.class;
    }

    @Override
    protected String getPropertyName() {
        return "sender";
    }
}
