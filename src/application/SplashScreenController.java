package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SplashScreenController implements Initializable {

	@FXML
	ImageView loadingIV;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void setLoadingScreen(String image) {
		Image img = new Image(getClass().getResourceAsStream("images/" + image + ".gif"));
		loadingIV.setImage(img);
	}

}
