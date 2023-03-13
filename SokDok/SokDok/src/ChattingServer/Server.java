package ChattingServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Server extends JFrame implements ActionListener{
	// import 자동 생성 단축키  : ctrl + shift + o
	
	private JPanel contentPane; 
	private JTextField port_tf;		// GUI 에서 기본 전역 변수 가져오기
	private	JTextArea textArea = new JTextArea(); // 전역 변수 설정 할때는  private
	private JButton start_btn = new JButton("서버 실행");
	private JButton stop_btn = new JButton("서버 중지");
	
	// Network 를 위한 변수 준비 (네트워크 자원)
	
	private ServerSocket server_socket;
	private Socket socket;
	private int port;
	// Vector 생성
	private Vector user_vc = new Vector();
	private Vector room_vc = new Vector();
	
	private StringTokenizer st; // 전체 하나로 두고 써도 되서 전역 변수로 토크나이저 설정
	

	Server() { //생성자
		
		init();  // 화면 생성 메소드
		start(); // 버튼 동작 메소드
		
	}
	
	private void start() {
		// start_btn , stop_btn 액션 리스너
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
	}
	
	private void init() { // 화면구성

		
		// GUI 에서 화면 구성하는 모든 소스 코드를 복사해서 붙여 넣기
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 280, 218);
		contentPane.add(scrollPane);
		
		// textArea 필드 여기서 글자 떠야함!!!
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false); // textArea 의 타이핑을 할 수 없도록 하는것.
		//---------------------------------------------------------------
		
		// 포트 넘버 라벨
		JLabel lblNewLabel = new JLabel("PORT NUM");
		lblNewLabel.setBounds(12, 253, 65, 15);
		contentPane.add(lblNewLabel);
		//---------------------------------------------------------------
		
		// 포트 번호 텍스트 필드
		port_tf = new JTextField();
		port_tf.setBounds(89, 250, 203, 21);
		contentPane.add(port_tf);
		port_tf.setColumns(10);
		//---------------------------------------------------------------
		
		// 서버 실행 버튼
		//JButton stop_btn = new JButton("서버 중지"); // 이걸 전역 버튼으로 설정
		start_btn.setBounds(12, 298, 142, 23);
		contentPane.add(start_btn);
		//---------------------------------------------------------------
		
		// 서버 중지 버튼
		stop_btn.setBounds(154, 298, 138, 23);
		contentPane.add(stop_btn);
		stop_btn.setEnabled(false);
		//---------------------------------------------------------------
		
		this.setVisible(true); // true = 화면 보이게 flase = 화면 안 뜨게
		setLocationRelativeTo(null); 
	}
	
	private void Server_start() {
		
		try {
			server_socket = new ServerSocket(port);  // 에러 뜨는데 try catch 로 묶어주는게 좋다는데???
		} catch (IOException e) { // 포트번호는 임의로 설정 쌉가능
			
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트", "알림", JOptionPane.ERROR_MESSAGE);
		} 
		
		if(server_socket != null) { // 정상적으로 포트가 열렸을 경우
		
			Connection(); // 메소드 선언
			
		}
		
	}
	
	private void Connection() { // Connection 메소드 생성
		
//		여기다 먼저 작성 후 소켓 부분으로 이동. 왜냐하면 소켓에서부터 받아야 하기 때문에 안그러면 에러남.
//		try {
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//		
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);
//		} catch(IOException e) {
//			
//		}
		
		
		// 자바에서는 1 가지의 스레드에서는 1가지의 일만 처리 할 수 있다.
		// accept 가 활성화 되어있으면 다른 프로그램들이 죽음
		
		Thread th = new Thread(new Runnable() { // 익명쓰레드

			@Override
			public void run() { // 쓰레드에서 처리할 일을 기재해야함!!!
				
			  while(true) {
				try {
					
					textArea.append("사용자 접속 대기중\n");
					socket = server_socket.accept(); // 사용자 접속 대시, 무한 대기
					textArea.append("사용자 접속!!!\n");
					
					// 사용자가 접속을 하게 되면 객체를 하나 생성하는 부분
					UserInfo user = new UserInfo(socket);
					
					user.start(); // 객체의 쓰레드 실행(user 각각의 개별 쓰레드를 실행하게 하는것임)
					

				} catch (IOException e) {
					
					textArea.append("서버 닫힘\n");
					
					// 에러메시지가 뜨면 while 문이 멈추게
					break;

				} // try-end
				
			  } // while-end
				
			}
			
		});
		
		th.start(); // 쓰레드 실행
		
		
	} //Connection-end
	
	public static void main(String[] args) {
		
		new Server();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == start_btn) {
			System.out.println("서버 실행 버튼 클릭");
			
			port = Integer.parseInt(port_tf.getText().trim()); // port는 int로 가져오는데 그냥 쓰면 string오류가 나오니 형변환을 해줘야함. 그 역할이 Integer.parseInt
			
			Server_start(); // 소켓 생성 및 사용자 접속 대기
			
			// 서버 실행이 되면 실행 버튼과 포트 텍스트 필드 잠그기
			start_btn.setEnabled(false);
			port_tf.setEditable(false);
			stop_btn.setEnabled(true);
			
		}
		else if(e.getSource() == stop_btn) {
			
			// 서버 중지 버튼을 누르면 서버 실행 버튼 및 포트 텍스트 필드 열기
			stop_btn.setEnabled(false);
			start_btn.setEnabled(true);
			port_tf.setEditable(true);
			
			try { // 중지 버튼 누르면 서버를 닫아주는 부분
				server_socket.close();
				user_vc.removeAllElements();
				room_vc.removeAllElements();
				JOptionPane.showMessageDialog(null, "서버가 종료 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				e1.printStackTrace();
				//JOptionPane.showMessageDialog(null, "서버가 종료 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
			
			System.out.println("서버 중지 버튼 클릭");
		}
		
	} // 액션 이벤트 끝
	
	//내부 클래스로 가동 (사용자 정보)
	class UserInfo extends Thread {
		
		private OutputStream os;
		private InputStream is;
		private DataOutputStream dos;
		private DataInputStream dis;
		
		private Socket user_socket;
		private String Nickname = "";
		
		private boolean RoomCheck =true;
		
		UserInfo(Socket soc) { // 생성자 메소드
			// 중요 부분
			this.user_socket = soc;
			
			UserNetwork();
			
		}
		
		private void UserNetwork() {
			
			try {
			is = user_socket.getInputStream();
			dis = new DataInputStream(is);
			
			os = user_socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			Nickname = dis.readUTF(); // 사용자의 닉네임을 받는다.
			textArea.append(Nickname+"님이 접속 했습니다.\n");
			
			// 기존 접속자들에게 새로운 접속자를 알림.
			System.out.println("현재 접속된 사용자 : "+user_vc.size()+"(+1)명");
			
/*			//---------- 이 부분이 BroadCast(String str) 인데 이건 자주 쓸꺼기 때문에 메소드로 따로 분리-----------
//			for(int i=0; i<user_vc.size(); i++) { // 현재 접속된 사용자에게 새로운 접속자를 알림
//				
//				UserInfo u =(UserInfo)user_vc.elementAt(i);
//				
//				u.send_Message("NewUser/"+Nickname); // protocol 부분은 NewUser 이고 Nickname 은 id
//				
//			} // 이 소스가 끝난 뒤 user_vc.add(this); //사용자에게 알린 후 Vector 에 자신을 저장.
//			//------------------------------------------------------------------------------------
*/
			
			// BroadCast 불러서 위에 긴 소스를 대체
			BroadCast("NewUser/"+Nickname); // 기존 접속자들을 알리는 부분
			
			// 새로운 접속자에게 기존 접속자를 보여주는 부분
			for(int i=0; i<user_vc.size(); i++) {
				
				UserInfo u = (UserInfo)user_vc.elementAt(i);
				
				send_Message("OldUser/"+u.Nickname);
			}		
			
			// 자신에게 기존 방 목록을 받아오는 부분
			
			for(int i=0; i<room_vc.size();i++) {
				RoomInfo r = (RoomInfo)room_vc.elementAt(i);
				
				send_Message("OldRoom/"+r.Room_name);
				
				//send_Message("room_list_update/");
			}			
			
			//send_Message("room_list_update/");
			
			user_vc.add(this); //사용자에게 알린 후 Vector 에 자신을 저장.
			
			//BroadCast("user_list_update/"); // AWT List를 사용하면 이 부분은 굳이 필요없음
			
			
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "Stream설정 오류", "알림", JOptionPane.ERROR_MESSAGE);
			}
			//------------- for부터 해서 필기 그림 1-2 부분 -----------------------
			
		} //UserNetwork-end
		
		public void run() { //Thread 에서 처리할 내용
			
			while(true) {
				
				try {
					String msg = dis.readUTF(); // 메세지를 전송 // 클라이언트로부터 들어오는 메세지
					textArea.append("("+Nickname+")으로부터 들어온 메세지 :"+msg+"\n");
					
					in_Message(msg);
					
				} catch (IOException e) {
					textArea.append(Nickname+"님의 접속이 끊어졌습니다.\n");
					
					try {
						dos.close();
						dis.close();
						user_socket.close();
						user_vc.remove(this);
						BroadCast("User_out/"+Nickname);
						BroadCast("user_list_update/"+Nickname);
					} catch(IOException e1) {

					}
					break;
				}
				
			} 
			
	
		} // run-end
		
		private void in_Message(String str) { // 클라이언트로 부터 들어오는 메세지 처리
			
			st = new StringTokenizer(str,"/");
			
			String protocol = st.nextToken();
			String message = st.nextToken();
			
			System.out.println("메세지프로토콜 :"+protocol);
			System.out.println("메세지"+message);
			
			if(protocol.equals("Letter")) {
				
				// protocol = Letter
				// message = user
				// letter = 받는 내용
				
				String letter = st.nextToken();
				
				System.out.println("받는 사람 : "+message);
				System.out.println("보낼 내용 : "+letter);
				
				// 벡터에서는 쪽지를 보낸 사용자를 찾아서 메세지 전송
				
				// 벡터를 한반퀴 순회...
				for(int i=0; i<user_vc.size(); i++) { // 전체 사용자에게 메세지를 보내는 부분
					
					UserInfo u =(UserInfo)user_vc.elementAt(i);
					
					if(u.Nickname.equals(message)) { // 벡터에서 Nickname 을 하나씩 비교를 해서 맞는 Nickname을 찾음.
						
						u.send_Message("Letter/"+Nickname+"/"+letter);
						// Letter/User2 / 쪽지내용
						
						// 원하는 사용자에게 보내야 하기 때문에, 그냥 send_Message 하면 자기 자신한테 돌아옴.
						// 찾은 사용자의 send_Message 를 통해서 "Letter/"+Nickname+"@"+letter 를 보내게 됨.
						
						
					} // for 안의 if-end

				} //if(protocol(Letter))의 for-end 
				
				
			} // if(protocol(Letter))-end
			
			else if(protocol.equals("CreateRoom")) {
				
				//1.현재 같은 방이 존재 하는지 확인한다.
				for(int i=0; i<room_vc.size(); i++) {
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					
					if(r.Room_name.equals(message)) { // 만들고자 하는 방이 이미 존재 할 때
						
						send_Message("CreateRoomFail/ok");
						RoomCheck = false;
						break;
					} // for 안의 if-end
					
				} // else if(protocol(CreateRoom))의 for-end
			
				if(RoomCheck) { // 방을 만들 수 있을때
				
					RoomInfo new_room = new RoomInfo(message, this);
					room_vc.add(new_room); // 전체 방 벡터에 방을 추가
				
					send_Message("CreateRoom/"+message);
				
					BroadCast("New_Room/"+message);
					
				}
				
				RoomCheck = true;
				
				
			} //else if(protocol(CreateRoom))-end
			
			else if(protocol.equals("Chatting")) {
				
				String msg = st.nextToken();
				
				for(int i=0; i<room_vc.size(); i++) {
					
					RoomInfo r =(RoomInfo)room_vc.elementAt(i);
					
					if(r.Room_name.equals(message)) { // 해당 방을 찾아 갔을 때
						
//						for(int j=0; j<r.Room_user_vc.size(); j++) {
//							UserInfo u = (UserInfo)r.Room_user_vc.elementAt(i);
//							
//							u.send_Message("채팅내용");
//						}
						// 쌉 비효율적이기때문에 아래 RoomInfo 에 BroadCast_Room 라는 BroadCast 를 생성
						r.BroadCast_Room("Chatting/"+Nickname+"/"+msg);
						
						
					}
					
				} // else if(protocol(Chatting))의 for-end
				
			} // else if(protocol(Chatting))-end
			
			else if(protocol.equals("JoinRoom")) {
				
				for(int i=0; i<room_vc.size(); i++) {
					
					RoomInfo r =(RoomInfo)room_vc.elementAt(i);
					if(r.Room_name.equals(message)) {
						
						// 새로운 사용자를 알리는 문구
						r.BroadCast_Room("Chatting/알림/*****("+Nickname+") 님이 입장하였습니다.*****");
						
						// 사용자 추가
						r.Add_User(this);
						send_Message("JoinRoom/"+message);
						
						
					}
					
				} // else if(protocol(JoinRoom))의 for-end
				
			} // else if(protocol(JoinRoom))-end
			
//			else if(protocol.equals("room_list_update")) {
//				for(int i=0; i<room_vc.size(); i++) {
//					
//					RoomInfo r =(RoomInfo)room_vc.elementAt(i);
//					
//				}
//				
//			}
			
			else if(protocol.equals("JoinRoomOut")) {
				
			}
			else if(protocol.equals("server_out")) {
				
			}
			
		} // in_Message-end
		
		private void BroadCast(String str) {
			// 이 부분이 BroadCast(); 
			for(int i=0; i<user_vc.size(); i++) { // 전체 사용자에게 메세지를 보내는 부분
				
				UserInfo u =(UserInfo)user_vc.elementAt(i);
				
				u.send_Message(str); 
				//break;
			}// 이 소스가 끝난 뒤 user_vc.add(this); //사용자에게 알린 후 Vector 에 자신을 저장.
			
			
		}
		
		private void send_Message(String str) { // 서버에서 나가는 outputstream // 문자열을 받아서 전송
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	} //UserInfo-end
	
	class RoomInfo { // extends Thread 추가
		

		
		private String Room_name;
		private Vector Room_user_vc = new Vector();
		
		RoomInfo(String str, UserInfo u) { //생성자
			
			this.Room_name = str;
			this.Room_user_vc.add(u);
		} //생성자-end
		
		public void BroadCast_Room(String str) { // 현재 방의 모든 사용자에게 알린다.
			
			for(int i=0; i<Room_user_vc.size(); i++) {
				UserInfo u = (UserInfo)Room_user_vc.elementAt(i);
				
				u.send_Message(str); 
				
			}
			
		}
		

		private void Add_User(UserInfo u) {
			
			this.Room_user_vc.add(u);
			
		
			
		}
		
	
		
	} //RoomInfo-end

}