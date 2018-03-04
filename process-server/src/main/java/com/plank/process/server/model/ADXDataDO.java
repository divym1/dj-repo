package com.plank.process.server.model;

import java.sql.Date;

public class ADXDataDO {

	private String symbol;
	private Date valueDate;
	private Decimal trPeriod = Decimal.ZERO;
	private Decimal dmPlusPeriod = Decimal.ZERO;
	private Decimal dmMinusPeriod = Decimal.ZERO;
	private Decimal adxPeriod = Decimal.ZERO;
	private Decimal trSmooth= Decimal.ZERO;;	
	private Decimal dmPlusSmooth= Decimal.ZERO;;
	private Decimal dmMinusSmooth= Decimal.ZERO;;
	private Decimal diPlus= Decimal.ZERO;;
	private Decimal diMinus= Decimal.ZERO;;
	private Decimal dxToday= Decimal.ZERO;;
	private Decimal adxToday= Decimal.ZERO;;
	private Decimal trueRangeCurrent= Decimal.ZERO;;
	private Decimal dmPlusCurrent= Decimal.ZERO;;
	private Decimal dmMinusCurrent= Decimal.ZERO;;
	private Decimal diDiff= Decimal.ZERO;;
	private Decimal diSum= Decimal.ZERO;;
	
	public ADXDataDO() {
		super();
	}


	public ADXDataDO(String symbol, Date valueDate, Decimal trPeriod, Decimal dmPlusPeriod, Decimal dmMinusPeriod,
			Decimal adxPeriod, Decimal trSmooth, Decimal dmPlusSmooth, Decimal dmMinusSmooth, Decimal diPlus,
			Decimal diMinus, Decimal dxToday, Decimal adxToday, Decimal trueRangeCurrent, Decimal dmPlusCurrent,
			Decimal dmMinusCurrent) {
		super();
		this.symbol = symbol;
		this.valueDate = valueDate;
		this.trPeriod = trPeriod;
		this.dmPlusPeriod = dmPlusPeriod;
		this.dmMinusPeriod = dmMinusPeriod;
		this.adxPeriod = adxPeriod;
		this.trSmooth = trSmooth;
		this.dmPlusSmooth = dmPlusSmooth;
		this.dmMinusSmooth = dmMinusSmooth;
		this.diPlus = diPlus;
		this.diMinus = diMinus;
		this.dxToday = dxToday;
		this.adxToday = adxToday;
		this.trueRangeCurrent = trueRangeCurrent;
		this.dmPlusCurrent = dmPlusCurrent;
		this.dmMinusCurrent = dmMinusCurrent;
	}


	public Decimal getDiMinus() {
		return diMinus;
	}


	public void setDiMinus(Decimal diMinus) {
		this.diMinus = diMinus;
	}


	public String getSymbol() {
		return symbol;
	}

	public Decimal getDiDiff() {
		return diDiff;
	}


	public void setDiDiff(Decimal diDiff) {
		this.diDiff = diDiff;
	}


	public Decimal getDiSum() {
		return diSum;
	}


	public void setDiSum(Decimal diSum) {
		this.diSum = diSum;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public Decimal getTrPeriod() {
		return trPeriod;
	}

	public void setTrPeriod(Decimal trPeriod) {
		this.trPeriod = trPeriod;
	}

	public Decimal getDmPlusPeriod() {
		return dmPlusPeriod;
	}

	public void setDmPlusPeriod(Decimal dmPlusPeriod) {
		this.dmPlusPeriod = dmPlusPeriod;
	}

	public Decimal getDmMinusPeriod() {
		return dmMinusPeriod;
	}

	public void setDmMinusPeriod(Decimal dmMinusPeriod) {
		this.dmMinusPeriod = dmMinusPeriod;
	}

	public Decimal getAdxPeriod() {
		return adxPeriod;
	}

	public void setAdxPeriod(Decimal adxPeriod) {
		this.adxPeriod = adxPeriod;
	}

	public Decimal getTrSmooth() {
		return trSmooth;
	}

	public void setTrSmooth(Decimal trSmooth) {
		this.trSmooth = trSmooth;
	}

	public Decimal getDmPlusSmooth() {
		return dmPlusSmooth;
	}

	public void setDmPlusSmooth(Decimal dmPlusSmooth) {
		this.dmPlusSmooth = dmPlusSmooth;
	}

	public Decimal getDmMinusSmooth() {
		return dmMinusSmooth;
	}

	public void setDmMinusSmooth(Decimal dmMinusSmooth) {
		this.dmMinusSmooth = dmMinusSmooth;
	}

	public Decimal getDiPlus() {
		return diPlus;
	}

	public void setDiPlus(Decimal diPlus) {
		this.diPlus = diPlus;
	}

	public Decimal getDxToday() {
		return dxToday;
	}

	public void setDxToday(Decimal dxToday) {
		this.dxToday = dxToday;
	}

	public Decimal getAdxToday() {
		return adxToday;
	}

	public void setAdxToday(Decimal adxToday) {
		this.adxToday = adxToday;
	}

	public Decimal getTrueRangeCurrent() {
		return trueRangeCurrent;
	}

	public void setTrueRangeCurrent(Decimal trueRangeCurrent) {
		this.trueRangeCurrent = trueRangeCurrent;
	}

	public Decimal getDmPlusCurrent() {
		return dmPlusCurrent;
	}

	public void setDmPlusCurrent(Decimal dmPlusCurrent) {
		this.dmPlusCurrent = dmPlusCurrent;
	}

	public Decimal getDmMinusCurrent() {
		return dmMinusCurrent;
	}

	public void setDmMinusCurrent(Decimal dmMinusCurrent) {
		this.dmMinusCurrent = dmMinusCurrent;
	}

	@Override
	public String toString() {
		return "ADXDataDO [symbol=" + symbol + ", valueDate=" + valueDate + ", trPeriod=" + trPeriod + ", dmPlusPeriod="
				+ dmPlusPeriod + ", dmMinusPeriod=" + dmMinusPeriod + ", adxPeriod=" + adxPeriod + ", trSmooth="
				+ trSmooth + ", dmPlusSmooth=" + dmPlusSmooth + ", dmMinusSmooth=" + dmMinusSmooth + ", diPlus="
				+ diPlus + ", dxToday=" + dxToday + ", adxToday=" + adxToday + ", trueRangeCurrent=" + trueRangeCurrent
				+ ", dmPlusCurrent=" + dmPlusCurrent + ", dmMinusCurrent=" + dmMinusCurrent + "]";
	}
	
}
