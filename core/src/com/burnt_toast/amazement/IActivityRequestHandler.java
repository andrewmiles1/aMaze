package com.burnt_toast.amazement;

import java.net.DatagramSocket;

public interface IActivityRequestHandler {
	public String getCode();
	public void printMessage(String msg);
	public void startListening();
	public void stopListening();
}
