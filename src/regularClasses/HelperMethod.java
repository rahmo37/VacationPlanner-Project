package regularClasses;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import application.Implementation;
import javafx.scene.control.TextField;

public class HelperMethod {

	public static Object findCorrectValue(JSONObject json, String key, int index) {

		/*
		 * Check if the given key exists in the top-level json object using the has
		 * method. The result is stored in the exists variable.
		 */
		boolean exists = json.has(key);

		/*
		 * Declare an Iterator variable named keys to iterate through the keys of the
		 * JSON object
		 */
		Iterator<?> keys;

		/*
		 * Declare a String variable named nextKey to store the current key while
		 * iterating.
		 */
		String nextKey;

		// If the key does not exist in the top-level JSON object(exists == false),
		// execute the following block.
		if (!exists) {

			// Initialize the keys iterator with the top-level keys from the json object
			keys = json.keys();

			// Iterate through the keys of the JSON object.
			while (keys.hasNext()) {

				/*
				 * When i call the keys() method on a JSONObject, it returns an iterator with
				 * keys of type Object. However, i know that the keys in a JSON object must be
				 * strings according to the JSON specification. To ensure that i am working with
				 * a String type while processing the keys in the JSON object, i explicitly cast
				 * the key returned by the iterator to a String type.
				 */
				// Get the next key in the iterator and store it in nextKey.
				nextKey = (String) keys.next();

				// Start a try block to handle any exceptions that might occur while processing
				// the JSON object.
				try {

					/*
					 * The key saved in the nextKey String could be a JSONObject or JSONArray,So I
					 * am checking to see if the variable saved in the nextKey is Array or Object
					 */

					// Check if the value associated with the current nextKey is a nested
					// JSONObject.
					if (json.get(nextKey) instanceof JSONObject) {

						// If the key has not been found yet, execute the following block.
						if (exists == false) {

							// Recursively call the findCorrectValue method with the nested JSONObject
							// to continue the search for the key
							Object result = findCorrectValue(json.getJSONObject(nextKey), key, index);

							/*
							 * This part is confusing but here is some explanation When the findCorrectValue
							 * method is called recursively, it returns the value associated with the target
							 * key if it is found in the nested JSON structure, or null if the key is not
							 * found. The if (result != null) condition checks whether the result contains a
							 * non-null value. If the result is not null, it means that the target key has
							 * been found in the nested JSON structure, and its associated value is stored
							 * in result. In this case, the method immediately returns the value associated
							 * with the key (i.e., result) to the caller. If the result is null, it means
							 * that the target key was not found in the current nested JSON structure
							 * (either a JSONObject or a JSONArray). The code will continue searching for
							 * the key in other nested structures (if any) until all possibilities have been
							 * exhausted. If the key is not found in any nested structure, the method will
							 * eventually return null to indicate that the key was not found.
							 */

							// If the recursive call returns a non-null value, it means the key has been
							// found.
							if (result != null) {

								// Return the value associated with the key.
								return result;
							}
						}
					}
					// If the value associated with the current nextKey is a JSONArray, execute the
					// following block.
					else if (json.get(nextKey) instanceof JSONArray) {

						// Get the JSONArray associated with the current nextKey
						JSONArray jsonarray = json.getJSONArray(nextKey);

						// Get the element at the specified index within the JSONArray,
						// convert it to a String, and store it in jsonarrayString
						String jsonarrayString = jsonarray.get(index).toString();

						// Create a new JSONObject named innerJSON using the jsonarrayString.
						JSONObject innerJSON = new JSONObject(jsonarrayString);

						// If the key has not been found yet, execute the following block.
						if (exists == false) {
							// Recursively call the findCorrectValue method with the innerJSON object
							Object result = findCorrectValue(innerJSON, key, index);

							// Same code, explained above
							if (result != null) {
								return result;
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			return null;
		} else {
			return json.get(key);
		}
	}

	public static String timeStamp() {
		String timeStamp = "";
		String dateTimePattern = "E,MMM-dd-yyyy HH:mm:ss";
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(dateTimePattern);
		return timeStamp = localDateTime.format(dateTimeFormat);
	}

	public static void insertionSort(List<String> strArr) {
		String temp;

		for (int i = 0; i < strArr.size(); i++) {
			for (int j = i + 1; j < strArr.size(); j++) {
				if (strArr.get(i).compareToIgnoreCase(strArr.get(j)) > 0) {
					temp = strArr.get(i);
					strArr.set(i, strArr.get(j));
					strArr.set(j, temp);
				}
			}
		}
	}

	public static int findTheSmallestValue(int x, int y) {
		if (x != 0 && y != 0) {
			if (x > y) {
				return y;
			} else if (x < y) {
				return x;
			} else {
				return x;
			}

		} else {
			return 0;
		}
	}

	public static String parseDateAndTime(String combinedDateTime, String signal) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(combinedDateTime, formatter);

		if (signal.equalsIgnoreCase("date")) {
			return dateTime.toLocalDate().toString();
		} else if (signal.equalsIgnoreCase("time")) {
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
			return dateTime.toLocalTime().format(timeFormat);
		}

		return null;
	}

	public static String formatDuration(String durationString) {
		Duration duration = Duration.parse(durationString);
		long hours = duration.toHours();
		long minutes = duration.toMinutes() % 60;
		return String.format("PT %02dH : %02dM", hours, minutes);
	}

	public static String extractMiddleIataCodes(String iataCodes) {
		List<String> codes = Arrays.asList(iataCodes.split(" "));
		StringBuilder middleCodes = new StringBuilder();

		if (codes.size() > 2) {
			for (int i = 1; i < codes.size() - 1; i++) {
				middleCodes.append(codes.get(i));

				// Add a space between codes, except for the last one
				if (i < codes.size() - 2) {
					middleCodes.append(" ");
				}
			}
		}

		return middleCodes.toString();
	}

	private static final Set<String> IGNORED_WORDS = new HashSet<>();

	static {
		IGNORED_WORDS.add("air");
		IGNORED_WORDS.add("airlines");
		IGNORED_WORDS.add("airways");
		IGNORED_WORDS.add("airline");
		// Add any other words to ignore if needed
	}

	public static String extractAirlineName(String airlineName) {
		String[] words = airlineName.split(" ");
		StringBuilder result = new StringBuilder();

		for (String word : words) {
			if (!IGNORED_WORDS.contains(word.toLowerCase())) {
				if (result.length() > 0) {
					result.append("_");
				}
				result.append(word);
			}
		}

		return result.toString().toLowerCase();
	}

	public static String returnCity(String iata) {
		for (int i = 0; i < Implementation.IATAcodes.size(); i++) {
			if (iata.equalsIgnoreCase(Implementation.IATAcodes.get(i).getCode())) {
				return Implementation.IATAcodes.get(i).getCity();
			}
		}
		return null;
	}

	public static ArrayList<String> returnAllIATA(String fromCode, String whereCode, String intervalCode) {
		String[] intervalIata = intervalCode.split(" ");
		ArrayList<String> allIata = new ArrayList<>();
		allIata.add(fromCode);
		for (int i = 0; i < intervalIata.length; i++) {
			allIata.add(intervalIata[i]);
		}
		allIata.add(whereCode);
		return allIata;
	}

	public static boolean emptyFieldExsists(ArrayList<TextField> fieldSet) {
		for (int i = 0; i < fieldSet.size(); i++) {
			String tempSt = fieldSet.get(i).getText();
			if (tempSt.isEmpty() || tempSt.isBlank() || tempSt == null) {
				return true;
			}
		}
		return false;
	}

	public static String returnCityOrAirport(String iataString, int index, String signal) {
		String[] iataSet = iataString.split(" - ");

		for (int i = 0; i < Implementation.IATAcodes.size(); i++) {
			if (iataSet[index].equalsIgnoreCase(Implementation.IATAcodes.get(i).getCode())) {
				if (signal.equalsIgnoreCase("city")) {
					return Implementation.IATAcodes.get(i).getCity();
				} else if (signal.equalsIgnoreCase("airport")) {
					return Implementation.IATAcodes.get(i).getAirport_Name();
				}
			}
		}
		return iataSet[0];
	}

	public static int returnUniqGroupId() {
		try {
			String query = "SELECT GroupId FROM Customer ORDER BY GroupId DESC LIMIT 1";
			Statement statement = Implementation.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet = statement.executeQuery(query);
			int groupId = 0;
			if (resultSet.next()) {
				return groupId = resultSet.getInt("GroupId") + new Random().nextInt(1000) + 1;
			} else {
				return groupId + new Random().nextInt(1000) + 1;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return 0;
	}

	public static int returnFlightId() {
		try {
			String query = "SELECT FlightId FROM Flight ORDER BY FlightId DESC LIMIT 1";
			Statement statement = Implementation.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("FlightId");
			} else {
				System.out.println("No flight id");
				return -1;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return -1;
	}

	public static int returnRoundTripFlightIds(int i) {
		ArrayList<Integer> flightIds = new ArrayList<>();
		try {
			String query = "SELECT FlightId FROM Flight ORDER BY FlightId DESC LIMIT 2";
			Statement statement = Implementation.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				flightIds.add(resultSet.getInt("FlightId"));
			}
			Collections.reverse(flightIds);
			if (flightIds.isEmpty()) {
				System.out.println("No flight ids");
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flightIds.get(i);
	}

	public static ArrayList<Integer> returnPrice(int noOfFlight, String signal) {
		ArrayList<Integer> priceList = new ArrayList<>();
		if (signal.equalsIgnoreCase("one-way")) {
			for (int i = 0; i < noOfFlight; i++) {
				Random rand = new Random();
				int price = rand.nextInt(2501) + 500;
				priceList.add(price);
			}
		} else {
			for (int i = 0; i < noOfFlight; i++) {
				Random rand = new Random();
				int price = (rand.nextInt(2501) + 500) * 2;
				priceList.add(price);
			}
		}
		Collections.sort(priceList);
		return priceList;
	}

	public static String getDayName(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateString, formatter);
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return dayOfWeek.toString().substring(0, 1) + dayOfWeek.toString().substring(1).toLowerCase();
	}
	
    public static String getWeatherDescription(String input) {
        String[] words = input.split("(?i) throughout");
        return words[0].trim();
    }

	public static void main(String[] args) throws Exception {
		String dateString = "2023-05-06";
		System.out.println(getDayName(dateString));
	}

}
