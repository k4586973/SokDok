package ChattingClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

public class Client extends JFrame implements ActionListener,KeyListener{
	
	// Login GUI 전역 변수
	private JFrame Login_GUI = new JFrame();
	private JPanel Login_Pane;
	private JTextField ip_tf; // ip 텍스트 필드
	private JTextField port_tf; // port 텍스트 필드
	private JTextField id_tf; //id 텍스트 필드
	private JButton login_btn = new JButton("로그인"); // 로그인 버튼
	private JLabel logo_lbl = new JLabel("로고 사진 넣어야 함!!!");
	
	// Main GUI 전역 변수
	private JPanel Main_Pane;
	private JTextField chat_tf; // 채팅바 텍스트 필드
	
	// Main GUI 버튼 전역 변수
	private JButton letter_btn = new JButton("쪽지보내기");		
	private JButton joinroom_btn = new JButton("채팅방참여");		
	private JButton joinroom_out_btn = new JButton("채팅방퇴장");
	private JButton createroom_btn = new JButton("방만들기");		
	private JButton send_btn = new JButton("↩전송");		
	
	private JButton server_btn = new JButton("서버 종료");
	
	
	

	//-------------------------------------------------------------------
	private JList User_list = new JList(); // 접속인원 리스트
	private JList Room_list = new JList(); // 전체 방 목록 리스트
	//-------------------------------------------------------------------
	private JTextArea Chatting_area = new JTextArea(); // 채팅창 변수
	
	// 네트워크를 위한 자원 변수 (지금은 강제적으로 부여해서 함!!)
	private Socket socket;
	private String ip=""; // ip string // 127.0.0.1 은 자기 자신
	private int port; // port 는 int 기억해야함!!!!!
	private String id="";
	//private String room=""; // 추가
	// Connection 들어갈 변수
	private InputStream is;
	private	OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	// 그 외 자원 변수들
	Vector user_list = new Vector(); // 자바는 대.소문자를 구분하기 때문에 위에 JList의 User_list와 구분이 됨.
	Vector room_list = new Vector();
	StringTokenizer st ; // 토큰 설정 
	
	private String My_Room; // 내가 현재 접속한 방의 이름
	
	
	
	
	
	
	
	
	Client() {
		
		
		Login_init(); // Login 창 구성 메소드
		Main_init(); // Main 창 구성 메소드
		start();	// 액션 리스너 메소드
		
	}
	
	private void start() { // 생성자
		
		login_btn.addActionListener(this); // 로그인 버튼 액션 리스너
		
		letter_btn.setBounds(12, 169, 100, 50);
		letter_btn.addActionListener(this); // 쪽지보내기 버튼 리스너
		
		joinroom_btn.setBounds(123, 401, 100, 23);
		joinroom_btn.addActionListener(this); // 채팅방 참여 버튼 리스너
		
		joinroom_out_btn.setBounds(228, 401, 100, 23);
		joinroom_out_btn.addActionListener(this); //채팅방 퇴장 버튼 리스너 
		
		createroom_btn.setBounds(12, 401, 100, 47);
		createroom_btn.addActionListener(this); // 방만들기 버튼 리스너
		
		server_btn.setBounds(467, 393, 120, 31);
		server_btn.addActionListener(this);  //서버종료 버튼 리스너 
		
		
		
		send_btn.setBounds(467, 426, 120, 23);
		send_btn.addActionListener(this); // 전송 버튼 리스너
		chat_tf.addKeyListener(this);
		
	}
	
	private void Main_init() { //여기가 this 임!!!! // GUI 부분
		// 여기가 main 이기 때문에 this 써도 오류 안 뜸!!! set 앞에 항상 this가 생략 되었다고 보면 됨!!!!
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 613, 502);
		Main_Pane = new JPanel();
		Main_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(Main_Pane);
		Main_Pane.setLayout(null);
		
		// 접속 인원 라벨
		JLabel lblNewLabel = new JLabel("접속인원");
		lblNewLabel.setBounds(12, 10, 100, 15);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		Main_Pane.add(lblNewLabel);
		Main_Pane.add(letter_btn);
		//--------------------------------------------------------
		
