package org.derewah.skelegram;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;


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
    }
}