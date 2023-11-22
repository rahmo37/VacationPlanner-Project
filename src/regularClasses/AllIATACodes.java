package regularClasses;

public class AllIATACodes {
	String Airport_Name;
	String City;
	String Country;
	String Code;

	public AllIATACodes(String airport_Name, String city, String country, String Code) {
		super();
		this.Airport_Name = airport_Name;
		this.City = city;
		this.Country = country;
		this.Code = Code;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getAirport_Name() {
		return Airport_Name;
	}

	public void setAirport_Name(String airport_Name) {
		Airport_Name = airport_Name;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	@Override
	public String toString() {
		return "AllIATACodes [Airport_Name=" + Airport_Name + ", City=" + City + ", Country=" + Country + "]";
	}

}
