package socketpkg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/kaja2")//client는 이 주소로 서버에 접속
						//kaja는 websocket endpoint 경로
public class KajaSocket2 {//KajaSocket.java

	private static Map<Session, String> users = Collections.synchronizedMap(new HashMap<Session,String>());
	
	ArrayList<String> live_mem = new ArrayList<String>() ;
	
	@OnMessage
	public void onMsg(String message, Session session) throws IOException{
		String userName = users.get(session);
		System.out.println(userName + " : " + message);
		
		if(message.split("changeNic").length == 2) {
			changeNic(message,session);
		}else if (message.split("/w:").length == 2){
			wMsg(message,session);
		}else {		
			synchronized (users) {
				Iterator<Session> it = users.keySet().iterator();
				while(it.hasNext()){
					Session currentSession = it.next();
					if(!currentSession.equals(session)){
						currentSession.getBasicRemote().sendText(userName + " : " + message);
					}
				}
			}
		}
	}
	
	public void changeNic(String message, Session session) throws IOException {
		String [] changeNic = message.split(":");
		if(changeNic[0].equals("changeNic")) {
			String change = "server : ";
			System.out.print(users.get(session)+"님이 ");
			change += users.get(session)+"님이 ";
			
		Iterator<String> memberNic = users.values().iterator();			
		ArrayList<String> checkNic = new ArrayList<String>();
		
		checkNic.clear();
		
		while(memberNic.hasNext()) {
			checkNic.add(memberNic.next());
		}

		if(checkNic.indexOf(changeNic[1]) == -1) {
		
	    users.put(session, changeNic[1]);
				change += changeNic[1]+"로 변경되었습니다.";
				
				session.getBasicRemote().sendText(change+" 본인 ");
				System.out.println(changeNic[1]+"로 변경되었습니다.");
				synchronized (users) {
					Iterator<Session> it = users.keySet().iterator();
					while(it.hasNext()){
						Session currentSession = it.next();
						if(!currentSession.equals(session)){
							currentSession.getBasicRemote().sendText(change);
						}
					}
				}
			}else {
				session.getBasicRemote().sendText("server : 중복된 닉네임 입니다.");
			}
		}
	}
	
	public void wMsg(String message,Session session) throws IOException {
		String [] wMsg = message.split("/w:");
		String bonae = users.get(session);
		String bada = wMsg[1].split(":")[0];
		String wMsgto = wMsg[1].split(":")[1];
		
		synchronized (users) {
			Iterator<Session> it = users.keySet().iterator();
			while(it.hasNext()){
				Session currentSession = it.next();
				if(!users.get(session).equals(bada)){
					if(users.get(currentSession).equals(bada)){
						currentSession.getBasicRemote().sendText("\'(귓속말)"+bonae+"\' :"+wMsgto);
					}
				}
			}
		}
				
	}
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		System.out.println(session);
			int num = (int)(Math.random()*10000);
			
			String userName = "user" + num;
			
			users.put(session, userName);
			
			Iterator<Session> it = users.keySet().iterator();
				
			while(it.hasNext()) {
				live_mem.add(users.get(it.next()));
			}
			
			session.getBasicRemote().sendText("server : "+live_mem.toString());
			
			session.getBasicRemote().sendText("server : userNick"+users.get(session));
			
			System.out.println("server : "+userName + "님이 입장하셨습니다. 현재 사용자 " + users.size() + "명");
		synchronized (users) {
			Iterator<Session> it1 = users.keySet().iterator();
			while(it1.hasNext()){
				Session currentSession = it1.next();
				if(!currentSession.equals(session)) {
				currentSession.getBasicRemote().sendText("server : "+userName + "님이 입장하셨습니다. 현재 사용자 " + users.size() + "명");
				}else {
				currentSession.getBasicRemote().sendText("server : "+userName + "님 채팅 시작합니다. 현재 사용자 " + users.size() + "명");
				}
			}
		}
		
				
	}
	
	
	public void sendNotice(String message) throws IOException{
		String userName = "server";
		System.out.println(userName + " : " + message);
		
		synchronized (users) {
			Iterator<Session> it = users.keySet().iterator();
			while(it.hasNext()){
				Session currentSession = it.next();
				currentSession.getBasicRemote().sendText(userName + " : " + message);
			}
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException{
		String userName = users.get(session);
		users.remove(session);
		sendNotice(userName + "님이 퇴장하셨습니다. 현재 사용자 " + users.size() + "명");
	}

}
