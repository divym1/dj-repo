package com.plank.process.server.model;

import java.sql.Date;
import java.sql.Timestamp;

public class EquityDataDO {
	private String symbol;
	private String series;
	private Decimal openPrice = null;
	private Decimal closePrice = null;
	private Decimal high = null;
	private Decimal low = null;
	private Decimal lastPrice;
	private Decimal prevClosePrice;
	private Decimal totalTradingQty = Decimal.ZERO;
	private Decimal totalTradingValue = Decimal.ZERO;
	private Timestamp timestamp;
	private Decimal totalTrade = Decimal.ZERO;
	private String isin;
	private Date valueDate;
	private Decimal smaValue;
	private Decimal emaValue;
	

	public EquityDataDO(String symbol, String series, Decimal openPrice, Decimal closePrice, Decimal high, Decimal low,
			Decimal lastPrice, Decimal prevClosePrice, Decimal totalTradingQty, Decimal totalTradingValue,
			Timestamp timestamp, Decimal totalTrade, String isin, Date valueDate, Decimal smaValue, Decimal emaValue
			) {
		super();
		this.symbol = symbol;
		this.series = series;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.high = high;
		this.low = low;
		this.lastPrice = lastPrice;
		this.prevClosePrice = prevClosePrice;
		this.totalTradingQty = totalTradingQty;
		this.totalTradingValue = totalTradingValue;
		this.timestamp = timestamp;
		this.totalTrade = totalTrade;
		this.isin = isin;
		this.valueDate = valueDate;
		this.smaValue = smaValue;
		this.emaValue = emaValue;
		
	}


	@Override
	public String toString() {
		return "EquityDataDO [symbol=" + symbol + ", series=" + series + ", openPrice=" + openPrice + ", closePrice="
				+ closePrice + ", high=" + high + ", low=" + low + ", lastPrice=" + lastPrice + ", prevClosePrice="
				+ prevClosePrice + ", totalTradingQty=" + totalTradingQty + ", totalTradingValue=" + totalTradingValue
				+ ", timestamp=" + timestamp + ", totalTrade=" + totalTrade + ", isin=" + isin + "]";
	}


	public Decimal getEmaValue() {
		return emaValue;
	}

	public void setEmaValue(Decimal emaValue) {
		this.emaValue = emaValue;
	}

	public Decimal getSmaValue() {
		return smaValue;
	}

	public void setSmaValue(Decimal smaValue) {
		this.smaValue = smaValue;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Decimal getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Decimal openPrice) {
		this.openPrice = openPrice;
	}

	public Decimal getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Decimal closePrice) {
		this.closePrice = closePrice;
	}

	public Decimal getHigh() {
		return high;
	}

	public void setHigh(Decimal high) {
		this.high = high;
	}

	public Decimal getLow() {
		return low;
	}

	public void setLow(Decimal low) {
		this.low = low;
	}

	public Decimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Decimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public Decimal getPrevClosePrice() {
		return prevClosePrice;
	}

	public void setPrevClosePrice(Decimal prevClosePrice) {
		this.prevClosePrice = prevClosePrice;
	}

	public Decimal getTotalTradingQty() {
		return totalTradingQty;
	}

	public void setTotalTradingQty(Decimal totalTradingQty) {
		this.totalTradingQty = totalTradingQty;
	}

	public Decimal getTotalTradingValue() {
		return totalTradingValue;
	}

	public void setTotalTradingValue(Decimal totalTradingValue) {
		this.totalTradingValue = totalTradingValue;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Decimal getTotalTrade() {
		return totalTrade;
	}

	public void setTotalTrade(Decimal totalTrade) {
		this.totalTrade = totalTrade;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

}
