package org.derewah.skelegram.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExprTelegramInlineButtonUrl extends SimplePropertyExpression<InlineKeyboardButton, String> {

    static {
        register(ExprTelegramInlineButtonUrl.class, String.class, "url", "inlinebutton");
    }

    @Override
    public String convert(InlineKeyboardButton button) {
        return button.getUrl();
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode){
        if (mode == Changer.ChangeMode.SET) {return CollectionUtils.array(String.class);}
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if(delta == null || delta.length == 0){
            return;
        }
        InlineKeyboardButton button = getExpr().getSingle(e);
        if (button != null){
            button.setUrl(String.join(" ", (String[]) delta));
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    
    @Override
    protected String getPropertyName() {
        return "url";
    }
}
