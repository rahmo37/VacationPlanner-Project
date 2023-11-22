package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import javafx.util.StringConverter;
import regularClasses.FlightApi;
import regularClasses.FlightConnecting;

public class SearchFlightsController implements Initializable {

	@FXML
	TextField fromTxt;

	@FXML
	TextField whereTxt;

	@FXML
	Button searchBtn;

	@FXML
	DatePicker returnDateDP;

	@FXML
	DatePicker startDateDP;

	@FXML
	RadioButton oneWayRB;

	@FXML
	RadioButton roundTripRB;

	@FXML
	ToggleGroup toggleGroup = new ToggleGroup();

	@FXML
	ComboBox<Integer> adultCB;

	@FXML
	ComboBox<Integer> childrenCB;

	@FXML
	ComboBox<Integer> infantCB;

	@FXML
	ComboBox<String> prefClassCB;

	@FXML
	Label adultLbl;

	@FXML
	Label childLbl;

	@FXML
	Label infantLbl;

	AutoCompletionBinding<String> autoCompletionBining;
	ArrayList<String> searchCityAndAirPort = new ArrayList<>();
	Implementation flight;
	Alert alert = new Alert(AlertType.INFORMATION);
	private MainPaneController mainPaneController;

	// Variables from the interface
	static String from;
	static String where;
	static String sDate;
	static String rDate;
	static String prefClass;

