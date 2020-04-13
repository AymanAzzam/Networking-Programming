import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	
	static int port = 7777;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket s = new Socket("localhost", port);
		
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter consoleOut = new PrintWriter(System.out, true);	
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		ObjectOutputStream serverOut = new ObjectOutputStream(s.getOutputStream());
		
		ArrayList<Triangle>triangles = new ArrayList<Triangle>();
		
		triangles = readFile(consoleIn,consoleOut);
		
		
		
		serverOut.write(triangles.size());
		for(int i=0;i<triangles.size();i++)
		{
			consoleOut.println();
			consoleOut.println("Press Enter to send Triangle " + Integer.toString(i+1));
			consoleIn.readLine();
			
			serverOut.writeObject(triangles.get(i));
			serverOut.flush();
			
			consoleOut.println(serverIn.readLine());
		}
		s.close();	consoleIn.close(); consoleOut.close(); serverIn.close();	serverOut.close();
	}
	
	public static ArrayList<Triangle> readFile(BufferedReader consoleIn, PrintWriter consoleOut) throws UnknownHostException, IOException {
		int n;
		String point;
		ArrayList<Triangle>triangles = new ArrayList<Triangle>();
		BufferedReader fileIn = null;	
		
		consoleOut.println("Enter the file name:");
		fileIn = new BufferedReader(new FileReader(consoleIn.readLine()));

		n = Integer.parseInt(fileIn.readLine());
		
		for(int i=0;i<n;i++)
		{
			Triangle tri = new Triangle();
			fileIn.readLine();
			for(int j=0;j<3;j++)
			{
				point = fileIn.readLine();
				tri.insertPoint(new Point(Integer.parseInt(point.split("\\s")[0]),Integer.parseInt(point.split("\\s")[1])));
			}
			triangles.add(tri);
		}
		
		fileIn.close();
		
		return triangles;
	}
}
