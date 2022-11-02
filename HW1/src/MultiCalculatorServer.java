import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiCalculatorServer {
	

	public static void main(String[] args) {
		
		ServerSocket listener = null; // 클라이언트의 요청을 받을 Server Socket
		
		try {
			listener = new ServerSocket(5678); // Server Socket 객체 생성
			ExecutorService pool = Executors.newFixedThreadPool(20); // Thread를 관리하기 위해 pool 선언, 20개의 Thread 생성 가능
			System.out.println("Waiting for connection...");
		
			while(true) {
				Socket socket = listener.accept(); // 클라이언트의 요청 들어오면 accept() method로 해당 클라이언트와 통신을 위한 socket 객체 생성
				pool.execute(new Calculator(socket)); // 생성된 소켓을 Calculator 클래스 객체를 생성하며 인자로 전달, 해당 객체는 Thread로 관리
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				listener.close(); // 작업이 끝나면 Server Socket 객체 close
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
			
		
	private static class Calculator implements Runnable {
		private Socket socket;
		
		Calculator(Socket socket) {
			this.socket = socket; // Calculator 객체 생성 시 인자로 받은 socket 객체를 본인의 socket 객체로 저장 
		}
		
		// 실제 계산을 수행하는 method, 클래스 내부에서만 호출하기 때문에 private 
		private String calc(String exp) {
			StringTokenizer st = new StringTokenizer(exp, " "); // 전달 받은 문자열을 공백으로 분할
			String sem = "100"; // semantic을 저장하는 변수
			String res=""; // 계산 결과를 저장하는 변수
			// 전달 받은 문자열이 형식에 맞지 않으면
			if (st.countTokens() != 2) {
				sem = "200"; 
				return sem; // error code 200 반환
			}
			String opcode = st.nextToken(); // 연산자 저장
			
			exp = st.nextToken();
			st = new StringTokenizer(exp, ","); // 피연산자 분할
			// 피연산자의 개수가 적으면
			if (st.countTokens() < 2) { 
				sem = "201";
				return sem; // error code 201 반환
			}
			// 피연산자의 개수가 많으면
			else if (st.countTokens() > 2) {
				sem = "202";
				return sem; // error code 202 반환
			}
			
			// 피연산자를 문자열로 분활
			String op1str = st.nextToken();
			String op2str = st.nextToken(); // 피연산자 저장 변수
			// 피연산자가 숫자가 아니면
			if(!op1str.matches("[+-]?\\d*(\\.]]d+)?")||!op2str.matches("[+-]?\\d*(\\.]]d+)?")) {
				sem = "205";
				return sem; // error code 205 반환
			}
			
			// 피연산자 문자열을 숫자로 변환
			int op1 = Integer.parseInt(op1str);
			int op2 = Integer.parseInt(op2str);
			// 연산자에 따라 피연산자를 연산
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
					return sem; // error code 204 반환
				}
				res = Integer.toString(op1/op2);
			}
			// 이외의 연산자일 경우
			else {
				sem = "203";
				return sem; // error code 203 반환
			}
			
			sem = "100"; // OK code 100
			return sem + " " + res;
		}
		
		// 클래스 객체 생성 시 실행되는 method 
		public void run() {
			
			System.out.println("Connected : "+ socket);
			BufferedReader in = null; // client가 전달한 데이터를 읽어오는 스트림
			BufferedWriter out = null; // 결과를 client에게 내보내는 스트림
			
			try {
				// 인자로 전달받은 소켓을 통해 client와 통신을 위한 스트림 객체 생성
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String inputMessage; // 읽어온 문자열을 저장하기 위한 변수
				
				inputMessage = in.readLine(); // input 스트림을 통해 문지열 read
				
				System.out.println(inputMessage);
					
				String outputMessage = calc(inputMessage); // clac method에서 연선한 결과를 저장
				out.write(outputMessage+"\n"); // 저장한 결과 버퍼에 write
				out.flush(); // 버퍼를 비워줌
			} catch(IOException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					System.out.println("Disconnected : "+socket);
					socket.close();
					in.close();
					out.close(); // stream 형성중인 객체들 close
				} catch (IOException e) {
					System.out.println("Error Occurred!");
				}
			}
		}
	}
}