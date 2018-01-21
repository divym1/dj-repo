package com.plank.process.server.indicator;

import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;

public class BaseAdxIndicator {

	public static Decimal calculateDirectionalMovementMinus(EquityDataDO currentDateDO, EquityDataDO prevDataDO) {

		Decimal prevMaxPrice = prevDataDO.getHigh();
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal prevMinPrice = prevDataDO.getLow();
		Decimal minPrice = currentDateDO.getLow();
		
		Decimal upMove = maxPrice.minus(prevMaxPrice);
		Decimal downMove = prevMinPrice.minus(minPrice);
		
		if(downMove.isGreaterThan(upMove) && downMove.isGreaterThan(Decimal.ZERO)) {
			return downMove;
		}

		return Decimal.ZERO;
	}

	public static Decimal calculateDirectionalMovementPlus(EquityDataDO currentDateDO, EquityDataDO prevDataDO) {

		Decimal prevMaxPrice = prevDataDO.getHigh();
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal prevMinPrice = prevDataDO.getLow();
		Decimal minPrice = currentDateDO.getLow();

		Decimal upMove = maxPrice.minus(prevMaxPrice);
		Decimal downMove = prevMinPrice.minus(minPrice);
		
		if(upMove.isGreaterThan(downMove) && upMove.isGreaterThan(Decimal.ZERO)) {
			return upMove;
		}
		
		return Decimal.ZERO;
	}

	public static Decimal getTrueRange(EquityDataDO equityDataDO) {

		double min = Math.min(equityDataDO.getLow().toDouble(), equityDataDO.getPrevClosePrice().toDouble());
		double maxHighPriClose = Math.max(equityDataDO.getHigh().toDouble(), equityDataDO.getPrevClosePrice().toDouble());
		Decimal trToday = Decimal.valueOf(maxHighPriClose).minus(Decimal.valueOf(min));
		
		return trToday;
		
	}

}
