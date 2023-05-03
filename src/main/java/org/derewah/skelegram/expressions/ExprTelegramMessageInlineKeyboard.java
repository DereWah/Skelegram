package org.derewah.skelegram.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class ExprTelegramMessageInlineKeyboard extends SimplePropertyExpression<Message, InlineKeyboardMarkup> {

    static {
        register(ExprTelegramMessageInlineKeyboard.class, InlineKeyboardMarkup.class, "inline keyboard", "telegrammessage");
    }

    @Override
    public InlineKeyboardMarkup convert(Message message) {
        InlineKeyboardMarkup kb = (message.hasReplyMarkup() == true && message.getReplyMarkup() instanceof InlineKeyboardMarkup) ? message.getReplyMarkup() : null;
        return kb;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode){
        if (mode == Changer.ChangeMode.SET) {return CollectionUtils.array(InlineKeyboardMarkup.class);}
        else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET){return CollectionUtils.array(null);}
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        if((delta == null  && mode != Changer.ChangeMode.RESET && mode != Changer.ChangeMode.DELETE) || (delta.length == 0)){
            return;
        }
        Message mess = getExpr().getSingle(event);
        if(mess != null){
            switch (mode) {
                case SET:
                    InlineKeyboardMarkup kb = ((InlineKeyboardMarkup[]) delta)[0];
                    mess.setReplyMarkup(kb);
                case RESET:
                    mess.setReplyMarkup(null);
                case DELETE:
                    mess.setReplyMarkup(null);
            }
        }
    }

    @Override
    public Class<? extends InlineKeyboardMarkup> getReturnType() {
        return InlineKeyboardMarkup.class;
    }

    @Override
    protected String getPropertyName() {
        return "inline keyboard";
    }
}
