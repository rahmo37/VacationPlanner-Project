package regularClasses;

public class FlightApi {
	String locationTo;
	String locationFrom;
	String departureAirport;
	String depaertureTerminal;
	String departureDateTime;
	String arrivalAirport;
	String arrivalTerminal;
	String arrivalDateTime;
	String marketingAirline;
	int flightNumber;
	int distance;
	String totalTripTime;
	String flightType;
	String uniqueFlightId;

	public FlightApi(String locationTo, String locationFrom, String departureAirport, String depaertureTerminal,
			String departureDateTime, String arrivalAirport, String arrivalTerminal, String arrivalDateTime,
			String marketingAirline, int flightNumber, int distance, String totalTripTime, String flightType,
			String uniqueFlightId) {
		super();
		this.locationTo = locationTo;
		this.locationFrom = locationFrom;
		this.departureAirport = departureAirport;
		this.depaertureTerminal = depaertureTerminal;
		this.departureDateTime = departureDateTime;
		this.arrivalAirport = arrivalAirport;
		this.arrivalTerminal = arrivalTerminal;
		this.arrivalDateTime = arrivalDateTime;
		this.marketingAirline = marketingAirline;
		this.flightNumber = flightNumber;
		this.distance = distance;
		this.totalTripTime = totalTripTime;
		this.flightType = flightType;
		this.uniqueFlightId = uniqueFlightId;
	}

	public String getLocationTo() {
		return locationTo;
	}

	public void setLocationTo(String locationTo) {
		this.locationTo = locationTo;
	}

	public String getLocationFrom() {
		return locationFrom;
	}

	public void setLocationFrom(String locationFrom) {
		this.locationFrom = locationFrom;
	}

	public String getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}

	public String getDepaertureTerminal() {
		return depaertureTerminal;
	}

	public void setDepaertureTerminal(String depaertureTerminal) {
		this.depaertureTerminal = depaertureTerminal;
	}

	public String getDepartureDateTime() {
		return departureDateTime;
	}

	public void setDepartureDateTime(String departureDateTime) {
		this.departureDateTime = departureDateTime;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public String getArrivalTerminal() {
		return arrivalTerminal;
	}

	public void setArrivalTerminal(String arrivalTerminal) {
		this.arrivalTerminal = arrivalTerminal;
	}

	public String getArrivalDateTime() {
		return arrivalDateTime;
	}

	public void setArrivalDateTime(String arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}

	public String getMarketingAirline() {
		return marketingAirline;
	}

	public void setMarketingAirline(String marketingAirline) {
		this.marketingAirline = marketingAirline;
	}

	public int getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(int flightNumber) {
		this.flightNumber = flightNumber;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getTotalTripTime() {
		return totalTripTime;
	}

	public void setTotalTripTime(String totalTripTime) {
		this.totalTripTime = totalTripTime;
	}

	public String getFlightType() {
		return flightType;
	}

	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}

	public String getUniqueFlightId() {
		return uniqueFlightId;
	}

	public void setUniqueFlightId(String uniqueFlightId) {
		this.uniqueFlightId = uniqueFlightId;
	}

	@Override
	public String toString() {
		return " locationTo=" + locationTo + "\n locationFrom=" + locationFrom + "\n departureAirport="
				+ departureAirport + "\n depaertureTerminal=" + depaertureTerminal + "\n departureDateTime="
				+ departureDateTime + "\n arrivalAirport=" + arrivalAirport + "\n arrivalTerminal=" + arrivalTerminal
				+ "\n arrivalDateTime=" + arrivalDateTime + "\n marketingAirline=" + marketingAirline
				+ "\n flightNumber=" + flightNumber + "\n distance=" + distance + "\n totalTripTime=" + totalTripTime
				+ "\n flightType=" + flightType + "\n uniqueFlightId=" + uniqueFlightId;
	}

}
