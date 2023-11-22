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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import regularClasses.FlightApi;

public class RoundTripFlightsController implements Initializable {

	// All interface variables
	@FXML
	Label dateLblOut;
	@FXML
	Label timeLblOut;
	@FXML
	Label airLblOut;
	@FXML
	Label tdLblOut;
	@FXML
	Label iataLblOut;
	@FXML
	Label tmLblOut;
	@FXML
	Label stopLblOut;
	@FXML
	Label stopCodeLblOut;
	@FXML
	ImageView flightLgIVOut;
	@FXML
	Label dateLblIn;
	@FXML
	Label timeLblIn;
	@FXML
	Label airLblIn;
	@FXML
	Label tdLblIn;
	@FXML
	Label iataLblIn;
	@FXML
	Label tmLblIn;
	@FXML
	Label stopLblIn;
	@FXML
	Label stopCodeLblIn;
	@FXML
	ImageView flightLgIVIn;
	@FXML
	Button viewBtn;
	@FXML
	Label resultLbl;
	@FXML
	Label priceLbl;
	static Scene scene;
	static Node loadOutBoundFlightData;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void setOutBoundData(OneWayFlightController outbound) {
		dateLblOut.setText(outbound.getDate());
		timeLblOut.setText(outbound.getTime());
		airLblOut.setText(outbound.getMarketingAirLine());
		tdLblOut.setText(outbound.getTotalDuartion());
		iataLblOut.setText(outbound.getIata());
		tmLblOut.setText(outbound.getTotalMiles() + " mi");
		stopLblOut.setText(outbound.getLayoverStatus());
		stopCodeLblOut.setText(outbound.getLayoverCode());
		System.out.println(outbound.getAirlineLogo());
		if (getClass().getResourceAsStream("AirLineLogos/" + outbound.getAirlineLogo() + ".png") == null) {
			flightLgIVOut.setImage(new Image(getClass().getResourceAsStream("AirLineLogos/no_logo.png")));
		} else {
			flightLgIVOut.setImage(
					new Image(getClass().getResourceAsStream("AirLineLogos/" + outbound.getAirlineLogo() + ".png")));
		}
	}

	public void setInBoundData(OneWayFlightController inbound) {
		dateLblIn.setText(inbound.getDate());
		timeLblIn.setText(inbound.getTime());
		airLblIn.setText(inbound.getMarketingAirLine());
		tdLblIn.setText(inbound.getTotalDuartion());
		iataLblIn.setText(inbound.getIata());
		tmLblIn.setText(inbound.getTotalMiles() + " mi");
		stopLblIn.setText(inbound.getLayoverStatus());
		stopCodeLblIn.setText(inbound.getLayoverCode());
		System.out.println(inbound.getAirlineLogo());
		if (getClass().getResourceAsStream("AirLineLogos/" + inbound.getAirlineLogo() + ".png") == null) {
			flightLgIVIn.setImage(new Image(getClass().getResourceAsStream("AirLineLogos/no_logo.png")));
		} else {
			flightLgIVIn.setImage(
					new Image(getClass().getResourceAsStream("AirLineLogos/" + inbound.getAirlineLogo() + ".png")));
		}
	}

	public void showRoundTripFlightInfo(ArrayList<Object> flightObj, ArrayList<Object> flightData) {
		viewBtn.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/loadFlightInfo.fxml"));
				Node content = loader.load();
				MainPaneController.getInstance().loadFXMLInMainContainer(content);
				LoadFlightInfoController flInfo = loader.getController();
				flInfo.nextBtn.setManaged(false);
				flInfo.backBtn.setVisible(true);
				flInfo.viewInBtn.setVisible(true);
				FlightsController.originalAirports();
				flInfo.addFlightInfoToContainer(flightObj.get(0), "Out-Bound");
				FlightsController.switchAirports();
				flInfo.viewInboundData(flightObj.get(1));
				LoadUserInfoController.roundTripData = (ArrayList<OneWayFlightController>) (ArrayList<?>) flightData;
				loadOutBoundFlightData = content;
			} catch (Exception ex) {
				System.out.println(ex.getCause());
			}
		});
	}

}
