package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FlightInfoControllerOW {

	String departingFrom;
	String departingFromCity;
	String departingDateTime;
	String departingAirPort;
	String departingTerminal;
	String arriviingAt;
	String arrivingAtCity;
	String arrivingTerminal;
	String arrivingAirport;
	String arrivingDateTime;
	String flightNo;
	String flightClass;
	String totalJourneyTime;
	String totalJourneyDistance;

	@FXML
	Label departingFromLbl;
	@FXML
	Label departingFromCityLbl;
	@FXML
	Label departingDateTimeLbl;
	@FXML
	Label departingAirPortLbl;
	@FXML
	Label departingterminalLbl;
	@FXML
	Label arriviingAtLbl;
	@FXML
	Label arrivingAtCityLbl;
	@FXML
	Label arrivingAtDateTimeLbl;
	@FXML
	Label arrivingAirportLbl;
	@FXML
	Label arrivingTerminalLbl;
	@FXML
	Label flightNoLbl;
	@FXML
	Label flightClassLbl;
	@FXML
	Label totalJourneyTimeLbl;
	@FXML
	Label totalJourneyDistanceLbl;

	public String getDepartingFromCity() {
		return departingFromCity;
	}

	public void setDepartingFromCity(String departingFromCity) {
		this.departingFromCity = departingFromCity;
	}

	public String getDepartingDateTime() {
		return departingDateTime;
	}

	public void setDepartingDateTime(String departingDateTime) {
		this.departingDateTime = departingDateTime;
	}

	public String getDepartingAirPort() {
		return departingAirPort;
	}

	public void setDepartingAirPort(String departingAirPort) {
		this.departingAirPort = departingAirPort;
	}

	public String getDepartingTerminal() {
		return departingTerminal;
	}

	public void setDepartingTerminal(String departingTerminal) {
		this.departingTerminal = departingTerminal;
	}

	public String getArrivingAtCity() {
		return arrivingAtCity;
	}

	public void setArrivingAtCity(String arrivingAtCity) {
		this.arrivingAtCity = arrivingAtCity;
	}

	public String getArrivingTerminal() {
		return arrivingTerminal;
	}

	public void setArrivingTerminal(String arrivingTerminal) {
		this.arrivingTerminal = arrivingTerminal;
	}

	public String getArrivingAirport() {
		return arrivingAirport;
	}

	public void setArrivingAirport(String arrivingAirport) {
		this.arrivingAirport = arrivingAirport;
	}

	public String getArrivingDateTime() {
		return arrivingDateTime;
	}

	public void setArrivingDateTime(String arrivingDateTime) {
		this.arrivingDateTime = arrivingDateTime;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(String flightClass) {
		this.flightClass = flightClass;
	}

	public String getTotalJourneyTime() {
		return totalJourneyTime;
	}

	public void setTotalJourneyTime(String totalJourneyTime) {
		this.totalJourneyTime = totalJourneyTime;
	}

	public String getTotalJourneyDistance() {
		return totalJourneyDistance;
	}

	public void setTotalJourneyDistance(String totalJourneyDistance) {
		this.totalJourneyDistance = totalJourneyDistance;
	}

	public void setUiData() {
		// set departing info
		departingFromCityLbl.setText(getDepartingFromCity());
		departingDateTimeLbl.setText(getDepartingDateTime());
		departingAirPortLbl.setText(getDepartingAirPort());
		departingterminalLbl.setText(getDepartingTerminal());
		// set arriving info
		arrivingAtCityLbl.setText(getArrivingAtCity());
		arrivingAtDateTimeLbl.setText(getArrivingDateTime());
		arrivingAirportLbl.setText(getArrivingAirport());
		arrivingTerminalLbl.setText(getArrivingTerminal());
		// flight info
		flightNoLbl.setText(getFlightNo());
		flightClassLbl.setText(getFlightClass());
		totalJourneyTimeLbl.setText(getTotalJourneyTime());
		totalJourneyDistanceLbl.setText(getTotalJourneyDistance());
	}
}
