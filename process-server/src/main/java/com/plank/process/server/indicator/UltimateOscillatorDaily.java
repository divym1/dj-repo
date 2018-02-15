package com.plank.process.server.indicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.helper.UODataComparatorDescending;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.model.UltiOsciDO;

public class UltimateOscillatorDaily {

	public void calculatUO(EquityDataDO currentDateDO, EquityDataDao equityDataDao) {
		List<UltiOsciDO> uoListBySymbol = new ArrayList<>();

		double min = Math.min(currentDateDO.getLow().toDouble(), currentDateDO.getPrevClosePrice().toDouble());
		Decimal bpToday = currentDateDO.getClosePrice().minus(Decimal.valueOf(min));
		double maxHighPriClose = Math.max(currentDateDO.getHigh().toDouble(), currentDateDO.getPrevClosePrice().toDouble());
		Decimal trToday = Decimal.valueOf(maxHighPriClose).minus(Decimal.valueOf(min));

		UltiOsciDO ultiOsciDODay1 = new UltiOsciDO();
		ultiOsciDODay1.setBuyingPressure(bpToday);
		ultiOsciDODay1.setTrueRange(trToday);
		ultiOsciDODay1.setValueDate(currentDateDO.getValueDate());

		uoListBySymbol.add(ultiOsciDODay1);
		
		List<UltiOsciDO> list = equityDataDao.getAllUORecord();

		Collections.sort(list, new UODataComparatorDescending());
		
		uoListBySymbol.addAll(list);
		
		// 7 days

		for (int k = 7; k < uoListBySymbol.size(); k++) {

			UltiOsciDO ultiOsciDO = uoListBySymbol.get(k);

			int last = k - 7;

			Decimal sum7PerBP = Decimal.ZERO;
			Decimal sum7PerTR = Decimal.ZERO;
			for (int l = k; l >= last; l--) {
				UltiOsciDO ultiOsciDOOld = uoListBySymbol.get(l);
				sum7PerBP = sum7PerBP.plus(ultiOsciDOOld.getBuyingPressure());
				sum7PerTR = sum7PerTR.plus(ultiOsciDOOld.getTrueRange());
			}

			ultiOsciDO.setDay7BP(sum7PerBP);
			ultiOsciDO.setDay7TR(sum7PerTR);

		}

		for (int k = 14; k < uoListBySymbol.size(); k++) {

			UltiOsciDO ultiOsciDO = uoListBySymbol.get(k);

			int last = k - 14;

			Decimal sum14PerTR = Decimal.ZERO;
			Decimal sum14PerBP = Decimal.ZERO;
			for (int l = k; l >= last; l--) {
				UltiOsciDO ultiOsciDOOld = uoListBySymbol.get(l);
				sum14PerBP = sum14PerBP.plus(ultiOsciDOOld.getBuyingPressure());
				sum14PerTR = sum14PerTR.plus(ultiOsciDOOld.getTrueRange());
			}

			ultiOsciDO.setDay14BP(sum14PerBP);
			ultiOsciDO.setDay14TR(sum14PerTR);
		}

		for (int k = 28; k < uoListBySymbol.size(); k++) {

			UltiOsciDO ultiOsciDO = uoListBySymbol.get(k);

			int last = k - 28;

			Decimal sum28PerBP = Decimal.ZERO;
			Decimal sum28PerTR = Decimal.ZERO;
			for (int l = k; l >= last; l--) {
				UltiOsciDO ultiOsciDOOld = uoListBySymbol.get(l);
				sum28PerBP = sum28PerBP.plus(ultiOsciDOOld.getBuyingPressure());
				sum28PerTR = sum28PerTR.plus(ultiOsciDOOld.getTrueRange());
			}

			ultiOsciDO.setDay28BP(sum28PerBP);
			ultiOsciDO.setDay28TR(sum28PerTR);
		}

		for (int k = 1; k < uoListBySymbol.size(); k++) {
			UltiOsciDO ultiOsciDO = uoListBySymbol.get(k);

			Decimal avg7 = Decimal.ZERO;

			if (!(ultiOsciDO.getDay7BP().equals(Decimal.ZERO) || ultiOsciDO.getDay7TR().equals(Decimal.ZERO))) {
				avg7 = ultiOsciDO.getDay7BP().dividedBy(ultiOsciDO.getDay7TR());
			}

			Decimal avg14 = Decimal.ZERO;

			if (!(ultiOsciDO.getDay7BP().equals(Decimal.ZERO) || ultiOsciDO.getDay7TR().equals(Decimal.ZERO))) {
				avg14 = ultiOsciDO.getDay14BP().dividedBy(ultiOsciDO.getDay14TR());
			}

			Decimal avg28 = Decimal.ZERO;

			if (!(ultiOsciDO.getDay7BP().equals(Decimal.ZERO) || ultiOsciDO.getDay7TR().equals(Decimal.ZERO))) {
				avg28 = ultiOsciDO.getDay28BP().dividedBy(ultiOsciDO.getDay28TR());
			}
			
			ultiOsciDO.setDay7Avg(avg7);
			ultiOsciDO.setDay14Avg(avg14);
			ultiOsciDO.setDay28Avg(avg28);

			Decimal val1 = Decimal.FOUR.multipliedBy(avg7);
			Decimal val2 = Decimal.TWO.multipliedBy(avg14);
			Decimal val3 = val1.plus(val2).plus(avg28);

			Decimal uoValue = Decimal.ZERO;
			
			if (!(ultiOsciDO.getDay7BP().equals(Decimal.ZERO))) {
				uoValue = Decimal.HUNDRED.multipliedBy(val3).dividedBy(Decimal.FOUR.plus(Decimal.TWO).plus(Decimal.ONE));
			}

			ultiOsciDO.setUoValue(uoValue);

			System.out.println(ultiOsciDO);
			
			equityDataDao.insertUltiOsci(ultiOsciDO);
		}

	}

}
