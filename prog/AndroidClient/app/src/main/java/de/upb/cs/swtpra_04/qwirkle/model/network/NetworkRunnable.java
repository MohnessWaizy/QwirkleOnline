package de.upb.cs.swtpra_04.qwirkle.model.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class NetworkRunnable implements Runnable {

    /**
     * TAG for Log.
     */
    private final String TAG = "NetworkRunnable";

    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private int port;
    private NetworkRunnableInterface client;
    private String ip;
    private boolean running = false;
    private ConnectionState connectionState = ConnectionState.NOT_STARTED;
    private String error = "";
    private short connectionTimeout;

    /**
     * ConnectionTimeout will be used for timeout while establishing connection to server.
     * If time will reach timeout, establishing will be cancelled.
     *
     * @param client
     * @param port
     * @param ip
     * @param connectionTimeout milliseconds
     */
    public NetworkRunnable(NetworkRunnableInterface client, int port, String ip, short connectionTimeout) {
        this.client = client;
        this.port = port;
        this.ip = ip;
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Runs method for thread.
     */
    public void run() {
        running = true;
        connectionState = ConnectionState.CONNECTION_BUILDING;
        Log.d(TAG, "Try to connect to Server " + ip);
        try {
            SocketAddress address = new InetSocketAddress(ip, port);
            socket = new Socket();
            // timeout for socket, e.g. while reading buffered reader
            socket.setSoTimeout(1000);
            socket.setKeepAlive(true);
            socket.connect(address, connectionTimeout);

            out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader input_reader = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(input_reader);
        } catch (UnknownHostException ex) {
            Log.d(TAG, "Unknown host " + ip);
            this.error = "Unknown host " + ip;
            connectionState = ConnectionState.CONNECTION_FAILED;
        } catch (IOException ex) {
            Log.d(TAG, "Couldn´t get I/O for connectionState " + ip);
            this.error = "Couldn´t get I/O for connectionState " + ip;
            connectionState = ConnectionState.CONNECTION_FAILED;
        }

        Log.d(TAG, "ConnectionState before going in while: " + connectionState.toString());
        String message = "";
        if (connectionState != ConnectionState.CONNECTION_FAILED) {
            connectionState = ConnectionState.CONNECTION_SUCCESSFULL;
            while (running) {
                // get message block
                try {
                    if (in.ready() && (message = in.readLine()) != null) {
                        if (!message.equals("")) {
                            Log.d(TAG, "Getting message" + message);
                            client.processMessage(message);
                        }
                    }
                } catch (IOException ex) {
                    Log.d(TAG, "Error reading buffered line: " + ex.getMessage());
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    // ignore exception
                }

            }
        }

        try {
            if (out != null) {
                out.close();
            }

            if (in != null) {
                in.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Log.d(TAG, "Connection close...");

        connectionState = ConnectionState.CONNECTION_CLOSED;
    }

    /**
     * Sends message to socket connection (e.g. Server).
     *
     * @param msg
     */
    public synchronized void sendMessage(String msg) {
        if (connectionState != ConnectionState.CONNECTION_FAILED && out != null) {
            Log.d(TAG, "Send raw json message...");
            try {
                out.println(msg);
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d(TAG, ex.getMessage());
            }
        }
    }

    /**
     * Ends while loop.
     */
    public synchronized void disconnect() {
        Log.d(TAG, "Set running = false for run method...");
        running = false;
    }

    /**
     * Returns error e.g. from establishing a connection.
     *
     * @return
     */
    public synchronized String getError() {
        return error;
    }

    /**
     * Returns actual connection state.
     *
     * @return
     */
    public ConnectionState getConnectionState() {
        return connectionState;
    }
}
