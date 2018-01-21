package com.plank.process.server.indicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.helper.DataComparatorAscending;
import com.plank.process.server.model.ADXDataDO;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class ADXIndicator extends BaseAdxIndicator {

	/**
	 * This is the main method or calculating the ADX vlues
	 * 
	 * @param timeframe
	 */
	public static void updateADXValue(int timeframe) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<String> list = equityDataDao.getAllSymbols();

		for (String symbol : list) {

			List<EquityDataDO> equityDataList = equityDataDao.getEquityData(symbol, null);
			
			if(! (equityDataList.size() > 30)) {
				System.out.println("NO DATA");
				continue;
			}
			Collections.sort(equityDataList, new DataComparatorAscending());

			
			
			EquityDataDO prevDataDO0 = equityDataList.get(0);
			EquityDataDO currentDateDO1 = equityDataList.get(1);

			Decimal trueRangeCurrent1 = getTrueRange(currentDateDO1);

			// Calculate directional movement UP
			Decimal dmPlusCurrent1 = calculateDirectionalMovementPlus(currentDateDO1, prevDataDO0);

			// Calculate directional movement DOWN
			Decimal dmMinusCurrent1 = calculateDirectionalMovementMinus(currentDateDO1, prevDataDO0);

			ADXDataDO adxDataDO1 = new ADXDataDO();
			adxDataDO1.setDmMinusCurrent(dmMinusCurrent1);
			adxDataDO1.setDmPlusCurrent(dmPlusCurrent1);
			adxDataDO1.setTrueRangeCurrent(trueRangeCurrent1);
			adxDataDO1.setValueDate(currentDateDO1.getValueDate());
			adxDataDO1.setSymbol(currentDateDO1.getSymbol());

			List<ADXDataDO> adxDataList = new ArrayList<>(equityDataList.size());
			// starting from 2 as we calculated for index 1 above. No data for
			// index 0;
			adxDataList.add(0, null);
			adxDataList.add(1, adxDataDO1);

			for (int i = 2; i < equityDataList.size(); i++) {

				EquityDataDO prevDataDO = equityDataList.get(i - 1);
				EquityDataDO currentDateDO = equityDataList.get(i);

				// Calculate TR
				Decimal trueRangeCurrent = getTrueRange(currentDateDO);

				// Calculate directional movement UP
				Decimal dmPlusCurrent = calculateDirectionalMovementPlus(currentDateDO, prevDataDO);

				// Calculate directional movement DOWN
				Decimal dmMinusCurrent = calculateDirectionalMovementMinus(currentDateDO, prevDataDO);

				ADXDataDO adxDataDO = new ADXDataDO();
				adxDataDO.setDmMinusCurrent(dmMinusCurrent);
				adxDataDO.setDmPlusCurrent(dmPlusCurrent);
				adxDataDO.setTrueRangeCurrent(trueRangeCurrent);
				adxDataDO.setValueDate(currentDateDO.getValueDate());
				adxDataDO.setSymbol(currentDateDO.getSymbol());

				adxDataList.add(i, adxDataDO);
				
			}
			
			// DX value calculation.
			for (int i = 15; i < adxDataList.size(); i++) {
				// this part should start when there is enough data.
				ADXDataDO adxDataDO = adxDataList.get(i);

				Decimal trPeriod = Decimal.ZERO;
				Decimal dmPlusPeriod = Decimal.ZERO;
				Decimal dmMinusPeriod = Decimal.ZERO;
				Decimal adxPeriod = Decimal.ZERO;
				Decimal trSmooth = Decimal.ZERO;
				Decimal dmPlusSmooth = Decimal.ZERO;
				Decimal dmMinusSmooth = Decimal.ZERO;
				Decimal diPlus = Decimal.ZERO;
				Decimal diMinus = Decimal.ZERO;
				Decimal dxToday = Decimal.ZERO;
				Decimal adxToday = Decimal.ZERO;

				int end = i - timeframe;

				for (int j = i; j > end; j--) {
					trPeriod = trPeriod.plus(adxDataList.get(j).getTrueRangeCurrent());
					dmPlusPeriod = dmPlusPeriod.plus(adxDataList.get(j).getDmPlusCurrent());
					dmMinusPeriod = dmMinusPeriod.plus(adxDataList.get(j).getDmMinusCurrent());
				}

				// Smoothening

				trSmooth = trPeriod.minus((trPeriod.dividedBy(Decimal.valueOf(timeframe)))).plus(adxDataDO.getTrueRangeCurrent());

				dmPlusSmooth = dmPlusPeriod.minus((dmPlusPeriod.dividedBy(Decimal.valueOf(timeframe)))).plus(adxDataDO.getDmPlusCurrent());

				dmMinusSmooth = dmMinusPeriod.minus((dmMinusPeriod.dividedBy(Decimal.valueOf(timeframe)))).plus(adxDataDO.getDmMinusCurrent());

//				System.out.println("TR - "+ trSmooth + " | dm plus - "+ dmPlusSmooth + " | dm minus - "+ dmMinusSmooth);
				
				// calculate directional index

				diPlus = (dmPlusSmooth.dividedBy(trSmooth)).multipliedBy(Decimal.HUNDRED);
				diMinus = (dmMinusSmooth.dividedBy(trSmooth)).multipliedBy(Decimal.HUNDRED);

//				System.out.println("diPlus - "+ diPlus + " | diMinus - "+ diMinus);
				
				// calculate the directional difference

				Decimal diDiff = diPlus.minus(diMinus);
				Decimal diSum = diPlus.plus(diMinus);

				// Calculate todays DX
				dxToday = (diDiff.dividedBy(diSum)).multipliedBy(Decimal.HUNDRED);

				
				adxDataDO.setAdxPeriod(adxPeriod);
				adxDataDO.setAdxToday(adxToday);
				adxDataDO.setDiMinus(diMinus);
				adxDataDO.setDiPlus(diPlus);

				adxDataDO.setDmMinusPeriod(dmMinusPeriod);
				adxDataDO.setDmMinusSmooth(dmMinusSmooth);
				adxDataDO.setDmPlusPeriod(dmPlusPeriod);
				adxDataDO.setDmPlusSmooth(dmPlusSmooth);

				adxDataDO.setDxToday(dxToday);
				adxDataDO.setSymbol(symbol);
				adxDataDO.setTrPeriod(trPeriod);
				adxDataDO.setTrSmooth(trSmooth);
			}

			// calculating ADX from 30 days data.
			
			int k = 16;
			Decimal dxFor14Day = Decimal.ZERO;

			while (k <= 29) {
				ADXDataDO adxDataDO = adxDataList.get(k);
				dxFor14Day = dxFor14Day.plus(adxDataDO.getDxToday());
				k++;
			}

			Decimal avgOfDx = dxFor14Day.dividedBy(Decimal.valueOf(14));


			adxDataList.get(29).setAdxToday(avgOfDx);

			for (int i = 30; i < adxDataList.size(); i++) {
				Decimal dxToday = adxDataList.get(i).getDxToday();
				Decimal adxPrev = adxDataList.get(i - 1).getAdxToday();

				Decimal adxToday = ((adxPrev.multipliedBy(Decimal.valueOf(timeframe - 1))).plus(dxToday)).dividedBy(Decimal.valueOf(timeframe));
				adxDataList.get(i).setAdxToday(adxToday);

			}

			for (ADXDataDO adxDataDO2 : adxDataList) {
				if (adxDataDO2 != null) {
					System.out.println(adxDataDO2);
					equityDataDao.insertADXRecord(adxDataDO2);
				}
			}
		}

	}

	class DataComparator implements Comparator<EquityDataDO> {

		@Override
		public int compare(EquityDataDO arg0, EquityDataDO arg1) {
			return arg0.getValueDate().compareTo(arg1.getValueDate()) * -1;
		}

	}

	public static void main(String[] args) {
		updateADXValue(14);
	}
}
