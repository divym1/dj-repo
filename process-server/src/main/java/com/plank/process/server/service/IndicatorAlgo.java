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
		EquityDataDao equityDataDao  = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<String> symbolList = equityDataDao.getAllSymbolsForLargeCap();
		
//		System.out.println("Large cap " + symbolList.size());
		
		List<String> matchedsymbols = new ArrayList<>();
		
		Calendar cal = new GregorianCalendar(); 
		cal.set(2018, 0, 18);
		
		Calendar calPrev = new GregorianCalendar(); 
		calPrev.set(2018, 0, 17);
		
		
		for (String symbol : symbolList) {
			List<EquityDataDO> dataList = equityDataDao.getEquityData(symbol, new Date(cal.getTimeInMillis()));

			if(dataList != null && dataList.size() > 0) {
				EquityDataDO equityDataDO = dataList.get(0);
//				System.out.println("val date "+ equityDataDO.getValueDate());
				Decimal smaValue = equityDataDO.getSmaValue();
				Decimal emaValue = equityDataDO.getEmaValue();

				if(emaValue.minus(smaValue).isGreaterThanOrEqual(Decimal.ONE) ) {
					
					List<EquityDataDO> prevData = equityDataDao.getEquityData(symbol, new Date(calPrev.getTimeInMillis()));

					if(prevData != null && prevData.size() > 0) {
						EquityDataDO equityDataDOPrev = prevData.get(0);
						
						Decimal smaValuePrev = equityDataDOPrev.getSmaValue();
						Decimal emaValuePrev = equityDataDOPrev.getEmaValue();

						if(emaValuePrev.minus(smaValuePrev).isGreaterThanOrEqual(Decimal.ONE) ) {
//							System.out.println("Same condition yesterday also ..");
						} else {
							matchedsymbols.add(symbol);
							System.out.println("Matched : " + symbol + " - SMA :  "+ smaValue +
									" - EMA " + emaValue + " - Prev SMA "+ smaValuePrev + "- Prev EMA "+ emaValuePrev);
						}
					}
				} 
			}
		}

		for (String matchedSymbol : matchedsymbols) {
			System.out.println("Matched Symbol : " +matchedSymbol);

			List<ADXDataDO> adxDataDOs = equityDataDao.getADXRecord(matchedSymbol, new Date(cal.getTimeInMillis()));
			
			if(adxDataDOs != null && adxDataDOs.size() >  0) {
			// get the adx value and filter with values greater then 20 .
				
				ADXDataDO adxDataDO = adxDataDOs.get(0);
				
				Decimal adxValue = adxDataDO.getAdxToday();
				
				if (adxValue.isGreaterThan(Decimal.valueOf(20))) {
					
					System.out.println("ADX Values confirmed");
					
					Decimal diPlus = adxDataDO.getDiPlus();
					
					if (diPlus.isGreaterThan(Decimal.valueOf(20))) {
						
						System.out.println("DI Plus confirmed ");
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		matchSMAEMA();
	}

}
