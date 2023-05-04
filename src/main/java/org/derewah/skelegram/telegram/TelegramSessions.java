package org.derewah.skelegram.telegram;




import ch.njol.util.Pair;
import org.telegram.telegrambots.meta.generics.BotSession;


import java.util.HashMap;

public class TelegramSessions {


    public HashMap<String, Pair<BotSession, TelegramBot>> sessions = new HashMap<>();


    public void clearAllSessions(){
        for(Pair<BotSession, TelegramBot> sess : sessions.values()){

            sess.getKey().stop();
        }
        sessions.clear();
    }

    public final TelegramBot getBot(String username) {

        if (sessions.containsKey(username)){
            return sessions.get(username).getValue();
        }
        return null;
    }

    public void stopSession(String username){
        if(sessions.containsKey(username)){
            sessions.get(username).getKey().stop();
            sessions.remove(username);
        }
    }

}
