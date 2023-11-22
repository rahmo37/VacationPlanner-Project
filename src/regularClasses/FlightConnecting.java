package regularClasses;

import java.util.ArrayList;

public class FlightConnecting {

	String fullTotalTT;
	int noOfConncetedFligts;
	int fullTotalMiles;
	String intervalIataCodes;

	String allIataCodes;
	String departureDateTime_OpeningAirport;
	String arraivalDateTime_FinalAirport;

	// Each flight gathered as An array
	public ArrayList<FlightApi> eachFlight = new ArrayList<>();

	public int getFullTotalMiles() {
		return fullTotalMiles;
	}

	public void setFullTotalMiles(int fullTotalMiles) {
		this.fullTotalMiles = fullTotalMiles;
	}

	public String getFullTotalTT() {
		return fullTotalTT;
	}

	public void setFullTotalTT(String fullTotalTT) {
		this.fullTotalTT = fullTotalTT;
	}

	public int getConncetedFligts() {
		return noOfConncetedFligts;
	}

	public void setConncetedFligts(int conncetedFligts) {
		this.noOfConncetedFligts = conncetedFligts;
	}

	public String getIntervalIataCodes() {
		return intervalIataCodes;
	}

	public void setIntervalIataCodes(String intervalIataCodes) {
		this.intervalIataCodes = intervalIataCodes;
	}

	public String getDepartureDateTime_OpeningAirport() {
		return departureDateTime_OpeningAirport;
	}

	public void setDepartureDateTime_OpeningAirport(String departureDate_OpeningAirport) {
		this.departureDateTime_OpeningAirport = departureDate_OpeningAirport;
	}

	public String getArrivalDateTime_FinalAirport() {
		return arraivalDateTime_FinalAirport;
	}

	public void setArrivalDateTime_FinalAirport(String arraivalDateTime_FinalAirport) {
		this.arraivalDateTime_FinalAirport = arraivalDateTime_FinalAirport;
	}

	public String getAllIataCodes() {
		return allIataCodes;
	}

	public void setAllIataCodes(String allIataCodes) {
		this.allIataCodes = allIataCodes;
	}
}
