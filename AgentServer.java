
/* 
 * AgentServer.java 
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This program is Server part of ClockSync agent
 * 
 * @author Vinay Vasant More
 *
 */

public class AgentServer {
	static String[] clockSyncTable = new String[10];

	/**
	 * The main program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) throws Exception {
		int client = 0;
		ServerSocket listener = new ServerSocket(10003);
		System.out.println("Server is running.");
		System.out.println("Server Socket Info: "+ InetAddress.getLocalHost().getHostAddress() +"  "+100003);
		try {
			while (true) {
				new Client(listener.accept(), client++).start();
			}
		} finally {
			listener.close();
		}
	}

	/**
	 * This class defines client thread.
	 * 
	 * @author Vinay Vasant More
	 *
	 */
	private static class Client extends Thread {
		private Socket socket;
		private int clientNo;

		// constructor setting socket and client number
		public Client(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNo = clientNumber;
			System.out.println("\nNew connection with client# " + clientNumber + " at " + socket);
		}

		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				out.println("Welcome");
				if (in.readLine().equals("Acknowledged")) {
					Date date = new Date();
					long time = date.getTime();
					Timestamp sysTime = new Timestamp(time);
					Timestamp ts = sysTime.valueOf(in.readLine());
					System.out.println(sysTime + "   :   " + ts);
					long offset = sysTime.getTime() - ts.getTime() + 9;
					clockSyncTable[clientNo] = offset + "";
				}
				System.out.println("Machine   Offset");
				for (int i = 0; i < clockSyncTable.length; i++) {
					System.out.println("Machine" + i + "  " + clockSyncTable[i]);
				}
				//System.out.print("\n"+new Timestamp(new Date().getTime())+"    "+clockSyncTable[clientNo]);
				out.println("Sync Check with timestamp "+new Timestamp(new Date().getTime()-Long.parseLong(clockSyncTable[clientNo])));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("\nProblem while closing a socket");
			}
			System.out.println("\nConnection with " + clientNo + " closed");
		}
	}
}