package regularClasses;

public class CurrencyApi {
	double rates;
	String fromCode;
	String toCode;

	public double getRates() {
		return rates;
	}

	public void setRates(double rates) {
		this.rates = rates;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getToCode() {
		return toCode;
	}

	public void setToCode(String toCode) {
		this.toCode = toCode;
	}

	@Override
	public String toString() {
		return "rates=" + rates + ", fromCode=" + fromCode + ", toCode=" + toCode;
	}

}
