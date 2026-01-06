package com.dentpulse.dentalsystem.chat;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionStore {

    private final ConcurrentHashMap<String, ChatStage> sessions = new ConcurrentHashMap<>();

    public ChatStage getStage(String sessionId) {
        return sessions.getOrDefault(sessionId, ChatStage.NORMAL);
    }

    public void setStage(String sessionId, ChatStage stage) {
        sessions.put(sessionId, stage);
    }

    public void clear(String sessionId) {
        sessions.remove(sessionId);
    }
}
