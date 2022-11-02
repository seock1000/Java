import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class MultiCalculatorClient {

	public static void main(String[] args) {
		BufferedReader in = null; // �����κ��� ������� �о���� ���� ��Ʈ�� 
		BufferedReader stin = null; // �ַܼκ��� ������ �Է¹ޱ� ���� ��Ʈ��
		BufferedWriter out = null; // ������ ������ ������ ���� ��Ʈ��
		Socket socket = null; // ������ ������ ���� ����
		String path = "serverinfo.dat"; // ������ ������ ��� ������ ���
		DataInputStream dis = null; // ���� ���� ������ �о���� ���� ��Ʈ��
		
		
		try {
			// ���� ������ �б� ���� ��Ʈ�� ��ü ����
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
			// �о�� ���� ������ ������ ����
			String host = dis.readUTF();
			int port = dis.readInt();
			
			// �о�� ���� ������ ���� ���� ��ü ����
			socket = new Socket(host, port);
			
		} catch (IOException e) {
			try {
				// ���� ���������� �������� �ʴ� �� ���� �߻� �� default ��
				socket = new Socket("localhost", 5678);
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		} finally {
			try {
				// ���� ������ ��� �о�� �ڿ��� ��Ʈ�� close
				dis.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			// �� ��Ʈ���� ������ �´� ��Ʈ�� ��ü ���� 
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String outputMessage; // ������ ������ ���� ���� ���ڿ� ����
			
			System.out.print("Enter the expression(ex)ADD 10,20)>> ");
			outputMessage = stin.readLine(); // �ַܼκ��� ���ڿ��� �о�ͼ� ����
			out.write(outputMessage+"\n"); // output ��Ʈ�� ���ۿ� ������ ���ڿ��� write
			out.flush(); // ���� ����
			String inputMessage = in.readLine(); // ������ ����� ��Ʈ������ ���ڿ� �о�� �� inputMessage�� ����
			StringTokenizer st = new StringTokenizer(inputMessage, " "); // inputMessage�� ���鿡 ���ؼ� ����
			
			// ������ ��� ���ڿ��� �� ���� ���ҵǸ�
			if(st.countTokens() == 2) {	
				String prtcl = st.nextToken();
				String answer = st.nextToken(); // ���ڿ��� protocol code�� ����� ����
				if(prtcl.equalsIgnoreCase("100")) // OK code�̸�, 
					System.out.println("Answer: " + answer); // ����� ���
				else
					System.out.println("Error message: Unknown error"); // �̿��� ��쿡�� ó������ ���� ����				
			}
			
			// ���� �ڵ��� ��� �� �����ڵ忡 �ش��ϴ� ���� �޼��� ���
			else {
				if(inputMessage.equalsIgnoreCase("200"))
					System.out.println("Error message: Illegal format");
				else if(inputMessage.equalsIgnoreCase("201"))
					System.out.println("Error message: Too few arguments");
				else if(inputMessage.equalsIgnoreCase("202"))
					System.out.println("Error message: Too many arguments");
				else if(inputMessage.equalsIgnoreCase("203"))
					System.out.println("Error message: Illegal operation");
				else if(inputMessage.equalsIgnoreCase("204"))
					System.out.println("Error message: Divided by zero");
				else if(inputMessage.equalsIgnoreCase("205"))
					System.out.println("Error message: Non-number argument exist");
				else					
					System.out.println("Error message: Unknown error");
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Error Occurred!");
			}
		}

	}

}