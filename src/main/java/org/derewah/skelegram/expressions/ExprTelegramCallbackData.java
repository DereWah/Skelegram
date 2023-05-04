package org.derewah.skelegram.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import ch.njol.skript.classes.Changer.ChangeMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExprTelegramCallbackData extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprTelegramCallbackData.class, String.class, "callback data", "inlinebutton/callbackquery");
    }

    @Override
    public String convert(Object object) {
        if(object instanceof InlineKeyboardButton) {
            return ((InlineKeyboardButton) object).getCallbackData();
        }
        else if(object instanceof CallbackQuery){
            return ((CallbackQuery) object).getData();
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode){
        switch(mode){
            case SET:
                return CollectionUtils.array(String.class);
        }
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        if(delta == null || delta.length == 0){
            return;
        }
        Object obj = getExpr().getSingle(event);
        if (obj instanceof InlineKeyboardButton){
            ((InlineKeyboardButton) obj).setCallbackData(String.join(" ", (String[]) delta));
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "callback data";
    }
}
