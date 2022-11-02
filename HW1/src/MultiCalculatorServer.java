import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiCalculatorServer {
	

	public static void main(String[] args) {
		
		ServerSocket listener = null; // Ŭ���̾�Ʈ�� ��û�� ���� Server Socket
		
		try {
			listener = new ServerSocket(5678); // Server Socket ��ü ����
			ExecutorService pool = Executors.newFixedThreadPool(20); // Thread�� �����ϱ� ���� pool ����, 20���� Thread ���� ����
			System.out.println("Waiting for connection...");
		
			while(true) {
				Socket socket = listener.accept(); // Ŭ���̾�Ʈ�� ��û ������ accept() method�� �ش� Ŭ���̾�Ʈ�� ����� ���� socket ��ü ����
				pool.execute(new Calculator(socket)); // ������ ������ Calculator Ŭ���� ��ü�� �����ϸ� ���ڷ� ����, �ش� ��ü�� Thread�� ����
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				listener.close(); // �۾��� ������ Server Socket ��ü close
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
			
		
	private static class Calculator implements Runnable {
		private Socket socket;
		
		Calculator(Socket socket) {
			this.socket = socket; // Calculator ��ü ���� �� ���ڷ� ���� socket ��ü�� ������ socket ��ü�� ���� 
		}
		
		// ���� ����� �����ϴ� method, Ŭ���� ���ο����� ȣ���ϱ� ������ private 
		private String calc(String exp) {
			StringTokenizer st = new StringTokenizer(exp, " "); // ���� ���� ���ڿ��� �������� ����
			String sem = "100"; // semantic�� �����ϴ� ����
			String res=""; // ��� ����� �����ϴ� ����
			// ���� ���� ���ڿ��� ���Ŀ� ���� ������
			if (st.countTokens() != 2) {
				sem = "200"; 
				return sem; // error code 200 ��ȯ
			}
			String opcode = st.nextToken(); // ������ ����
			
			exp = st.nextToken();
			st = new StringTokenizer(exp, ","); // �ǿ����� ����
			// �ǿ������� ������ ������
			if (st.countTokens() < 2) { 
				sem = "201";
				return sem; // error code 201 ��ȯ
			}
			// �ǿ������� ������ ������
			else if (st.countTokens() > 2) {
				sem = "202";
				return sem; // error code 202 ��ȯ
			}
			
			// �ǿ����ڸ� ���ڿ��� ��Ȱ
			String op1str = st.nextToken();
			String op2str = st.nextToken(); // �ǿ����� ���� ����
			// �ǿ����ڰ� ���ڰ� �ƴϸ�
			if(!op1str.matches("[+-]?\\d*(\\.]]d+)?")||!op2str.matches("[+-]?\\d*(\\.]]d+)?")) {
				sem = "205";
				return sem; // error code 205 ��ȯ
			}
			
			// �ǿ����� ���ڿ��� ���ڷ� ��ȯ
			int op1 = Integer.parseInt(op1str);
			int op2 = Integer.parseInt(op2str);
			// �����ڿ� ���� �ǿ����ڸ� ����
			if(opcode.equalsIgnoreCase("ADD"))
				res = Integer.toString(op1+op2);
			else if(opcode.equalsIgnoreCase("MINUS"))
				res = Integer.toString(op1-op2);
			else if(opcode.equalsIgnoreCase("MUL"))
				res = Integer.toString(op1*op2);
			else if(opcode.equalsIgnoreCase("DIV")) {
				// divided by zero
				if(op2 == 0) {
					sem = "204";
					return sem; // error code 204 ��ȯ
				}
				res = Integer.toString(op1/op2);
			}
			// �̿��� �������� ���
			else {
				sem = "203";
				return sem; // error code 203 ��ȯ
			}
			
			sem = "100"; // OK code 100
			return sem + " " + res;
		}
		
		// Ŭ���� ��ü ���� �� ����Ǵ� method 
		public void run() {
			
			System.out.println("Connected : "+ socket);
			BufferedReader in = null; // client�� ������ �����͸� �о���� ��Ʈ��
			BufferedWriter out = null; // ����� client���� �������� ��Ʈ��
			
			try {
				// ���ڷ� ���޹��� ������ ���� client�� ����� ���� ��Ʈ�� ��ü ����
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String inputMessage; // �о�� ���ڿ��� �����ϱ� ���� ����
				
				inputMessage = in.readLine(); // input ��Ʈ���� ���� ������ read
				
				System.out.println(inputMessage);
					
				String outputMessage = calc(inputMessage); // clac method���� ������ ����� ����
				out.write(outputMessage+"\n"); // ������ ��� ���ۿ� write
				out.flush(); // ���۸� �����
			} catch(IOException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					System.out.println("Disconnected : "+socket);
					socket.close();
					in.close();
					out.close(); // stream �������� ��ü�� close
				} catch (IOException e) {
					System.out.println("Error Occurred!");
				}
			}
		}
	}
}