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

		Decimal highMinusLow = (equityDataDO.getHigh().minus(equityDataDO.getLow())).abs();
		Decimal highMinusPrevClose = (equityDataDO.getHigh().minus(equityDataDO.getPrevClosePrice())).abs();
		Decimal prevCloseMinusLow = (equityDataDO.getPrevClosePrice().minus(equityDataDO.getLow())).abs();
		
		
		
		double max1 = Math.max(highMinusLow.abs().toDouble(), highMinusPrevClose.abs().toDouble());
		double max2 = Math.max(max1, prevCloseMinusLow.abs().toDouble());
		
//		System.out.println("highMinusLow "+ highMinusLow + " | highMinusPrevClose " + highMinusPrevClose + " | prevCloseMinusLow "+ prevCloseMinusLow + " | max2 " + max2) ;
		
		return Decimal.valueOf(max2);
		
	}

}
