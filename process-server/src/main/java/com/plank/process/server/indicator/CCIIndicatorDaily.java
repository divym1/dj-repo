package com.plank.process.server.indicator;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.helper.DataComparatorDescending;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class CCIIndicatorDaily {

	public void calculateCCI(EquityDataDao equityDataDao, List<EquityDataDO> listOfEquData, int timeFrame) {

		Collections.sort(listOfEquData, new DataComparatorDescending());

		EquityDataDO currentDateDO = listOfEquData.get(0);
		Decimal typicalPrice = calculateTypicalPrice(currentDateDO);
		currentDateDO.setTypicalPrice(typicalPrice);

		Decimal sumTypicalPrice = Decimal.ZERO;

		for (int i = 0; i < timeFrame; i++) {
			sumTypicalPrice = sumTypicalPrice.plus(listOfEquData.get(i).getTypicalPrice());
		}

		Decimal typicalPriceAvg = sumTypicalPrice.dividedBy(Decimal.valueOf(timeFrame));

		
		Decimal sumTypicalPriceMinus = Decimal.ZERO;

		for (int i = 0; i < timeFrame; i++) {
			Decimal value = (listOfEquData.get(i).getTypicalPrice().minus(typicalPriceAvg)).abs();
			sumTypicalPriceMinus = sumTypicalPriceMinus.plus(value);
		}

		Decimal meanDeviation = sumTypicalPriceMinus.dividedBy(Decimal.valueOf(timeFrame));

		Decimal cciValue = Decimal.ZERO;
		
		if (!meanDeviation.isZero()) {

			Decimal val1 = currentDateDO.getTypicalPrice().minus(typicalPriceAvg);
			Decimal val2 = meanDeviation.multipliedBy(Decimal.valueOf(0.015));

			System.out.println("Val1 " + val1 + " || Val2 " + val2);

			cciValue = val1.dividedBy(val2);
			
		}

		System.out.println("Value Date " + currentDateDO.getValueDate() + " | typical price " + currentDateDO.getTypicalPrice() + " | typicalPriceAvg " + typicalPriceAvg + "|  mean " + meanDeviation + " | CCI " + cciValue);
		
		equityDataDao.updateCCIValue(currentDateDO.getSymbol(), currentDateDO.getValueDate(), cciValue, currentDateDO.getTypicalPrice());
	}

	protected Decimal calculateTypicalPrice(EquityDataDO currentDateDO) {
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal minPrice = currentDateDO.getLow();
		Decimal closePrice = currentDateDO.getClosePrice();
		return maxPrice.plus(minPrice).plus(closePrice).dividedBy(Decimal.THREE);
	}

	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		CCIIndicatorDaily cciIndicator = new CCIIndicatorDaily();

		Calendar cal = new GregorianCalendar();

		cal.set(2018, 0, 17);

		System.out.println(cal.getTime());

		List<EquityDataDO> equityDataList = equityDataDao.getEquityData("AJMERA", null);


		cciIndicator.calculateCCI(equityDataDao, equityDataList, 20);

	}

}
