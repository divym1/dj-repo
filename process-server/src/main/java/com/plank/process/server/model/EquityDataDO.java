package com.plank.process.server.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
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
	private Decimal smaValue9Day;
	private Decimal typicalPrice;
	private Decimal cciValue;
	
}
