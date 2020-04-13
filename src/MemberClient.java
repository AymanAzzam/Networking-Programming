import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MemberClient {
	
	static Integer port;
	public static final int SUBSCRIBE = 3000;
	public MemberClient() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket s = new Socket("localhost", SUBSCRIBE);
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter consoleOut = new PrintWriter(System.out, true);	
		PrintWriter serverOut = new PrintWriter(s.getOutputStream(), true);	
		
		
		consoleOut.println(serverIn.readLine());  // which group do you want to join
		String groupNum = consoleIn.readLine(); // for debugging
		System.out.println("you entered " + Integer.parseInt(groupNum));
		serverOut.println(groupNum);
		consoleOut.println("waiting for server response ..."); // for debugging
		String serverReply = serverIn.readLine(); // success or failure addition to group
		consoleOut.println(serverReply); // for debugging
		String str = serverReply.replaceAll("[^\\d]", " "); 
		str = str.trim(); 
	    str = str.replaceAll(" +", " "); 
	    consoleOut.println(str); // for debugging
		if(str == "") {
			consoleOut.println(serverReply);
		}
		else {
			consoleOut.println(serverReply);
			port = Integer.parseInt(str.split("\\s+")[1]);
			consoleOut.println(port); // for debugging
			byte[] receiveData = new byte[1024];
			DatagramSocket clientSocket = new DatagramSocket(port);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			byte[] recieveData = new byte[1024]; // for debugging
			while (true) {
				clientSocket.receive(receivePacket);
				recieveData = receivePacket.getData();
				System.out.println(recieveData);
				consoleOut.println(new String(recieveData)); // to be changed
			}		
		}
		consoleOut.close(); 
		serverOut.close(); 
		serverIn.close(); 
		consoleIn.close();
		s.close();	
	}

}
