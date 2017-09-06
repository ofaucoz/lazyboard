package remoteserver;

import java.util.ArrayList;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import observer.Observable;
import observer.Observer;

public class WaitThread implements Runnable, Observable {
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();


    /** Constructor */
    public WaitThread() {
    }

    @Override
    public void run() {
        waitForConnection();
    }

    /** Waiting for connection from devices */
    private void waitForConnection() {
        // retrieve the local Bluetooth device object
        LocalDevice local = null;

        StreamConnectionNotifier notifier;
        StreamConnection connection = null;

        // setup the server to listen for connection
        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            String url = "btspp://localhost:" + "0511a889e39e475b8789b61f74fd92d5" + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier)Connector.open(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
                // waiting for connection
        while(true) {
            try {
                notifyObserver("waiting for connection...");
                        connection = notifier.acceptAndOpen();
                ProcessConnectionThread subThread = new ProcessConnectionThread(connection);
                Thread processThread = new Thread(subThread);
                subThread.addObserver(listObserver.get(0));
                processThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
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
