package com.sentieo.privatecompanies;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.sentieo.finance.InputTicker;

public class example {
	public static HashMap<Integer, String> parameters = new HashMap<Integer, String>();

	public static void generateRandomTickers() {
		InputTicker obj = new InputTicker();
		List<String[]> tickers = obj.readTickerCSV();

		for (String[] row : tickers) {
			int highlightLabelRandom = new Random().nextInt(tickers.size());
			String[] cell = tickers.get(highlightLabelRandom);
			for (String tickerName : cell) {
				parameters.put(highlightLabelRandom, tickerName);
				if (parameters.size() >= 100)
					break;
			}

			if (parameters.size() >= 100)
				break;
		}
	}

	public static void main(String[] args) {
		generateRandomTickers();
		System.out.println(parameters);
	}
}
