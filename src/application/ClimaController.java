package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import regularClasses.HelperMethod;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class ClimaController implements Initializable {

	@FXML
	private Label date1;
	@FXML
	private Label date2;
	@FXML
	private Label date3;
	@FXML
	private Label date4;
	@FXML
	private Label date5;
	@FXML
	private Label date6;
	@FXML
	private Label date7;
	@FXML
	private Label feelsLike1;
	@FXML
	private Label max1;
	@FXML
	private Label min1;
	@FXML
	private Label temp1;
	@FXML
	private Label temp2;
	@FXML
	private Label temp3;
	@FXML
	private Label temp4;
	@FXML
	private Label temp5;
	@FXML
	private Label temp6;
	@FXML
	private Label temp7;
	@FXML
	private Label weather1;
	@FXML
	private Label weather2;
	@FXML
	private Label weather3;
	@FXML
	private Label weather4;
	@FXML
	private Label weather5;
	@FXML
	private Label weather6;
	@FXML
	private Label weather7;
	@FXML
	private ImageView img1 = new ImageView();
	@FXML
	private ImageView img2 = new ImageView();
	@FXML
	private ImageView img3 = new ImageView();
	@FXML
	private ImageView img4 = new ImageView();
	@FXML
	private ImageView img5 = new ImageView();
	@FXML
	private ImageView img6 = new ImageView();
	@FXML
	private ImageView img7 = new ImageView();
	@FXML
	private VBox box1 = new VBox();
	@FXML
	private VBox box2 = new VBox();
	@FXML
	private VBox box3 = new VBox();
	@FXML
	private VBox box4 = new VBox();
	@FXML
	private VBox box5 = new VBox();
	@FXML
	private VBox box6 = new VBox();
	@FXML
	private VBox box7 = new VBox();
	@FXML
	private HBox weatherBox = new HBox();
	@FXML
	Label weatherLocation;
	@FXML
	private TextField weatherSearchBar;
	@FXML
	private Button weatherSearchBtn;
	@FXML
	private ImageView weatherImage = new ImageView();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void displayWeather(int i, String date, String temp, String feelsLike, String max, String min,
			String description, String icon) {
		int day = i + 1;
		temp = Integer.toString((int) Math.round(Double.parseDouble(temp))) + "°";
		date = HelperMethod.getDayName(date);
		if (day != 1) {
			description = HelperMethod.getWeatherDescription(description);
		}
		Image img = new Image(getClass().getResourceAsStream("WeatherIcons/" + icon + ".png"));
		switch (day) {
		case 1:
			date1.setText("Today");
			temp1.setText(temp);
			feelsLike1.setText("Feels Like: " + feelsLike);
			max1.setText("Max: " + max);
			min1.setText("Min: " + min);
			weather1.setText(description);
			img1.setImage(img);
			break;
		case 2:
			date2.setText("Tomorrow");
			temp2.setText(temp);
			weather2.setText(description);
			img2.setImage(img);
			break;
		case 3:
			date3.setText(date);
			temp3.setText(temp);
			weather3.setText(description);
			img3.setImage(img);
			break;
		case 4:
			date4.setText(date);
			temp4.setText(temp);
			weather4.setText(description);
			img4.setImage(img);
			break;
		case 5:
			date5.setText(date);
			temp5.setText(temp);
			weather5.setText(description);
			img5.setImage(img);
			break;
		case 6:
			date6.setText(date);
			temp6.setText(temp);
			weather6.setText(description);
			img6.setImage(img);
			break;
		case 7:
			date7.setText(date);
			temp7.setText(temp);
			weather7.setText(description);
			img7.setImage(img);
			break;
		}
	}
}