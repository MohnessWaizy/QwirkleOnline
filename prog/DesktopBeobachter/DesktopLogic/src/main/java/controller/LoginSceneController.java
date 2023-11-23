package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.NetworkInterface.NetClient;

import com.google.common.net.InternetDomainName;

import de.upb.swtpra1819interface.messages.ConnectRequest;
import de.upb.swtpra1819interface.models.ClientType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.LoginWrapper;
import parser.LoginParser;

/**
 * <p>
 * The <i>LoginSceneController</i> is the controller for the login interface. It
 * delegates the control of the login.
 * </p>
 * 
 * It inherits from AbstractSceneController.<br>
 * And it implements the methods from the LoginSceneControllerInterface.
 * 
 */
public class LoginSceneController extends AbstractSceneController implements LoginSceneControllerInterface {

	@FXML
	Button button_login;
	@FXML
	ComboBox<String> comboBox_Ip;
	@FXML
	TextField textField_port;
	@FXML
	TextField textField_name;
	@FXML
	Label label_error;

	final int ipCount = 3;
	LoginParser lp;
	NetClient netClient = null;
	boolean requestSend = false;
	String savePath;

	private static final Pattern IPPATTERN = Pattern
			.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

	/**
	 * Constructor from LoginSceneController
	 * 
	 */
	public LoginSceneController() {
		super();
		File saveFile = new File(System.getProperty("user.home") + "\\AppData\\Local\\Qwirkle\\ipsave.txt");
		if (!saveFile.exists()) {
			saveFile.getParentFile().mkdir();
			try {
				saveFile.createNewFile();
				savePath = saveFile.getPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			savePath = saveFile.getPath();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button_login.setOnAction(action -> {
			this.login();
		});

		// Login with enter key
		comboBox_Ip.setOnKeyPressed(action -> {
			if (action.getCode() == KeyCode.ENTER) {
				login();
			}
		});

		textField_port.setOnKeyPressed(action -> {
			if (action.getCode() == KeyCode.ENTER) {
				login();
			}
		});

		textField_name.setOnKeyPressed(action -> {
			if (action.getCode() == KeyCode.ENTER) {
				login();
			}
		});

		this.getSavedIps();

	}

	@Override
	public void onSceneSwitch() {
		this.netClient = null;
	}

	/**
	 * Main Login function. Where the Inputfields gets checked.
	 * 
	 */
	private void login() {

		boolean loginAllowed = true;

		// validate IP
		if (comboBox_Ip.getValue() == null || !this.testIp(comboBox_Ip.getValue().toString())) {
			comboBox_Ip.setStyle("-fx-border-color: red;");
			loginAllowed = false;
		} else {
			comboBox_Ip.setStyle("-fx-border-color: none;");

		}

		// validate port
		if (!this.testPort(textField_port.getText())) {
			textField_port.setStyle("-fx-border-color: red;");
			loginAllowed = false;
		} else {
			textField_port.setStyle("-fx-border-color: none;");
		}

		// validate name
		if (!this.testName(textField_name.getText())) {
			textField_name.setStyle("-fx-border-color: red;");
			loginAllowed = false;
		} else {
			textField_name.setStyle("-fx-border-color: none;");
		}

		if (loginAllowed) {

			// For ipsave
			this.addLastIp(comboBox_Ip.getValue().toString());

			// Saves the given login informations
			int port = Integer.parseInt(textField_port.getText());
			String ip = comboBox_Ip.getValue().toString();

			// Connection gets established
			if (this.netClient == null) {
				this.netClient = new NetClient(port, ip);

				if (this.netClient.checkConnection()) {
					ConnectRequest request = new ConnectRequest(this.textField_name.getText(), ClientType.SPECTATOR);
					this.lp = new LoginParser(this.netClient, this, 1000);

					if (!this.requestSend) {
						this.netClient.sendMsg(request);
						this.requestSend = true;
						this.label_error.setText("Anmelden...");
					} else {
						System.out.println("Request sent");
					}

				} else {
					this.label_error.setText(
							"Es konnte keine Verbindung mit den Angegebenen Daten erstellt werden.\nBitte andere Daten eingeben.");
					this.netClient = null;
				}
			}

		}
	}

	/**
	 * Checks the name if its valid.
	 * 
	 * 
	 * @param inputAge
	 *            The String of the name the user wanted to select
	 * @return Valid -> false<br>
	 *         Invalid -> true
	 */
	private boolean testName(String name) {
		if (name.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks the Port if its valid.
	 * 
	 * 
	 * @param inputAge
	 *            The String of the port the user wanted to select
	 * @return Valid -> false<br>
	 *         Invalid -> true
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

	/**
	 * Checks if the given String is a valid IPv4 address
	 * 
	 * @param ip
	 *            String of an IP
	 * @return Valid -> false<br>
	 *         Invalid -> true
	 */
	private boolean testIp(String ip) {

		if (ip.equals("")) {

			return false;

		} else if (ip.equals("localhost")) {
			return true;
		} else if (InternetDomainName.isValid(ip)) {
			return true;
		} else {
			return IPPATTERN.matcher(ip).matches();
		}

	}

	/**
	 * Adds the given value to the IP combobox.<br>
	 * If the ComboBox has more than 3 options, the last option gets removed.
	 * 
	 * @param value
	 *            String of an IP
	 */
	private void addLastIp(String value) {
		ObservableList<String> items = comboBox_Ip.getItems();
		if (!items.contains(value)) {
			if (items.size() == this.ipCount) {
				items.remove(this.ipCount - 1, this.ipCount);
			}
			comboBox_Ip.getItems().add(0, value);
			comboBox_Ip.getSelectionModel().select(0);
		}
	}

	/**
	 * Gets the saved Ips from the txt File in the saves folder and adds them into
	 * the Combobox
	 * 
	 */
	private void getSavedIps() {
		ObservableList<String> items = comboBox_Ip.getItems();

		try {

			FileReader fr = new FileReader(savePath);
			BufferedReader br = new BufferedReader(fr);

			String read;
			while ((read = br.readLine()) != null) {
				items.add(read);

			}
			fr.close();
			br.close();

			comboBox_Ip.setItems(items);

		} catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}

	}

	/**
	 * Saves the Values from the Combobox in the txt File located in the saves
	 * folder
	 * 
	 */
	private void saveIps() {
		ObservableList<String> items = comboBox_Ip.getItems();

		try {
			FileWriter fw = new FileWriter(savePath, false);
			BufferedWriter bw = new BufferedWriter(fw);

			for (String string : items) {
				bw.write(string);
				bw.newLine();
			}

			bw.close();
			fw.close();

		} catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}
	}

	/**
	 * Method used when the server sends a loginRequestValid message
	 * 
	 * Called by {@link LoginParser}
	 * 
	 */
	@Override
	public void loginRequestIsValid(int clientId) {
		saveIps();

		LoginWrapper loginWrapper = new LoginWrapper(this.netClient, clientId, this.textField_name.getText(),
				ClientType.SPECTATOR);
		this.lp.stopParser();
		this.requestSend = false;
		this.label_error.setText("");

		switchScene(SceneMapping.LOBBY, loginWrapper);
	}

	/**
	 * Method used when the server sends a NotAllowed message when try to log in
	 * 
	 * Called by {@link LoginParser}
	 */
	@Override
	public void loginRequestNotValid() {
		this.netClient = null;
		this.requestSend = false;

		this.label_error.setText("Verbindung vom Server abgelehnt. \nVersuchen Sie es mit einem anderen Namen");

	}

}
