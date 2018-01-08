/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.plank.process.server.loaders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.opencsv.CSVReader;
import com.plank.process.server.dao.EquityDataDao;
import com.plank.process.server.dao.EquityDataDaoImpl;
import com.plank.process.server.indicator.EMAIndicatorNewDaily;
import com.plank.process.server.indicator.SMAIndicatorNewDaily;
import com.plank.process.server.model.Decimal;
import com.plank.process.server.model.EquityDataDO;
import com.plank.process.server.service.DaoController;

/**
 * This class build a Ta4j time series from a CSV file containing ticks.
 */
public class CsvTicksLoader {

	public static void main(String[] args) {

		CsvTicksLoader csvTicksLoader = new CsvTicksLoader();

		File folder = new File("G:/bhav/consolidate/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				csvTicksLoader.loadCsvFile(listOfFiles[i].getName());
			}
		}
	}

	/**
	 * @return a time series from Apple Inc. ticks.
	 */
	public void loadCsvFile(String fileName) {

		InputStream stream = CsvTicksLoader.class.getClassLoader().getResourceAsStream(fileName);

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(DaoController.class);
		EquityDataDao equityDataDao = (EquityDataDao) context.getBean(EquityDataDaoImpl.class);


		InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));

		EMAIndicatorNewDaily emaIndicator = new EMAIndicatorNewDaily();
		SMAIndicatorNewDaily smaIndicator = new SMAIndicatorNewDaily();
		
		CSVReader csvReader = new CSVReader(inputStreamReader, ',', '"', 1);
		try {
			String[] line;
			while ((line = csvReader.readNext()) != null) {

				String type = String.valueOf(line[1]);

				if ("EQ".equals(type)) {
					String symbol = String.valueOf(line[0]);
					Decimal open = Decimal.valueOf(Double.parseDouble(line[2]));
					Decimal high = Decimal.valueOf(Double.parseDouble(line[3]));
					Decimal low = Decimal.valueOf(Double.parseDouble(line[4]));
					Decimal close = Decimal.valueOf(Double.parseDouble(line[5]));
					Decimal last = Decimal.valueOf(Double.parseDouble(line[6]));
					Decimal prevClose = Decimal.valueOf(Double.parseDouble(line[7]));
					Decimal totalTradingQty = Decimal.valueOf(Double.parseDouble(line[8]));
					Decimal totalTradingVal = Decimal.valueOf(Double.parseDouble(line[9]));
					String dataDate = String.valueOf(line[10]);
					Decimal totalTrades = Decimal.valueOf(Double.parseDouble(line[11]));
					String isin = String.valueOf(line[12]);

					DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
					Date date = new Date((df.parse(dataDate)).getTime());

					EquityDataDO equityDataDO = new EquityDataDO(symbol, type, open, close,
							high, low, last, prevClose,	totalTradingQty, totalTradingVal, null, totalTrades, isin, date,
							Decimal.ZERO, Decimal.ZERO);

					List<EquityDataDO> equityDataList = equityDataDao.getEquityData(symbol, null);
					
					smaIndicator.calulateSMAIndicatorNewDaily(equityDataDO, equityDataList, 20);
					emaIndicator.calculateEMADaily(equityDataDO, equityDataList, 9);
					
					equityDataDao.insertIntoEquityData(equityDataDO);
					
//					ADXIndicatorDaily adxIndicatorDaily = new ADXIndicatorDaily();
//					adxIndicatorDaily.calculateADX(equityDataDO, equityDataList, 14);

				}
			}
		} catch (IOException ioe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE, "Unable to load ticks from CSV", ioe);
		} catch (NumberFormatException nfe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
