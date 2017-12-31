package com.plank.process.server.indicator;

import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;

public class BaseAdxIndicator {

	public static Decimal calculateDirectionalMovementMinus(EquityDataDO currentDateDO, EquityDataDO prevDataDO) {

		Decimal prevMaxPrice = prevDataDO.getHigh();
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal prevMinPrice = prevDataDO.getLow();
		Decimal minPrice = currentDateDO.getLow();

		if ((prevMaxPrice.isGreaterThanOrEqual(maxPrice) && prevMinPrice.isLessThanOrEqual(minPrice))
				|| maxPrice.minus(prevMaxPrice).isGreaterThanOrEqual(prevMinPrice.minus(minPrice))) {
			return Decimal.ZERO;
		}
		return prevMinPrice.minus(minPrice);
	}

	public static Decimal calculateDirectionalMovementPlus(EquityDataDO currentDateDO, EquityDataDO prevDataDO) {

		Decimal prevMaxPrice = prevDataDO.getHigh();
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal prevMinPrice = prevDataDO.getLow();
		Decimal minPrice = currentDateDO.getLow();

		if ((maxPrice.isLessThan(prevMaxPrice) && minPrice.isGreaterThan(prevMinPrice))
				|| prevMinPrice.minus(minPrice).isEqual(maxPrice.minus(prevMaxPrice))) {
			return Decimal.ZERO;
		}

		if (maxPrice.minus(prevMaxPrice).isGreaterThan(prevMinPrice.minus(minPrice))) {
			return maxPrice.minus(prevMaxPrice);
		}

		return Decimal.ZERO;
	}

	public static Decimal getTrueRange(EquityDataDO currentDateDO, EquityDataDO prevDataDO) {

		Decimal ts = currentDateDO.getHigh().minus(currentDateDO.getLow());
		Decimal ys = currentDateDO.getHigh().minus(prevDataDO.getClosePrice());
		Decimal yst = prevDataDO.getClosePrice().minus(currentDateDO.getLow());

		return ts.abs().max(ys.abs()).max(yst.abs());
	}

}
