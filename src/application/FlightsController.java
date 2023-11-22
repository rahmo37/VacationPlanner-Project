package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import regularClasses.FlightApi;
import regularClasses.FlightConnecting;
import regularClasses.HelperMethod;

public class FlightsController implements Initializable {
	FXMLLoader loader;
	Parent root;
	OneWayFlightController oneWayFlight;
	RoundTripFlightsController roundTripFlight;

	@FXML
	VBox FlightsContainerVB;
	@FXML
	Label topLbl;
	@FXML
	ImageView flightsLg;

	static String tempFromLocation = SearchFlightsController.from;
	static String tempWhereLocation = SearchFlightsController.where;

	static boolean isRoundTrip = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void addFlightsToContainer(String flightType, Implementation flightObj) {
		try {
			if (flightType.equalsIgnoreCase("one-way")) {
				flightsLg.setImage(new Image(getClass().getResourceAsStream("images/one-way.gif")));
				topLbl.setText(flightType + " flights");
				isRoundTrip = false;
				ArrayList<Integer> priceList = HelperMethod.returnPrice(flightObj.combinedFlights.size(), "one-way");
				for (int i = 0; i < flightObj.combinedFlights.size(); i++) {
					loader = new FXMLLoader(getClass().getResource("fxml/OneWayFlight.fxml"));
					root = loader.load();
					oneWayFlight = loader.getController();

					if (flightObj.combinedFlights.get(i) instanceof FlightApi) {
						FlightApi nonStopObj = (FlightApi) flightObj.combinedFlights.get(i);
						populateNonStopFlights(nonStopObj);
						oneWayFlight.showFlightInfo(nonStopObj);
					} else {
						FlightConnecting connectingObj = (FlightConnecting) flightObj.combinedFlights.get(i);
						populateConnectingFlight(connectingObj);
						oneWayFlight.showFlightInfo(connectingObj);
					}
					oneWayFlight.setUiData();
					oneWayFlight.priceLbl.setText("$" + priceList.get(i));
					FlightsContainerVB.getChildren().add(root);
					FlightsContainerVB.requestLayout();
				}
			} else if (flightType.equalsIgnoreCase("Round-Trip")) {
				ArrayList<Integer> priceList = HelperMethod.returnPrice(flightObj.roundTripFlights.size(), "round-trip");
				flightsLg.setImage(new Image(getClass().getResourceAsStream("images/round-trip.gif")));
				topLbl.setText(flightType + " flights");
				isRoundTrip = true;
				for (int i = 0; i < flightObj.roundTripFlights.size(); i++) {
					ArrayList<Object> bothFlightsInRT = new ArrayList<>();
					ArrayList<Object> flightsDataToWriteInDB = new ArrayList<>();
					loader = new FXMLLoader(getClass().getResource("fxml/RoundTripFlights.fxml"));
					root = loader.load();
					roundTripFlight = loader.getController();
					for (int j = 0; j < 2; j++) {
						oneWayFlight = new OneWayFlightController();
						if (j == 0) {
							originalAirports();
						} else {
							switchAirports();
						}
						if (flightObj.roundTripFlights.get(i).eachRoundTripFlights.get(j) instanceof FlightApi) {
							FlightApi nonStopObj = (FlightApi) flightObj.roundTripFlights.get(i).eachRoundTripFlights
									.get(j);
							populateNonStopFlights(nonStopObj);
							bothFlightsInRT.add(nonStopObj);
						} else {
							FlightConnecting connectingObj = (FlightConnecting) flightObj.roundTripFlights
									.get(i).eachRoundTripFlights.get(j);
							populateConnectingFlight(connectingObj);
							bothFlightsInRT.add(connectingObj);
						}
						if (j == 0) {
							roundTripFlight.setOutBoundData(oneWayFlight);
							flightsDataToWriteInDB.add(oneWayFlight);

						} else {
							roundTripFlight.setInBoundData(oneWayFlight);
							flightsDataToWriteInDB.add(oneWayFlight);
						}
					}
					originalAirports();
					roundTripFlight.showRoundTripFlightInfo(bothFlightsInRT, flightsDataToWriteInDB);
					roundTripFlight.resultLbl.setText("Flight Result: " + (i + 1));
					roundTripFlight.priceLbl.setText("$" + priceList.get(i));
					FlightsContainerVB.getChildren().add(root);
					FlightsContainerVB.requestLayout();
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getCause() + " " + ex.getMessage());
		}
	}

	public static void switchAirports() {
		String tempSt;
		tempSt = SearchFlightsController.from;
		SearchFlightsController.from = SearchFlightsController.where;
		SearchFlightsController.where = tempSt;
	}

	public static void originalAirports() {
		SearchFlightsController.from = tempFromLocation;
		SearchFlightsController.where = tempWhereLocation;
	}

	private void populateNonStopFlights(FlightApi nonStop) {
		oneWayFlight.setDate(HelperMethod.parseDateAndTime(nonStop.getDepartureDateTime(), "date"));
		oneWayFlight.setTime(HelperMethod.parseDateAndTime(nonStop.getDepartureDateTime(), "time") + " - "
				+ HelperMethod.parseDateAndTime(nonStop.getArrivalDateTime(), "time"));
		oneWayFlight.setMarketingAirLine(nonStop.getMarketingAirline());
		oneWayFlight.setTotalDuartion(HelperMethod.formatDuration(nonStop.getTotalTripTime()));
		oneWayFlight.setIata(SearchFlightsController.from + " - " + SearchFlightsController.where);
		oneWayFlight.setTotalMiles(nonStop.getDistance());
		oneWayFlight.setLayoverStatus(nonStop.getFlightType());
		if (oneWayFlight.stopCodeLbl != null) {
			oneWayFlight.stopCodeLbl.setVisible(false);
			oneWayFlight.stopCodeLbl.setManaged(false);
		}
		oneWayFlight.setAirlineLogo(HelperMethod.extractAirlineName(nonStop.getMarketingAirline()));
	}

	private void populateConnectingFlight(FlightConnecting connecting) {
		oneWayFlight.setDate(HelperMethod.parseDateAndTime(connecting.getDepartureDateTime_OpeningAirport(), "date"));
		oneWayFlight.setTime(HelperMethod.parseDateAndTime(connecting.getDepartureDateTime_OpeningAirport(), "time")
				+ " - " + HelperMethod.parseDateAndTime(connecting.getArrivalDateTime_FinalAirport(), "time"));
		oneWayFlight.setMarketingAirLine(connecting.eachFlight.get(0).getMarketingAirline());
		oneWayFlight.setTotalDuartion(HelperMethod.formatDuration(connecting.getFullTotalTT()));
		oneWayFlight.setIata(SearchFlightsController.from + " - " + SearchFlightsController.where);
		oneWayFlight.setTotalMiles(connecting.getFullTotalMiles());
		oneWayFlight.setLayoverStatus(connecting.getConncetedFligts() - 1 + " Stop(s)");
		if (oneWayFlight.stopCodeLbl != null) {
			oneWayFlight.stopCodeLbl.setVisible(true);
			oneWayFlight.stopCodeLbl.setManaged(true);
		}
		oneWayFlight.setLayoverCode(connecting.getIntervalIataCodes());
		oneWayFlight
				.setAirlineLogo(HelperMethod.extractAirlineName(connecting.eachFlight.get(0).getMarketingAirline()));
//		System.out.println(HelperMethod.extractAirlineName(connecting.eachFlight.get(0).getMarketingAirline()));
	}
}
