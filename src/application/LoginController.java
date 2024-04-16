package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.Authentication;
import database.DB;
import database.SqlQueries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	private String[] accountType = { "Admin", "Receptionist", "Doctor", "Patient" };

	Authentication authentication = new Authentication();

	Connection conn;

	PreparedStatement preparedStatement = null;

	public LoginController() {
		conn = DB.DBConnection();
		if (conn == null) {
			System.exit(1);
		}

		if (!authentication.isDBConnected()) {
			warning.setText("Database not Connected");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		signupVBox.setVisible(false);

		type.getItems().addAll(accountType);

	}

	public void userLogin(ActionEvent event) throws IOException {
		try {
			switch (type.getValue()) {
			case "Admin":
				initializeDashboard("/fxml/AdminDashboard.fxml", "Admin Dashboard", event);
				break;
			case "Doctor":
				initializeDashboard("/fxml/DoctorDashboard.fxml", "Doctor Dashboard", event);
				break;
			case "Receptionist":
				initializeDashboard("/fxml/ReceptionistDashboard.fxml", "Receptionist Dashboard", event);
				break;
			case "Patient":
				initializeDashboard("/fxml/PatientDashboard.fxml", "Patient Dashboard", event);
				break;
			default:
				warning.setText("Please, fill the form completely to proceed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initializeDashboard(String dashboardFXML, String dashboardTitle, ActionEvent event)
			throws IOException, SQLException {
		if (authentication.isLogin(username.getText(), password.getText(), type.getValue())) {
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFXML));
			Pane root = loader.load();
			Scene scene = new Scene(root);

			preparedStatement = conn.prepareStatement(SqlQueries.GET_NAME_QUERY);
			preparedStatement.setString(1, username.getText());
			ResultSet resultSet = preparedStatement.executeQuery();

			// Use the Dashboard interface to interact with the dashboard controller
			Dashboard dashboard = loader.getController();
			dashboard.userInfo(resultSet.getString("fullname"), username.getText(), type.getValue());

			stage.setScene(scene);
			stage.setTitle(dashboardTitle);
			stage.getIcons().add(new Image("logo.png"));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setMinHeight(864);
			stage.setMinWidth(1200);
			stage.setMaximized(true);
			stage.show();

			conn.close();
		} else {
			warning.setText("Username and Password are Incorrect!");
		}
	}

	public void signupToggle() {
		loginVBox.setVisible(false);
		signupVBox.setVisible(true);
	}

	public void loginToggle() {
		loginVBox.setVisible(true);
		signupVBox.setVisible(false);
	}

	public void userSignup(ActionEvent event) throws SQLException, IOException {
		if (!signFullname.getText().isEmpty() && !signUsername.getText().isEmpty()
				&& !signPassword.getText().isEmpty()) {
			authentication.onSignup(signFullname.getText(), signUsername.getText(), signPassword.getText());

			((Node) event.getSource()).getScene().getWindow().hide();

			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
			Pane root;
			try {
				root = loader.load();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Hospital Management System");
				stage.setResizable(false);
				stage.show();

				conn.close();
			} catch (IOException e) {
				System.out.println("Error: " + e);
			}
		} else {
			warning.setText("Please, fill the form completely to proceed.");
		}
	}

}