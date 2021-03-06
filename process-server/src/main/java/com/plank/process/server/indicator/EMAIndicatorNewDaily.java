package com.plank.process.server.indicator;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class EMAIndicatorNewDaily {
	
	public void calculateEMADaily(EquityDataDO currentDateDO, List<EquityDataDO> equityDataList, int timeFrame) {
		
		Collections.sort(equityDataList, new DataComparator());
		
		Decimal multiplier = Decimal.TWO.dividedBy(Decimal.valueOf(timeFrame + 1));
		
		if(equityDataList != null && equityDataList.size() > 0) {
			Decimal emaPrev = equityDataList.get(0).getEmaValue();

			Decimal ema = currentDateDO.getClosePrice().minus(emaPrev).multipliedBy(multiplier).plus(emaPrev);

			System.out.println(currentDateDO.getSymbol() + " - EMA PREV - " + emaPrev + " --- EMA  : " + ema);

			currentDateDO.setEmaValue(ema);
		}
		
	}
	
	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		EMAIndicatorNewDaily daily = new EMAIndicatorNewDaily();
		
		Calendar cal = new GregorianCalendar(); 

		for(int i= 2; i <=30 ; i ++) {
		
			cal.set(2017, 10, i);
			List<EquityDataDO> equityDataList = equityDataDao.getEquityData("ASHOKLEY", new Date(cal.getTimeInMillis()));

			if(equityDataList != null && equityDataList.size()> 0) {
				EquityDataDO currentDateDO = equityDataList.get(0);
				daily.calculateEMADaily(currentDateDO, equityDataList, 9);
			}
		}
	}

	class DataComparator implements Comparator<EquityDataDO> {

		@Override
		public int compare(EquityDataDO arg0, EquityDataDO arg1) {
			return arg0.getValueDate().compareTo(arg1.getValueDate())* -1;
		}
	}

}
