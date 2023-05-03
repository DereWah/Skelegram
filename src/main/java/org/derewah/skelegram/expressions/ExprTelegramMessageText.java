package org.derewah.skelegram.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramMessageText extends SimplePropertyExpression<Message, String> {

    static {
        register(ExprTelegramMessageText.class, String.class, "text", "telegrammessage");
    }

    @Override
    public String convert(Message message) {
        String text = message.hasText() == true ? message.getText() : null;
        return text;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode){
        if (mode == Changer.ChangeMode.SET) {return CollectionUtils.array(String.class);}
        else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET){return CollectionUtils.array(null);}
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if((delta == null  && mode != Changer.ChangeMode.RESET && mode != Changer.ChangeMode.DELETE) || (delta != null && delta.length == 0)){
            return;
        }
        Message mess = getExpr().getSingle(e);
        if(mess != null){
            switch (mode) {
                case SET:
                    String[] result = ((String[]) delta);
                    String merged = String.join(",", result);
                    mess.setText(merged);
                case RESET:
                    mess.setText(null);
                case DELETE:
                    mess.setText(null);
            }
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "text";
    }
}
