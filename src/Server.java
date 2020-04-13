import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {
	
	public static final int SUBSCRIBE = 3000;
	public static final int BROADCAST = 3001;
	static ArrayList<Group> groups;
	static int totalClientsNumber;
	static final Object NUMBERLOCK = new Object(); 
	
	public Server() {
		groups = new ArrayList<Group>();
		groups.add(new Group("1"));
		groups.add(new Group("2"));
		totalClientsNumber = 0;
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("The server started .. ");

		new Thread() {
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(SUBSCRIBE);
					while (true) {	new MemberHandler(ss.accept()).start();	}
				} 
				catch (IOException e) {		e.printStackTrace();	}
			}
		}.start();

		new Thread() {
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(BROADCAST);
					while (true) {	new AdminHandler(ss.accept()).start();	}
				} 
				catch (IOException e) {		e.printStackTrace();	}
			}
		}.start();
	}

	public static final class Member {
		Integer id; 
		
		public Member (Integer id){	this.id = id;	}
		public int getID () {	return this.id;		}
	}
	
	
	public static final class Group{
		
		String name;
		ArrayList<Member> members;
		final int maxNumber = 3;
		
		public Group (String name) {	this.name = name;	members = new ArrayList<Member>();	}
		
		
		public boolean addMember ( Member m) {
			if (members.size() == maxNumber )	return false ;
			else	members.add(m);
			return true;
		}
	}
	
	private static class MemberHandler extends Thread{
		
		Socket socket;
		
		public MemberHandler(Socket socket) {	this.socket = socket;	}
		
		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			int groupNum = 0; 
			boolean success = false;
			int id = 0;
			
			try {	
				out = new PrintWriter(this.socket.getOutputStream(), true);	
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} 
			catch (IOException e) {	e.printStackTrace();	}
			
			out.println("Welcome, Which group do you want to join? (1) Section-1 or (2) Section-2");
			
			try {
				groupNum = Integer.parseIntin.readLine());
				
				synchronized(NUMBERLOCK)	{	id = 6000+totalClientsNumber++;	}
				Member m = new Member(id);
				
				synchronized (groups.get(groupNum-1))	{	success = groups.get(groupNum-1).addMember(m);	}
				
				if (success)
					out.println("You are successfully added to the group Section-"+ message + " with ID = " + id);
				else	out.println("Sorry, the group reached its maximum count");
				
				out.close();	in.close();		socket.close();
			} 
			catch (IOException e) {		e.printStackTrace();	}	
		}
	} 
	
	private static class AdminHandler extends Thread {
		Socket socket;
		
		public AdminHandler(Socket socket) {	this.socket = socket;	}
		
		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			String message = null; 
			int groupNumber = 0;
			
			int port;
			byte[] sendData = new byte[1024];
			DatagramSocket clientSocket = null;
			InetAddress IPAddress = null;
			
			try {	
				out = new PrintWriter(this.socket.getOutputStream(), true);	
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} 
			catch (IOException e) {	e.printStackTrace();	}
			
			out.println("Welcome, Which group do you want to broadcast your message to? (1) Section-1 or (2) Section-2");
			
			try {	groupNumber = Integer.parseInt(in.readLine());	} 
			catch (IOException e) {		e.printStackTrace();	}
			
			out.println("What is the message to be broadcast?");
			
			try {	clientSocket = new DatagramSocket();	} 
			catch (SocketException e) {	e.printStackTrace();	}
			
			try {	IPAddress = InetAddress.getByName("localhost");	} 
			catch (UnknownHostException e1) {	e1.printStackTrace();}
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6000);
			// 6000 is a garbage port
			
			while(true)
			{
				try {	message = in.readLine();	} 
				catch (IOException e) {		e.printStackTrace();	}
				
				if(message == "end")	break;
				
				sendData = message.getBytes();
				
				if (groupNumber == 1 || groupNumber == 2) 
					synchronized (groups.get(groupNumber-1))
					{
						for(int i=0;i<groups.get(groupNumber-1).members.size();i++)
						{
							port = groups.get(groupNumber-1).members.get(i).getID();
							sendPacket.setPort(port);
							try {	clientSocket.send(sendPacket);	}
							catch (IOException e) {	e.printStackTrace();	}	
						}
					}
			}			
			try {	out.close();	in.close();	socket.close();	clientSocket.close();} 
			catch (IOException e) {	e.printStackTrace();	}	
		}
	}

	
}
