import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	
	public static final int SUBSCRIBE = 3000;
	public static final int BROADCAST = 3001;
	
	static final Group group1 = new Group("1"); 
	static final Group group2 = new Group("2"); 
	static final Object NUMBERLOCK = new Object(); 
	
	ArrayList<Group> groups;
	static int totalClientsNumber;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("The server started .. ");

		new Thread() {
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(SUBSCRIBE); // either subscribe or publish socket
					while (true) {
						// functionality to be done
						new ClientHandler(ss.accept()).start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

		new Thread() {
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(BROADCAST);
					while (true) {
						new AdminHandler(ss.accept()).start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}
	
	public Server() {
		groups = new ArrayList<Group>();
		totalClientsNumber = 0;
	}

	public static final class Member {
		Integer id; 
		
		public Member (Integer id){
			this.id = id;
		}
		
		public int getID () {
			return this.id;
		}
	}
	
	
	public static final class Group{
		
		String name;
		ArrayList<Member> members;
		final int maxNumber = 3;
		
		public Group (String name) {
			this.name = name;
			members = new ArrayList<Member>();
			
		}
		
		
		public boolean addMember ( Member m) {
			if (members.size() == maxNumber ) {
				return false ;
			}
			else {
				members.add(m);
			}
			return true;
		}
		
		
	}
	
	private static class ClientHandler extends Thread{
		
		Socket socket;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			
		}
		
		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			String message; 
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("Welcome, Which group do you want to join (1) Section-1 or (2) Section-2");
			
			try {
				message = in.readLine();
				boolean success = false;
				int id =0;
				if (message == "1") {
					
					synchronized(NUMBERLOCK){	
						id = 6000+totalClientsNumber++;
					}
					
					Member m = new Member(id);
					synchronized (group1){
						
						success = group1.addMember(m);
					}
					
				}
				
				else if (message == "2") {
					
					
					synchronized(NUMBERLOCK){
						id = 6000+totalClientsNumber++;	
					}
					
					Member m = new Member(id);
					synchronized (group2){
						success = group2.addMember(m);
					}				
				}
				if (success) {
					out.println("You are successfully added to the group Section-"+ message + " with ID =" + id);
				}
				else {
					out.println("Sorry, the group reached its maximum count");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // reads an integer
		
			
		}
	} 
	
	private static class AdminHandler extends Thread {
		Socket socket;
		
		public AdminHandler(Socket socket) {
			this.socket = socket;
			
		}
		
		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			String message = null; 
			String groupNumber = null;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("Welcome, Which group do you want to broadcast your message to? (1) Section-1 or (2) Section-2");
			try {
				groupNumber = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("What is the message to be broadcast?");
			try {
				message = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (groupNumber == "1") {
				
			}
			else if (groupNumber == "2") {
				
			}
			
			

			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
