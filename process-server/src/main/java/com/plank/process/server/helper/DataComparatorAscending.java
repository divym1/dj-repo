package com.plank.process.server.helper;

import java.util.Comparator;

import com.plank.process.server.model.EquityDataDO;

public class DataComparatorAscending implements Comparator<EquityDataDO> {

	@Override
	public int compare(EquityDataDO arg0, EquityDataDO arg1) {
		return arg0.getValueDate().compareTo(arg1.getValueDate());
	}

	

}
