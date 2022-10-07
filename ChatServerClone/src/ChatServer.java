import java.io.*;
import java.net.*;

public class ChatServer {

	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;
		ServerSocket listener = null;
		Socket socket = null;
		
		try {
			listener = new ServerSocket(9999);
			socket = listener.accept();
			System.out.println("Client is Connected.");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String inputMessage;
			
			while(true) {
				inputMessage = in.readLine();
				
				if(inputMessage.equalsIgnoreCase("bye"))
					break;
				
				System.out.println(inputMessage);
				String outputMessage =stin.readLine();
				out.write("Server> "+ outputMessage+"\n");
				out.flush();
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
				listener.close();
			} catch (IOException e) {
				System.out.println("Error Occurred!");
			}
		}

	}

}
