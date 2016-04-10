
/* 
 * AgentClient.java 
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

/**
 * This program is Client part of ClockSync agent.
 *
 * @author Vinay Vasant More
 *
 */

public class AgentClient {

	private BufferedReader ins;
	private PrintWriter out;
	DataInputStream input;

	public AgentClient() {
	}

	/**
	 * connectServer method connects client to the server
	 * 
	 * @param String
	 *            ip - IPAddress of machine to connect
	 * @param String
	 *            port - Port of machine to connect
	 *
	 * @return void method
	 *
	 */
	public void connectServer(String ip, int port) throws IOException {
		// Make connection and initialize streams
		Socket socket = new Socket(ip, port);
		ins = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		input = new DataInputStream(System.in);
		out = new PrintWriter(socket.getOutputStream(), true);

		String ack = ins.readLine();
		System.out.println(ack);

		// Player acknowledging its connection
		if (ack.equals("Welcome")) {
			out.println("Acknowledged");
			System.out.println("Acknowledged");

			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			// System.out.println("Current Time Stamp: " + "2016-03-31
			// 20:25:23.452");
			out.println("2016-03-31 20:25:23.452"); // should be machines current time here but we have to
								// add other hard coded one during simulation as machines may
								// have same time

			System.out.println("Message with Timestamp sent by server: " + ins.readLine());
		} else {
			System.out.println("Connection unsuccessful");
		}
	}

	/**
	 * The main program.
	 *
	 * @param args
	 *            command line arguments to get ipaddress and port
	 */
	public static void main(String[] args) throws Exception {
		AgentClient client = new AgentClient();
		String ipAddress[] = new String[10];
		int port[] = new int[10];

		System.out.print("How many machines to connect?: ");
		Scanner sc = new Scanner(System.in);
		int num = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < num; i++) {
			System.out.print("Machine" + i + ": ");
			String str = sc.nextLine();
			ipAddress[i] = str.substring(0, str.indexOf(" "));
			port[i] = Integer.parseInt(str.substring(str.indexOf(" ") + 1));
			client.connectServer(ipAddress[i], port[i]);
		}
	}
}