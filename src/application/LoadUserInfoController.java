package application;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import regularClasses.HelperMethod;

public class LoadUserInfoController implements Initializable {

	// Load info fields to the VBox
	FXMLLoader loader;
	Parent root;
	CustomerInfoController custInfo;
	CongratsController congratsPage;

	@FXML
	VBox infoContainerVb;
	@FXML
	Button bookBtn;
	@FXML
	Button backBtn;

	int adults;
	int children;
	int totalTraveler;
	int groupId;

	ArrayList<ArrayList<TextField>> travelerInfo = new ArrayList<>();
	Alert alert = new Alert(AlertType.INFORMATION);
	static OneWayFlightController flightToBook;
	static ArrayList<OneWayFlightController> roundTripData = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		goBack();
	}

	public void addInfoToContainer() {
		adults = Implementation.travelerCount.get("Adults");
		children = Implementation.travelerCount.get("Children");
		for (int i = 1; i <= adults; i++) {
			loadInfoPage();
			custInfo.setCustomerLabel("Adult", (i));
			travelerInfo.add(new ArrayList<>(Arrays.asList(custInfo.firstNameTF, custInfo.lastNameTF, custInfo.ageTF)));
		}
		for (int j = 1; j <= children; j++) {
			loadInfoPage();
			custInfo.setCustomerLabel("Child", (j));
			travelerInfo.add(new ArrayList<>(Arrays.asList(custInfo.firstNameTF, custInfo.lastNameTF, custInfo.ageTF)));
		}
		clickBookBtn();

	}

	public void loadInfoPage() {
		try {
			loader = new FXMLLoader(getClass().getResource("fxml/CustomerInfo.fxml"));
			root = loader.load();
			custInfo = loader.getController();
			infoContainerVb.getChildren().add(root);
			infoContainerVb.requestLayout();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void loadCongratsPage() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Congrats.fxml"));
			Node content = loader.load();
			MainPaneController.getInstance().loadFXMLInMainContainer(content);
			congratsPage = loader.getController();
			CongratsController.refNo = groupId;
			if (!FlightsController.isRoundTrip) {
				congratsPage.setLabel(
						"You are going to " + HelperMethod.returnCityOrAirport(flightToBook.getIata(), 1, "city"));
			} else {
				congratsPage.setLabel("You are going to "
						+ HelperMethod.returnCityOrAirport(roundTripData.get(0).getIata(), 1, "city"));
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	private void writeCustomerData() {
		try {
			groupId = HelperMethod.returnUniqGroupId();
			String query = "INSERT INTO Customer (First_Name, Last_Name, Age, GroupId)" + "Values (? , ?, ?, ?)";
			for (int i = 0; i < travelerInfo.size(); i++) {
				System.out.println(travelerInfo.size());
				PreparedStatement statement = Implementation.connection.prepareStatement(query);
				statement.setString(1, travelerInfo.get(i).get(0).getText());
				statement.setString(2, travelerInfo.get(i).get(1).getText());
				statement.setInt(3, Integer.parseInt(travelerInfo.get(i).get(2).getText()));
				statement.setInt(4, groupId);
				int rowsAffected = statement.executeUpdate();
				System.out.println(rowsAffected + " rows inserted.");
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage() + " and " + ex.getCause());
		}
	}

	private void writeFlightData() {
		try {
			String query = "INSERT INTO Flight (Origin_City, Destination_City, Marketing_Airline, Departure_Date, Time, Flight_Type, Flight_Duration)"
					+ "Values (?, ?, ?, ?, ?, ?, ?)";
			System.out.println(travelerInfo.size());
			PreparedStatement statement = Implementation.connection.prepareStatement(query);
			if (flightToBook != null) {
				statement.setString(1, HelperMethod.returnCityOrAirport(flightToBook.getIata(), 0, "city"));
				statement.setString(2, HelperMethod.returnCityOrAirport(flightToBook.getIata(), 1, "city"));
				statement.setString(3, flightToBook.getMarketingAirLine());
				statement.setString(4, flightToBook.getDate());
				statement.setString(5, flightToBook.getTime());
				statement.setString(6, flightToBook.getLayoverStatus());
				statement.setString(7, flightToBook.totalDuartion);
				int rowsAffected = statement.executeUpdate();
				System.out.println(rowsAffected + " rows inserted.");
			} else {
				for (int i = 0; i < roundTripData.size(); i++) {
					statement.setString(1, HelperMethod.returnCityOrAirport(roundTripData.get(i).getIata(), 0, "city"));
					statement.setString(2, HelperMethod.returnCityOrAirport(roundTripData.get(i).getIata(), 1, "city"));
					statement.setString(3, roundTripData.get(i).getMarketingAirLine());
					statement.setString(4, roundTripData.get(i).getDate());
					statement.setString(5, roundTripData.get(i).getTime());
					statement.setString(6, roundTripData.get(i).getLayoverStatus());
					statement.setString(7, roundTripData.get(i).getTotalDuartion());
					int rowsAffected = statement.executeUpdate();
					System.out.println(rowsAffected + " rows inserted.");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage() + " and " + ex.getCause());
		}
	}

	private void writeBookingData() {
		try {
			String query = "INSERT INTO Booking (Booking_Date_Time, Booking_Status, Number_Of_Passangers, Price, GroupId, FlightId)"
					+ "Values (?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = Implementation.connection.prepareStatement(query);
			if (!FlightsController.isRoundTrip) {
				statement.setString(1, HelperMethod.timeStamp());
				statement.setString(2, "Confirmed");
				statement.setInt(3, adults + children);
				statement.setString(4, "Under-development");
				statement.setInt(5, groupId);
				statement.setInt(6, HelperMethod.returnFlightId());
				int rowsAffected = statement.executeUpdate();
				System.out.println(rowsAffected + " rows inserted.");
			} else {
				for (int i = 0; i < roundTripData.size(); i++) {
					statement.setString(1, HelperMethod.timeStamp());
					statement.setString(2, "Confirmed");
					statement.setInt(3, adults + children);
					statement.setString(4, "Under-development");
					statement.setInt(5, groupId);
					statement.setInt(6, HelperMethod.returnRoundTripFlightIds(i));
					int rowsAffected = statement.executeUpdate();
					System.out.println(rowsAffected + " rows inserted.");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage() + " and " + ex.getCause());
		}
	}

	private void clickBookBtn() {
		bookBtn.setOnAction(event -> {

			for (int i = 0; i < travelerInfo.size(); i++) {
				if (HelperMethod.emptyFieldExsists(travelerInfo.get(i))) {
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Please complete any empty fields and try again");
					alert.showAndWait();
					return;
				} else {
					continue;
				}
			}
			writeFlightData();
			writeCustomerData();
			writeBookingData();
			loadCongratsPage();
		});
	}

	private void goBack() {
		backBtn.setOnAction(event -> {
			try {
				if (!FlightsController.isRoundTrip) {
					MainPaneController.getInstance().loadFXMLInMainContainer(OneWayFlightController.loadFlightInfo);
				} else {
					MainPaneController.getInstance().loadFXMLInMainContainer(LoadFlightInfoController.loadInBoundData);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		});
	}

}
