package de.upb.cs.swtpra_04.qwirkle.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.net.InternetDomainName;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.upb.cs.swtpra_04.qwirkle.controller.LoginController;
import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.messages.ConnectAccepted;

/**
 * LoginActivity class provides the controll funktions for the Aktivity
 */
public class LoginActivity extends AppCompatActivity implements LoginController {

    private static final String TAG = "LOGIN_ACTIVITY";

    private Presenter presenter;

    private EditText editText_username;
    private AutoCompleteTextView autoCompleteTextView_ipAdress;
    private EditText editText_port;
    private Button button_connect;
    private boolean connectionAcceptedResponse = false;

    private Connector connector;

    private ArrayList<String> ips;

    private static final Pattern IPPATTERN = Pattern
            .compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    /**
     * Function called when activity is created and instantiates all GUI elements
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // checkIpFile();
        setContentView(R.layout.activity_login);

        editText_username = (EditText) findViewById(R.id.editText_username);
        autoCompleteTextView_ipAdress = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_ipAdress);
        editText_port = (EditText) findViewById(R.id.editText_port);
        button_connect = (Button) findViewById(R.id.button_connect);

        ips = loadIps();
        System.out.println(ips);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ips);

        autoCompleteTextView_ipAdress.setAdapter(adapter);

        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connector = new Connector();
                connector.execute();
            }
        });

        presenter = Presenter.getInstance();
        presenter.registerLoginActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Loads the last used ips from the save file.
     *
     * @return ArrayList<String> the last used Ips
     */
    private ArrayList<String> loadIps() {

        ArrayList<String> ips = new ArrayList<String>();

        try {
            InputStream is = getApplicationContext().openFileInput("ipsave.txt");

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String text;

                while ((text = br.readLine()) != null) {
                    ips.add(text);
                }
                br.close();
                isr.close();

            }
            is.close();
        } catch (Exception e) {
            System.err.println("Error beim Laden der IP's: " + e.toString());
        }

        return ips;

    }

    /**
     * Stores the Ip into the Array and deletes the last used Ip if the size is greater than 5
     */
    private void storeIp() {

        String ip = autoCompleteTextView_ipAdress.getText().toString();
        if (!ips.contains(ip)) {

            if (ips.size() > 4) {
                ips.add(ip);
                ips.remove(0);
            } else {
                ips.add(ip);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                    ips);

            autoCompleteTextView_ipAdress.setAdapter(adapter);
        } else {
            ips.remove(ip);
            ips.add(ip);
        }
    }


    /**
     * Saves the Ips from the ArrayList into a save file.
     */
    private void saveIps() {
        try {
            OutputStream os = getApplicationContext().openFileOutput("ipsave.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            for (String item : ips) {
                bw.write(item);
                bw.newLine();
            }

            bw.close();
            osw.close();
            os.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public void onLoginAccepted(ConnectAccepted connectAccepted) {
        Log.d(TAG, "Login accepted from server...");
        presenter.setClientID(connectAccepted.getClientId());
        connectionAcceptedResponse = true;
    }

    @Override
    public void handleNotAllowed(String message) {
        Toast.makeText(LoginActivity.this, "Not allowed to connect to the Sever. Try another username.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleParsingError(String message) {
        Toast.makeText(LoginActivity.this, "An Error occured.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleAccessDenied(String message) {
    }


    /**
     * Connector class is a AsyncTask used for connecting to a Socket
     */
    private class Connector extends AsyncTask<Void, Void, Void> {

        private boolean connectionEstablished = false;

        private LoadingDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Show connection label...");
            dialog = new LoadingDialog(LoginActivity.this, "Connecting to server...");
            dialog.showDialog();
        }

        /**
         * Tries to connect to Socket in the Background
         *
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            if (!editText_username.getText().equals("")
                    && testIp(autoCompleteTextView_ipAdress.getText().toString())
                    && testPort(editText_port.getText().toString())) {
                connectionEstablished = presenter.connectClientToServer(editText_username.getText().toString(),
                        autoCompleteTextView_ipAdress.getText().toString(),
                        Integer.parseInt(editText_port.getText().toString()));
            }

            if (connectionEstablished) {
                Log.d(TAG, "Connection to server established correctly...");
            } else {
                Log.d(TAG, "Connection to server isn´t established....");
            }

            if (connectionEstablished) {
                // wait 2 seconds to get ConnectionAccepted 101 from server
                short timeout = 5000;
                short time = 0;
                while (time < timeout && !connectionAcceptedResponse) {
                    time++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Log.d(TAG, "Exception:" + ex.getMessage());
                    }
                }
            }
            return null;
        }

        /**
         * After trying to connect to Server, the result gets shown
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (!connectionEstablished) {
                Log.d(TAG, "Connection isn´t established, some error occured");
                Toast.makeText(LoginActivity.this, "Unable to connect to server...", Toast.LENGTH_LONG).show();
            } else if (!connectionAcceptedResponse) {
                Log.d(TAG, "Did not get ConnectionAccepted 101 from server...");
                Toast.makeText(LoginActivity.this, "Server did not response properly...", Toast.LENGTH_LONG).show();
            } else {
                changeToLobby();
            }
        }

        /**
         * Method to change to the Lobby.
         * First store the Ip in the ArrayList.
         * Then switch to LobbyActivity.
         */
        public void changeToLobby() {
            storeIp();
            // connection should be established again when back in lobby
            connectionAcceptedResponse = false;
            saveIps();
            Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
            startActivity(intent);
        }

        /**
         * Checks if the given String is a valid IPv4 address
         *
         * @param ip String of an IP
         * @return Valid -> false<br>
         * Invalid -> true
         */
        private boolean testIp(String ip) {

            if (ip.equals("")) {
                return false;
            } else if (ip.equals("localhost")) {
                return true;
            } else if (InternetDomainName.isValid(ip)) {
                return true;
            } else {
                Log.d("Validation", "IS NOT VALID");
                return IPPATTERN.matcher(ip).matches();
            }


        }

        /**
         * Checks the Port if its valid.
         *
         * @param inputAge The String of the port the user wanted to select
         * @return Valid -> false<br>
         * Invalid -> true
         */
        private boolean testPort(String inputAge) {
            try {
                Integer age = Integer.valueOf(inputAge);
                if (age >= 0 && age <= 65535) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }

        }

    }
}
