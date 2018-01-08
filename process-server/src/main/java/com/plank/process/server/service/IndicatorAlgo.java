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
		
		System.out.println("Large cap " + symbolList.size());
		
		List<String> matchedsymbols = new ArrayList<>();
		
		Calendar cal = new GregorianCalendar(); 
		cal.set(2017, 8, 28);
		
		for (String symbol : symbolList) {
			List<EquityDataDO> dataList = equityDataDao.getEquityData(symbol, new Date(cal.getTimeInMillis()));
			
			for (EquityDataDO equityDataDO : dataList) {
				Decimal smaValue = equityDataDO.getSmaValue();
				Decimal emaValue = equityDataDO.getEmaValue();
				
				if(emaValue.minus(smaValue).isGreaterThanOrEqual(Decimal.ZERO) ) {
					matchedsymbols.add(symbol);
					System.out.println("Matched : " + symbol + " - SMA :  "+ smaValue + " - EMA " + emaValue );
				} 
			}
		}
		
		for (String matchedSymbol : matchedsymbols) {
			System.out.println("Matched Symbol : " +matchedSymbol);
			
			// get the adx value and filter with values greater then 20 .
			
			// 
		}
		
		
	}
	
	
	public static void main(String[] args) {
		matchSMAEMA();
	}

}
