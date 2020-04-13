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
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket s = new Socket("localhost", SUBSCRIBE);
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter consoleOut = new PrintWriter(System.out, true);	
		PrintWriter serverOut = new PrintWriter(s.getOutputStream(), true);	
		
		
		consoleOut.println(serverIn.readLine());  // which group do you want to join
		serverOut.println(consoleIn.readLine());
		String serverReply = serverIn.readLine(); // success or failure addition to group
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
			
			// connection-less
			byte[] receiveData = new byte[1024];
			DatagramSocket clientSocket = new DatagramSocket(port);
			
			//byte[] recieveData = new byte[1024]; // for debugging
			while (true) {
				receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

				clientSocket.receive(receivePacket);
				receiveData = receivePacket.getData(); // array of bytes
				consoleOut.println(new String(receiveData));
			}		
		}
		consoleOut.close(); 
		serverOut.close(); 
		serverIn.close(); 
		consoleIn.close();
		s.close();	
	}

}
