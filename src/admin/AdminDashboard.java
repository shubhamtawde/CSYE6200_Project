package admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.Dashboard;
import database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class AdminDashboard implements Initializable, Dashboard {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Label username, accountType, nameLabel, warning;

	@FXML
	private TableView<UserModel> model;

	@FXML
	private TableView<UserModel> tv;

	@FXML
	private TableColumn<UserModel, Integer> tvID;

	@FXML
	private TableColumn<UserModel, String> tvFullname, tvUsername, tvPassword, tvType, tvPhone, tvEmail,
			tvQualification, tvExpertise, tvEnroll, tvDOB, tvAddress, tvGender;

	@FXML
	private ComboBox<String> tfType, tfGender;

	@FXML
	private TextField tfID, tfUsername, tfFullname, tfEmail, tfPhone, tfAddress, tfQualification, tfExpertise;

	@FXML
	private PasswordField tfPassword;

	@FXML
	private DatePicker tfEnroll, tfDOB;

	Authentication authentication = new Authentication();

	Connection connection = DB.DBConnection();

	private String[] accountTypes = { "Admin", "Receptionist", "Doctor", "Patient", "Worker" };

	private String[] genders = { "Male", "Female", "Others" };

	String userString = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tfType.getItems().addAll(accountTypes);
		tfGender.getItems().addAll(genders);
		try {
			showUserList();
		} catch (SQLException e) {
			warning.setText("Couldn't Load Data");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void userInfo(String name, String user, String type) {
		username.setText(user);
		accountType.setText(type);
		nameLabel.setText(name);

		userString = user;
	}

	@Override
	public void logout(@SuppressWarnings("exports") ActionEvent event) {
		authentication.logoutFunction(event);
	}

	@SuppressWarnings("exports")
	public ObservableList<UserModel> getUserList(String type) throws SQLException {
		ObservableList<UserModel> userList = FXCollections.observableArrayList();

		Connection connection = DB.DBConnection();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		String queryString = "SELECT * FROM user WHERE type = '" + type + "'";

		try {
			preparedStatement = connection.prepareStatement(queryString);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString("username"));
				System.out.println(resultSet.getString("password"));
				UserModel users = new UserModel(resultSet.getInt("id"), resultSet.getString("username"),
						resultSet.getString("password"), resultSet.getString("fullname"), resultSet.getString("email"),
						resultSet.getString("phone"), resultSet.getString("address"), resultSet.getString("dob"),
						resultSet.getString("type"), resultSet.getString("enroll"),
						resultSet.getString("qualification"), resultSet.getString("expertise"),
						resultSet.getString("gender"));

				userList.add(users);
			}
		} catch (Exception e) {
			warning.setText("Couldn't Fetch Data");
			System.out.println(e.getMessage());
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
		return userList;
	}

	public void showUserList() throws SQLException, IOException {
		ObservableList<UserModel> usersList = getUserList("Admin");
		usersList.addAll(getUserList("Doctor"));
		usersList.addAll(getUserList("Patient"));
		usersList.addAll(getUserList("Receptionist"));
		usersList.addAll(getUserList("Worker"));

		showInTable();

		tv.setItems(usersList);
	}

	public void showAdminList() throws SQLException {
		ObservableList<UserModel> adminsList = getUserList("Admin");

		showInTable();

		tv.setItems(adminsList);
	}

	public void showDoctorList() throws SQLException {
		ObservableList<UserModel> doctorList = getUserList("Doctor");

		showInTable();

		tv.setItems(doctorList);
	}

	public void showPatientList() throws SQLException {
		ObservableList<UserModel> patientList = getUserList("Patient");

		showInTable();

		tv.setItems(patientList);
	}
	
	public void showReceptionistList() throws SQLException {
		ObservableList<UserModel> receptionistList = getUserList("Receptionist");
		
		showInTable();
		
		tv.setItems(receptionistList);
	}

	public void showWorkerList() throws SQLException {
		ObservableList<UserModel> workerList = getUserList("Worker");

		showInTable();

		tv.setItems(workerList);
	}

	public void onInsert() throws SQLException, IOException {
		PreparedStatement preparedStatement = null;
		try {
			if (tfUsername.getText() != "" && tfPassword.getText() != "" && tfFullname.getText() != ""
					&& tfType.getValue() != null) {
				String query = "INSERT INTO user(username, password, gender, fullname, email, phone, address, type, dob, enroll, qualification, expertise) values ('"
						+ tfUsername.getText() + "', '" + tfPassword.getText() + "', '" + tfGender.getValue() + "', '"
						+ tfFullname.getText() + "', '" + tfEmail.getText() + "', '" + tfPhone.getText() + "', '"
						+ tfAddress.getText() + "', '" + tfType.getValue() + "', '" + tfDOB.getValue() + "', '"
						+ tfEnroll.getValue() + "', '" + tfQualification.getText() + "', '" + tfExpertise.getText()
						+ "')";

				preparedStatement = connection.prepareStatement(query);

				preparedStatement.executeUpdate();

				warning.setText("");

				setEmpty();

				showFilter();

			} else {
				warning.setText("Please, fill the form properly.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			warning.setText("Constraint Failed");
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}

	public void onUpdate() throws SQLException {
		try {
			if (tfID.getText() != "" && tfUsername.getText() != "" && tfPassword.getText() != ""
					&& tfFullname.getText() != "" && tfType.getValue() != null) {
				String queryString = "UPDATE user SET username = ?, password = ?, fullname = ?, email = ?, phone = ?, address = ?, type = ?, dob = ?, enroll = ?, qualification = ?, expertise = ?, gender = ? WHERE id = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(queryString);
				preparedStatement.setString(1, tfUsername.getText());
				preparedStatement.setString(2, tfPassword.getText());
				preparedStatement.setString(3, tfFullname.getText());
				preparedStatement.setString(4, tfEmail.getText());
				preparedStatement.setString(5, tfPhone.getText());
				preparedStatement.setString(6, tfAddress.getText());
				preparedStatement.setString(7, tfType.getValue());
				preparedStatement.setString(8, tfDOB.getValue().toString());
				preparedStatement.setString(9, tfEnroll.getValue().toString());
				preparedStatement.setString(10, tfQualification.getText());
				preparedStatement.setString(11, tfExpertise.getText());
				preparedStatement.setString(12, tfGender.getValue());
				preparedStatement.setInt(13, Integer.parseInt(tfID.getText()));

				preparedStatement.executeUpdate();

				warning.setText("");

				setEmpty();
				showUserList();
			} else {
				warning.setText("Please, fill the form properly.");
			}
		} catch (Exception e) {

		} finally {
			showFilter();
		}
	}

	public void onDelete() throws SQLException, IOException {
		try {
			
			if (!tfID.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete");
				alert.setHeaderText("You are about to delete!");
				alert.setContentText("Are you sure to delete the user?");

				if (alert.showAndWait().get() == ButtonType.OK) {
					String queryString = "DELETE FROM user WHERE id = ?";
					PreparedStatement preparedStatement = connection.prepareStatement(queryString);
					preparedStatement.setString(1, tfID.getText());

					preparedStatement.executeUpdate();
				}
			} else {
				warning.setText("Please select a user to delete");
			}

		} catch (Exception e) {
		} finally {
			setEmpty();
			showFilter();
		}

	}

	public void handleMouseAction() {
		try {
			UserModel users = tv.getSelectionModel().getSelectedItem();
			if (users != null) {
				tfID.setText(Integer.toString(users.getId()));
				tfUsername.setText(users.getUsername());
				tfPassword.setText(users.getPassword());
				tfFullname.setText(users.getFullname());
				tfType.setValue(users.getType());
				tfEmail.setText(users.getEmail());
				tfPhone.setText(users.getPhone());
				tfAddress.setText(users.getAddress());
				tfDOB.setValue(LocalDate.parse(users.getDob()));
				tfEnroll.setValue(LocalDate.parse(users.getEnroll()));
				tfQualification.setText(users.getQualification());
				tfExpertise.setText(users.getExpertise());
				tfGender.setValue(users.getGender());

				tfID.setEditable(false);
			}

		} catch (Exception e) {

		}
	}

	public void showInTable() {
		tvID.setCellValueFactory(new PropertyValueFactory<UserModel, Integer>("id"));
		tvUsername.setCellValueFactory(new PropertyValueFactory<UserModel, String>("username"));
		tvPassword.setCellValueFactory(new PropertyValueFactory<UserModel, String>("password"));
		tvFullname.setCellValueFactory(new PropertyValueFactory<UserModel, String>("fullname"));
		tvType.setCellValueFactory(new PropertyValueFactory<UserModel, String>("type"));
		tvDOB.setCellValueFactory(new PropertyValueFactory<UserModel, String>("dob"));
		tvPhone.setCellValueFactory(new PropertyValueFactory<UserModel, String>("phone"));
		tvEmail.setCellValueFactory(new PropertyValueFactory<UserModel, String>("email"));
		tvAddress.setCellValueFactory(new PropertyValueFactory<UserModel, String>("address"));
		tvQualification.setCellValueFactory(new PropertyValueFactory<UserModel, String>("qualification"));
		tvExpertise.setCellValueFactory(new PropertyValueFactory<UserModel, String>("expertise"));
		tvEnroll.setCellValueFactory(new PropertyValueFactory<UserModel, String>("enroll"));
		tvGender.setCellValueFactory(new PropertyValueFactory<UserModel, String>("gender"));
	}

	public void setEmpty() {
		tfID.setText("");
		tfUsername.setText("");
		tfPassword.setText("");
		tfFullname.setText("");
		tfEmail.setText("");
		tfAddress.setText("");
		tfPhone.setText("");
		tfQualification.setText("");
		tfExpertise.setText("");
		tfDOB.setValue(LocalDate.now());
		tfEnroll.setValue(LocalDate.now());

		warning.setText("");

		tfID.setEditable(false);
	}

	public void showFilter() throws SQLException {
		if (tfType.getValue() == "Admin") {
			showAdminList();
		} else if (tfType.getValue() == "Doctor") {
			showDoctorList();
		} else if(tfType.getValue() == "Receptionist") {
			showReceptionistList();
		} else if (tfType.getValue() == "Worker") {
			showWorkerList();
		} else if (tfType.getValue() == "Patient") {
			showPatientList();
		}
	}
}