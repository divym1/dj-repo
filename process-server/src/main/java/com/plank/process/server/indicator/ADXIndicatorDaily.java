package com.plank.process.server.indicator;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.helper.AdxDataComparatorDescending;
import com.plank.process.server.model.ADXDataDO;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class ADXIndicatorDaily extends BaseAdxIndicator {

	/**
	 * 
	 * @param currentDateDO
	 * @param listOfPrevEquData
	 * @param timeFrame
	 * @return
	 */
	public Decimal calculateADX(EquityDataDO currentDateDO, List<EquityDataDO> listOfPrevEquData, EquityDataDao equityDataDao, int timeFrame) {

		if(listOfPrevEquData.size() < 1) {
			System.out.println(currentDateDO.getSymbol() + " - Not able to calculate ADX for this as no previous equity data. Creating zero value record. " );
			
			ADXDataDO adxDataDONoValue = new ADXDataDO();
			adxDataDONoValue.setSymbol(currentDateDO.getSymbol());
			adxDataDONoValue.setValueDate(currentDateDO.getValueDate());
			
			equityDataDao.insertADXRecord(adxDataDONoValue);
			return Decimal.ZERO;
		}
		
		EquityDataDO prevDataDO = listOfPrevEquData.get(0);

		// Calculate TR
		Decimal trueRangeCurrent = getTrueRange(currentDateDO);

		// Calculate directional movement UP
		Decimal dmPlusCurrent = calculateDirectionalMovementPlus(currentDateDO, prevDataDO);

		// Calculate directional movement DOWN
		Decimal dmMinusCurrent = calculateDirectionalMovementMinus(currentDateDO, prevDataDO);

		// accumulate the values for last "timeframe" period.

		Decimal trPeriod = Decimal.ZERO;
		Decimal dmPlusPeriod = Decimal.ZERO;
		Decimal dmMinusPeriod = Decimal.ZERO;
		Decimal adxPeriod = Decimal.ZERO;
		
		
		List<ADXDataDO > adxDataList = equityDataDao.getADXRecord(currentDateDO.getSymbol());

		Collections.sort(adxDataList, new AdxDataComparatorDescending());
		
		// Smoothening

		Decimal trPrev = adxDataList.get(1).getTrSmooth();
		
		Decimal trSmooth = (trPrev.minus((trPrev.dividedBy(Decimal.valueOf(timeFrame)))).plus(trueRangeCurrent)).abs();

		Decimal dmPlusPrev = adxDataList.get(1).getDmPlusSmooth();
		
		Decimal dmPlusSmooth = (dmPlusPrev.minus((dmPlusPrev.dividedBy(Decimal.valueOf(timeFrame)))).plus(dmPlusCurrent)).abs();

		Decimal dmMinusPrev = adxDataList.get(1).getDmMinusSmooth();
		
		Decimal dmMinusSmooth = (dmMinusPrev.minus((dmMinusPrev.dividedBy(Decimal.valueOf(timeFrame)))).plus(dmMinusCurrent)).abs();
		

		// calculate directional index

		Decimal diPlus = dmPlusSmooth.dividedBy(trSmooth).multipliedBy(Decimal.HUNDRED);
		Decimal diMinus = dmMinusSmooth.dividedBy(trSmooth).multipliedBy(Decimal.HUNDRED);

		// calculate the directional difference

		Decimal diDiff = (diPlus.minus(diMinus)).abs();
		Decimal diSum = (diPlus.plus(diMinus)).abs();

		// Calculate todays DX
		Decimal dxToday = diDiff.dividedBy(diSum).multipliedBy(Decimal.HUNDRED);

		if(dxToday.isNaN()) {
			dxToday = Decimal.ZERO;
		}
		
		// calculate ADX

		Decimal adxPrev = adxDataList.get(0).getAdxToday();
		
		Decimal adxToday = ((adxPrev.multipliedBy(Decimal.valueOf(timeFrame - 1))).plus(dxToday)).dividedBy(Decimal.valueOf(timeFrame));

		if(adxToday.isNaN()) {
			adxToday = Decimal.ZERO;
		}
		
		ADXDataDO adxDataDO = new ADXDataDO();
		
		adxDataDO.setDmMinusCurrent(dmMinusCurrent);
		adxDataDO.setDmPlusCurrent(dmPlusCurrent);
		adxDataDO.setTrueRangeCurrent(trueRangeCurrent);
		adxDataDO.setValueDate(currentDateDO.getValueDate());
		adxDataDO.setSymbol(currentDateDO.getSymbol());
		
		adxDataDO.setAdxPeriod(adxPeriod);
		adxDataDO.setAdxToday(adxToday);
		adxDataDO.setDiMinus(diMinus);
		adxDataDO.setDiPlus(diPlus);

		adxDataDO.setDmMinusPeriod(dmMinusPeriod);
		adxDataDO.setDmMinusSmooth(dmMinusSmooth);
		adxDataDO.setDmPlusPeriod(dmPlusPeriod);
		adxDataDO.setDmPlusSmooth(dmPlusSmooth);

		adxDataDO.setDxToday(dxToday);
		adxDataDO.setTrPeriod(trPeriod);
		adxDataDO.setTrSmooth(trSmooth);
		
		equityDataDao.insertADXRecord(adxDataDO);
		
		return adxToday;
	}

	public static void main(String[] args) {
		
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		Calendar cal = new GregorianCalendar(); 

		ADXIndicatorDaily adxIndicatorDaily = new ADXIndicatorDaily();
		
		for(int i= 2; i <= 10 ; i ++) {
		
			cal.set(2017, 10, i);
			List<EquityDataDO> equityDataList = equityDataDao.getEquityData("ASHOKLEY", new Date(cal.getTimeInMillis()));

			if(equityDataList != null && equityDataList.size()> 0) {
				EquityDataDO currentDateDO = equityDataList.get(0);
				adxIndicatorDaily.calculateADX(currentDateDO, equityDataList,  equityDataDao, 20);
			}
		}		
		
	}
	
}
