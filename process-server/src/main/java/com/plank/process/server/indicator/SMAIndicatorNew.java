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
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class SMAIndicatorNew {

	
	private static void updateSMA(List<EquityDataDO> list, int timeFrame,  EquityDataDao equityDataDao, String symbol) {
		
		int i = timeFrame;
		int size = list.size();
		
		double sum = 0;
		if(size == i) {
			for (EquityDataDO baseTick : list) {
				sum = sum + baseTick.getClosePrice().toDouble();
			}
		} else {
		
			int startIdx = 0;
			int endIdx = timeFrame;
			
			while(endIdx < size) {
				
				sum = 0;
				for (int j = startIdx; j < endIdx; j++) {
					sum = sum + list.get(j).getClosePrice().toDouble();
				}
				
				double avg = sum / timeFrame;
				
				
				System.out.println(timeFrame + " --- SMA  : " + sum + " - " + avg + " - " + list.get(startIdx).getValueDate());
				
//				equityDataDao.updateSMA(symbol, baseTicks.get(startIdx).getValueDate(), Decimal.valueOf(avg));
				
				startIdx ++;
				endIdx ++;
			}
		}
		
	}
	

	
	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao  = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);


		List<String> list = equityDataDao.getAllSymbols();

		
//		for (String symbol : list) {
		String symbol = "ASHOKLEY";
		

			Calendar calendar = new GregorianCalendar();
			calendar.set(2017, 9, 19);
			
			List<EquityDataDO> equityDataList = equityDataDao.getEquityData(symbol, new Date(calendar.getTimeInMillis()));

			System.out.println(equityDataList.get(0).getSymbol());
			SMAIndicatorNew new1 = new SMAIndicatorNew();

			Collections.sort(equityDataList, new1.new DataComparator());

			updateSMA(equityDataList, 20, equityDataDao, symbol);
//		}
	}
	
	class DataComparator implements Comparator<EquityDataDO> {

		@Override
		public int compare(EquityDataDO arg0, EquityDataDO arg1) {
			return arg0.getValueDate().compareTo(arg1.getValueDate()) * -1;
		}
	}
	
}
