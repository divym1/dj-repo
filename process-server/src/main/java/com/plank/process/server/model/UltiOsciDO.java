package com.plank.process.server.model;

import java.sql.Date;

public class UltiOsciDO {
	
	private Decimal buyingPressure= Decimal.ZERO;
	private Decimal trueRange= Decimal.ZERO;
	
	private Decimal Day7Avg = Decimal.ZERO;
	private Decimal Day14Avg= Decimal.ZERO;
	private Decimal Day28Avg= Decimal.ZERO;
	

	private Decimal Day7BP= Decimal.ZERO;
	private Decimal Day14BP= Decimal.ZERO;
	private Decimal Day28BP= Decimal.ZERO;

	private Decimal Day7TR= Decimal.ZERO;
	private Decimal Day14TR= Decimal.ZERO;
	private Decimal Day28TR= Decimal.ZERO;

	private Decimal uoValue= Decimal.ZERO;

	private Date valueDate;
	private String symbol;
	
	public Decimal getBuyingPressure() {
		return buyingPressure;
	}

	public void setBuyingPressure(Decimal buyingPressure) {
		this.buyingPressure = buyingPressure;
	}

	public Decimal getTrueRange() {
		return trueRange;
	}

	public void setTrueRange(Decimal trueRange) {
		this.trueRange = trueRange;
	}
	
	

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public Decimal getDay7Avg() {
		return Day7Avg;
	}

	public void setDay7Avg(Decimal day7Avg) {
		Day7Avg = day7Avg;
	}

	public Decimal getDay14Avg() {
		return Day14Avg;
	}

	public void setDay14Avg(Decimal day14Avg) {
		Day14Avg = day14Avg;
	}

	public Decimal getDay28Avg() {
		return Day28Avg;
	}

	public void setDay28Avg(Decimal day28Avg) {
		Day28Avg = day28Avg;
	}

	public Decimal getUoValue() {
		return uoValue;
	}

	public void setUoValue(Decimal uoValue) {
		this.uoValue = uoValue;
	}

	public Decimal getDay7BP() {
		return Day7BP;
	}

	public void setDay7BP(Decimal day7bp) {
		Day7BP = day7bp;
	}

	public Decimal getDay14BP() {
		return Day14BP;
	}

	public void setDay14BP(Decimal day14bp) {
		Day14BP = day14bp;
	}

	public Decimal getDay28BP() {
		return Day28BP;
	}

	public void setDay28BP(Decimal day28bp) {
		Day28BP = day28bp;
	}

	public Decimal getDay7TR() {
		return Day7TR;
	}

	public void setDay7TR(Decimal day7tr) {
		Day7TR = day7tr;
	}

	public Decimal getDay14TR() {
		return Day14TR;
	}

	public void setDay14TR(Decimal day14tr) {
		Day14TR = day14tr;
	}

	public Decimal getDay28TR() {
		return Day28TR;
	}

	public void setDay28TR(Decimal day28tr) {
		Day28TR = day28tr;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "UltiOsciDO [buyingPressure=" + buyingPressure + ", trueRange=" + trueRange + ", Day7Avg=" + Day7Avg
				+ ", Day14Avg=" + Day14Avg + ", Day28Avg=" + Day28Avg + ", Day7BP=" + Day7BP + ", Day14BP=" + Day14BP
				+ ", Day28BP=" + Day28BP + ", Day7TR=" + Day7TR + ", Day14TR=" + Day14TR + ", Day28TR=" + Day28TR
				+ ", uoValue=" + uoValue + ", valueDate=" + valueDate + "]";
	}


	
}
