import java.util.Scanner;
import java.net.Socket;
import java.io.IOException;

public class DateClient {

	public static void main(String[] args) throws IOException{
		Socket socket = new Socket("localhost", 59090);
		Scanner in = new Scanner(socket.getInputStream());
		System.out.println("Server response : "+in.nextLine());

	}

}
