package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import regularClasses.FlightApi;
import regularClasses.FlightConnecting;

public class OneWayFlightController implements Initializable {

	// All interface variables
	@FXML
	Label dateLbl;
	@FXML
	Label timeLbl;
	@FXML
	Label airLbl;
	@FXML
	Label tdLbl;
	@FXML
	Label iataLbl;
	@FXML
	Label tmLbl;
	@FXML
	Label stopLbl;
	@FXML
	Label stopCodeLbl;
	@FXML
	Label priceLbl;
	@FXML
	ImageView flightLgIV;
	@FXML
	Button viewBtn;

	// Variables to set to the interfaces
	String date;
	String time;
	String marketingAirLine;
	String totalDuartion;
	int totalMiles;
	String iata;
	String layoverStatus;
	String layoverCode;
	String price;
	String airlineLogo;

	static Scene scene;
	static Node loadFlightInfo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMarketingAirLine() {
		return marketingAirLine;
	}

	public void setMarketingAirLine(String marketingAirLine) {
		this.marketingAirLine = marketingAirLine;
	}

	public String getTotalDuartion() {
		return totalDuartion;
	}

	public void setTotalDuartion(String totalDuartion) {
		this.totalDuartion = totalDuartion;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public int getTotalMiles() {
		return totalMiles;
	}

	public void setTotalMiles(int totalMiles) {
		this.totalMiles = totalMiles;
	}

	public String getLayoverStatus() {
		return layoverStatus;
	}

	public void setLayoverStatus(String layoverStatus) {
		this.layoverStatus = layoverStatus;
	}

	public String getLayoverCode() {
		return layoverCode;
	}

	public void setLayoverCode(String layoverCode) {
		this.layoverCode = layoverCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAirlineLogo() {
		return airlineLogo;
	}

	public void setAirlineLogo(String setAirlineLogo) {
		this.airlineLogo = setAirlineLogo;
	}

	public void setUiData() {
		dateLbl.setText(getDate());
		timeLbl.setText(getTime());
		airLbl.setText(getMarketingAirLine());
		tdLbl.setText(getTotalDuartion());
		iataLbl.setText(getIata());
		tmLbl.setText(getTotalMiles() + " mi");
		stopLbl.setText(getLayoverStatus());
		stopCodeLbl.setText(getLayoverCode());
		System.out.println(getAirlineLogo());
		if (getClass().getResourceAsStream("AirLineLogos/" + getAirlineLogo() + ".png") == null) {
			flightLgIV.setImage(new Image(getClass().getResourceAsStream("AirLineLogos/no_logo.png")));
		} else {
			flightLgIV.setImage(new Image(getClass().getResourceAsStream("AirLineLogos/" + getAirlineLogo() + ".png")));
		}

//		priceLbl.setText(getPrice());
	}

	public void showFlightInfo(Object flightObj) {
		viewBtn.setOnMouseClicked(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/loadFlightInfo.fxml"));
				Node content = loader.load();
				MainPaneController.getInstance().loadFXMLInMainContainer(content);
				LoadFlightInfoController flInfo = loader.getController();
				flInfo.viewInBtn.setManaged(false);
				flInfo.backBtn.setVisible(true);
				flInfo.nextBtn.setVisible(true);
				if (flightObj instanceof FlightApi) {
					flInfo.addFlightInfoToContainer(flightObj, null);
				} else {
					flInfo.addFlightInfoToContainer(flightObj, null);
				}
				LoadUserInfoController.flightToBook = this;
				loadFlightInfo = content;
			} catch (Exception ex) {
				System.out.println(ex.getCause());
			}
		});
	}
}
