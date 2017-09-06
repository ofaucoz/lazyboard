package controler;

import remoteserver.WaitThread;

public class Controler {
	private Thread waitThread;
	public Controler(Thread waitThread) {
		this.waitThread = waitThread;
	}
	
	public void start() {
		this.waitThread.start();
	}

}
