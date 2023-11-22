package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import regularClasses.FlightApi;
import regularClasses.FlightConnecting;
import regularClasses.HelperMethod;

public class LoadFlightInfoController implements Initializable {

	FXMLLoader loader;
	Parent root;
	static Scene scene;
	static Scene inBoundScene;
	LoadUserInfoController loadInfo;
	static boolean isRoundTrip = false;

	@FXML
	VBox FlightsContainerVB;
	@FXML
	Button backBtn;
	@FXML
	Label topLbl;
	@FXML
	Label topLblRt;
	@FXML
	Button nextBtn;
	@FXML
	Button viewInBtn;

	FlightInfoControllerOW flightInfo;
	ArrayList<String> allIataCodes;
	static Node loadInBoundData;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void addFlightInfoToContainer(Object flightObj, String whichBound) {
		try {

			if (flightObj instanceof FlightApi) {
				FlightApi nonStop = (FlightApi) flightObj;
				loader = new FXMLLoader(getClass().getResource("fxml/FlightInformationOW.fxml"));
				root = loader.load();
				flightInfo = loader.getController();
				setFlightInfo(nonStop, HelperMethod.returnCity(SearchFlightsController.from),
						HelperMethod.returnCity(SearchFlightsController.where),whichBound);
				flightInfo.setUiData();
				FlightsContainerVB.getChildren().add(root);
			} else {
				FlightConnecting connecting = (FlightConnecting) flightObj;
				allIataCodes = HelperMethod.returnAllIATA(SearchFlightsController.from, SearchFlightsController.where,
						connecting.getIntervalIataCodes());

				for (int i = 0; i < connecting.eachFlight.size(); i++) {
					loader = new FXMLLoader(getClass().getResource("fxml/FlightInformationOW.fxml"));
					root = loader.load();
					flightInfo = loader.getController();
					setFlightInfo(connecting.eachFlight.get(i), HelperMethod.returnCity(allIataCodes.get(i)),
							HelperMethod.returnCity(allIataCodes.get(i + 1)),whichBound);
					flightInfo.setUiData();
					FlightsContainerVB.getChildren().add(root);
				}
			}
			goBack();
			goNext();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	private void setFlightInfo(FlightApi eachFlight, String from, String where, String whichBound) {
		// Airline label
		if (FlightsController.isRoundTrip == false) {
			topLblRt.setVisible(false);
			topLblRt.setManaged(false);
			topLbl.setText(eachFlight.getMarketingAirline());
		} else {
			topLblRt.setVisible(true);
			topLblRt.setManaged(true);
			topLbl.setText(eachFlight.getMarketingAirline());
			topLblRt.setText(whichBound);
		}
		// Departing info
		flightInfo.setDepartingFromCity(from);
		flightInfo.setDepartingDateTime(HelperMethod.parseDateAndTime(eachFlight.getDepartureDateTime(), "date") + " | "
				+ HelperMethod.parseDateAndTime(eachFlight.getDepartureDateTime(), "time"));
		flightInfo.setDepartingAirPort(eachFlight.getDepartureAirport());
		System.out.println(eachFlight.getDepaertureTerminal());
		// ternary conditional operator (? :)
		flightInfo.setDepartingTerminal(
				"Terminal: " + (!eachFlight.getDepaertureTerminal().isBlank() ? eachFlight.getDepaertureTerminal()
						: "To Be Determined"));
		// Arriving info
		flightInfo.setArrivingAtCity(where);
		flightInfo.setArrivingDateTime(HelperMethod.parseDateAndTime(eachFlight.getArrivalDateTime(), "date") + " | "
				+ HelperMethod.parseDateAndTime(eachFlight.getArrivalDateTime(), "time"));
		flightInfo.setArrivingAirport(eachFlight.getArrivalAirport());
		flightInfo.setArrivingTerminal("Terminal: "
				+ (!eachFlight.getArrivalTerminal().isBlank() ? eachFlight.getArrivalTerminal() : "To Be Determined"));
		// Flight info
		flightInfo.setFlightNo("Flight No. " + Integer.toString(eachFlight.getFlightNumber()));
		flightInfo.setFlightClass(SearchFlightsController.prefClass);
		flightInfo.setTotalJourneyTime(HelperMethod.formatDuration(eachFlight.getTotalTripTime()));
		flightInfo.setTotalJourneyDistance(eachFlight.getDistance() + "mi");

	}

	private void goBack() {
		backBtn.setOnMouseClicked(e -> {
			try {
				if (!isRoundTrip) {
					MainPaneController.getInstance().loadFXMLInMainContainer(SearchFlightsController.loadedFlights);
				} else {
					MainPaneController.getInstance()
							.loadFXMLInMainContainer(RoundTripFlightsController.loadOutBoundFlightData);
					isRoundTrip = false;
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

	private void goNext() {
		nextBtn.setOnMouseClicked(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoadUserInfo.fxml"));
				Node content = loader.load();
				MainPaneController.getInstance().loadFXMLInMainContainer(content);
				loadInfo = loader.getController();
				loadInfo.addInfoToContainer();

			} catch (Exception ex) {
				System.out.println(ex.getCause() + " " + ex.getStackTrace());
			}
		});
	}

	public void viewInboundData(Object flightObj) {
		viewInBtn.setOnMouseClicked(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/loadFlightInfo.fxml"));
				Node content = loader.load();
				MainPaneController.getInstance().loadFXMLInMainContainer(content);
				isRoundTrip = true;
				LoadFlightInfoController flInfo = loader.getController();
				flInfo.viewInBtn.setManaged(false);
				flInfo.backBtn.setVisible(true);
				flInfo.nextBtn.setVisible(true);
				flInfo.addFlightInfoToContainer(flightObj, "In-Bound");
				loadInBoundData = content;
			} catch (Exception ex) {
				System.out.println(ex.getCause() + " " + ex.getStackTrace());
			}
		});
	}

}
