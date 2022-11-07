import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LocalDNS {
	private static String[] hostCache = new String[5];
	private static String[] ipCache = new String[5];
	private static int cacheCnt;
	
	private static int indexOf(String[] array, String str)
	{
		str = str.trim();
		for (int i=0; i < array.length; i++)
		{
			if (array[i] != null && array[i].equals(str)) return i;
		}
		return -1;
	}
	
	public static class FindIP implements Runnable {
		
		private DatagramPacket recvpack;
		
		FindIP(DatagramPacket recvpack) {
			this.recvpack = recvpack; 
		}
		
		public void run() {
			try {
				byte[] senddata = new byte[1021];
				String sen = new String(recvpack.getData());
				int clientPort = recvpack.getPort();
			
				String capsent;
			
				System.out.println("Request for host " + sen);
				
				DatagramSocket localsocket = new DatagramSocket();
			
				// 	caching hit
				if(indexOf (hostCache, sen) != -1)
					capsent = ipCache[indexOf (hostCache, sen)];
				
				// caching fail
				else {
					
					int rootPort = 9000;
					int tldPort;
				
					byte[] rootSendData = new byte[1021];
					byte[] rootRcvData = new byte[1021];
					byte[] tldSendData = new byte[1021];
					byte[] tldRcvData = new byte[1021];
					
					
					String[] str = sen.split("\\.");
					
					rootSendData = str[str.length - 1].getBytes(); 
					
					
					DatagramPacket rootSendPack = new DatagramPacket(rootSendData, rootSendData.length, InetAddress.getByName("localhost"), rootPort);
					localsocket.send(rootSendPack);
					
					
					DatagramPacket rootRcvPack = new DatagramPacket(rootRcvData, rootRcvData.length);
					localsocket.receive(rootRcvPack);
					
					String tld = new String(rootRcvData);
		
					tldPort = Integer.parseInt("9001");
					
					
					tldSendData = sen.getBytes();
					DatagramPacket tldSendPack = new DatagramPacket(tldSendData, tldSendData.length, InetAddress.getByName("localhost"), tldPort);
					localsocket.send(tldSendPack);
					
					DatagramPacket tldRcvPack = new DatagramPacket(tldRcvData, tldRcvData.length);
					localsocket.receive(tldRcvPack);
				
					capsent = new String(tldRcvPack.getData());
					
					if(!capsent.equals("Host Not Found")) {
						hostCache[cacheCnt] = sen;
						ipCache[cacheCnt] = capsent;
						
						cacheCnt = (cacheCnt + 1) % 5;
						
					}
					
				}
				
				System.out.println(capsent);
			
				senddata = capsent.getBytes();
			
				DatagramPacket pack = new DatagramPacket(senddata, senddata.length, InetAddress.getByName("localhost"), clientPort);
				localsocket.send(pack);
				
				localsocket.close();
				
			} catch(IOException e) { }
		}
	}
	
	public static void main(String arg[])throws IOException {
		
		ExecutorService pool = Executors.newFixedThreadPool(20); // Thread를 관리하기 위해 pool 선언, 20개의 Thread 생성 가능
		while (true)
		{
			DatagramSocket serversocket = new DatagramSocket(9999);

			byte[] receivedata = new byte[1021];
			DatagramPacket recvpack = new DatagramPacket(receivedata, receivedata.length);
			
			serversocket.receive(recvpack);
			
			pool.execute(new FindIP(recvpack));
			
			serversocket.close();
		}
	}
}
