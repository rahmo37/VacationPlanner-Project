package application;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class WeatherController implements Initializable {

	@FXML
	TextField weatherSearchBar;
	@FXML
	Button weatherSearchBtn;
	private MainPaneController mainPaneController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		clickSearchBtn();
		mainPaneController = MainPaneController.getInstance();
	}

	String inline = "";

	public void clickSearchBtn() {
		weatherSearchBtn.setOnAction(e -> {
			mainPaneController.setLoadingScreenVisible(true, "wind");
			Task<Void> searchWeather = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					inline = "";
					try {
						Thread.sleep(5000);
						String head = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
						String location = weatherSearchBar.getText().toLowerCase().trim().toString();
						String tail = "?unitGroup=us&key=X32BCAVR96ANGVLY6CG7PUN6S&contentType=json";
						String query = head + location + tail;
						// String full =
						// "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/london?unitGroup=us&key=X32BCAVR96ANGVLY6CG7PUN6S&contentType=json";
						URL url = new URL(query);
						// Check if connection is made
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.connect();
						int responseCode = conn.getResponseCode();
						// If response code is 200, then success, otherwise a connection error ocurred
						// and an exception must be thrown
						if (responseCode != 200) {
							throw new RuntimeException("HttpURLConnection: " + responseCode);
						} else {
							// Scan the data received from the API Request
							Scanner sc = new Scanner(url.openStream());
							while (sc.hasNextLine()) {
								inline += sc.nextLine();
							}
							sc.close();
						}
						System.out.print("Here is the raw data in JSON format: " + "\n");
						System.out.println("-----");
						System.out.println(inline);
						// Parse
						ObjectMapper objectMapper = new ObjectMapper();
						ObjectReader reader = objectMapper.reader();
						JsonNode node = objectMapper.readValue(inline, JsonNode.class);
						JsonNode array = node.get("days");
						JsonNode address = node.get("resolvedAddress");
						FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoadWeather.fxml"));
						Node content = loader.load();
						ClimaController weatherSevenDays = loader.getController();
						weatherSevenDays.weatherLocation.setText(address.asText());
						for (int i = 0; i < 7; i++) {
							JsonNode jsonNameNode = array.get(i);
							// Get Values
							String date = jsonNameNode.get("datetime").asText();
							String temp = jsonNameNode.get("temp").asText();
							String feelsLike = jsonNameNode.get("feelslike").asText();
							String max = jsonNameNode.get("tempmax").asText();
							String min = jsonNameNode.get("tempmin").asText();
							String description = jsonNameNode.get("description").asText();
							String icon = jsonNameNode.get("icon").asText();
							System.out.println(icon);
							weatherSevenDays.displayWeather(i, date, temp, feelsLike, max, min, description, icon);
						}
						Platform.runLater(() -> MainPaneController.getInstance().loadFXMLInMainContainer(content));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return null;
				}
			};
			searchWeather.setOnSucceeded(event -> {
				mainPaneController.setLoadingScreenVisible(false, null);
			});
			searchWeather.setOnFailed(event -> {
				mainPaneController.setLoadingScreenVisible(false, null);
			});
			new Thread(searchWeather).start();
		});
	}

}
