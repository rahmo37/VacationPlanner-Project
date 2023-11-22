package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SignInController implements Initializable {
	@FXML
	TextField emailTF;
	@FXML
	PasswordField passwordTF;
	@FXML
	Button loginBtn;
	@FXML
	Button signUpBtn;
	Alert alert = new Alert(AlertType.INFORMATION);
	int attempt = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		clickLogin();
		clickSignUp();
	}

	private void clickLogin() {

		loginBtn.setOnAction(e -> {
			String email = emailTF.getText();
			String password = passwordTF.getText();
			if (email.isEmpty() || email.isBlank() || password.isEmpty() || password.isBlank()) {
				showAlert("Information Dialog", "Please complete any empty fields and try again");
			} else if (!Implementation.loginUser(email, password)) {
				showAlert("Information Dialog", "Email or Password not valid! Try again..");
				attempt++;
				if (attempt >= 3) {
					showAlert("Terminating...", "Maximum Attempt reaceh \nSystem Terminating...");
					System.exit(0);
				}
			} else {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainPane.fxml"));
					Parent root = loader.load();
					Scene scene = new Scene(root);
					Stage stage = (Stage) loginBtn.getScene().getWindow();
					stage.setScene(scene);
					stage.setMaximized(true);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		});
	}

	private void clickSignUp() {
		signUpBtn.setOnAction(e -> {
			try {
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SignUp.fxml"));
				System.out.println("Hi");
				Parent root = loader.load();
				Scene scene = new Scene(root, 1000, 750);
				Stage stage = (Stage) signUpBtn.getScene().getWindow();
				stage.setScene(scene);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	private void showAlert(String dialogType, String message) {
		alert.setTitle(dialogType);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
