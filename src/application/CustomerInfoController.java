package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomerInfoController implements Initializable {
	@FXML
	Label customerLbl;
	@FXML
	TextField firstNameTF;
	@FXML
	TextField lastNameTF;
	@FXML
	TextField ageTF;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setCustomerLabel(String customerCatagory, int i) {
		customerLbl.setText(customerCatagory + " " + i);
	}

//	public void getText() {
//		System.out.println(firstNameTF.getText());
//	}
}
