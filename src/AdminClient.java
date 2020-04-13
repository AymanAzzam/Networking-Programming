import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AdminClient {

	static Integer port;
	public static final int BROADCAST = 3001;
	public AdminClient() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub'
		
		Socket s = new Socket("localhost", BROADCAST);
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter consoleOut = new PrintWriter(System.out, true);	
		PrintWriter serverOut = new PrintWriter(s.getOutputStream(), true);	
		
		consoleOut.println(serverIn.readLine()); // group choosing
		serverOut.println(consoleIn.readLine());
		consoleOut.println(serverIn.readLine()); // message sending // try this
		
		String msg = null; 
		while(true) {
			
			msg = consoleIn.readLine();
			serverOut.println(msg);
			System.out.println(msg.length());
			if(msg.contentEquals("end")) {
				System.out.println("admin sent end ");
				break;
			} 
			
		}
		System.out.println("admin sent end ");
		s.close();
		consoleIn.close();
		consoleOut.close();
		serverIn.close();
		serverOut.close();
		
		

	}

}
