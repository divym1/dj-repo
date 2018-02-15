package com.plank.process.server.helper;

import java.util.Comparator;

import com.plank.process.server.model.UltiOsciDO;

public class UODataComparatorDescending implements Comparator<UltiOsciDO> {

	@Override
	public int compare(UltiOsciDO arg0, UltiOsciDO arg1) {
		return arg0.getValueDate().compareTo(arg1.getValueDate())* -1;
	}


}
