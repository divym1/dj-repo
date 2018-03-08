package com.plank.process.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.plank.process.server.model.ADXDataDO;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.model.UltiOsciDO;

@Component
@Qualifier("equityDataDao")
public class EquityDataDaoImpl implements EquityDataDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void insertIntoEquityData(EquityDataDO equityDataDO) {
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.DOUBLE, Types.DOUBLE, Types.DOUBLE,
				Types.DOUBLE, Types.DOUBLE, Types.BIGINT, Types.DOUBLE, Types.TIMESTAMP, Types.BIGINT, Types.VARCHAR,
				Types.DATE, Types.DOUBLE, Types.DOUBLE, Types.DOUBLE, Types.DOUBLE, Types.DOUBLE};

		Object[] args = new Object[] { equityDataDO.getSymbol(),
				equityDataDO.getSeries(),
				equityDataDO.getOpenPrice().toDouble(), 
				equityDataDO.getHigh().toDouble(),
				equityDataDO.getLow().toDouble(),
				equityDataDO.getClosePrice().toDouble(),
				equityDataDO.getLastPrice().toDouble(), 
				equityDataDO.getPrevClosePrice().toDouble(),
				equityDataDO.getTotalTradingQty().toDouble(), 
				equityDataDO.getTotalTradingValue().toDouble(),
				new Timestamp(System.currentTimeMillis()), 
				equityDataDO.getTotalTrade().toDouble(),
				equityDataDO.getIsin(), 
				equityDataDO.getValueDate(), 
				equityDataDO.getSmaValue().toDouble(),
				equityDataDO.getEmaValue().toDouble(),
				equityDataDO.getSmaValue9Day().toDouble(),
				equityDataDO.getTypicalPrice().toDouble(),
				equityDataDO.getCciValue().toDouble()
				};

		jdbcTemplate.update(
				"insert into plankdb.equity_data values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ? , ?) ",
				args, types);
	}

	public void updateSMA(String symbol, Date valueDate, Decimal smaValue) {

		int[] types = new int[] { Types.DOUBLE, Types.VARCHAR, Types.DATE };
		Object[] args = new Object[] { smaValue.toDouble(), symbol, valueDate };

		jdbcTemplate.update("update plankdb.equity_data set sma_value = ? where symbol = ? and value_date = ? ", args,
				types);

	}

	public void updateSMA9Day(String symbol, Date valueDate, Decimal smaValue) {

		int[] types = new int[] { Types.DOUBLE, Types.VARCHAR, Types.DATE };
		Object[] args = new Object[] { smaValue.toDouble(), symbol, valueDate };

		jdbcTemplate.update("update plankdb.equity_data set sma_value_9_day = ? where symbol = ? and value_date = ? ", args,
				types);

	}
	
	public void insertADXRecord(ADXDataDO adxDataDO){
		
		Object[] args = new Object[] { adxDataDO.getSymbol(), 
				adxDataDO.getValueDate(),
				adxDataDO.getTrueRangeCurrent().toDouble(), 
				adxDataDO.getDmMinusCurrent().toDouble(),
				adxDataDO.getDmPlusCurrent().toDouble(),
				adxDataDO.getDxToday().toDouble(),
				adxDataDO.getAdxToday().toDouble(), 
				adxDataDO.getDiMinus().toDouble(),
				adxDataDO.getDiPlus().toDouble(), 
				new Timestamp(System.currentTimeMillis()),
				adxDataDO.getTrPeriod().toDouble(),
				adxDataDO.getDmPlusPeriod().toDouble(),
				adxDataDO.getDmMinusPeriod().toDouble(),
				adxDataDO.getTrSmooth().toDouble(),
				adxDataDO.getDmPlusSmooth().toDouble(),
				adxDataDO.getDmMinusSmooth().toDouble(),
				adxDataDO.getAdxPeriod().toDouble()};

		int[] types = new int[] { Types.VARCHAR,
									Types.DATE, 
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.TIMESTAMP,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE};
							
		jdbcTemplate.update("insert into plankdb.adx_indicator_data values (?, ?, ?, ?,"
																	+ " ?, ?, ?, ?,"
																	+ " ?, ?, ?, ?,"
																	+ " ?, ?, ?, ?, ? ) ",
																	
				args, types);
	}

	public void insertUltiOsci(UltiOsciDO ultiOsciDO){
		
		Object[] args = new Object[] { ultiOsciDO.getSymbol(), 
									ultiOsciDO.getValueDate(),
									ultiOsciDO.getUoValue().toDouble(), 
									ultiOsciDO.getBuyingPressure().toDouble(),
									ultiOsciDO.getTrueRange().toDouble(),
									ultiOsciDO.getDay7Avg().toDouble(),
									ultiOsciDO.getDay14Avg().toDouble(), 
									ultiOsciDO.getDay28Avg().toDouble(),
									new Timestamp(System.currentTimeMillis())
									};

		int[] types = new int[] { Types.VARCHAR,
									Types.DATE, 
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.DOUBLE,
									Types.TIMESTAMP};
							
		jdbcTemplate.update("insert into plankdb.ultimate_osci_data values (?, ?, ?, ?,"
																	+ " ?, ?, ?, ?,"
																	+ " ? ) ",
																	
				args, types);
	}

	/**
	 * 
	 * @param symbol
	 * @param valueDate
	 * @param adxValue
	 */
	public void updateADXValue(String symbol, Date valueDate, Decimal adxValue) {

		int[] types = new int[] { Types.DOUBLE, Types.VARCHAR, Types.DATE };
		Object[] args = new Object[] { adxValue.toDouble(), symbol, valueDate };

		jdbcTemplate.update("update plankdb.adx_indicator_data set adx_value = ? where symbol = ? and value_date = ? ", args, types);

	}

	public void updateCCIValue(String symbol, Date valueDate, Decimal cciValue, Decimal typicalPrice) {

		int[] types = new int[] { Types.DOUBLE, Types.DOUBLE, Types.VARCHAR, Types.DATE };
		Object[] args = new Object[] { typicalPrice.toDouble() , cciValue.toDouble(), symbol, valueDate };

		jdbcTemplate.update("update plankdb.equity_data set typical_price = ? , cci_value = ? where symbol = ? and value_date = ? ", args, types);

	}

	public void updateADXParams(String symbol, Date valueDate, Decimal trueRange, Decimal plusDm, Decimal minusDM) {

		int[] types = new int[] { Types.DOUBLE, Types.DOUBLE,Types.DOUBLE, Types.VARCHAR, Types.DATE };
		Object[] args = new Object[] { trueRange.toDouble(), plusDm.toDouble(), minusDM.toDouble(), symbol, valueDate };

		jdbcTemplate.update("update plankdb.equity_data set true_range = ? , plus_dm = ? , minus_dm = ? where symbol = ? and value_date = ? ", args,
				types);

	}
	
	public int[] updateEMA(List<EquityDataDO> dos) {
		int[] updateCounts = jdbcTemplate.batchUpdate(
				"update plankdb.equity_data set ema_value = ? where symbol = ? and value_date = ? ",
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setDouble(1, dos.get(i).getEmaValue().toDouble());
						ps.setString(2, dos.get(i).getSymbol());
						ps.setDate(3, dos.get(i).getValueDate());
					}

					public int getBatchSize() {
						return dos.size();
					}
				});

		return updateCounts;
	}

	@Override
	public List<EquityDataDO> getEquityData(String symbol, Date date) {

		String query = "select symbol, series, open_price, high_price, low_price, close_price, last_price, prev_close_price, "
				+ " total_trd_qty, total_trd_val, today_date, total_trd, isin, value_date, DATE(value_date) AS VD, sma_value, ema_value, sma_value_9_day, typical_price, cci_value  "
				+ " from plankdb.equity_data where symbol = ? ";

		String DATE = " and value_date = ? ";

		String ORDER_BY = "order by ? DESC";

		String append = query + ORDER_BY;
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] args = new Object[] { symbol, "VD" };

		if (date != null) {
			append = query + DATE + ORDER_BY;
			types = new int[] { Types.VARCHAR, Types.DATE, Types.VARCHAR };
			args = new Object[] { symbol, date, "VD" };
		}

		List<EquityDataDO> equityDataList = jdbcTemplate.query(append, args, types, new EquityDataMapper());
		return equityDataList;
	}

	public List<EquityDataDO> getEquityDataForLargeCAP(String symbol, Date date) {

		String query = "select symbol, series, open_price, high_price, low_price, close_price, last_price, prev_close_price, "
				+ " total_trd_qty, total_trd_val, today_date, total_trd, isin, value_date, DATE(value_date) AS VD, sma_value, ema_value, sma_value_9_day, typical_price, cci_value "
				+ " from plankdb.equity_data where symbol = ? and total_trd_qty >= 1000000 and open_price > 50 ";

		String DATE = " and value_date = ? ";

		String ORDER_BY = "order by ? DESC";

		String append = query + ORDER_BY;
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] args = new Object[] { symbol, "VD" };

		if (date != null) {
			append = query + DATE + ORDER_BY;
			types = new int[] { Types.VARCHAR, Types.DATE, Types.VARCHAR };
			args = new Object[] { symbol, date, "VD" };
		}

		List<EquityDataDO> equityDataList = jdbcTemplate.query(append, args, types, new EquityDataMapper());
		return equityDataList;

	}

	@Override
	public List<EquityDataDO> getAllEquityData() {
		return null;
	}

	@Override
	public List<String> getAllSymbols() {

		String query = "select distinct symbol from plankdb.equity_data";
		List<String> symbolList = jdbcTemplate.queryForList(query, String.class);
		return symbolList;
	}

	public List<String> getAllSymbolsForLargeCap() {
		
		String query = "select  distinct symbol from plankdb.equity_data where total_trd_qty >= 100000 and open_price > 50";
		List<String> symbolList = jdbcTemplate.queryForList(query, String.class);
		return symbolList;
	}
	
	class EquityDataMapper implements RowMapper<EquityDataDO> {

		@Override
		public EquityDataDO mapRow(ResultSet resultSet, int arg1) throws SQLException {

			EquityDataDO equityData = new EquityDataDO(resultSet.getString("symbol"),
					resultSet.getString("series"),
					Decimal.valueOf(resultSet.getDouble("open_price")),
					Decimal.valueOf(resultSet.getDouble("close_price")),
					Decimal.valueOf(resultSet.getDouble("high_price")),
					Decimal.valueOf(resultSet.getDouble("low_price")),
					Decimal.valueOf(resultSet.getDouble("last_price")),
					Decimal.valueOf(resultSet.getDouble("prev_close_price")),
					Decimal.valueOf(resultSet.getDouble("total_trd_qty")),
					Decimal.valueOf(resultSet.getDouble("total_trd_val")),
					resultSet.getTimestamp("today_date"),
					Decimal.valueOf(resultSet.getDouble("total_trd")), 
					resultSet.getString("isin"),
					resultSet.getDate("value_date"), 
					Decimal.valueOf(resultSet.getDouble("sma_value")),
					Decimal.valueOf(resultSet.getDouble("ema_value")),
					Decimal.valueOf(resultSet.getDouble("sma_value_9_day")),
					Decimal.valueOf(resultSet.getDouble("typical_price")),
					Decimal.valueOf(resultSet.getDouble("cci_value")));
			return equityData;
		}
	}

	@Override
	public List<ADXDataDO> getADXRecord(String symbol, Date date) {
	String query = "select * from plankdb.adx_indicator_data where symbol = ? and value_date = ?";
		
		int[] types = new int[] { Types.VARCHAR , Types.DATE};
		Object[] args = new Object[] { symbol , date};
		
		List<ADXDataDO> symbolList = jdbcTemplate.query(query,args, types , new AdxDoDataMapper());
		
		return symbolList;
	}
	
	public List<ADXDataDO> getADXRecord(String symbol) {
		String query = "select * from plankdb.adx_indicator_data where symbol = ?";
		
		int[] types = new int[] { Types.VARCHAR };
		Object[] args = new Object[] { symbol };
		
		List<ADXDataDO> symbolList = jdbcTemplate.query(query,args, types , new AdxDoDataMapper());
		
		return symbolList;
	}
	
	class AdxDoDataMapper implements RowMapper<ADXDataDO> {
		@Override
		public ADXDataDO mapRow(ResultSet resultSet, int arg1) throws SQLException {

			ADXDataDO adxDataDO = new ADXDataDO();
			
			adxDataDO.setAdxPeriod(Decimal.valueOf(resultSet.getDouble("adx_period")));
			adxDataDO.setAdxToday(Decimal.valueOf(resultSet.getDouble("adx_value")));
			adxDataDO.setDiMinus(Decimal.valueOf(resultSet.getDouble("minus_di")));
			adxDataDO.setDiPlus(Decimal.valueOf(resultSet.getDouble("plus_di")));
			adxDataDO.setDmMinusCurrent(Decimal.valueOf(resultSet.getDouble("minus_dm")));
			adxDataDO.setDmMinusPeriod(Decimal.valueOf(resultSet.getDouble("dmminus_period")));
			adxDataDO.setDmMinusSmooth(Decimal.valueOf(resultSet.getDouble("dmminus_smooth")));
			adxDataDO.setDmPlusCurrent(Decimal.valueOf(resultSet.getDouble("plus_dm")));
			adxDataDO.setDmPlusPeriod(Decimal.valueOf(resultSet.getDouble("dmplus_period")));
			adxDataDO.setDmPlusSmooth(Decimal.valueOf(resultSet.getDouble("dmplus_smooth")));
			
			adxDataDO.setDxToday(Decimal.valueOf(resultSet.getDouble("dx_value")));
			adxDataDO.setSymbol(resultSet.getString("symbol"));
			adxDataDO.setTrPeriod(Decimal.valueOf(resultSet.getDouble("tr_period")));
			adxDataDO.setTrSmooth(Decimal.valueOf(resultSet.getDouble("tr_smooth")));
			adxDataDO.setTrueRangeCurrent(Decimal.valueOf(resultSet.getDouble("true_range")));
			adxDataDO.setValueDate(resultSet.getDate("value_date"));
			
			return adxDataDO;
		}
	}

	@Override
	public List<UltiOsciDO> getUORecord(String symbol, Date date) {
	String query = "select * from plankdb.ultimate_osci_data where symbol = ? and value_date = ?";
		
		int[] types = new int[] { Types.VARCHAR , Types.DATE};
		Object[] args = new Object[] { symbol , date};
		
		List<UltiOsciDO> symbolList = jdbcTemplate.query(query,args, types , new UltiOsciDataMapper());
		
		return symbolList;
	}

	@Override
	public List<UltiOsciDO> getAllUORecord() {
	String query = "select * from plankdb.ultimate_osci_data";
		List<UltiOsciDO> symbolList = jdbcTemplate.query(query, new UltiOsciDataMapper());
		return symbolList;
	}


	class UltiOsciDataMapper implements RowMapper<UltiOsciDO> {
		@Override
		public UltiOsciDO mapRow(ResultSet resultSet, int arg1) throws SQLException {

			UltiOsciDO ultiOsciDO = new UltiOsciDO();
			ultiOsciDO.setSymbol(resultSet.getString("symbol"));
			ultiOsciDO.setValueDate(resultSet.getDate("value_date"));
			ultiOsciDO.setDay14Avg(Decimal.valueOf(resultSet.getDouble("day14_avg")));
			ultiOsciDO.setDay28Avg(Decimal.valueOf(resultSet.getDouble("day28_avg")));
			ultiOsciDO.setDay7Avg(Decimal.valueOf(resultSet.getDouble("day7_avg")));
			ultiOsciDO.setUoValue(Decimal.valueOf(resultSet.getDouble("uo_value")));
			ultiOsciDO.setDay14Avg(Decimal.valueOf(resultSet.getDouble("day14_avg")));
			ultiOsciDO.setTrueRange(Decimal.valueOf(resultSet.getDouble("true_range")));
			ultiOsciDO.setBuyingPressure(Decimal.valueOf(resultSet.getDouble("bp_value")));
			
			return ultiOsciDO;
		}
	}

}
