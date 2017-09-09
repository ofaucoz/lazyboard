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

	// 0: writing mode 1: command mode(maccro)
	private int mode;

	private static ArrayList<String> catchable_command = new ArrayList<String>() {
		{
			add("youtube");
			add("google");
			add("écrire");
		}
	};

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
				if (textToBeSend.startsWith("mode commande")) {
					this.mode = 1;
				} else if (textToBeSend.startsWith("mode écriture")) {
					this.mode = 0;
				}
				if (this.mode == 0) {
					StringSelection stringSelection = new StringSelection(textToBeSend);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, stringSelection);

					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					textToBeSend = "";
				}
				if (this.mode == 1) {
					System.out.println("mode 1");
					// removing "mode commande" if it exists
					String command_line = textToBeSend.replace("mode commande", "");
					// get additional arguments, ie "youtube half life" => "half life"
					String additional_arguments = "";
					int position = 0;
					String current_command = "";
					for (String word : command_line.split(" ")) {
						System.out.println(word);
						position++;
						if (!catchable_command.contains(word)) {
							additional_arguments += word;
						}
						if (catchable_command.contains(word) || position == command_line.split(" ").length) {
							System.out.println("B1");
							if (current_command != "") {
								switch (current_command) {
								case "youtube":
									System.out.println("y" + additional_arguments);
									break;
								case "google":
									System.out.println("g" + additional_arguments);
									break;
								case "écrire":
									System.out.println("e" + additional_arguments);
									break;

								}
							}
							current_command = word;
							additional_arguments = "";
						}
					}
					textToBeSend = "";
				}
			} else {
				notifyObserver("cannot process this command");
			}

		} catch (

		Exception e) {
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