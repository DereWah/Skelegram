package org.derewah.skelegram;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(Message.class, "telegrammessage")
                        .user("telegram message")
                        .defaultExpression(new EventValueExpression<>(Message.class))
                        .name("telegram message")
                        .description("Represents a telegram message object.")
                        .parser(new Parser<Message>(){


                            @Override
                            public boolean canParse(ParseContext parseContext){
                                return false;
                            }


                            @Override
                            public String toString(Message message, int arg1){
                                return message.getText();
                            }

                            @Override
                            public String toVariableNameString(Message message){
                                return "chat_id:"+message.getChatId()+"message_id:"+message.getMessageId();
                            }



                        })
        );
        Classes.registerClass(new ClassInfo<>(User.class, "telegramuser")
                .user("telegram user")
                .defaultExpression(new EventValueExpression<>(User.class))
                .name("telegram user")
                .description("Represents a telegram user object.")
                .parser(new Parser<User>(){


                    @Override
                    public boolean canParse(ParseContext parseContext){
                        return false;
                    }


                    @Override
                    public String toString(User user, int arg1){
                        return user.getUserName();
                    }

                    @Override
                    public String toVariableNameString(User user){
                        return user.getUserName();
                    }

                })
        );
        Classes.registerClass(new ClassInfo<>(Chat.class, "telegramchat")
                .user("telegram chat")
                .defaultExpression(new EventValueExpression<>(Chat.class))
                .name("telegram chat")
                .description("Represents a telegram chat object.")
                .parser(new Parser<Chat>(){


                    @Override
                    public boolean canParse(ParseContext parseContext){
                        return false;
                    }


                    @Override
                    public String toString(Chat chat, int arg1){
                        return chat.getTitle();
                    }

                    @Override
                    public String toVariableNameString(Chat chat){
                        return chat.getTitle();
                    }

                })
        );
        Classes.registerClass(new ClassInfo<>(InlineKeyboardMarkup.class, "inlinekeyboard")
                .user("inline keyboard")
                .defaultExpression(new EventValueExpression<>(InlineKeyboardMarkup.class))
                .name("inline keyboard")
                .description("Represents a telegram inline keyboard object.")
                .parser(new Parser<InlineKeyboardMarkup>(){


                    @Override
                    public boolean canParse(ParseContext parseContext){
                        return false;
                    }


                    @Override
                    public String toString(InlineKeyboardMarkup kb, int arg1){
                        return kb.toString();
                    }

                    @Override
                    public String toVariableNameString(InlineKeyboardMarkup kb){
                        return kb.toString();
                    }

                })
        );
        Classes.registerClass(new ClassInfo<>(InlineKeyboardButton.class, "inlinebutton")
                .user("inline button")
                .defaultExpression(new EventValueExpression<>(InlineKeyboardButton.class))
                .name("inline button")
                .description("Represents a telegram inline button object.")
                .parser(new Parser<InlineKeyboardButton>(){


                    @Override
                    public boolean canParse(ParseContext parseContext){
                        return false;
                    }


                    @Override
                    public String toString(InlineKeyboardButton button, int arg1){
                        return button.getText();
                    }

                    @Override
                    public String toVariableNameString(InlineKeyboardButton button){
                        return button.toString();
                    }

                })
        );
    }
}