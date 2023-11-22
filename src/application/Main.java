package application;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import regularClasses.HelperMethod;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;

public class Main extends Application {
	static Scene scene;
	Parent root;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent splashScreen = FXMLLoader.load(getClass().getResource("fxml/SplashScreen.fxml"));
		Stage splashScreenStage = new Stage();
		splashScreenStage.initStyle(StageStyle.TRANSPARENT); 
		splashScreenStage.setScene(new Scene(splashScreen, 200, 150));

		splashScreenStage.showingProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
				splashScreenStage.setX((screenBounds.getWidth() - splashScreenStage.getWidth()) / 2);
				splashScreenStage.setY((screenBounds.getHeight() - splashScreenStage.getHeight()) / 2);
			}
		});

		splashScreenStage.show();

		PauseTransition pause = new PauseTransition(Duration.seconds(4));
		pause.setOnFinished(e -> {
			try {
				root = FXMLLoader.load(getClass().getResource("/application/fxml/SignIn.fxml"));
				scene = new Scene(root, 1000, 750);
//			scene.getStylesheets().add(getClass().getResource("style2.css").toExternalForm());
				primaryStage.setMinWidth(1000);
				primaryStage.setMinHeight(750);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			splashScreenStage.close();
		});
		pause.play();
	}

	public static void main(String[] args) throws Exception {
		Implementation imp = new Implementation();
		imp.initialize();
		launch(args);
	}
}
