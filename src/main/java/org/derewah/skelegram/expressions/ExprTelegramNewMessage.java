package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramNewMessage extends SimpleExpression<Message> {

    static {
        Skript.registerExpression(ExprTelegramNewMessage.class, Message.class, ExpressionType.SIMPLE, "[[a] new] telegram message with text %string%");
    }

    private Expression<String> text;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        text = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected Message[] get(Event event) {
        Message message = new Message();
        message.setText(text.getSingle(event));
        return new Message[]{message};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Message> getReturnType() {
        return Message.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "new telegram message with text " + text.toString(event, debug);
    }

}
