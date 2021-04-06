package com.xuanzhi.tools.transport3;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class WebSocketHandler {
	
	private static String APPEND_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    static class Header {
        private Map<String, String> properties = new HashMap<String, String>();

        public String get(String key) {
            return properties.get(key);
        }
    }

    private WebSocketHandler() {}

    private static Header phrase(String request) {
        Header header = new Header();
        String[] pros = request.split("\r\n");
        for (String pro : pros) {
            if (pro.contains(":")) {
                int index = pro.indexOf(":");
                String key = pro.substring(0, index).trim();
                String value = pro.substring(index + 1).trim();
                header.properties.put(key, value);
            }
        }
        return header;
    }

    public static String getResponse(String request) {
        Header header = phrase(request);
        String acceptKey = header.get("Sec-WebSocket-Key") + APPEND_STRING;
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("sha1");
            sha1.update(acceptKey.getBytes());
//            acceptKey = new String(Base64.getEncoder().encode(sha1.digest()));
            acceptKey = new String(Base64.encodeBase64(sha1.digest()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("fail to encode " + e.getMessage());
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 101 Switching Protocols\r\n").append("Upgrade: websocket\r\n")
                .append("Connection: Upgrade\r\n").append("Sec-WebSocket-Accept: " + acceptKey + "\r\n")
                .append("\r\n");
        return stringBuilder.toString();
    }

}
