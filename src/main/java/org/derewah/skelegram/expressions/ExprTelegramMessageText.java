package org.derewah.skelegram.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExprTelegramMessageText extends SimplePropertyExpression<Message, String> {

    static {
        register(ExprTelegramMessageText.class, String.class, "text", "telegrammessage");
    }


    @Override
    protected String getPropertyName() {
        return "text";
    }

    @Override
    public String convert(Message message) {
        String text = message.hasText() == true ? message.getText() : null;
        return text;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
