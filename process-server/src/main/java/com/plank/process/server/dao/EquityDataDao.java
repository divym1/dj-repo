package com.plank.process.server.dao;

import java.util.Date;
import java.util.List;

import com.plank.process.server.model.ADXDataDO;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.model.UltiOsciDO;

public interface EquityDataDao {

	public void insertIntoEquityData(EquityDataDO equityDataDO);
	
	public void insertUltiOsci(UltiOsciDO ultiOsciDO);

	public List<EquityDataDO> getEquityData(String symbol, Date date);

	public List<EquityDataDO> getEquityDataForLargeCAP(String symbol, Date date);

	public List<EquityDataDO> getAllEquityData();
	
	public void updateSMA(String symbol, Date valueDate, Decimal smaValue) ;

	public int[] updateEMA(List<EquityDataDO> dos) ;
	
	List<String> getAllSymbols();
	
	List<String> getAllSymbolsForLargeCap();
	
	public void updateADXParams(String symbol, Date valueDate, Decimal trueRange, Decimal plusDm, Decimal minusDM) ;
	
	public void updateADXValue(String symbol, Date valueDate, Decimal adxValue);
	
	public void insertADXRecord(ADXDataDO adxDataDO);
	
	public List<ADXDataDO> getADXRecord(String symbol, Date date);
	
	public List<ADXDataDO> getADXRecord(String symbol);
	
	public List<UltiOsciDO> getUORecord(String symbol, Date date);
}
