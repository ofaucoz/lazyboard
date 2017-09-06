package remoteserver;

import controler.Controler;
import view.Interface;

public class BluetoothServer {
	public static void main(String[] args) {
	    //System.setProperty("java.awt.headless", "false");
		WaitThread subThread = new WaitThread();
		Thread waitThread = new Thread(subThread);
		Controler controler = new Controler(waitThread);
		Interface view = new Interface(controler);
		subThread.addObserver(view);		
	}

}// 