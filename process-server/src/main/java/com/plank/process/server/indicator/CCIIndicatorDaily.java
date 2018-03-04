package com.plank.process.server.indicator;

import java.io.File;
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
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

public class CCIIndicatorDaily {

	public void calculateCCI(EquityDataDao equityDataDao, List<EquityDataDO> listOfEquData, int timeFrame) {

		for (EquityDataDO currentDateDO : listOfEquData) {
			// Calculate the typical price
			Decimal typicalPrice = calculateTypicalPrice(currentDateDO);
			currentDateDO.setTypicalPrice(typicalPrice);
		}
		
		
		for (int i = timeFrame; i < listOfEquData.size(); i++) {

			EquityDataDO currentDateDO = listOfEquData.get(i);

			int end = i - timeFrame;

			Decimal sumTypicalPrice = Decimal.ZERO;
			
			for (int j = i; j > end; j--) {
				sumTypicalPrice = sumTypicalPrice.plus(listOfEquData.get(j).getTypicalPrice());
			}

			
			Decimal typicalPriceAvg = sumTypicalPrice.dividedBy(Decimal.valueOf(timeFrame));

			Decimal sumTypicalPriceMinus = Decimal.ZERO;
			
			for (int j = i; j > end; j--) {
				
				Decimal value = (listOfEquData.get(j).getTypicalPrice().minus(typicalPriceAvg)).abs();
				sumTypicalPriceMinus = sumTypicalPriceMinus.plus(value);
			}
			
			Decimal meanDeviation = sumTypicalPriceMinus.dividedBy(Decimal.valueOf(timeFrame));

			
			if (!meanDeviation.isZero()) {
	
				Decimal val1 = currentDateDO.getTypicalPrice().minus(typicalPriceAvg);
				Decimal val2 = meanDeviation.multipliedBy(Decimal.valueOf(0.015));
				
				System.out.println("Val1 "+ val1 + " || Val2 "+ val2);
				
				Decimal cciValue = val1.dividedBy(val2);

				System.out.println("Value Date " + currentDateDO.getValueDate()+ " | typical price "+ currentDateDO.getTypicalPrice() + " | typicalPriceAvg "+ typicalPriceAvg   + "|  mean "+ meanDeviation + " | CCI " + cciValue );
			}
		}

	}

	protected Decimal calculateTypicalPrice(EquityDataDO currentDateDO) {
		Decimal maxPrice = currentDateDO.getHigh();
		Decimal minPrice = currentDateDO.getLow();
		Decimal closePrice = currentDateDO.getClosePrice();
		return maxPrice.plus(minPrice).plus(closePrice).dividedBy(Decimal.THREE);
	}

	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);

		List<EquityDataDO> equityDataList = new ArrayList<>();
		
		try(InputStream stream = new FileInputStream("E:/plank/process-server/src/main/java/com/plank/process/server/indicator/cs-cci.csv");
				
			InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
			CSVReader csvReader = new CSVReader(inputStreamReader, ',', '"', 1);)  {
						
			String[] line;
			
			
			while ((line = csvReader.readNext()) != null) {
				String dataDate = String.valueOf(line[0]);
				Decimal open = Decimal.valueOf(Double.parseDouble(line[1]));
				Decimal high = Decimal.valueOf(Double.parseDouble(line[2]));
				Decimal low = Decimal.valueOf(Double.parseDouble(line[3]));
				Decimal close = Decimal.valueOf(Double.parseDouble(line[4]));
				
				DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				Date date = new Date((df.parse(dataDate)).getTime());

				EquityDataDO equityDataDO = new EquityDataDO(null, null, open, close, high, low, Decimal.ZERO, Decimal.ZERO,
						Decimal.ZERO, Decimal.ZERO, null, Decimal.ZERO, null, date, Decimal.ZERO, Decimal.ZERO,
						Decimal.ZERO, Decimal.ZERO, Decimal.ZERO);

				equityDataList.add(equityDataDO);
				
			}
			
		} catch (IOException ioe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE, "Unable to load ticks from CSV", ioe);
		} catch (NumberFormatException nfe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
		} catch (ParseException e) {
			e.printStackTrace();
		} 

		CCIIndicatorDaily cciIndicator = new CCIIndicatorDaily();
	
		cciIndicator.calculateCCI(equityDataDao, equityDataList, 20);
		
	}

}
