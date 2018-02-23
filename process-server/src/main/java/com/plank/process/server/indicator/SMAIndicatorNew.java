package com.plank.process.server.indicator;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

public class SMAIndicatorNew {

	private static void updateSMA(List<EquityDataDO> equityDataList, int timeFrame, EquityDataDao equityDataDao,
			String symbol) {

		Collections.sort(equityDataList, new DataComparatorDescending());

		int i = timeFrame;
		int size = equityDataList.size();

		double sum = 0;
		if (size == i) {
			for (EquityDataDO baseTick : equityDataList) {
				sum = sum + baseTick.getClosePrice().toDouble();
			}
		} else {

			int startIdx = 0;
			int endIdx = timeFrame;

			while (endIdx < size) {

				sum = 0;
				for (int j = startIdx; j < endIdx; j++) {
					sum = sum + equityDataList.get(j).getClosePrice().toDouble();
				}

				double avg = sum / timeFrame;

				System.out.println("Symbol : " + symbol + " | timeframe :" + timeFrame + " | SMA  : " + sum + " | Average " + avg + " | value date  "+ equityDataList.get(startIdx).getValueDate());

				equityDataDao.updateSMA9Day(symbol, equityDataList.get(startIdx).getValueDate(), Decimal.valueOf(avg));

				startIdx++;
				endIdx++;
			}
		}

	}

	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<String> list = equityDataDao.getAllSymbols();

		for (String symbol : list) {

			List<EquityDataDO> equityDataList = equityDataDao.getEquityData(symbol, null);
			
			updateSMA(equityDataList, 9, equityDataDao, symbol);
		}
	}

	class DataComparator implements Comparator<EquityDataDO> {

		@Override
		public int compare(EquityDataDO arg0, EquityDataDO arg1) {
			return arg0.getValueDate().compareTo(arg1.getValueDate()) * -1;
		}
	}

}
