package com.zvdev.example.chat.handler;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TextSocketHandler extends TextWebSocketHandler {

	HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
	
	// 클라이언트에서 서버로 메시지를 전송할 때 실행되어 모든 세션에 메세지를 전송합니다.
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		sessionMap.forEach((key, socketSession) -> {
			try {
				socketSession.sendMessage(new TextMessage(message.getPayload()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	// 소켓 연결이 활성화될 때 sessionMap에 해당 세션을 추가하고 테스트용 메세지를 전송합니다.
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		session.sendMessage(new TextMessage("채팅방에 오신 걸 환영합니다!"));
	}
	
	// 소켓 연결이 종료될 때 sessionMap에서 해당 세션을 삭제합니다.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}
	
	
}
