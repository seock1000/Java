import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ThreadClient {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 7777);
		System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");
		Scanner sc = new Scanner(System.in);
		Scanner in = new Scanner(socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		while(sc.hasNextLine()) {
			out.println(sc.nextLine());
			System.out.println(in.nextLine());
		}
	}
}