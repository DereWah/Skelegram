package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramMessageSender extends SimplePropertyExpression<Message, User> {

    static {
        register(ExprTelegramMessageSender.class, User.class, "sender", "telegrammessage");
    }

    @Override
    public User convert(Message message) { return message.getFrom(); }

    @Override
    public Class<? extends User> getReturnType() {
        return User.class;
    }

    @Override
    protected String getPropertyName() {
        return "sender";
    }
}
