package application;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import regularClasses.HelperMethod;

public class SignUpController implements Initializable {

	@FXML
	ImageView imageView;

	@FXML
	AnchorPane rootPane;

	@FXML
	TextField emailTF;

	@FXML
	TextField firstNameTF;

	@FXML
	TextField lastNameTF;

	@FXML
	PasswordField passwordTF;

	@FXML
	TextField phoneTF;

	@FXML
	Button signUpBTN;

	@FXML
	Button backBtn;

	Stage stage;
	int counter = 0;
	Alert alert = new Alert(AlertType.INFORMATION);

	public SignUpController() {
		/*
		 * none of the FXML components initialize at this stage. We use the initialize
		 * method to initialize components that came from Scene Builder
		 */
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		playNStopAnimation();
		clickBackBtn();
	}

	@FXML
	public void createAccount(ActionEvent event) {
		ArrayList<TextField> fieldSet = new ArrayList<>(
				Arrays.asList(firstNameTF, lastNameTF, emailTF, passwordTF, phoneTF));
		if (!HelperMethod.emptyFieldExsists(fieldSet)) {
			try {
				String firstName = firstNameTF.getText();
				String lastName = lastNameTF.getText();
				String email = emailTF.getText();
				String phone = phoneTF.getText();
				String password = passwordTF.getText();
				Implementation.signUp(firstName, lastName, email, phone, password);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SignIn.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				Stage stage = (Stage) signUpBTN.getScene().getWindow();
				stage.setScene(scene);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		} else {
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Please complete any empty fields and try again");
			alert.showAndWait();
		}
	}

	private void clickBackBtn() {
		backBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SignIn.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root, 1000, 750);
				Stage stage = (Stage) backBtn.getScene().getWindow();
				stage.setScene(scene);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	public void playNStopAnimation() {
		Timeline animation = new Timeline(new KeyFrame(Duration.ZERO), new KeyFrame(Duration.seconds(2.65)));

		animation.play();
		if (counter == 0) {
			animation.setOnFinished(event -> {
				// Set the ImageView to display the still image after the animation completes
				Image img = new Image(getClass().getResourceAsStream("images/userStill.png"));
				imageView.setImage(img);
			});
			counter = 1;
		}
//		animation.setCycleCount(1);
		// Play animation on mouse hover
		imageView.setOnMouseClicked(e -> {
			Image img = new Image(getClass().getResourceAsStream("images/user.gif"));
			imageView.setImage(img);
			animation.play();
		});

		imageView.setOnMouseEntered(e -> {
			imageView.setCursor(Cursor.HAND);
		});
		signUpBTN.setOnMouseEntered(e -> {
			signUpBTN.setCursor(Cursor.HAND);
		});

	}
}
