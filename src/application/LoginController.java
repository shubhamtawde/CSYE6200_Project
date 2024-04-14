package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController implements Initializable {
	
	@FXML
	private Label warning;
	@FXML
	private TextField username, signUsername, signFullname;
	@FXML
	private PasswordField password, signPassword;
	@FXML
	private ComboBox<String> type;
	@FXML
	private VBox loginVBox, signupVBox;
	
	private String[] accountType = {"Admin", "Receptionist", "Doctor", "Patient"};
	
	public LoginController() {
		System.out.println("Login Controller iniatilize");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		signupVBox.setVisible(false);
		
		type.getItems().addAll(accountType);
		
	}
	
	public void userLogin(ActionEvent event) throws IOException {
		System.out.println("User Login");
	}
	
	public void signupToggle() {
		System.out.println("Sign Up");
	}
	
	public void loginToggle() {
		System.out.println("Login");
	}
	
	public void userSignup(ActionEvent event) throws SQLException, IOException {
		System.out.println("User Sign Up");
	}

}
