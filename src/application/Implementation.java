package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import regularClasses.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Implementation {

	public static Connection connection;
	Statement statement;
	ResultSet resultSet;

	// DS used for users
	static Vector<User> users;

	// DS used for flights

	// Each individual flights
	ArrayList<FlightApi> flights;

	// Each individual flights but connected as one instance of FlightConnecting
	// class, creating a complete one-way
	ArrayList<FlightConnecting> conFlights;

	// Both the non-stop flights and connecting flights combined
	ArrayList<Object> combinedFlights;

	// Each out-bound and in-bound flights in a round trip, combined
	ArrayList<FlightReturning> roundTripFlights;

	// Static and unchanging IATAcodes
	public static ArrayList<AllIATACodes> IATAcodes = new ArrayList<>();

	// Unchanging traveler count when searching for flights
	static HashMap<String, Integer> travelerCount = new HashMap<>();

	// Static and unchanging currency codes
	static HashMap<String, String> currencyCode = new HashMap<>();

	// DS used for weather
	static ArrayList<WeatherApi> weatherOfSevenDays;

	Scanner keyboard;
	int loggedinuser;

	// Temporary variables used in flights
	String xml = "";
	String wTo = null;
	String wFrom = null;
	String dAirport = null;
	String deTerminal = null;
	String dDaTi = null;
	String aAirport = null;
	String arTerminal = null;
	String aDaTi = null;
	String mAirline = null;
	int flNo = 0;
	int dis = 0;
	String totalTT = null;
	String flType = null;
	String uFlId = null;
	String noFligthsFound;
	int price;

	// Initializing the Data Structures
	public Implementation() {
		// Initialize the DS for users
		users = new Vector<>();

		// Initialize the DS for flights
		keyboard = new Scanner(System.in);
		flights = new ArrayList<>();
		conFlights = new ArrayList<>();
		combinedFlights = new ArrayList<>();
		roundTripFlights = new ArrayList<>();

		// Initialize the DS for weather
		weatherOfSevenDays = new ArrayList<>();

	}

	public void initialize() throws Exception {
		connectDB();
		loadUsers();
		loadCurrCode();
		loadIATACode();
	}

	private void loadUsers() {

		try {
			String query = "SELECT * FROM User";
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				users.add(new User(loggedinuser, resultSet.getString("FirstName"), resultSet.getString("LastName"),
						resultSet.getString("Email"), resultSet.getString("Phone"), resultSet.getString("Password"),
						resultSet.getBoolean("isAdmin")));

//				i++;
			}
			resultSet.close();
			Thread.sleep(250);

			System.out.println("Users Loaded...");
			Thread.sleep(500);
		} catch (Exception e) {

		}
	}

	private void loadCurrCode() {
		try {
			String query = "SELECT * FROM Currency";
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				currencyCode.put(resultSet.getString("Name"), resultSet.getString("Code"));
			}
		} catch (Exception e) {

		}
		System.out.println(currencyCode);
	}

	public void loadIATACode() {
		try {
			String query = "SELECT * FROM IATA";
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				IATAcodes.add(new AllIATACodes(resultSet.getString("Airport_Name"), resultSet.getString("City"),
						resultSet.getString("Country"), resultSet.getString("IATA")));
			}
			resultSet.close();
			Thread.sleep(250);
			System.out.println("IATA codes loaded...");
			System.out.println(IATAcodes.get(0).getCode());
			Thread.sleep(500);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void connectDB() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String msAccDB = "VacationPlanner.accdb";
			String dbURL = "jdbc:ucanaccess://" + msAccDB;
			connection = DriverManager.getConnection(dbURL);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("Database Connected...");
			Thread.sleep(300);
		} catch (Exception e) {
			System.out.println("Error!");
		}
	}

	public void flightRequest(String whereFrom, String whereTo, String date) {

		try {
			// Send HTTP request
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://timetable-lookup.p.rapidapi.com/TimeTable/" + whereFrom + "/" + whereTo
							+ "/" + date + "/" + "?Max_Results=25"))
					.header("X-RapidAPI-Key", "7365c86a9fmsh9e8481deb81d7acp1bb663jsn906e105c08ec")
					.header("X-RapidAPI-Host", "timetable-lookup.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody()).build();
			// BodyPublishers.noBody() indicates that i don't need to post a body to receive
			// the Http resource

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			// The BodyHandler is responsible for processing the response body when the
			// request is completed. In this case,
//			ofString() indicates that the response body should be treated as a String.

			xml = response.body();
			JSONObject json = XML.toJSONObject(xml);
			System.out.println(json);
//			 xml to Json

//			System.out.println(json);
//			 Printing the returned json object

//			Flight data for test
//			JSONObject json = new JSONObject(Temp.mixedFlights);
			/*
			 * Sometimes if only one flight is returned, the FlightDetails variable is
			 * returned just as an object, if multiple flights are returned then the
			 * FlightDetails Variable is an Array So i am checking if this variable is an
			 * array or not, depending on that i will have to parse the Variable differently
			 */

			// if FlightDetails is json array
			if (HelperMethod.findCorrectValue(json, "FlightDetails", 0) instanceof JSONArray) {

				// All flights
				JSONArray totalFlightsArray = new JSONArray(
						(JSONArray) HelperMethod.findCorrectValue(json, "FlightDetails", 0));

				for (int h = 0; h < totalFlightsArray.length(); h++) {

					// Flight Type
					// Flight Type determines which block of code to use
					flType = (String) HelperMethod.findCorrectValue(totalFlightsArray.getJSONObject(h), "FLSFlightType",
							h);
					System.out.println(" Filght Type: " + flType + "\n --- --- --- ---");

					if (flType.equals("Connect")) {

						conncetedFlight(totalFlightsArray.getJSONObject(h));
					} else {
						nonStopFlights(totalFlightsArray.getJSONObject(h));
					}
				}
			} else {
				// if FlightDetails is json object
				JSONObject totalFlightObject = (JSONObject) HelperMethod.findCorrectValue(json, "FlightDetails", 0);
				System.out.println(totalFlightObject);
				flType = (String) HelperMethod.findCorrectValue(totalFlightObject, "FLSFlightType", 0);

				System.out.println(" Filght Type: " + flType + "\n--- --- --- ---");

				if (flType.equals("Connect")) {

					conncetedFlight(totalFlightObject);
				} else {
					nonStopFlights(totalFlightObject);
				}
			}
			System.out.println(flights.size());
			System.out.println(conFlights.size());

			/*
			 * After populating both the non-stop flights and connecting flights I am
			 * combining both flights in one ArrayList(combinedFlihts)
			 */
			combinedFlights.addAll(flights);
			combinedFlights.addAll(conFlights);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Selected combination of airports has no flights available!");
		}

	}

