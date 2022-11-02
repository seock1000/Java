import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class MultiCalculatorClient {

	public static void main(String[] args) {
		BufferedReader in = null; // 서버로부터 결과값을 읽어오기 위한 스트림 
		BufferedReader stin = null; // 콘솔로부터 수식을 입력받기 위한 스트림
		BufferedWriter out = null; // 수식을 서버로 보내기 위한 스트림
		Socket socket = null; // 서버와 연결을 위한 소켓
		String path = "serverinfo.dat"; // 서버의 정보가 담긴 파일의 경로
		DataInputStream dis = null; // 서버 정보 파일을 읽어오기 위한 스트림
		
		
		try {
			// 서버 정보를 읽기 위한 스트림 객체 생성
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
			// 읽어온 서버 정보를 변수로 저장
			String host = dis.readUTF();
			int port = dis.readInt();
			
			// 읽어온 서버 정보에 따라 소켓 객체 생성
			socket = new Socket(host, port);
			
		} catch (IOException e) {
			try {
				// 서버 정보파일이 존재하지 않는 등 문제 발생 시 default 값
				socket = new Socket("localhost", 5678);
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		} finally {
			try {
				// 서버 정보를 모두 읽어온 뒤에는 스트림 close
				dis.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			// 각 스트림의 목적에 맞는 스트림 객체 생성 
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String outputMessage; // 서버에 전달할 수식 저장 문자열 선언
			
			System.out.print("Enter the expression(ex)ADD 10,20)>> ");
			outputMessage = stin.readLine(); // 콘솔로부터 문자열을 읽어와서 저장
			out.write(outputMessage+"\n"); // output 스트림 버퍼에 저장한 문자열을 write
			out.flush(); // 버퍼 비우기
			String inputMessage = in.readLine(); // 서버와 연결된 스트림으로 문자열 읽어온 뒤 inputMessage에 저장
			StringTokenizer st = new StringTokenizer(inputMessage, " "); // inputMessage를 공백에 대해서 분할
			
			// 분할한 결과 문자열이 두 개로 분할되면
			if(st.countTokens() == 2) {	
				String prtcl = st.nextToken();
				String answer = st.nextToken(); // 문자열을 protocol code와 결과로 분할
				if(prtcl.equalsIgnoreCase("100")) // OK code이면, 
					System.out.println("Answer: " + answer); // 결과값 출력
				else
					System.out.println("Error message: Unknown error"); // 이외의 경우에는 처리되지 않은 에러				
			}
			
			// 에러 코드인 경우 각 에러코드에 해당하는 에러 메세지 출력
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