package com.plank.process.server.indicator;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class SMAIndicatorNewDaily {

	/**
	 * 
	 * @param currentDateDO
	 * @param equityDataList
	 * @param timeFrame
	 */
	public void calulateSMAIndicatorNewDaily(EquityDataDO currentDateDO, List<EquityDataDO> equityDataList, int timeFrame) {
		
		Collections.sort(equityDataList, new DataComparator());

		Decimal sum = currentDateDO.getClosePrice();
		
		if(equityDataList.size() < timeFrame) {
			System.out.println("Not able to calculate SMA  "+ currentDateDO.getSymbol() );
			return;
		}
		
		for (int i = 0; i < timeFrame - 1; i++) {
			sum = sum.plus(equityDataList.get(i).getClosePrice());
		}

		Decimal avg = sum.dividedBy(Decimal.valueOf(timeFrame));
		
		System.out.println(timeFrame + " --- SMA  : " + sum + " - " + avg + " - " + currentDateDO.getValueDate());
		
		currentDateDO.setSmaValue(avg);

	}

	public static void main(String[] args) {
		
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao  = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		EquityDataDO currentDateDO = new  EquityDataDO("ASHOKLEY", "EQ", Decimal.ZERO, Decimal.valueOf(129.15), Decimal.ZERO, Decimal.ZERO,
				Decimal.ZERO, Decimal.ZERO, Decimal.ZERO, Decimal.ZERO, null, Decimal.ZERO, "ASK", new Date(System.currentTimeMillis()),
				Decimal.ZERO, Decimal.ZERO);
		
		List<EquityDataDO> equityDataList = equityDataDao.getEquityData("ASHOKLEY", null);
		
		SMAIndicatorNewDaily smaIndicatorNewDaily = new SMAIndicatorNewDaily();
		smaIndicatorNewDaily.calulateSMAIndicatorNewDaily(currentDateDO, equityDataList, 20);
	}

	class DataComparator implements Comparator<EquityDataDO> {

		@Override
		public int compare(EquityDataDO arg0, EquityDataDO arg1) {
			return arg0.getValueDate().compareTo(arg1.getValueDate()) * -1;
		}
	}
}
