package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import regularClasses.HelperMethod;

public class CongratsController implements Initializable {

	@FXML
	Label goingToLbl;

	@FXML
	Label referenceLbl;

	@FXML
	Button okBtn;

	static int refNo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		clickOkBtn();
	}

	public void setLabel(String city) {
		goingToLbl.setText(city);
		referenceLbl.setText("You reference #" + refNo);

	}

	public void clickOkBtn() {
		okBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SearchFlights.fxml"));
				Node content = loader.load();
				MainPaneController.getInstance().loadFXMLInMainContainer(content);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		});
	}

}
