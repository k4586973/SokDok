package Chatting2;

import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import java.util.Vector;




public class Server implements Runnable {

Vector vc = new Vector(); //client 정보보관장소

public void run()  
{
ServerSocket ss = null ;

try 
{
	ss = new ServerSocket(8888);
	
}catch(Exception e) {
	//e.printStackTrace();
}
while(true) 
{
	try {
		Socket s = ss.accept();
		
		System.out.println("client 정보 : " + s);
		
		Service sv = new Service(s); //소켓을 가지고 있다(ip)
		//서비스 안으로 런으로 호출 인너클래스호출 순차개념과 쓰레드가 병행처리
		sv.start();
	}catch(Exception e) {}
	//e.printStackTrace();
	}
}






public static void main(String[] args) {

Server server = new Server(); //서버구현
new Thread(server).start();//런호출


}

	class Service extends Thread{
		BufferedReader in;
		OutputStream out;
		Socket s;
		String id; // 대화명

//==========================
		Service(Socket s){
	//System.out.println("서비스 생성자");
			try {
				this.s = s;
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));

				out = s.getOutputStream();
				}catch(Exception e) {}
		} // 생성자 종료
		public void run() {
	//System.out.println("쓰레드 run");
			while(true) {

				try {

					String msg = in.readLine(); //클라이언트가 엔터를 칠 때 까지
	
					if(msg == null) return;
					//System.out.println(msg);
					StringTokenizer st = new StringTokenizer(msg,"|");
					//System.out.println(st);
					int protocol = Integer.parseInt(st.nextToken());
					//System.out.println(protocol);
					switch(protocol) {

					case 100:
					{
						id = st.nextToken();
						multicast("100|"+id); //기존 모든이에게 알려준다
						vc.addElement(this);
						for(int i = 0;i<vc.size();i++) {
							Service sv =(Service)vc.elementAt(i);
							try {
								unicast("100|"+sv.id);//나한테 보인다.
							}catch(Exception e) {}
						}
					}break;
					case 200:
					{
						String temp = st.nextToken();
						multicast("200|"+"("+id+")->"+temp);
					}	break;

					case 900:
					{
						for(int i=0;i<vc.size();i++) 
						{
							Service sv = (Service)vc.elementAt(i);
							if(id.equals(sv.id)) {
								vc.removeElementAt(i);
								break;
							}
						} //for - end
						multicast("900|"+id);
						try {
							in.close();
							out.close();
							return;
						}catch(Exception e) {return;}

						}
					}//switch end
					
					}catch(Exception e) {}

				}
		}
		protected void multicast(String msg) 
		{
			synchronized(this) {
				//모든 데이터를 버리지 않고 다 처리하여 준다.
				for(int i = 0;i<vc.size();i++) {
					Service sv =(Service)vc.elementAt(i);

					try {
						sv.unicast(msg);
						}catch(Exception e) {vc.removeElementAt(i--);}
				}// for end
			}
		} //multicast end
		protected void unicast(String msg)throws Exception
		{
			synchronized(this) 
			{
				try {
					out.write((msg+"\r\n").getBytes()); //바이트는 비트열에 의해 구성, 유니코드(2byte)를 바이트로 변환시킨다.
					}catch(Exception e) {}
				}				
		}

	}
}
