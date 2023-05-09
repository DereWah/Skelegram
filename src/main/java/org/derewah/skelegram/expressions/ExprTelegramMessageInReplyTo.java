package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramMessageInReplyTo extends SimplePropertyExpression<Object, Message> {

    static {
        register(ExprTelegramMessageInReplyTo.class, Message.class, "replied message", "telegrammessage");
    }

    @Override
    public Message convert(Object object) {
        if(object instanceof Message) {
            return ((Message) object).getReplyToMessage();
        }
        return null;
    }

    @Override
    public Class<? extends Message> getReturnType() {
        return Message.class;
    }

    @Override
    protected String getPropertyName() {
        return "replied message";
    }
}
