package org.derewah.skelegram.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExprTelegramText extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprTelegramText.class, String.class, "text", "telegrammessage/inlinebutton");
    }

    @Override
    public String convert(Object object) {
        if(object instanceof Message) {
            return ((Message) object).getText();
        }
        else if(object instanceof InlineKeyboardButton){
            return ((InlineKeyboardButton) object).getText();
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode){
        switch (mode){
            case SET:
                return CollectionUtils.array(String.class);
            case DELETE:
            case RESET:
                return CollectionUtils.array();
        }
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        if((delta == null  && mode != ChangeMode.RESET && mode != ChangeMode.DELETE) || (delta != null && delta.length == 0)){
            return;
        }

        Object obj = getExpr().getSingle(event);
        if(obj instanceof Message){
            switch (mode) {
                case RESET:
                case DELETE:
                    ((Message) obj).setText(null);
                    break;
                case SET:
                    ((Message) obj).setText((String) delta[0]);
                    break;
            }
        }else if(obj instanceof InlineKeyboardButton){
            switch (mode){
                case SET:
                    ((InlineKeyboardButton) obj).setText((String) delta[0]);
                case RESET:
                case DELETE:
                    ((InlineKeyboardButton) obj).setText(null);
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