		// 채팅방 목록 라벨
		JLabel lblNewLabel_1 = new JLabel("채팅방 목록");
		lblNewLabel_1.setBounds(12, 229, 100, 15);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		Main_Pane.add(lblNewLabel_1);
		Main_Pane.add(joinroom_btn);
		Main_Pane.add(joinroom_out_btn);
		Main_Pane.add(createroom_btn);
		JScrollPane Chatting_scroll = new JScrollPane();
		Chatting_scroll.setBounds(124, 23, 463, 357);
		
		Main_Pane.add(Chatting_scroll);
		Chatting_scroll.setViewportView(Chatting_area);
		Chatting_area.setEditable(false);
		
		chat_tf = new JTextField();
		chat_tf.setBounds(122, 427, 344, 21);
		Main_Pane.add(chat_tf);
		chat_tf.setColumns(10);
		chat_tf.setEnabled(false);
		Main_Pane.add(send_btn);
		send_btn.setEnabled(false);
		
		
		Main_Pane.add(server_btn);
		User_list.setBounds(14, 35, 98, 124);
		Main_Pane.add(User_list);
		Room_list.setBounds(12, 250, 98, 141);
		Main_Pane.add(Room_list);

	
		//--------------------------------------------------------
		
		
		
		this.setVisible(false);
		setLocationRelativeTo(null); 
		
	}
	
	private void Login_init() { // 로그인 GUI 부분!!!!
		
		//this 쓰면 안됨!!! 여기는 sub-frame 이기 때문에 앞에 위에 전역변수로 쓴 JFrame 이름을 앞에다 붙여 줘야함!!!!
		Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Login_GUI.setBounds(100, 100, 280, 425);
		Login_Pane = new JPanel();
		Login_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));

		Login_GUI.setContentPane(Login_Pane); 
		Login_Pane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("SERVER IP");
		lblNewLabel.setBounds(12, 163, 92, 15);
		Login_Pane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("SERVER PORT");
		lblNewLabel_1.setBounds(12, 235, 90, 15);
		Login_Pane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("ID");
		lblNewLabel_2.setBounds(12, 298, 92, 15);
		Login_Pane.add(lblNewLabel_2);
		
		ip_tf = new JTextField();
		ip_tf.setColumns(10);
		ip_tf.setBounds(116, 160, 140, 21);
		Login_Pane.add(ip_tf);
		
		port_tf = new JTextField();
		port_tf.setColumns(10);
		port_tf.setBounds(114, 232, 142, 21);
		Login_Pane.add(port_tf);
		
		id_tf = new JTextField();
		id_tf.setColumns(10);
		id_tf.setBounds(114, 295, 142, 21);
		Login_Pane.add(id_tf);
		
		login_btn.setBounds(12, 345, 244, 23);
		Login_Pane.add(login_btn);
		
		logo_lbl.setIcon(new ImageIcon("C:/Users/user/Desktop/ChattingApp/Chatting/Logo2.png"));
		logo_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		logo_lbl.setBounds(12, 10, 240, 143);
		Login_Pane.add(logo_lbl);
		
		Login_GUI.setVisible(true); // 꼭 써워야 함!! 그래야 창 보임!!!!
		Login_GUI.setLocationRelativeTo(null); 
		
	}

	private void Network() { // 여기선 멀티catch
		
			try {
				socket = new Socket(ip,port);
				
				if(socket != null) { //정상적으로 소켓이 연결되었을 경우
					Connection();
					
				}

			} catch (UnknownHostException e) { // 해당 호스트는 찾을수 없다라는 그런 에러들 발생 할 수 있음.
				
				JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				
				JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
			}
	
	}
	
	
	private void Connection() { //실질적인 메소드 부분 (Stream 설정)
		
		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
		
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch(IOException e) { // 에러 처리 부분
			
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
			
		} // Stream 설정 끝
		
		this.setVisible(true); // main GUI 표시
		this.Login_GUI.setVisible(false); // 메인 창이 뜨면, 로그인창 사라침
		
		// 처음 접속시에 ID 전송
		send_message(id);
		
		//send_message(room); //추가
		
		// User_list 에 사용자 추가
		user_list.add(id);
		User_list.setListData(user_list);
		
		Thread th = new  Thread(new  Runnable() {
			
			@Override
			public void run() { // 쓰레드 부분 
				
				while(true) {
					
					try {
						String msg = dis.readUTF(); // 메세지 수신
						
						System.out.println("서버로부터 수신된 메세지 : "+msg);
						
						inmessage(msg);
						
					} catch (IOException e) {
						
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							JOptionPane.showMessageDialog(null, "서버와의 연결 끊김", "알림", JOptionPane.ERROR_MESSAGE);
						} catch(IOException e1) {
							
						}
						
							break;
						
					} 
					
				}
				
			} // Thread run-end
		});
		th.start();

	}
	
	private void inmessage(String str) { // 여기는 서버로 부터 들어오는 모든 메세지
		
		st = new StringTokenizer(str, "/"); // str 이 어떤 문자열을 사용할 건지 , "/" 은 어떠한 토큰으로 자를 것인지.
		
		String protocol = st.nextToken();
		String Message = st.nextToken();
		
		System.out.println("프르토콜 : "+protocol);
		System.out.println("내용 :"+Message);
		
		// 프로토콜 쪽 중요
		if(protocol.equals("NewUser")) { // 만약에 protocol = NewUser 이면 //새로운 접속자

			user_list.add(Message);
			User_list.setListData(user_list);
		
		}
		else if(protocol.equals("OldUser")) { // 서버에서 클라이언트로 OldUser 이 날라오면
			
			user_list.add(Message);				// OldUser의 아이디를 추가 시켜줌
			User_list.setListData(user_list);	// 현재 유저리스트를 갱신 시켜주는 부분 (위에랑 똑같이 할것임)
		}
		else if(protocol.equals("Letter")) {
			
			String letter = st.nextToken();
			
			System.out.println(Message+"님 으로부터 온 쪽지 :"+letter);
			
			JOptionPane.showMessageDialog // 받은 쪽지 보여주는 창 메소드 
			(null,letter,Message+"님으로 부터 쪽지",JOptionPane.CLOSED_OPTION);
		//값:null , letter;쪽지내용 , user+"님으로 부터 쪽지":상태창에 나타날 메시지 , 닫기 버튼 옵션
			
		}
		else if(protocol.equals("user_list_update")) {
			
			// User_list.updateUI(); 를 쓸수는 있는데 잘 동작이 되지 않아서 뺌.
			User_list.setListData(user_list);//직접적으로 접근하는 방식
			
		}
		else if(protocol.equals("CreateRoom")) { // 방만들기 성공했을 경우
			
			My_Room = Message;
			chat_tf.setEnabled(true);
			send_btn.setEnabled(true);
			joinroom_btn.setEnabled(false);
			createroom_btn.setEnabled(false);
			
		}
		else if(protocol.equals("CreateRoomFail")) { // 방만들기 실패했을 경우
			
			JOptionPane.showMessageDialog(null, "방 만들기 실패", "알림", JOptionPane.ERROR_MESSAGE);
			
		}
		else if(protocol.equals("New_Room")) { // 새로운 방을 만들었을때
			
			room_list.add(Message);
			Room_list.setListData(room_list);
			
		}
		else if(protocol.equals("Chatting")) {
			
			String msg = st.nextToken();
			
			Chatting_area.append(Message+":"+msg+"\n");
			
		}
		else if(protocol.equals("OldRoom")) {
			
			room_list.add(Message);
			Room_list.setListData(room_list);
			
		}
		else if(protocol.equals("room_list_update")) { 
			
			//room_list.add(Message);
			Room_list.setListData(room_list);
		}
		else if(protocol.equals("JoinRoom")) {
			
			My_Room = Message;
			chat_tf.setEnabled(true);
			send_btn.setEnabled(true);
			joinroom_btn.setEnabled(false);
			createroom_btn.setEnabled(false);
			
			JOptionPane.showMessageDialog(null, "채팅방 입장에 성공했습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else if(protocol.equals("JoinRoomOut")) { //채팅방 퇴장
			
		}
		else if(protocol.equals("User_out")) {
			
			user_list.remove(Message);
			
		}
		else if(protocol.equals("server_out")) { //서버 종료
			
		} 
		
	}
	
	//서버와 통신 할 메소드 작성
	private void send_message(String str) { // 서버에게 메세지를 보내는 부분
		
		try {
			dos.writeUTF(str);
		} catch (IOException e) { // 에러처리 부분
			e.printStackTrace();
		} 
		
	}
	

	public static void main(String[] args) {
		
		
		new Client();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// login_btn = 로그인 버튼
		if(e.getSource() == login_btn) {
			System.out.println("로그인 버튼 클릭");
			
			if(ip_tf.getText().length() == 0) {
				ip_tf.setText("IP를 입력해주세요.");
				ip_tf.requestFocus();
			}
			else if(port_tf.getText().length() == 0) {
				port_tf.setText("PORT를 입력해주세요.");
				port_tf.requestFocus();
			}
			else if(id_tf.getText().length() == 0) {
				id_tf.setText("ID를 입력해주세요.");
				id_tf.requestFocus();
			}
			else { // 위 모든 if 문에 해당하지 않는다면.
				ip = ip_tf.getText().trim();	// ip 를 작성하면 그 값을 가져오는 작업. // .trim()은 혹시나 공백이 있을수도 있으니 그 공백을 지워주는 역할.
				
				port =Integer.parseInt(port_tf.getText().trim()); // port는 int로 가져오는데 그냥 쓰면 string오류가 나오니 형변환을 해줘야함. 그 역할이 Integer.parseInt 
				
				id = id_tf.getText().trim(); // id 받아오는 부분
			}
			

//			ip = ip_tf.getText().trim();	// ip 를 작성하면 그 값을 가져오는 작업. // .trim()은 혹시나 공백이 있을수도 있으니 그 공백을 지워주는 역할.
//			
//			port =Integer.parseInt(port_tf.getText().trim()); // port는 int로 가져오는데 그냥 쓰면 string오류가 나오니 형변환을 해줘야함. 그 역할이 Integer.parseInt 
//			
//			id = id_tf.getText().trim(); // id 받아오는 부분
			
			Network();
		}
		else if(e.getSource() == letter_btn) {
			System.out.println("쪽지보내기 버튼 클릭");
			
			String user = (String)User_list.getSelectedValue();
			
			String letter = JOptionPane.showInputDialog("보낼메세지");
			
			if(letter != null) {
				send_message("Letter/"+user+"/"+letter);
				// ex) Letter/User2/나는 User1 입니다. 라고 보낸다는 뜻
			}
			System.out.println("받는 사람 :"+user+"| 보낼 내용 :"+letter);
			// 코딩을 하면 꼭 콘솔창에 무엇이 뜨는지 알려주는 sysout 절차 꼭 해주기 잊지마? 응?
			
		}
		else if(e.getSource() == joinroom_btn) {
			
			String JoinRoom =(String)Room_list.getSelectedValue();
			
			send_message("JoinRoom/"+JoinRoom);
			
			System.out.println("채팅방참여 버튼 클릭");
		}
		else if(e.getSource() == joinroom_out_btn) {
			
			
			System.out.println("채팅방 퇴장 버튼 클릭");
		}
		else if(e.getSource() == createroom_btn) {
			
			String roomname = JOptionPane.showInputDialog("방 이름");
			if(roomname != null) {
				
				send_message("CreateRoom/"+roomname);
			}
			//else if (roomname = )
			
			System.out.println("방 만들기 버튼 클릭");
		}
		else if(e.getSource() == send_btn) {
			
			send_message("Chatting/"+My_Room+"/"+chat_tf.getText().trim());
			chat_tf.setText("");
			chat_tf.requestFocus();
			
			// 프로토토콜: Chatting + 방이름 + 내용
			
			
			System.out.println("전송 버튼 클릭");
		}
		else if(e.getSource() == server_btn) {
			
			
			System.out.println("서버 종료 버튼 클릭");
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) { // 타이핑
	
		
	}

	@Override
	public void keyPressed(KeyEvent e) { // 눌렀다 땠을때
	//	System.out.println(e);
		if(e.getKeyCode() == 10) {
			send_message("Chatting/"+My_Room+"/"+chat_tf.getText().trim());
			chat_tf.setText("");
			chat_tf.requestFocus(); 
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) { // 눌렀을때
		// TODO Auto-generated method stub
		
	}

} 