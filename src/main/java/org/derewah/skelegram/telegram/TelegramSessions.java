package org.derewah.skelegram.telegram;

import org.telegram.telegrambots.meta.generics.BotSession;

import java.util.HashMap;

public class TelegramSessions {


    public HashMap<String, BotSession> sessions = new HashMap<>();


    public void clearAllSessions(){
        for(BotSession sess : sessions.values()){
            sess.stop();
        }
        sessions.clear();
    }

    public void stopSession(String username){
        if(sessions.containsKey(username)){
            sessions.get(username).stop();
            sessions.remove(username);
        }
    }

}
