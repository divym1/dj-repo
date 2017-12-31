package com.plank.process.server.indicator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class EMAIndicatorNew {

	private static void getEMA(LinkedList<EquityDataDO> baseTicks, int timeFrame, EquityDataDao equityDataDao,
			String symbol) {

		int size = baseTicks.size();
		int startIdx = 1;
		Decimal multiplier = Decimal.TWO.dividedBy(Decimal.valueOf(timeFrame + 1));

		baseTicks.get(0).setEmaValue(baseTicks.get(0).getSmaValue());

		while (startIdx < size) {
			Decimal emaPrev = baseTicks.get(startIdx - 1).getEmaValue();
			Decimal ema = baseTicks.get(startIdx).getClosePrice().minus(emaPrev).multipliedBy(multiplier).plus(emaPrev);
			baseTicks.get(startIdx).setEmaValue(ema);

			System.out.println(symbol + " - EMA PREV - " + emaPrev + " --- EMA  : " + ema + " - "
					+ baseTicks.get(startIdx).getSmaValue() + " - " + baseTicks.get(startIdx).getValueDate());

			startIdx++;
		}

		equityDataDao.updateEMA(baseTicks);
		
	}

	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<String> list = equityDataDao.getAllSymbols();

		EMAIndicatorNew new1 = new EMAIndicatorNew();

		ExecutorService executor = Executors.newFixedThreadPool(50);
		executor.submit(() -> {
		    String threadName = Thread.currentThread().getName();
		    System.out.println("Hello " + threadName);
		});
		
		for (String symbol : list) {
			LinkedList<EquityDataDO> baseTicks = new LinkedList<>();
	
			List<EquityDataDO> equityDataList = equityDataDao.getEquityData(symbol, null);
			Collections.sort(equityDataList, new1.new DataComparator());

			for (EquityDataDO equityDataDO : equityDataList) {

				if (equityDataDO.getSmaValue().isGreaterThan(Decimal.ZERO)) {
					baseTicks.add(equityDataDO);
				}
				
			}

			if(baseTicks.size() > 0) {
				getEMA(baseTicks, 9, equityDataDao, symbol);
			}
		}

	}

	class DataComparator implements Comparator<EquityDataDO> {

		@Override
		public int compare(EquityDataDO arg0, EquityDataDO arg1) {
			return arg0.getValueDate().compareTo(arg1.getValueDate());
		}
	}

}
