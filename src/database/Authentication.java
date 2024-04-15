package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Authentication {

	Connection connection;

	public Authentication() {
		connection = DB.DBConnection();
		if (connection == null) {
			System.exit(1);
		}
		else {
			createTablesIfNotExists();
		}
	}

	private void createTablesIfNotExists() {
		// TODO Auto-generated method stub
		
		try {
			PreparedStatement createAppointmentStatement = connection.prepareStatement(SqlQueries.CREATE_APPOINTMENT_TABLE);
	        createAppointmentStatement.execute();

	        PreparedStatement createUserStatement = connection.prepareStatement(SqlQueries.CREATE_USER_TABLE);
	        createUserStatement.execute();

	        createAppointmentStatement.close();
	        createUserStatement.close();
		}
		catch (SQLException sqlException) {
			System.out.println("Error creating tables: " + sqlException.getMessage());
			System.exit(1);
		}
		
	}

	public boolean isDBConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean isLogin(String username, String password, String userType) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		System.out.println(username + " " + password);
		try {
			preparedStatement = connection.prepareStatement(SqlQueries.LOGIN_QUERY);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, userType);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return false;
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
	}

	public void onSignup(String name, String username, String password) throws SQLException {
		PreparedStatement preparedStatement = null;

		String enrollDate = LocalDate.now().toString();

		String patient = "Patient";

		try {
			String queryString = "INSERT INTO user(fullname, username, password, type, enroll) VALUES ('" + name
					+ "', '" + username + "', '" + password + "', '" + patient + "', '" + enrollDate + "')";

			preparedStatement = connection.prepareStatement(queryString);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			preparedStatement.close();
		}
	}

	public void logoutFunction(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setHeaderText("You are about to logout!");
			alert.setContentText("Are you sure to logout?");

			if (alert.showAndWait().get() == ButtonType.OK) {
				((Node) event.getSource()).getScene().getWindow().hide();
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
				Pane root;
				root = loader.load();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Hospital Management System");
				stage.getIcons().add(new Image("logo.png"));
				stage.setResizable(false);
				stage.show();
			}

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}
}
