package remoteserver;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.io.StreamConnection;

import observer.Observable;
import observer.Observer;

public class ProcessConnectionThread implements Runnable, Observable {

	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	private String textToBeSend = "";

	private StreamConnection mConnection;

	// Constant that indicate command from devices
	private static final int EXIT_CMD = -1;

	public ProcessConnectionThread(StreamConnection connection) {
		mConnection = connection;
	}

	@Override
	public void run() {
		try {
			// prepare to receive data
			InputStream inputStream = mConnection.openInputStream();

			notifyObserver("waiting for input");

			while (true) {
				int command = inputStream.read();

				if (command == EXIT_CMD) {
					notifyObserver("finish process");
					break;
				}
				processCommand(command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process the command from client
	 * 
	 * @param command
	 *            the command code
	 */
	private void processCommand(int command) {

		try {
			String ascii = Character.toString((char) command);
			if (command >= 32 && command <= 254) {
				System.out.println(command);
				textToBeSend += ascii;
			} else if (command == 10) {
				StringSelection stringSelection = new StringSelection(textToBeSend);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, stringSelection);

				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				textToBeSend = "";
			} else {
				notifyObserver("cannot process this command");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addObserver(Observer obs) {

		this.listObserver.add(obs);

	}

	public void notifyObserver(String str) {
		for (Observer obs : listObserver)

			obs.update(str);

	}

	public void removeObserver() {

		listObserver = new ArrayList<Observer>();

	}
}