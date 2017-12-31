package com.plank.process.server.helper;

import java.util.Comparator;

import com.plank.process.server.model.ADXDataDO;

public class AdxDataComparatorAscending implements Comparator<ADXDataDO> {

	@Override
	public int compare(ADXDataDO arg0, ADXDataDO arg1) {
		return arg0.getValueDate().compareTo(arg1.getValueDate());
	}
}


