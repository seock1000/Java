import java.io.*;
import java.net.*;
import java.util.*;

public class CalcServer {
	public static String calc(String exp) {
		StringTokenizer st = new StringTokenizer(exp, " ");
		if (st.countTokens() != 3) return "error";
		String res="";
		int op1 = Integer.parseInt(st.nextToken());
		String opcode = st.nextToken();
		int op2 = Integer.parseInt(st.nextToken());
		switch (opcode) {
		case "+": res = Integer.toString(op1 + op2);
		break;
		case "-": res = Integer.toString(op1 - op2);
		break;
		case "*": res = Integer.toString(op1 * op2);
		break;
		default : res = "error";
		}
		return res;
		}

	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;
		ServerSocket listener = null;
		Socket socket = null;
		
		try {
			listener = new ServerSocket(5678);
			System.out.println("Waiting for connection...");
			socket = listener.accept();
			System.out.println("Client is Connected.");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String inputMessage;
			
			while(true) {
				inputMessage = in.readLine();
				
				System.out.println(inputMessage);
				
				if(inputMessage.equalsIgnoreCase("bye")) {
					System.out.println("Disconnected by client.");
					break;
				}
				
				String outputMessage = calc(inputMessage);
				out.write(outputMessage+"\n");
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
