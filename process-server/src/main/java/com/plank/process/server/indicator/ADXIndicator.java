package com.plank.process.server.indicator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.opencsv.CSVReader;
import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.helper.DataComparatorAscending;
import com.plank.process.server.loaders.CsvTicksLoader;
import com.plank.process.server.model.ADXDataDO;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class ADXIndicator extends BaseAdxIndicator {

	/**
	 * This is the main method or calculating the ADX vlues
	 * 
	 * @param timeframe
	 */
	public void updateADXValue(int timeframe, List<EquityDataDO> equityDataList, EquityDataDao equityDataDao) {

		Collections.sort(equityDataList, new DataComparatorAscending());

		EquityDataDO dataAt0 = equityDataList.get(0);

		ADXDataDO adxDataDO0 = new ADXDataDO();
		adxDataDO0.setDmMinusCurrent(Decimal.ZERO);
		adxDataDO0.setDmPlusCurrent(Decimal.ZERO);
		adxDataDO0.setTrueRangeCurrent(Decimal.ZERO);
		adxDataDO0.setValueDate(dataAt0.getValueDate());
		adxDataDO0.setSymbol(dataAt0.getSymbol());

		List<ADXDataDO> adxDataList = new ArrayList<>(equityDataList.size());
		// starting from 2 as we calculated for index 1 above. No data for
		// index 0;
		adxDataList.add(0, adxDataDO0);

		for (int i = 1; i < equityDataList.size(); i++) {

			EquityDataDO prevDataDO = equityDataList.get(i - 1);
			EquityDataDO currentDateDO = equityDataList.get(i);

			// Calculate TR
			Decimal trueRangeCurrent = getTrueRange(currentDateDO);

			// Calculate directional movement UP
			Decimal dmPlusCurrent = calculateDirectionalMovementPlus(currentDateDO, prevDataDO);

			// Calculate directional movement DOWN
			Decimal dmMinusCurrent = calculateDirectionalMovementMinus(currentDateDO, prevDataDO);

			ADXDataDO adxDataDO = new ADXDataDO();
			adxDataDO.setDmMinusCurrent(dmMinusCurrent);
			adxDataDO.setDmPlusCurrent(dmPlusCurrent);
			adxDataDO.setTrueRangeCurrent(trueRangeCurrent);
			adxDataDO.setValueDate(currentDateDO.getValueDate());
			adxDataDO.setSymbol(currentDateDO.getSymbol());

			adxDataList.add(i, adxDataDO);

		}

		// Starting calculation 

		Double trPeriod14thDay = 0.0;
		Double dmPlusPeriod14thDay =0.0;
		Double dmMinusPeriod14thDay = 0.0;
	
		for(int i = 14; i >= 1 ; i --) {
			trPeriod14thDay = trPeriod14thDay + adxDataList.get(i).getTrueRangeCurrent().toDouble();
			dmPlusPeriod14thDay = dmPlusPeriod14thDay + adxDataList.get(i).getDmPlusCurrent().toDouble();
			dmMinusPeriod14thDay = dmMinusPeriod14thDay+ adxDataList.get(i).getDmMinusCurrent().toDouble();
		}
		
		adxDataList.get(14).setTrSmooth(Decimal.valueOf(trPeriod14thDay));
		adxDataList.get(14).setDmMinusSmooth(Decimal.valueOf(dmMinusPeriod14thDay));
		adxDataList.get(14).setDmPlusSmooth(Decimal.valueOf(dmPlusPeriod14thDay));
		
		Double diPlus14Day = (dmPlusPeriod14thDay / trPeriod14thDay) * 100;
		Double diMinus14Day = (dmMinusPeriod14thDay / trPeriod14thDay) * 100;
		
		adxDataList.get(14).setDiPlus(Decimal.valueOf(diPlus14Day));
		adxDataList.get(14).setDiMinus(Decimal.valueOf(diMinus14Day));
		
		Double diDiff14Day = Math.abs(diPlus14Day - diMinus14Day);
		Double diSum14Day = Math.abs(diPlus14Day + diMinus14Day);

		adxDataList.get(14).setDiDiff(Decimal.valueOf(diDiff14Day));
		adxDataList.get(14).setDiSum(Decimal.valueOf(diSum14Day));
		
		Double dxToday14Day = ( diDiff14Day/ diSum14Day )  * 100;
		adxDataList.get(14).setDxToday(Decimal.valueOf(dxToday14Day));
		
		// DX value calculation.
		for (int i = 15; i < adxDataList.size(); i++) {
			// this part should start when there is enough data.
			ADXDataDO adxDataDO = adxDataList.get(i);

			Decimal trPeriod = Decimal.ZERO;
			Decimal dmPlusPeriod = Decimal.ZERO;
			Decimal dmMinusPeriod = Decimal.ZERO;
			Decimal adxPeriod = Decimal.ZERO;
			Decimal trSmooth = Decimal.ZERO;
			Decimal dmPlusSmooth = Decimal.ZERO;
			Decimal dmMinusSmooth = Decimal.ZERO;
			Decimal diPlus = Decimal.ZERO;
			Decimal diMinus = Decimal.ZERO;
			Decimal dxToday = Decimal.ZERO;
			Decimal adxToday = Decimal.ZERO;
			
			// Smoothening

			Decimal trPrev = adxDataList.get(i - 1).getTrSmooth();
			
			trSmooth = trPrev.minus((trPrev.dividedBy(Decimal.valueOf(timeframe)))).plus(adxDataDO.getTrueRangeCurrent());
			
			Decimal dmPlusPrev = adxDataList.get(i - 1).getDmPlusSmooth();
			
			dmPlusSmooth = dmPlusPrev.minus((dmPlusPrev.dividedBy(Decimal.valueOf(timeframe)))).plus(adxDataDO.getDmPlusCurrent());

			Decimal dmMinusPrev = adxDataList.get(i - 1).getDmMinusSmooth();
			
			dmMinusSmooth = dmMinusPrev.minus((dmMinusPrev.dividedBy(Decimal.valueOf(timeframe)))).plus(adxDataDO.getDmMinusCurrent());

			
			// calculate directional index

			diPlus = (dmPlusSmooth.dividedBy(trSmooth)).multipliedBy(Decimal.HUNDRED);
			diMinus = (dmMinusSmooth.dividedBy(trSmooth)).multipliedBy(Decimal.HUNDRED);

			// System.out.println("diPlus - "+ diPlus + " | diMinus - "+
			// diMinus);

			// calculate the directional difference

			Decimal diDiff = (diPlus.minus(diMinus)).abs();
			Decimal diSum = (diPlus.plus(diMinus)).abs();

			adxDataDO.setDiDiff(diDiff);
			adxDataDO.setDiSum(diSum);

			// Calculate todays DX
			dxToday = (diDiff.dividedBy(diSum)).multipliedBy(Decimal.HUNDRED);

			if (dxToday.isNaN()) {
				dxToday = Decimal.ZERO;
			}

			adxDataDO.setAdxPeriod(adxPeriod);
			adxDataDO.setAdxToday(adxToday);
			adxDataDO.setDiMinus(diMinus);
			adxDataDO.setDiPlus(diPlus);

			adxDataDO.setDmMinusPeriod(dmMinusPeriod);
			adxDataDO.setDmMinusSmooth(dmMinusSmooth);
			adxDataDO.setDmPlusPeriod(dmPlusPeriod);
			adxDataDO.setDmPlusSmooth(dmPlusSmooth);

			adxDataDO.setDxToday(dxToday);
			adxDataDO.setSymbol(dataAt0.getSymbol());
			adxDataDO.setTrPeriod(trPeriod);
			adxDataDO.setTrSmooth(trSmooth);
		}

		// calculating ADX from 30 days data.
		int k = 14;

		Decimal dxFor14Day = Decimal.ZERO;

		while (k < 28) {
			
			ADXDataDO adxDataDO = adxDataList.get(k);
			dxFor14Day = dxFor14Day.plus(adxDataDO.getDxToday());
			k++;
		}

		Decimal avgOfDx = dxFor14Day.dividedBy(Decimal.valueOf(14));

		adxDataList.get(27).setAdxToday(avgOfDx);

		for (int i = 28; i < adxDataList.size(); i++) {
			Decimal dxToday = adxDataList.get(i).getDxToday();
			Decimal adxPrev = adxDataList.get(i - 1).getAdxToday();

			Decimal adxToday = ((adxPrev.multipliedBy(Decimal.valueOf(timeframe - 1))).plus(dxToday)).dividedBy(Decimal.valueOf(timeframe));

			if (adxToday.isNaN()) {
				adxToday = Decimal.ZERO;
			}

			adxDataList.get(i).setAdxToday(adxToday);

		}

		for (int i = 0; i < adxDataList.size(); i++) {
			ADXDataDO adxDataDO2  =  adxDataList.get(i);
			System.out.println(i + " | "+adxDataDO2.getValueDate() + " | TR " + adxDataDO2.getTrueRangeCurrent() + " | DM + " + adxDataDO2.getDmPlusCurrent()
			+ "| DM - "+ adxDataDO2.getDmMinusCurrent() + " | TR14 " + adxDataDO2.getTrSmooth() + " | DM+14 " + adxDataDO2.getDmPlusSmooth() + " | DM-14 "
			+ adxDataDO2.getDmMinusSmooth() + " | DI + " + adxDataDO2.getDiPlus() + " | DI- " + adxDataDO2.getDiMinus() + " | DIFF " + adxDataDO2.getDiDiff()
			+ " | di sum " + adxDataDO2.getDiSum() + " | DX " + adxDataDO2.getDxToday() + " | ADX " + adxDataDO2.getAdxToday());

			equityDataDao.insertADXRecord(adxDataDO2);
		}
	}

	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<EquityDataDO> equityDataList = new ArrayList<>();

		try (InputStream stream = new FileInputStream("E:/plank/process-server/src/main/java/com/plank/process/server/indicator/cs-adx-csv.csv");

				InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
				CSVReader csvReader = new CSVReader(inputStreamReader, ',', '"', 1);) {

			String[] line;

			while ((line = csvReader.readNext()) != null) {
				String dataDate = String.valueOf(line[0]);
				Decimal high = Decimal.valueOf(Double.parseDouble(line[1]));
				Decimal low = Decimal.valueOf(Double.parseDouble(line[2]));
				Decimal close = Decimal.valueOf(Double.parseDouble(line[3]));

				DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				Date date = new Date((df.parse(dataDate)).getTime());

				EquityDataDO equityDataDO = new EquityDataDO(null, null, Decimal.ZERO, close, high, low, Decimal.ZERO, Decimal.ZERO, Decimal.ZERO, Decimal.ZERO, null, Decimal.ZERO,
						null, date, Decimal.ZERO, Decimal.ZERO, Decimal.ZERO, Decimal.ZERO, Decimal.ZERO);

//				equityDataList.add(equityDataDO);

			}

//			for(int i = 1 ; i < equityDataList.size() ; i++ ) {
//				equityDataList.get(i).setPrevClosePrice(equityDataList.get(i - 1).getClosePrice());
//			}
			
		} catch (IOException ioe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE, "Unable to load ticks from CSV", ioe);
		} catch (NumberFormatException nfe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			
		}

//		List<String> list = new ArrayList<>();
//		list.add("KSL");
		
		List<String> errorList = new ArrayList<>();

		List<String> list = equityDataDao.getAllSymbols();
		
		ADXIndicator adxIndicator = new ADXIndicator();
		
		for (String symbol : list) {
			
			equityDataList = equityDataDao.getEquityData(symbol, null);
			
			if (equityDataList.size() < 30) {
				System.out.println("NO DATA");
				continue;
			}
			
			try {
				adxIndicator.updateADXValue(14, equityDataList, equityDataDao);
			} catch (Exception e) {
				
				e.printStackTrace();
				
				errorList.add(symbol);
			}

		}
		
		for (String symbol : errorList) {
		
			System.out.println("Error Symbol " + symbol);
		}
		
	}
}
