package application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AskController implements Initializable {

	@FXML
	private TextField destination;
	@FXML
	private Button planButton;
	@FXML
	private VBox tripBox;
	@FXML
	private Spinner<Integer> spinner;
	private MainPaneController mainPaneController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20);

		valueFactory.setValue(1);
		spinner.setValueFactory(valueFactory);
		mainPaneController = MainPaneController.getInstance();

	}

	@FXML
	void planTrip(ActionEvent event) throws IOException, InterruptedException {
		// Get location and amount of days
		String location = destination.getText();
		int days = spinner.getValue();
		// Check if valid location
		if (location.length() < 2 || location == null) {
			System.out.print("Please enter a valid location");
			return;
		}
		mainPaneController.setLoadingScreenVisible(true, "navigation");
		Task<Void> itineraryTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				String answer = getAnswer(location, days);
				// Create and display itinerery
				Platform.runLater(() -> {
					createItinerary(answer);
				});
				return null;
			}
		};
		itineraryTask.setOnSucceeded(e -> {
			mainPaneController.setLoadingScreenVisible(false, null);
		});
		itineraryTask.setOnFailed(e -> {
			mainPaneController.setLoadingScreenVisible(false, null);
		});
		new Thread(itineraryTask).start();
	}

	private String getAnswer(String location, int days) throws IOException, InterruptedException {
		// This code communicates with ChatGPT
		String apiKey = "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";
		String prompt = "Please suggest an itinerary for " + days + " day(s) in " + location
				+ " that covers each day by hour.";

		Map<String, String> params = new HashMap<>();
		params.put("model", "text-davinci-002");
		params.put("prompt", prompt);
		params.put("temperature", "0.1");
		params.put("max_tokens", "2000");

		String apiUrl = "https://api.openai.com/v1/engines/" + params.get("model") + "/completions";

		// Build the HTTP request
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest.Builder builder = HttpRequest.newBuilder();
		builder.uri(URI.create(apiUrl));
		builder.header("Content-Type", "application/json");
		builder.header("Authorization", "Bearer " + apiKey);
		builder.POST(HttpRequest.BodyPublishers.ofString("{\"prompt\": \""
				+ URLEncoder.encode(prompt, StandardCharsets.UTF_8.toString()) + "\", \"temperature\": "
				+ params.get("temperature") + ", \"max_tokens\": " + params.get("max_tokens") + "}"));

		// Send the request and get the response
		HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());

		// Parse the response using Jackson and extract the generated text
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(response.body());
		String generatedText = rootNode.get("choices").get(0).get("text").asText();
		return generatedText;
	}

	private void createItinerary(String itinerary) {
		tripBox.getChildren().clear();
		// Split the itinerary into separate days
		String[] days = itinerary.split("Day ");
		// Add each day to the ListView
		for (int i = 1; i < days.length; i++) {
			// Create a ListView and VBox
			ListView<String> itineraryList = new ListView<>();
			// Create a new ListCell that displays the day's itinerary
			ListCell<String> cell = new ListCell<String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
					} else {
						setText(item);
						setStyle("-fx-font-size: 14; -fx-text-fill: navy;");
					}
				}
			};
			// Set the day's itinerary as the ListCell's text
			cell.setText("Day " + days[i]);
			// Add the ListCell to the ListView
			itineraryList.getItems().add(cell.getText());
			itineraryList.setMinSize(600, 400);
			// Add the ListView to the VBox
			tripBox.getChildren().add(itineraryList);
		}

	}

}
