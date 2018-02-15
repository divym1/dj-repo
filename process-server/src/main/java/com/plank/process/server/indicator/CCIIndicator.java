package com.plank.process.server.indicator;

import java.util.List;

import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;

public class CCIIndicator {

	public Decimal calculateCCI(EquityDataDO currentDateDO, EquityDataDao equityDataDao,
			List<EquityDataDO> listOfPrevEquData, int timeFrame) {

		// Calculate the typical price

		Decimal typicalPrice = calculateTypicalPrice(currentDateDO);

		// calculate mean deviation
        
        final Decimal meanDeviation = calculateMeanDeviation(currentDateDO, listOfPrevEquData, timeFrame);

        
        if (meanDeviation.isZero()) {
            return Decimal.ZERO;
        }
        
        final Decimal typicalPriceAvg = currentDateDO.getSmaValue();

        return (typicalPrice.minus(typicalPriceAvg)).dividedBy(meanDeviation.multipliedBy(Decimal.valueOf(0.15)));
    
	}

	protected Decimal calculateTypicalPrice(EquityDataDO currentDateDO) {
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal minPrice = currentDateDO.getLow();
		Decimal closePrice = currentDateDO.getClosePrice();
		return maxPrice.plus(minPrice).plus(closePrice).dividedBy(Decimal.THREE);
	}

	protected Decimal calculateMeanDeviation(EquityDataDO currentDateDO, List<EquityDataDO> listOfPrevEquData, int timeFrame) {
		Decimal absoluteDeviations = Decimal.ZERO;
		final Decimal average = currentDateDO.getSmaValue();
		
		for (int i = 0; i < timeFrame; i++) {
//			absoluteDeviations = absoluteDeviations.plus(listOfPrevEquData.get(i).getTypicalPrice().minus(average).abs());
		}
		
		return absoluteDeviations.dividedBy(Decimal.valueOf(timeFrame));
	}

}
