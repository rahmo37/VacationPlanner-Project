package regularClasses;

public class WeatherApi {
	String location;
	String day;
	String date;
	double currentTemp;
	double tempmax;
	double tempmin;
	double feelslilke;
	String description;

	public WeatherApi(String location, String day, String date, double currentTemp, double tempmax, double tempmin,
			double feelslilke, String description) {
		super();
		this.location = location;
		this.day = day;
		this.date = date;
		this.currentTemp = currentTemp;
		this.tempmax = tempmax;
		this.tempmin = tempmin;
		this.feelslilke = feelslilke;
		this.description = description;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getcurrentTemp() {
		return currentTemp;
	}

	public void setcurrentTemp(double currentTemp) {
		this.currentTemp = currentTemp;
	}

	public double getTempmax() {
		return tempmax;
	}

	public void setTempmax(double tempmax) {
		this.tempmax = tempmax;
	}

	public double getTempmin() {
		return tempmin;
	}

	public void setTempmin(double tempmin) {
		this.tempmin = tempmin;
	}

	public double getFeelslilke() {
		return feelslilke;
	}

	public void setFeelslilke(double feelslilke) {
		this.feelslilke = feelslilke;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "location = " + location + ", day = " + day + ", date = " + date + ", Current Temp = " + currentTemp + ", tempmax = " + tempmax
				+ ", tempmin = " + tempmin + ", feelslilke = " + feelslilke + ", description = " + description;
	}

}
