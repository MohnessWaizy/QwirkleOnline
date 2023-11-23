package de.upb.cs.swtpra_04.qwirkle.model.network;

import android.util.Log;

import de.upb.cs.swtpra_04.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.messages.ConnectRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.ParsingError;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

public class NetClient implements NetworkRunnableInterface {

    /**
     * Tag for logger.
     */
    private final String TAG = "NetClient";

    /**
     * The thread where NetworkRunnable runs.
     */
    private Thread thread = null;

    /**
     * The actual started network thread.
     */
    private NetworkRunnable runnable = null;

    /**
     * Parser where messages will be parsed to message objects.
     */
    private Parser parser = null;

    /**
     * Presenter where parsed message object will be processed.
     */
    private Presenter presenter = null;

    /**
     * Instantiate NetClient for network communication.
     *
     * @param ip
     * @param port
     * @param presenter
     * @param connectionTimeout
     */
    public NetClient(String ip, int port, Presenter presenter, short connectionTimeout) {
        this.parser = new Parser();
        this.runnable = new NetworkRunnable(this, port, ip, connectionTimeout);
        this.presenter = presenter;
        this.thread = new Thread(runnable);
        Log.d(TAG, "Starting Thread...");
        thread.start();
    }

    /**
     * Blocks thread until connection to server is established or timeout reached.
     *
     * @return
     */
    public synchronized boolean checkConnection() {
        short time = 0;

        // will exit after in construct given timeout or connection is established
        while ((runnable.getConnectionState() == ConnectionState.CONNECTION_BUILDING || runnable.getConnectionState() == ConnectionState.NOT_STARTED)
                && time < 5000) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            time++;
        }

        Log.d(TAG, "Connection state: " + runnable.getConnectionState());

        if (runnable.getConnectionState() != ConnectionState.CONNECTION_SUCCESSFULL) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns actual connection state of network client.
     *
     * @return
     */
    public synchronized ConnectionState getConnectionState() {
        return runnable.getConnectionState();
    }

    /**
     * This method is called by thread when reading a new message.
     *
     * @param message
     */
    @Override
    public void processMessage(String message) {
        Log.d(TAG, "Processing message from Server: " + message);
        Message msg;
        try {
            msg = parser.deserialize(message);
            presenter.processMessage(msg);
        } catch (ParsingException ex) {
            Log.d(TAG, "Parsing error...");
            ParsingError error = new ParsingError("Message could not be parsed", 0);
            sendMessage(error);
        }
    }

    /**
     * Adds message to message queue.
     * All messages in message queue will be send to server.
     *
     * @param msg
     */
    public void sendMessage(Message msg) {
        Log.d(TAG, "Send message: " + msg.getUniqueId());
        String json = parser.serialize(msg);

        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.sendMessage(json);
            }
        });

        sendThread.start();
    }

    /**
     * Send connect request to server.
     *
     * @param username
     * @param clientType
     */
    public void connectToServer(String username, ClientType clientType) {
        ConnectRequest connectRequest = new ConnectRequest(username, clientType);
        sendMessage(connectRequest);
    }

    /**
     * Disconnects from server.
     * Joins thread properly.
     */
    public void disconnect() {
        runnable.disconnect();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