	// Variables required for loading flight instances
	String flightType;
	int totalFlights;
	static Scene scene;
	static Node loadedFlights;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		mainPaneController = MainPaneController.getInstance();
		populateTheSearchArray();
		bindTextField(fromTxt);
		bindTextField(whereTxt);
		radioButtonSelection();
		changeDatePickerObjects();
		disableAllDatesBeforeToday();
		setConstrainOnReturnDate();
		getAndSendFlightParameters();
		populateComboBox(adultCB, Integer.class);
		populateComboBox(infantCB, Integer.class);
		populateComboBox(childrenCB, Integer.class);
		populateComboBox(prefClassCB, String.class);
		travelerLabelVisibility();
	}

	// populating the search array which will be placed in the airport search
	// TextFileds
	private void populateTheSearchArray() {
		for (int i = 0; i < Implementation.IATAcodes.size(); i++) {
			searchCityAndAirPort.add(Implementation.IATAcodes.get(i).getCity() + " - "
					+ Implementation.IATAcodes.get(i).getAirport_Name() + " ("
					+ Implementation.IATAcodes.get(i).getCode() + ")");
		}
	}

	// placing the array in text fields
	private void bindTextField(TextField txtf) {
		autoCompletionBining = TextFields.bindAutoCompletion(txtf, searchCityAndAirPort);
		autoCompletionBining.setPrefWidth(500);
		autoCompletionBining.visibleRowCountProperty();
		autoCompletionBining.setVisibleRowCount(8);
	}

	private void getAndSendFlightParameters() throws NullPointerException {
		searchBtn.setOnMouseClicked(e -> {
			// Show the loading screen
			mainPaneController.setLoadingScreenVisible(true, "radar");
			Task<Void> searchTask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Thread.sleep(3000);
					// Implementation object where all the flights are gathered
					flight = new Implementation();
					try {
						// Retrieving whereFrom and whereTo
						from = fromTxt.getText().substring(fromTxt.getText().indexOf("(") + 1,
								fromTxt.getText().indexOf(")"));
						where = whereTxt.getText().substring(whereTxt.getText().indexOf("(") + 1,
								whereTxt.getText().indexOf(")"));

						// gathering all travelers in a hash-map
						accumulateTravelers(adultCB, adultCB.getPromptText());
						accumulateTravelers(childrenCB, childrenCB.getPromptText());
						accumulateTravelers(infantCB, infantCB.getPromptText());

						// Retrieving the preferred Class (Economy, business, first class)
						// PrefClass is static because its value will not change, and will need to use
						// later in the code
						if (prefClassCB.valueProperty().get() != null) {
							prefClass = prefClassCB.valueProperty().get();
						} else {
							// an exception is thrown so that it can be caught and an alert msg can be
							// displayed
							throw new NullPointerException();
						}

						// Retrieving dates and sending the appropriate flight requests
						if (oneWayRB.isSelected() && startDateDP != null) {
							sDate = startDateDP.getValue().toString().replaceAll("-", "");
							flight.flightRequest(from, where, sDate);

							// if any flights are returned than we call the next scene, else we stay on the
							// same scene
							// So we test if the combinedFlights array list has any flights
							Platform.runLater(() -> {
								try {
									if (!flight.combinedFlights.isEmpty()) {
										flightType = "One-way";
										FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Flights.fxml"));
										Node content = loader.load();
										MainPaneController.getInstance().loadFXMLInMainContainer(content);
										FlightsController allFlights = loader.getController();
										allFlights.addFlightsToContainer(flightType, flight);
										loadedFlights = content;
										System.out.println("Helo");
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						} else if (roundTripRB.isSelected()) {
							sDate = startDateDP.getValue().toString().replaceAll("-", "");
							rDate = returnDateDP.getValue().toString().replaceAll("-", "");

							flight.roundTrip(from, where, sDate, rDate);

							if (flight.roundTripFlights.get(0).eachRoundTripFlights.get(0) != null
									&& flight.roundTripFlights.get(0).eachRoundTripFlights.get(1) != null) {
								Platform.runLater(() -> {
									try {
										if (flight.roundTripFlights.get(0).eachRoundTripFlights.get(0) != null
												&& flight.roundTripFlights.get(0).eachRoundTripFlights.get(1) != null) {
											flightType = "Round-Trip";
											FXMLLoader loader = new FXMLLoader(
													getClass().getResource("fxml/Flights.fxml"));
											Node content = loader.load();
											MainPaneController.getInstance().loadFXMLInMainContainer(content);
											FlightsController allFlights = loader.getController();
											allFlights.addFlightsToContainer(flightType, flight);
											loadedFlights = content;
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								});
							}

						}
					} catch (Exception ex) {
						throw ex;
					}
					return null;
				}
			};

			// Hide the loading screen when the task is completed
			searchTask.setOnSucceeded(event -> {
				mainPaneController.setLoadingScreenVisible(false, null);
			});

			// Show an error message if the task fails and hide the loading screen
			searchTask.setOnFailed(event -> {
				mainPaneController.setLoadingScreenVisible(false, null);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Please complete any empty fields and try again.");
				alert.showAndWait();
			});

			// Start the search task
			new Thread(searchTask).start();
		});
	}

	// WITH OUT LOADING

////	 After clicking the search button numerous events occurs, all of them are
////	 called from this method
//	private void getAndSendFlightParameters() throws NullPointerException {
//		searchBtn.setOnMouseClicked(e -> {
//			// Implementation object where all the flights are gathered
//			flight = new Implementation();
//			try {
//				// Retrieving whereFrom and whereTo
//				from = fromTxt.getText().substring(fromTxt.getText().indexOf("(") + 1, fromTxt.getText().indexOf(")"));
//				where = whereTxt.getText().substring(whereTxt.getText().indexOf("(") + 1,
//						whereTxt.getText().indexOf(")"));
//
//				// gathering all travelers in a hash-map
//				accumulateTravelers(adultCB, adultCB.getPromptText());
//				accumulateTravelers(childrenCB, childrenCB.getPromptText());
//				accumulateTravelers(infantCB, infantCB.getPromptText());
//
//				// Retrieving the preferred Class (Economy,business,first class)
//				// PrefClass is static because its value will not change, and will need to use
//				// later in the code
//				if (prefClassCB.valueProperty().get() != null) {
//					prefClass = prefClassCB.valueProperty().get();
//				} else {
//					// an exception is thrown so that it can be caught and an alert msg can be
//					// displayed
//					throw new NullPointerException();
//				}
//
//				// Retrieving dates and sending the appropriate flight requests
//				if (oneWayRB.isSelected() && startDateDP != null) {
//					sDate = startDateDP.getValue().toString().replaceAll("-", "");
//					flight.flightRequest(from, where, sDate);
//
//					// Viewing the Non-stop flights in console
////					for (int i = 0; i < flight.combinedFlights.size(); i++) {
////
////						if (flight.combinedFlights.get(i) instanceof FlightConnecting) {
////							System.out.println("\nThis Flight is a connecting flight");
////							for (int j = 0; j < ((FlightConnecting) flight.combinedFlights.get(i)).eachFlight
////									.size(); j++) {
////								System.out.println(
////										((FlightConnecting) flight.combinedFlights.get(i)).eachFlight.get(j).toString()
////												+ "\n");
////							}
////						} else {
////							System.out.println("\nThis Flight is a non-stop flight");
////							System.out.println(flight.combinedFlights.get(i).toString() + "\n");
////						}
////						System.out.println("End of flight : #" + i);
////					}
//
//					try {
//						// if any flights are returned than we call the next scene, else we stay on the
//						// same scene
//						// So we test if the combinedFlights array list has any flights
//						if (!flight.combinedFlights.isEmpty()) {
//							flightType = "One-way";
//							FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Flights.fxml"));
//							Node content = loader.load();
//							MainPaneController.getInstance().loadFXMLInMainContainer(content);
//							FlightsController allFlights = loader.getController();
//							allFlights.addFlightsToContainer(flightType, flight);
//							loadedFlights = content;
//						}
//					} catch (Exception ex) {
//						System.out.println(ex);
//					}
//					// if round trip flight this block is executed
//				} else if (roundTripRB.isSelected()) {
//					sDate = startDateDP.getValue().toString().replaceAll("-", "");
//					rDate = returnDateDP.getValue().toString().replaceAll("-", "");
//
////					System.out.println(" round trip flight request, but disabled for now ");
////					System.out.println("Before making the api call" + flight.roundTripFlights.size());
//
//					flight.roundTrip(from, where, sDate, rDate);
//
//					// Viewing the round trip flights
////					for (int i = 0; i < flight.roundTripFlights.size(); i++) {
////						System.out.println("\nRound Trip Flight # " + i + "\n");
////						System.out.println("----------------------------------------\n\n");
////						for (int j = 0; j < 2; j++) {
////							if (j == 0) {
////								System.out.println("\nOut bound flights\n");
////							} else {
////								System.out.println("\nIn bound flights\n");
////							}
////							if (flight.roundTripFlights.get(i).eachRoundTripFlights
////									.get(j) instanceof FlightConnecting) {
////								for (int k = 0; k < ((FlightConnecting) flight.roundTripFlights
////										.get(i).eachRoundTripFlights.get(j)).eachFlight.size(); k++) {
////									System.out.println("\n\nEach Connecting flight " + k);
////									System.out.println(
////											((FlightConnecting) flight.roundTripFlights.get(i).eachRoundTripFlights
////													.get(j)).eachFlight.get(k).toString());
////
////								}
////							} else {
////								System.out.println(
////										((FlightApi) flight.roundTripFlights.get(i).eachRoundTripFlights.get(j))
////												.toString());
////
////							}
////						}
////					}
//
//					if (flight.roundTripFlights.get(0).eachRoundTripFlights.get(0) != null
//							&& flight.roundTripFlights.get(0).eachRoundTripFlights.get(1) != null) {
//						flightType = "Round-Trip";
//						FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Flights.fxml"));
//						Node content = loader.load();
//						MainPaneController.getInstance().loadFXMLInMainContainer(content);
//						FlightsController allFlights = loader.getController();
//						allFlights.addFlightsToContainer(flightType, flight);
//						loadedFlights = content;
//					}
//
//				}
//
//				// Print/check all retrieved information
////				System.out.println(from + " " + where + " " + sDate + " " + rDate + " " + prefClass + " "
////						+ Implementation.travelerCount);
//
//			} catch (Exception ex) {
//				alert.setTitle("Information Dialog");
//				alert.setHeaderText(null);
//				alert.setContentText("Please complete any empty fields and try again");
//				alert.showAndWait();
//			}
//		});
//	}

	// This method places the traveler in hash map (Called from above
	// getAndSendFlightParameters())
	private void accumulateTravelers(ComboBox<Integer> comBox, String travelerCatagory) {
		if (comBox.valueProperty().get() == null || comBox.valueProperty().get() == 0) {
			Implementation.travelerCount.put(travelerCatagory, 0);
		} else {
			Implementation.travelerCount.put(travelerCatagory, comBox.valueProperty().get());
		}
	}

	// Starting from here all the methods below are just formatting the layouts of
	// the objects in the interface
	private void radioButtonSelection() {
		oneWayRB.setSelected(true);
		oneWayRB.setToggleGroup(toggleGroup);
		roundTripRB.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				// If nothing is selected, clear both radio buttons
				oneWayRB.setSelected(false);
				roundTripRB.setSelected(false);
			} else {
				// If a radio button is selected, clear the other radio button
				RadioButton selectedRadioButton = (RadioButton) newValue;
				if (selectedRadioButton == oneWayRB) {
					roundTripRB.setSelected(false);
				} else {
					oneWayRB.setSelected(false);
				}
			}
		});
	}

	private void changeDatePickerObjects() {
		// Initially return date is not visible
		returnDateStatus(false);
		oneWayRB.setOnAction(e -> {
			// If One way radio button is selected the return date is disabled
			startDateDP.setPromptText("Departure date");
			returnDateStatus(false);
		});
		roundTripRB.setOnAction(e -> {
			// If roundTripRB is selected the return date is enabled
			startDateDP.setPromptText("Start date");
			returnDateStatus(true);
		});
	}

	private void returnDateStatus(boolean status) {
		returnDateDP.setManaged(status);
		returnDateDP.setVisible(status);
	}

	private void disableAllDatesBeforeToday() {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #ffead5"); // change background color of past dates
						}
					}
				};
			}
		};
		startDateDP.setDayCellFactory(dayCellFactory);
		returnDateDP.setDayCellFactory(dayCellFactory);
	}

	private void setConstrainOnReturnDate() {
		startDateDP.valueProperty().addListener((observable, oldValue, newValue) -> {
			// Set the minimum value of the returnDateDP to the selected date on startDateDP
			returnDateDP.setDayCellFactory(picker -> new DateCell() {
				@Override
				public void updateItem(LocalDate date, boolean empty) {
					super.updateItem(date, empty);
					setDisable(empty || date.compareTo(startDateDP.getValue()) < 0);
				}
			});
		});
	}

	private <T> void populateComboBox(ComboBox<T> comBox, Class<T> type) {
		ObservableList<T> dropDownList = comBox.getItems();

		if (type == Integer.class) {
			for (int i = 0; i <= 15; i++) {
				dropDownList.add(type.cast(i));
			}

		} else {
			dropDownList.add(type.cast("First Class"));
			dropDownList.add(type.cast("Business Class"));
			dropDownList.add(type.cast("Economy Class"));
		}
	}

	private void travelerLabelVisibility() {
		adultCB.setOnAction(e -> {
			adultLbl.setVisible(true);
		});
		childrenCB.setOnAction(e -> {
			childLbl.setVisible(true);
		});
		infantCB.setOnAction(e -> {
			infantLbl.setVisible(true);
		});
	}

}
