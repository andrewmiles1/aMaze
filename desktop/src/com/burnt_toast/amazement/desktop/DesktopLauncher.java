package com.burnt_toast.amazement.desktop;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.burnt_toast.amazement.IActivityRequestHandler;
import com.burnt_toast.amazement.MainFrame;

public class DesktopLauncher implements IActivityRequestHandler{
	private static DesktopLauncher application;
	private static DatagramSocket listeningSock;
	private static DatagramSocket sendingSock;
	private static InetAddress add;
	private static byte[] buff;
	private static DatagramPacket listeningPack;
	private static DatagramPacket sendingPack;
	private String currCode;
	
	private final int LIST_PORT = 5152;
	private final int SEND_PORT = 5153;
	
	public DesktopLauncher() {
		//initiate all the things here
/*		try {
			listeningSock = new DatagramSocket(LIST_PORT);
			sendingSock = new DatagramSocket(SEND_PORT);
			buff = new byte[256];
			listeningPack = new DatagramPacket(buff, buff.length);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public static void main (String[] arg) {
		if(application == null) {
			application = new DesktopLauncher();
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 10 * 50;
		config.height = 16 * 50;
		new LwjglApplication(new MainFrame(application), config);
	}

	@Override
	public void printMessage(String msg) {
		// TODO Auto-generated method stub
		try {
			buff = msg.getBytes();//convert the msg
			//get the packet ready
			sendingPack = new DatagramPacket(buff, buff.length, InetAddress.getLoopbackAddress(), LIST_PORT);
			//send it off
			sendingSock.send(sendingPack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//sending socket.
		
		System.out.println("MESSAGE SENT " + System.currentTimeMillis());
	}

	@Override
	public void startListening() {
		//start listening to the port
		try {
			while(this.currCode != "stop") {//as long as we didn't get anything that said stop
				System.out.println("TEST");
				listeningSock.receive(listeningPack);
				this.currCode = listeningPack.getData().toString();
				System.out.println("RECEIVED MESSAGE: " + currCode);
				currCode = "stop";
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//5153 is listening port.
		
	}

	@Override
	public void stopListening() {
		printMessage("stop");
		
	}

	@Override
	public String getCode() {
		return this.currCode;
	}
	
	
}
