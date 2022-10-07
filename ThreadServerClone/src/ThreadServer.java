import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class ThreadServer {

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(7777);
		System.out.println("The capitalization server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(20);
		while(true) {
			Socket sock = listener.accept();
			pool.execute(new Capitalizer(sock));
		}
	}
	
	private static class Capitalizer implements Runnable {
		private Socket socket;
		
		Capitalizer(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				while(in.hasNextLine() ) {
					out.println(in.nextLine().toUpperCase());
				}
			} catch (Exception e) {
				System.out.println("Error: " + socket);
			} finally {
				try { socket.close(); } catch (IOException e) {}
				System.out.println("Closed: " + socket);
			}
		}
	}
}
