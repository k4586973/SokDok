package Chatting2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class Chat extends JFrame {
	
	JPanel center = new JPanel ();
	JPanel center1 = new JPanel ();
	//JPanel center2 = new JPanel ();
	JPanel east = new JPanel ();
	JPanel east1 = new JPanel ();
	JPanel global = new JPanel ();
	
	JTextArea area = new JTextArea();
	//JScrollPane scrollPane = new JScrollPane(area); 

	
	JTextField globalsend = new JTextField();
	//JScrollPane scrollpane = new JScrollPane();	
	//JScrollBar scrollbar = new JScrollBar();
	
	List list = new List();
	JButton close = new JButton("나가기");
	private final JScrollPane scrollPane = new JScrollPane();
	
	public Chat() {
		center1.setBounds(0, 380, 499, 35);
		
		//center2.setLayout(new BorderLayout(5, 0));
		//center2.setBackground(new Color(90, 240, 255));
		
		center1.setLayout(new GridLayout(1, 1, 0, 5));
		center1.setBackground(new Color(255, 255, 255));
		//center1.add(center2);
		//center2.add(globalsend, BorderLayout.CENTER);
		center1.add(globalsend);
		globalsend.setFont(new Font("Noto Sans", 1, 20));
		center.setBackground(new Color(204, 255, 204));
		center.setLayout(null);
		scrollPane.setBounds(0, 0, 499, 375);
		
		center.add(scrollPane);
		scrollPane.setViewportView(area);
		area.setBackground(new Color(204, 255, 204));
		center.add(center1);
		
		//scrollpane.add(area);
		//scrollpane.add(scrollbar);
		
		
		east1.setLayout(new GridLayout(1, 1, 0, 5));
		east1.setBackground(new Color(204, 255, 204));
		east1.add(close);
		
		close.setBackground(new Color(102, 204, 153));
		close.setFont(new Font("Noto Sans", 1,12));
		//center.add(east, BorderLayout.EAST);
		
		east.setLayout(new BorderLayout(0, 3));
		east.setBackground(new Color(204, 255, 204));
		list.setBackground(new Color(204, 255, 204));
		east.add(list,BorderLayout.CENTER);
		east.add(east1,BorderLayout.SOUTH);
		
		global.setLayout(new BorderLayout(3, 2));
		global.setBackground(new Color(206, 255, 248));
		global.add("Center",center);
		
		//center.add(scrollpane, BorderLayout.EAST);
		//center.add(scrollbar, BorderLayout.EAST);
		global.add("East", east);
		
		Dimension  dim=getToolkit().getScreenSize();
		setLocation(dim.width/4-getWidth()/4,dim.height/4-getHeight()/4);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		setTitle("채팅창");
		getContentPane().add("North", new Label(""));
		getContentPane().add("South", new Label(""));
		getContentPane().add("West", new Label(""));
		getContentPane().add("East", new Label(""));
		getContentPane().add("Center", global);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(670,500);
		//setVisible(true);
		
//		close.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				setVisible(false);
//				System.exit(0);
//			}
//		});
		
	}
	
	public static void main(String[] args) {
		
		new Chat();
	}

}
