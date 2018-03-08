package com.plank.process.server.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
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
}
