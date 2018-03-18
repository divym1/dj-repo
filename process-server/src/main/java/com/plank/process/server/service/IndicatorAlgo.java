package com.plank.process.server.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.model.ADXDataDO;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;

public class IndicatorAlgo {

	public String getSymbol() {
		return "ASHOKLEY";
	}

	public static void matchSMAEMA() {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<String> symbolList = equityDataDao.getAllSymbolsForLargeCap();

		List<EquityDataDO> matchedsymbols = new ArrayList<>();
		List<EquityDataDO> matchedsymbols9DaySMA = new ArrayList<>();

		Calendar cal = new GregorianCalendar();
		cal.set(2018, 2, 6);

		Calendar calPrev = new GregorianCalendar();
		calPrev.set(2018, 2, 5);

		for (String symbol : symbolList) {

			List<EquityDataDO> dataList = equityDataDao.getEquityData(symbol, cal.getTime());

			if (dataList != null && dataList.size() > 0) {

				EquityDataDO equityDataDO = dataList.get(0);
				Decimal smaValue = equityDataDO.getSmaValue();
				Decimal emaValue = equityDataDO.getEmaValue();

				
				if ((emaValue.minus(smaValue)).isGreaterThanOrEqual(Decimal.ZERO)) {
					
					List<EquityDataDO> prevData = equityDataDao.getEquityData(symbol, calPrev.getTime());

					if (prevData != null && prevData.size() > 0) {
					
						EquityDataDO equityDataDOPrev = prevData.get(0);

						Decimal smaValuePrev = equityDataDOPrev.getSmaValue();
						Decimal emaValuePrev = equityDataDOPrev.getEmaValue();

						if ((emaValuePrev.minus(smaValuePrev)).isLessThan(Decimal.ZERO)) {
							matchedsymbols.add(equityDataDO);
						}
						
					}
				}

			} // data list check.

			if (dataList != null && dataList.size() > 0) {

				EquityDataDO equityDataDO = dataList.get(0);
				Decimal emaValue = equityDataDO.getEmaValue();

				Decimal smaValue9Day = equityDataDO.getSmaValue9Day();

				if ((emaValue.minus(smaValue9Day)).isGreaterThanOrEqual(Decimal.ZERO)) {

					List<EquityDataDO> prevData = equityDataDao.getEquityData(symbol, calPrev.getTime());

					if (prevData != null && prevData.size() > 0) {
						EquityDataDO equityDataDOPrev = prevData.get(0);

						Decimal smaValue9DayPrev = equityDataDOPrev.getSmaValue9Day();
						Decimal emaValuePrev = equityDataDOPrev.getEmaValue();

						if ((emaValuePrev.minus(smaValue9DayPrev)).isLessThan(Decimal.ZERO)) {
							matchedsymbols9DaySMA.add(equityDataDO);
						}
					}
				}
			}
		} // Symbol loop

		for (EquityDataDO matchedSymbol : matchedsymbols) {

			System.out.println("Matched 20 Day : " + matchedSymbol.getSymbol() + " - SMA :  "
					+ matchedSymbol.getSmaValue() + " - EMA " + matchedSymbol.getEmaValue() + " | Closing Price " + matchedSymbol.getClosePrice());
			// + " - Prev SMA " + matchedSymbol.getsma + "- Prev EMA " + emaValuePrev);

			List<ADXDataDO> adxDataDOs = equityDataDao.getADXRecord(matchedSymbol.getSymbol(), cal.getTime());

			boolean adxHigh = false;
			boolean dxHigh = false;

			if (adxDataDOs != null && adxDataDOs.size() > 0) {

				ADXDataDO adxDataDO = adxDataDOs.get(0);

				Decimal adxValue = adxDataDO.getAdxToday();

				if (adxValue.isGreaterThan(Decimal.valueOf(20))) {

					System.out.println(
							"ADX Values confirmed for : " + matchedSymbol.getSymbol() + " || ADX IS : " + adxValue);

					adxHigh = true;
				}

				Decimal diPlus = adxDataDO.getDiPlus();

				if (diPlus.isGreaterThan(Decimal.valueOf(20))) {

					System.out.println(
							"DI Plus confirmed for :  " + matchedSymbol.getSymbol() + " || DI Plus IS : " + diPlus);
					dxHigh = true;
				}

				if (adxHigh && dxHigh) {

					System.err.println("Both ADX and DX matched ");
				}
			}

			System.out.println(
					"-----------------------------------------------------------------------------------------------------");
		}

		for (EquityDataDO matchedSymbol : matchedsymbols9DaySMA) {

			System.out.println("Matched 9 Day : " + matchedSymbol.getSymbol() + " - SMA 9 DAY :  "
					+ matchedSymbol.getSmaValue9Day() + " - EMA " + matchedSymbol.getEmaValue()+ " | Closing Price " + matchedSymbol.getClosePrice()) ;

			List<ADXDataDO> adxDataDOs = equityDataDao.getADXRecord(matchedSymbol.getSymbol(),
					new Date(cal.getTimeInMillis()));

			if (adxDataDOs != null && adxDataDOs.size() > 0) {

				ADXDataDO adxDataDO = adxDataDOs.get(0);

				Decimal adxValue = adxDataDO.getAdxToday();

				if (adxValue.isGreaterThan(Decimal.valueOf(20))) {

					System.out.println("ADX Values (9 day SMA) confirmed for : " + matchedSymbol.getSymbol()
							+ " || ADX IS : " + adxValue);

					Decimal diPlus = adxDataDO.getDiPlus();

					if (diPlus.isGreaterThan(Decimal.valueOf(20))) {

						System.out.println("DI Plus (9 day SMA) confirmed for :  " + matchedSymbol.getSymbol()
								+ " || DI Plus IS : " + diPlus);
					}
				}
			}

			System.out.println(
					"-----------------------------------------------------------------------------------------------------");
		}

		System.out.println(
				"*********************************************************************************************************");
		System.out.println(
				"*********************************************************************************************************");

		for (String symbol : symbolList) {

			List<EquityDataDO> dataList = equityDataDao.getEquityData(symbol, cal.getTime());
			List<EquityDataDO> dataListPrev = equityDataDao.getEquityData(symbol, calPrev.getTime());

			if (dataList != null && dataList.size() > 0) {

				if (dataListPrev != null && dataListPrev.size() > 0) {
				
				EquityDataDO equityDataDO = dataList.get(0);
				EquityDataDO equityDataDOPrev = dataListPrev.get(0);

				Decimal cciValue = equityDataDO.getCciValue();
				Decimal cciValuePrev = equityDataDOPrev.getCciValue();
				
				
//				if(cciValuePrev.isLessThan(Decimal.ZERO)) {
				
					if(cciValuePrev.isLessThan(Decimal.HUNDRED.multipliedBy(Decimal.valueOf(-1)))) {
						
						if(cciValue.isGreaterThan(Decimal.HUNDRED.multipliedBy(Decimal.valueOf(-1)))) {
							
							System.err.println("CCI crossed above -100 :  " + equityDataDO.getSymbol()
							+ " | CCI Value : " + equityDataDO.getCciValue());

							
						}
						
					}
				
				}
			}

		}

	}

	public static void main(String[] args) {
		matchSMAEMA();
	}

}
