package com.stutzen.stock.yapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StockSymbolServlet  extends HttpServlet  {
	private static final long serialVersionUID = 1L;

	public StockSymbolServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// HttpClient httpclient = new DefaultHttpClient();
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			StockYahooApiModel yam = new StockYahooApiModel();
			int count=0;
			JSONArray arrayObj = new JSONArray();
			int start=Integer.parseInt(request.getParameter("start"));
			int limit=Integer.parseInt(request.getParameter("limit"));
			ArrayList<StockSymbol> stcksymList = yam.getStockSymbolsList(start,limit);
			System.out.println("stcksymList-->"+stcksymList.size());
			for (int i = 0; i < stcksymList.size(); i++) {
				StockSymbol ya = stcksymList.get(i);
				JSONObject itemObj = JSONObject.fromObject(ya);
				// add to array list
				arrayObj.add(itemObj);
			}
			JSONObject myObj = new JSONObject();
			// sets success to true
			myObj.put("success", true);
			myObj.put("count", arrayObj);
			myObj.put("totalCount", yam.getStockSymbolCount());
			out.println(myObj.toString());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// HttpClient httpclient = new DefaultHttpClient();
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			StockYahooApiModel yam = new StockYahooApiModel();
			JSONArray arrayObj = new JSONArray();
			int start=Integer.parseInt(request.getParameter("start"));
			int limit=Integer.parseInt(request.getParameter("limit"));
			ArrayList<StockSymbol> stcksymList = yam.getStockSymbolsList(start,limit);
			System.out.println("stcksymList-->"+stcksymList.size());
			for (int i = 0; i < stcksymList.size(); i++) {
				StockSymbol ya = stcksymList.get(i);
				JSONObject itemObj = JSONObject.fromObject(ya);
				// add to array list
				arrayObj.add(itemObj);
			}
			JSONObject myObj = new JSONObject();
			// sets success to true
			myObj.put("success", true);
			myObj.put("count", arrayObj);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
