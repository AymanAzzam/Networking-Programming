import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.lang.Math; 


public class Server {
	
	static int port = 7777;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		ServerSocket ss = new ServerSocket(port);
		
		System.out.println("The server started .. ");
		System.out.println();

		while (true) {	
			new ClientHandler(ss.accept()).start();
			System.out.println("i recieved  Client");
			System.out.println();
		}
	}
	
	private static class ClientHandler extends Thread {
		
		Socket socket;
		
		public ClientHandler(Socket socket) {	
			this.socket = socket;
		}

		
		public void run() {
			ObjectInputStream clientIn = null;
			PrintWriter clientOut = null;	
			int n = 0;
			
			try {
				clientIn = new ObjectInputStream(socket.getInputStream());
				clientOut = new PrintWriter(socket.getOutputStream(), true);
			} 
			catch (IOException e) {	e.printStackTrace();	}
			
			
			try {	n = clientIn.read();	} 
			catch (IOException e1) {	e1.printStackTrace();	}
			
			while(n > 0)
			{
				try {	clientOut.println(solve((Triangle)clientIn.readObject()));	} 
				catch (ClassNotFoundException e) {	e.printStackTrace();	} 
				catch (IOException e) {	e.printStackTrace();	}
				
				n--;
			}
			try {	clientIn.close(); clientOut.close();  socket.close();	} 
			catch (IOException e) {	e.printStackTrace();	} 
			
			System.out.println("i finished  Client");
			System.out.println();
		}
		
		public String solve(Triangle tri)
		{
			long l1,l2,l3;
			ArrayList<Point> points = tri.getPoints();
			
			l1 = (long) (Math.pow(points.get(0).getX()-points.get(1).getX(),2) + Math.pow(points.get(0).getY()-points.get(1).getY(),2));
			l1 = Math.round(Math.sqrt(l1));
			l2 = (long) (Math.pow(points.get(0).getX()-points.get(2).getX(),2) + Math.pow(points.get(0).getY()-points.get(2).getY(),2));
			l2 = Math.round(Math.sqrt(l2));
			l3 = (long) (Math.pow(points.get(1).getX()-points.get(2).getX(),2) + Math.pow(points.get(1).getY()-points.get(2).getY(),2));
			l3 = Math.round(Math.sqrt(l3));
			
			if(l1 == l2 && l2 == l3)
				return "Equaliateral triangle";
			
			if(l1 != l2 && l1 != l3 && l2 != l3)
				return "Scalene triangle";
			
			return "Isosceles triangle";
		}
		
	}

}