//Sub method of flight request
	private void nonStopFlights(JSONObject json) {

		// Non Stop flights
		// Assigning temp variables with flight details
		wTo = (String) HelperMethod.findCorrectValue(json, "FLSArrivalName", 0);
		wFrom = (String) HelperMethod.findCorrectValue(json, "FLSDepartureName", 0);

		dAirport = (String) HelperMethod.findCorrectValue(
				(JSONObject) HelperMethod.findCorrectValue(json, "DepartureAirport", 0), "FLSLocationName", 0);

		if (new JSONObject(HelperMethod.findCorrectValue(json, "DepartureAirport", 0).toString())
				.get("Terminal") instanceof Integer) {
			deTerminal = String.valueOf(HelperMethod.findCorrectValue(
					(JSONObject) HelperMethod.findCorrectValue(json, "DepartureAirport", 0), "Terminal", 0));
		} else {
			deTerminal = (String) HelperMethod.findCorrectValue(
					(JSONObject) HelperMethod.findCorrectValue(json, "DepartureAirport", 0), "Terminal", 0);
		}
		dDaTi = (String) HelperMethod.findCorrectValue(json, "FLSDepartureDateTime", 0);

		aAirport = (String) HelperMethod.findCorrectValue(
				(JSONObject) HelperMethod.findCorrectValue(json, "ArrivalAirport", 0), "FLSLocationName", 0);

		if (new JSONObject(HelperMethod.findCorrectValue(json, "ArrivalAirport", 0).toString())
				.get("Terminal") instanceof Integer) {
			arTerminal = String.valueOf(HelperMethod.findCorrectValue(
					(JSONObject) HelperMethod.findCorrectValue(json, "ArrivalAirport", 0), "Terminal", 0));
		} else {
			arTerminal = (String) HelperMethod.findCorrectValue(
					(JSONObject) HelperMethod.findCorrectValue(json, "ArrivalAirport", 0), "Terminal", 0);
		}
		aDaTi = (String) HelperMethod.findCorrectValue(json, "FLSArrivalDateTime", 0);
		mAirline = (String) HelperMethod.findCorrectValue(
				(JSONObject) HelperMethod.findCorrectValue(json, "MarketingAirline", 0), "CompanyShortName", 0);

		flNo = (int) HelperMethod.findCorrectValue(json, "FlightNumber", 0);
		dis = (int) HelperMethod.findCorrectValue(json, "TotalMiles", 0);
		totalTT = (String) HelperMethod.findCorrectValue(json, "TotalTripTime", 0);
		flType = (String) HelperMethod.findCorrectValue(json, "FLSFlightType", 0);
		uFlId = (String) HelperMethod.findCorrectValue(json, "FLSUUID", 0);

//		System.out.println(wTo + " " + wFrom + " " + dAirport + " " + deTerminal + " " + dDaTi + " " + aAirport + " "
//				+ arTerminal + " " + aDaTi + " " + mAirline + " " + flNo + " " + dis + " " + totalTT + " " + flType
//				+ " " + uFlId);

//			 Create flight objects
		flights.add(new FlightApi(wTo, wFrom, dAirport, deTerminal, dDaTi, aAirport, arTerminal, aDaTi, mAirline, flNo,
				dis, totalTT, flType, uFlId));
//		System.out.println(flights.get(flights.size() - 1).toString() + "\n");

//		System.out.println("End of Fligth: " + (flights.size()) + "\n\n");
	}

	// Sub method of flight request
	private void conncetedFlight(JSONObject json) {

		JSONArray connectingFlightsArray = new JSONArray(

				// Individual Flights instances
				(JSONArray) HelperMethod.findCorrectValue(json, "FlightLegDetails", 0));

		// Creating connecting flight object
		conFlights.add(new FlightConnecting());

		// initializing each variable in the Flight connecting class
		conFlights.get(conFlights.size() - 1).setConncetedFligts(connectingFlightsArray.length());
		conFlights.get(conFlights.size() - 1)
				.setFullTotalTT((String) HelperMethod.findCorrectValue(json, "TotalFlightTime", 0));
		conFlights.get(conFlights.size() - 1)
				.setFullTotalMiles((int) HelperMethod.findCorrectValue(json, "TotalMiles", 0));
		String tempSt1 = "";
		for (int i = connectingFlightsArray.length() - 1; i >= 1; i--) {
			tempSt1 = (String) HelperMethod.findCorrectValue(json, "LocationCode", i) + " " + tempSt1;
		}
		conFlights.get(conFlights.size() - 1).setIntervalIataCodes(tempSt1);

		conFlights.get(conFlights.size() - 1).setDepartureDateTime_OpeningAirport(
				(String) HelperMethod.findCorrectValue(json, "FLSDepartureDateTime", 0));
		conFlights.get(conFlights.size() - 1)
				.setArrivalDateTime_FinalAirport((String) HelperMethod.findCorrectValue(json, "FLSArrivalDateTime", 0));

		// Printing the Connecting flight variables
		System.out.println("This trip has :" + conFlights.get(0).getConncetedFligts() + " Flights and a total "
				+ conFlights.get(0).getFullTotalTT() + " Trip Time " + conFlights.get(0).getFullTotalMiles()
				+ " Trip  Total Miles" + conFlights.get(0).getIntervalIataCodes() + "Departure Date Time "
				+ conFlights.get(0).getDepartureDateTime_OpeningAirport()
				+ conFlights.get(0).getArrivalDateTime_FinalAirport() + conFlights.get(0).getAllIataCodes() + "\n");

		// Now populating each flight and saving them in eachFlight array

		// where to
		wTo = (String) HelperMethod.findCorrectValue(json, "FLSArrivalName", 0);

		// where from
		wFrom = (String) HelperMethod.findCorrectValue(json, "FLSDepartureName", 0);

		for (int j = 0; j < connectingFlightsArray.length(); j++) {
			// departure airports
			dAirport = new JSONObject(HelperMethod
					.findCorrectValue(connectingFlightsArray.getJSONObject(j), "DepartureAirport", j).toString())
					.getString("FLSLocationName");
			// departing terminals
			if (new JSONObject(HelperMethod
					.findCorrectValue(connectingFlightsArray.getJSONObject(j), "DepartureAirport", j).toString())
					.get("Terminal") instanceof Integer) {
				deTerminal = String.valueOf(new JSONObject(HelperMethod
						.findCorrectValue(connectingFlightsArray.getJSONObject(j), "DepartureAirport", j).toString())
						.get("Terminal"));
			} else {
				if (new JSONObject(HelperMethod
						.findCorrectValue(connectingFlightsArray.getJSONObject(j), "DepartureAirport", j).toString())
						.getString("Terminal").equals(" ")) {
					deTerminal = "To Be Determined";
				} else {
					deTerminal = new JSONObject(HelperMethod
							.findCorrectValue(connectingFlightsArray.getJSONObject(j), "DepartureAirport", j)
							.toString()).getString("Terminal");
				}
			}

			// departing date and times
			dDaTi = (String) HelperMethod.findCorrectValue(connectingFlightsArray.getJSONObject(j), "DepartureDateTime",
					j);

			// arrival airports
			aAirport = new JSONObject(HelperMethod
					.findCorrectValue(connectingFlightsArray.getJSONObject(j), "ArrivalAirport", j).toString())
					.getString("FLSLocationName");

			// arriving terminals
			if (new JSONObject(HelperMethod
					.findCorrectValue(connectingFlightsArray.getJSONObject(j), "ArrivalAirport", j).toString())
					.get("Terminal") instanceof Integer) {
				arTerminal = String.valueOf(new JSONObject(HelperMethod
						.findCorrectValue(connectingFlightsArray.getJSONObject(j), "ArrivalAirport", j).toString())
						.get("Terminal"));
			} else {
				if (new JSONObject(HelperMethod
						.findCorrectValue(connectingFlightsArray.getJSONObject(j), "ArrivalAirport", j).toString())
						.getString("Terminal").equals(" ")) {
					arTerminal = "To Be Determined";
				} else {
					arTerminal = new JSONObject(HelperMethod
							.findCorrectValue(connectingFlightsArray.getJSONObject(j), "ArrivalAirport", j).toString())
							.getString("Terminal");
				}
			}

			// arrival date and time
			aDaTi = (String) HelperMethod.findCorrectValue(connectingFlightsArray.getJSONObject(j), "ArrivalDateTime",
					j);

			// marketing airline
			mAirline = new JSONObject(HelperMethod
					.findCorrectValue(connectingFlightsArray.getJSONObject(j), "MarketingAirline", j).toString())
					.getString("CompanyShortName");

			// Flight numbers
			flNo = (int) HelperMethod.findCorrectValue(connectingFlightsArray.getJSONObject(j), "FlightNumber", j);

			dis = (int) HelperMethod.findCorrectValue(connectingFlightsArray.getJSONObject(j), "LegDistance", j);

			// Total Trip Time
			totalTT = (String) HelperMethod.findCorrectValue(connectingFlightsArray.getJSONObject(j), "JourneyDuration",
					j);

			uFlId = (String) HelperMethod.findCorrectValue(connectingFlightsArray.getJSONObject(j), "FLSUUID", j);
			// initializing each connecting flight
			conFlights.get(conFlights.size() - 1).eachFlight.add(new FlightApi(wTo, wFrom, dAirport, deTerminal, dDaTi,
					aAirport, arTerminal, aDaTi, mAirline, flNo, dis, totalTT, flType, uFlId));
			System.out.println(conFlights.get(conFlights.size() - 1).eachFlight.get(j) + "\n");
		}
		System.out.println("End of Fligth: " + (conFlights.size()) + "\n\n");

	}

	// combines the out-bound and in-bound trips and creates one round trip
	public void roundTrip(String locationA, String locationB, String startDate, String returnDate) {
		Implementation outboundFlights = new Implementation();
		Implementation inboundFlights = new Implementation();

		// making a Flight Api call from current location to destination
		outboundFlights.flightRequest(locationA, locationB, startDate);
		// thread will sleep for 2 seconds because i can make only one API call
		// per-second
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// making a Flight Api call from destination to current location
		inboundFlights.flightRequest(locationB, locationA, returnDate);

		// Finding the smallest Array of flights
		int totalIteration = HelperMethod.findTheSmallestValue(outboundFlights.combinedFlights.size(),
				inboundFlights.combinedFlights.size());

		/*
		 * Paring the first combinedFlights(non-stop or connecting)object from the
		 * outboundFlights with the first object in the combinedFlights from the
		 * inboundFlights
		 */
		if (totalIteration != 0) {
			for (int i = 0; i < totalIteration; i++) {
				roundTripFlights.add(new FlightReturning());
				roundTripFlights.get(i).eachRoundTripFlights.add(outboundFlights.combinedFlights.get(i));
				roundTripFlights.get(i).eachRoundTripFlights.add(inboundFlights.combinedFlights.get(i));
			}
		} else {
			System.out.println("no returning flights found");
		}
//		for (int i = 0; i < 2; i++) {
//			for (int j = 0; j < ((FlightConnecting) roundTripFlights.get(0).eachRoundTripFlights.get(i)).eachFlight
//					.size(); j++) {
//				System.out.println(((FlightConnecting) roundTripFlights.get(0).eachRoundTripFlights.get(i)).eachFlight
//						.get(j).toString() + "\n\n----------------------------------------\n\n");
//			}
//		}
	}

	// under-development
	private static int retrunPrice(Implementation flight) {
		int price = 0;
		return price;

	}

	// sends weather request
	private void weatherRequest() {
		String inline = "";
		String day = "";
		String date;
		double temp;
		double feelsLike;
		double max;
		double min;
		String description;
		try {
			String head = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
			String location = "Deer Park";
			String tail = "?unitGroup=us&key=X32BCAVR96ANGVLY6CG7PUN6S&contentType=json";
			String query = head + location + tail;
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {
				throw new RuntimeException("HttpURLConnection: " + responseCode);
			} else {
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNextLine()) {
					inline += sc.nextLine();
				}
				sc.close();
			}
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode node = objectMapper.readValue(inline, JsonNode.class);
			JsonNode array = node.get("days");
			for (int i = 0; i < 7; i++) {
				JsonNode jsonNameNode = array.get(i);
				// Get Values
				if (i == 0) {
					day = "Today";
				} else if (i == 1) {
					day = "Tomorrow";
				} else {
					day = "day: " + (i + 1);
				}
				date = jsonNameNode.get("datetime").asText();
				temp = jsonNameNode.get("temp").asDouble();
				feelsLike = jsonNameNode.get("feelslike").asDouble();
				max = jsonNameNode.get("tempmax").asDouble();
				min = jsonNameNode.get("tempmin").asDouble();
				description = jsonNameNode.get("description").asText();
				weatherOfSevenDays.add(new WeatherApi(location, day, date, temp, max, min, feelsLike, description));
				System.out.println(weatherOfSevenDays.get(i).toString());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// sends weather request
	public static double currencyRequest(String fromCode, String toCode, double amount) {
		try {
			final String APIKEY = "c850460cdd5e1931d6dc83a6";
			String GET_URL = "https://v6.exchangerate-api.com/v6/" + APIKEY + "/pair/" + fromCode + "/" + toCode;
			URL url = new URL(GET_URL);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Authorization", "Bearer " + APIKEY);
			int responseCode = httpURLConnection.getResponseCode();
			System.out.println(responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {

				BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				String inputline;
				StringBuffer response = new StringBuffer();

				while ((inputline = in.readLine()) != null) {
					response.append(inputline);
				}
				in.close();

				JSONObject obj = new JSONObject(response.toString());
				System.out.println(obj);
				double exchangeRate = obj.getDouble("conversion_rate");
				return exchangeRate * amount;
			} else {
				System.out.println("Get a request failed!");
				return 0.0;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0.0;
	}

	public static boolean loginUser(String email, String password) {

		for (int i = 0; i < users.size(); i++) {
			if (email.equalsIgnoreCase(users.get(i).getEmail())) {
				if (password.equals(users.get(i).getPassword())) {
					MainPaneController.userName = users.get(i).getFirstName();
					return true;
				} else {
					return false;
				}
			}
		}
		return false;

	}

	static void signUp(String firstName, String lastName, String email, String phone, String password) {
		String querry = "INSERT INTO User (FirstName, LastName, Email, Phone, Password, isAdmin, DateAdded) "
				+ "VALUES (? ,? ,? ,? ,?, ?, ?)";
		int rows = 0;
		Boolean isAdmin = false;
		try {
			PreparedStatement ps = connection.prepareStatement(querry);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, email);
			ps.setString(4, phone);
			ps.setString(5, password);
			ps.setBoolean(6, isAdmin);
			ps.setString(7, HelperMethod.timeStamp());

			rows += ps.executeUpdate();
			if (rows > 0) {
				JOptionPane.showMessageDialog(null, "Account Created");
			}
			users.add(new User(users.size() + 1, firstName, lastName, email, phone, password, isAdmin));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
