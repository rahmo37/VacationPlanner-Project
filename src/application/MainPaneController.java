package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.SplitPane;
import javafx.fxml.FXML;

public class MainPaneController implements Initializable {
	@FXML
	private Button weatherButton;
	@FXML
	private Button currencyButton;
	@FXML
	private Button flightsButton;
	@FXML
	private Button homeButton;
	@FXML
	private Button askButton;
	@FXML
	private Button hotelButton;
	@FXML
	BorderPane centerPane;
	@FXML
	private VBox leftVBox;
	@FXML
	private AnchorPane leftAnchorPane;
	@FXML
	private SplitPane root;
	@FXML
	private Label nameLbl;
	@FXML
	private Button logoutBtn;

	private Node initialContent;
	private static MainPaneController instance;
	public static String userName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		clickCurrencyBtn();
		clickFlightBtn();
		clickWeatherBtn();
		clickHotelBtn();
		clickAskBtn();
		clickLogoutBtn();
		homeButton();
		instance = this;
		initialContent = centerPane.getCenter();
		root.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double dividerPos = 0.112;
				root.setDividerPositions(dividerPos);
			}
		});
		nameLbl.setText("Hello " + userName + "!");
	}

	public static MainPaneController getInstance() {
		return instance;
	}

	private void clickCurrencyBtn() {
		currencyButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Currency.fxml"));
				Node content = loader.load();
				loadFXMLInMainContainer(content);
			} catch (Exception ex) {

			}
		});
	}

	private void clickFlightBtn() {
		flightsButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SearchFlights.fxml"));
				Node content = loader.load();
				loadFXMLInMainContainer(content);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	private void clickWeatherBtn() {
		weatherButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/WeatherPane.fxml"));
				Node content = loader.load();
				loadFXMLInMainContainer(content);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		});
	}

	private void clickHotelBtn() {
		hotelButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Hotel.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.show();
				PauseTransition delay = new PauseTransition(Duration.seconds(4.5));
	            delay.setOnFinished(event -> stage.close());
	            delay.play();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	private void clickAskBtn() {
		askButton.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/AskPane.fxml"));
				Node content = loader.load();
				loadFXMLInMainContainer(content);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	private void homeButton() {
		homeButton.setOnAction(e -> {
			try {
				centerPane.setCenter(initialContent);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	public void loadFXMLInMainContainer(Node content) {
		ScrollPane scrollPane = new ScrollPane();
		AnchorPane anchorPane = new AnchorPane();

		// Add the content to the AnchorPane
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		anchorPane.getChildren().add(content);

		// Check if content is an instance of Region
		if (content instanceof Region) {
			Region contentRegion = (Region) content;
			// Bind the content's minWidth, minHeight, maxWidth, and maxHeight to the
			// ScrollPane's width and height
			contentRegion.minWidthProperty()
					.bind(Bindings.createDoubleBinding(
							() -> scrollPane.getWidth() - scrollPane.getPadding().getLeft()
									- scrollPane.getPadding().getRight(),
							scrollPane.widthProperty(), scrollPane.paddingProperty()));
			contentRegion.minHeightProperty()
					.bind(Bindings.createDoubleBinding(
							() -> scrollPane.getHeight() - scrollPane.getPadding().getTop()
									- scrollPane.getPadding().getBottom(),
							scrollPane.heightProperty(), scrollPane.paddingProperty()));
			contentRegion.maxWidthProperty().bind(contentRegion.minWidthProperty());
			contentRegion.maxHeightProperty().bind(contentRegion.minHeightProperty());
		}

		// Add the AnchorPane to the ScrollPane
		scrollPane.setContent(anchorPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		// Set the ScrollPane as the center of the BorderPane
		centerPane.setCenter(scrollPane);
	}

	private Stage loadingScreenStage;

	public void setLoadingScreenVisible(boolean visible, String image) {
		if (visible) {
			Platform.runLater(() -> {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SplashScreen.fxml"));
					AnchorPane loadingScreen = loader.load();
					loadingScreen.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
					SplashScreenController splashScreenController = loader.getController();
					splashScreenController.setLoadingScreen(image);
					loadingScreenStage = new Stage();
					loadingScreenStage.initModality(Modality.APPLICATION_MODAL);
					loadingScreenStage.initStyle(StageStyle.TRANSPARENT);
					loadingScreenStage.setScene(new Scene(loadingScreen, Color.TRANSPARENT));

					// Center the loading screen stage relative to the primary stage
					if (!Window.getWindows().isEmpty()) {
						Window primaryStage = Window.getWindows().get(0);
						double x = primaryStage.getX() + (primaryStage.getWidth() - loadingScreen.getWidth()) / 2;
						double y = primaryStage.getY() + (primaryStage.getHeight() - loadingScreen.getHeight()) / 2;
						loadingScreenStage.setX(x);
						loadingScreenStage.setY(y);
					} else {
						loadingScreenStage.centerOnScreen();
					}

					loadingScreenStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else {
			Platform.runLater(() -> {
				if (loadingScreenStage != null) {
					loadingScreenStage.close();
					loadingScreenStage = null;
				}
			});
		}
	}

	private void clickLogoutBtn() {
		logoutBtn.setOnAction(e -> {
			showLogoutPrompt();
		});
	}

	private void showLogoutPrompt() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Logout Confirmation");
		alert.setHeaderText("Are you sure you want to log out?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			// Logout user
			try {
				Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
				currentStage.close();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SignIn.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root, 1000, 750);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}
}
