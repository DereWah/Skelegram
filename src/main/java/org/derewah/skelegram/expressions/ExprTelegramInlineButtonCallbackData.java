package org.derewah.skelegram.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExprTelegramInlineButtonCallbackData extends SimplePropertyExpression<InlineKeyboardButton, String> {

    static {
        register(ExprTelegramInlineButtonCallbackData.class, String.class, "callback data", "inlinebutton");
    }

    @Override
    public String convert(InlineKeyboardButton button) {
        return button.getCallbackData();
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode){
        if (mode == Changer.ChangeMode.SET) {return CollectionUtils.array(String.class);}
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        if(delta == null || delta.length == 0){
            return;
        }
        InlineKeyboardButton button = getExpr().getSingle(event);
        if (button != null){
            button.setCallbackData(String.join(" ", (String[]) delta));
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
