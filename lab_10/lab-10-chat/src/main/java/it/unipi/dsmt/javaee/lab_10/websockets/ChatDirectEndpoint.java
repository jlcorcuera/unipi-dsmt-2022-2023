package it.unipi.dsmt.javaee.lab_10.websockets;

import it.unipi.dsmt.javaee.lab_10.dto.MessageDTO;
import it.unipi.dsmt.javaee.lab_10.serializers.MessageDTODecoder;
import it.unipi.dsmt.javaee.lab_10.serializers.MessageDTOEncoder;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat-direct/{username}/{name}", decoders = MessageDTODecoder.class, encoders = MessageDTOEncoder.class)
public class ChatDirectEndpoint {
    private static final Set<Session> chatEndpoints = new CopyOnWriteArraySet<Session>();

    private static Map<String, Session> usernameSessionIdMap = new HashMap<String, Session>();
    private static Map<String, String> usernameNameMap = new HashMap<String, String>();

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("username") String username,
                       @PathParam("name") String name) throws IOException, EncodeException {
        chatEndpoints.add(session);
        usernameSessionIdMap.put(username, session);
        usernameNameMap.put(username, name);
        MessageDTO message = new MessageDTO();
        message.setFrom(name + "(" + username + ")");
        message.setContent("Connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, MessageDTO message) throws IOException, EncodeException {
        String toUsername = message.getTo();
        String username = message.getFrom();
        String name = usernameNameMap.get(username);
        message.setFrom(name + "(" + username + ")");
        if (toUsername != null && !toUsername.isEmpty()){
            Session sessionToNotify = usernameSessionIdMap.get(toUsername);
            if (sessionToNotify != null) {
                sendMessageToSession(sessionToNotify, message);
            } else {
                message.setContent("server> User doesn't exist.");
            }
            sendMessageToSession(session, message);
        } else {
            broadcast(message);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(session);
        String username = null;
        for(Map.Entry<String, Session> entry: usernameSessionIdMap.entrySet()){
            if (entry.getValue().getId().equals(session.getId())){
                username = entry.getKey();
                break;
            }
        }
        String name = usernameNameMap.get(username);
        MessageDTO message = new MessageDTO();
        message.setFrom(name);
        message.setContent("Disconnected!");
        broadcast(message);
        usernameSessionIdMap.remove(username);
        usernameNameMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(MessageDTO message) throws IOException, EncodeException {
        chatEndpoints.forEach(session -> {
            sendMessageToSession(session, message);
        });
    }

    private static void sendMessageToSession(Session session, MessageDTO message){
        synchronized (session) {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }
    }
}