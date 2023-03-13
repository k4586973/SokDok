package Chatting2;

// 유튜브에선 이게 로그인창이라는거래

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.*;
import java.awt.List.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Client extends JFrame implements Runnable {

	Chat chat = new Chat();
	
	JPanel center = new JPanel ();
	JPanel center1 = new JPanel ();
	JPanel center2 = new JPanel ();
	JPanel south = new JPanel ();
	JPanel global = new JPanel ();
	
	JTextField portTF = new JTextField(); //PORT
	JTextField nameTF = new JTextField(); //NAME
	
	JButton ok = new JButton("확인");
	JButton cancel = new JButton("취소");
	
	String myid,myname,port; //참조선언만 되어있음.
	BufferedReader in; //서버가 보내주는 메시지
	OutputStream out; //내가 서버로 보내주는게 아웃스트림
	Socket s; //서버와 접속
	
	public Client() {
		center1.setBackground(new Color(204, 255, 204));
		
		center1.setLayout(null);
		JLabel lblport = new JLabel("PORT", JLabel.CENTER);
		lblport.setBackground(new Color(204, 255, 204));
		lblport.setBounds(0, 0, 45, 45);
		center1.add(lblport);
		
		portTF.setBounds(51, 0, 143, 44);
		portTF.setPreferredSize(new Dimension(5, 10));
		center1.add(portTF);
		center2.setBackground(new Color(204, 255, 204));
		
		center2.setLayout(null);
		JLabel lbltitle = new JLabel("ID", JLabel.CENTER);
		lbltitle.setBackground(new Color(204, 255, 204));
		lbltitle.setBounds(0, 0, 45, 45);
		center2.add(lbltitle);
		
		nameTF.setBounds(51, 0, 143, 44);
		center2.add(nameTF);
		center.setBackground(new Color(204, 255, 204));
		
		center.setLayout(new GridLayout(2, 1, 0, 5));
		center.add(center1);
		center.add(center2);
		south.setBackground(new Color(204, 255, 204));
		ok.setBackground(new Color(102, 204, 153));
		
		south.add(ok);
		cancel.setBackground(new Color(102, 204, 153));
		south.add(cancel);
		global.setBackground(new Color(204, 255, 204));
		
		global.setLayout(new BorderLayout(0, 5));
		global.add("Center",center);
		global.add("South",south);
		
		setBackground(new Color(198, 201, 210));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("North", new JLabel(""));
		getContentPane().add("South", new JLabel(""));
		getContentPane().add("West", new JLabel(""));
		getContentPane().add("East", new JLabel(""));
		getContentPane().add("Center", global);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setSize(210,170);
		setVisible(true);
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectProcess();
			}
		});
		
		chat.globalsend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendProcess();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});
		
		chat.close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});
		
		
	}
	
	void closeProcess() //나가기
	{
		chat.list.removeAll(); //다 지우기
		chat.area.setText(null);
		//chat.who.setText(null);
		chat.setVisible(false);
		
		try { //서버로 전달.
			out.write(("900|\n").getBytes());
			in.close();
			out.close();
		}catch(Exception e) {}
	}
	
	void sendProcess()
	{
		String msg = chat.globalsend.getText().trim();
		if(msg.length()<1)return;
		try
		{
			out.write(("200|"+msg+"\n").getBytes());
		}catch(Exception e) {}
		chat.globalsend.setText(null);
		chat.globalsend.requestFocus();
	}
	
	
	void connectProcess()
	{
		String port=portTF.getText().trim();
		myname=nameTF.getText().trim();
		if(myname.length()<1)return;
		if(port.length()<1 || !port.equals("8888"))return;
		try {
			s = new Socket("localhost", 8888); //소켓으로 서버 2345
			setVisible(false);
			chat.setVisible(true);
			chat.area.setText("접속성공!");
			//chat.area.setText(chat.area.getText()+"\n줄바꿔진다고");
			
			/*
			 서버가 받을때 메시지인지 귓속말인지 모른다. 따라서 구분자를 정한다.
			 프로토콜 지정, 숫자로 처리하는 것이 좋다. 문자열을 구분하기 위해 토큰으로 분리
			 소켓통신은 하나의 메시지를 전송할 때 1024 바이트를 초과할 수 없다.
			 참조만 되어있던 것을 객체화 시킨다.
			 */
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = s.getOutputStream();
			System.out.println();
			out.write(("100|"+myname+"\n").getBytes());
			
			new Thread(this).start();
		}catch(Exception e) {chat.area.setText("접속실패!");}
	}
	
	
	public void run() {
		//System.out.println("run실행");
		while(true) 
		{
			try {
				//System.out.println("try실행");
				String msg = in.readLine(); // in은 소켓
				//System.out.println(msg);
				if(msg == null) //메시지가 파괴 되면
					return;
				StringTokenizer st = new StringTokenizer(msg,"|");
				int protocol = Integer.parseInt(st.nextToken());
				System.out.println(protocol);
				switch(protocol) 
				{
				case 100: // 입장 현재 100을 가리키고 있음
				{
					String temp = st.nextToken(); //대화명/닉네임
					chat.list.add(temp);
					System.out.println((chat.area.getText()));
					//chat.area.setText(chat.area.getText()+"\n"+temp+"님이(가) 입장했습니다.");
					chat.area.append("\n"+temp+"님이(가) 입장했습니다.");
				}break;
				
				case 200: 
				{
					//chat.area.setText(chat.area.getText()+"\n"+st.nextToken());
					chat.area.append("\n"+st.nextToken());
				}break;
				
				case 900: 
				{ //서버에서 나를 포함한 모두에게 알림.
					String temp = st.nextToken();
					chat.list.remove(temp); //리스트에서 temp의 정보를 없앤다.
					//chat.area.setText(chat.area.getText()+"\n"+temp+"님이(가) 채팅을 나갔습니다.");
					chat.area.append("\n"+temp+"님이(가) 채팅을 나갔습니다.");
				}break;
			} //switch-end
				
			} catch(Exception e) {return;}
		}
	}
	

	public static void main(String[] args) {
		
		new Client();
		
	}


}
