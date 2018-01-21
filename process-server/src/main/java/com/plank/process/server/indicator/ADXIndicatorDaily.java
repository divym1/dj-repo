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
	public Decimal calculateADX(EquityDataDO currentDateDO, List<EquityDataDO> listOfPrevEquData, int timeFrame, EquityDataDao equityDataDao) {

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
		
		for (int i = 0; i < timeFrame; i ++) {
			trPeriod = trPeriod.plus(adxDataList.get(i).getTrPeriod());
			dmPlusPeriod = dmPlusPeriod.plus(adxDataList.get(i).getDmPlusCurrent());
			dmMinusPeriod = dmMinusPeriod.plus(adxDataList.get(i).getDmMinusCurrent());
		}

		// Smoothening

		Decimal trSmooth = trPeriod.minus((trPeriod.dividedBy(Decimal.valueOf(timeFrame)))).plus(trueRangeCurrent);

		Decimal dmPlusSmooth = dmPlusPeriod.minus((dmPlusPeriod.dividedBy(Decimal.valueOf(timeFrame)))).plus(dmPlusCurrent);

		Decimal dmMinusSmooth = dmMinusPeriod.minus((dmMinusPeriod.dividedBy(Decimal.valueOf(timeFrame)))).plus(dmMinusCurrent);


		// calculate directional index

		Decimal diPlus = dmPlusSmooth.dividedBy(trSmooth).multipliedBy(Decimal.HUNDRED);
		Decimal diMinus = dmMinusSmooth.dividedBy(trSmooth).multipliedBy(Decimal.HUNDRED);

		// calculate the directional difference

		Decimal diDiff = diPlus.minus(diMinus);
		Decimal diSum = diPlus.plus(diMinus);

		// Calculate todays DX
		Decimal dxToday = diDiff.abs().dividedBy(diSum).multipliedBy(Decimal.HUNDRED);

		// calculate ADX

		Decimal adxPrev = adxDataList.get(0).getAdxToday();
		
		Decimal adxToday = ((adxPrev.multipliedBy(Decimal.valueOf(timeFrame - 1))).plus(dxToday)).dividedBy(Decimal.valueOf(timeFrame));

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

		
		System.out.println(adxDataDO);
//		equityDataDao.insertADXRecord(adxDataDO);
		
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
				adxIndicatorDaily.calculateADX(currentDateDO, equityDataList, 20, equityDataDao);
			}
		}		
		
	}
	
}
