package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import regularClasses.*;

public class CurrencyController implements Initializable {
	@FXML
	ComboBox<String> fromComBox;
	@FXML
	ComboBox<String> toComBox;
	@FXML
	TextField amountTF;
	@FXML
	Button convertBTN;
	@FXML
	TextField resultTF;
	@FXML
	Label currCodeLbl;

	String fromCode;
	String toCode;
	double amount;
	private MainPaneController mainPaneController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		callCurrencyApi();
		mainPaneController = MainPaneController.getInstance();
	}

	private void populateComboBox(ComboBox<String> comBox) {
		ObservableList<String> dropDownList = comBox.getItems();
		dropDownList.addAll(Implementation.currencyCode.keySet());
		HelperMethod.insertionSort(dropDownList);
	}

	private void callCurrencyApi() {
		populateComboBox(fromComBox);
		populateComboBox(toComBox);
		fromComBox.setOnAction(e -> {
			fromCode = Implementation.currencyCode.get(fromComBox.valueProperty().get());
			System.out.println(fromCode);
		});
		toComBox.setOnAction(e -> {
			toCode = Implementation.currencyCode.get(toComBox.valueProperty().get());
			System.out.println(toCode);
		});
		convertBTN.setOnMouseClicked(e -> {
			mainPaneController.setLoadingScreenVisible(true, "Exchange");
			Task<Void> currencytask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Thread.sleep(4000);
					// TODO Auto-generated method stub
					amount = Double.parseDouble(amountTF.getText());
					// Converting to two decimal places.
                    double result = Implementation.currencyRequest(fromCode, toCode, amount);
                    Platform.runLater(() -> {
                        resultTF.setText(String.format("%.2f", result));
                        currCodeLbl.setText(toCode);
                    });
					return null;
				}

			};
			currencytask.setOnSucceeded(event -> {
				mainPaneController.setLoadingScreenVisible(false, null);
			});
			currencytask.setOnFailed(event -> {
				mainPaneController.setLoadingScreenVisible(false, null);
			});
			new Thread(currencytask).start();
		});
	}

}
