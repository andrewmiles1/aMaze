package com.burnt_toast.amazement.desktop;

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
	private static byte[] buff = new byte[256];
	private static DatagramPacket listeningPack;
	private static DatagramPacket sendingPack;
	
	public DesktopLauncher() {
		//initiate all the things here
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
		if(sendingSock == null) {
			try {
				sendingSock = new DatagramSocket(5153);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//sending socket.
		}
		
		buff = msg.getBytes();
		
		System.out.println("MESSAGE SENT " + System.currentTimeMillis());
	}

	@Override
	public void startListening() {
		//start listening to the port
		try {
			listeningSock = new java.net.DatagramSocket(5154);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//5153 is listening port.
		
	}

	@Override
	public void stopListening() {
		// TODO Auto-generated method stub
		
	}
	
	
}
