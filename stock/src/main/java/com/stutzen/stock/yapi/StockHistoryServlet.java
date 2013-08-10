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

public class StockHistoryServlet  extends HttpServlet  {
	private static final long serialVersionUID = 1L;

	public StockHistoryServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// HttpClient httpclient = new DefaultHttpClient();
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			StockYahooApiModel yam = new StockYahooApiModel();
			int insertFlag=Integer.parseInt(request.getParameter("insertFlag"));
			int count=0;
			JSONArray arrayObj = new JSONArray();
			if(insertFlag==0){
				ArrayList<StockYahooHistoryApi> yahooapiList = yam.getStockLog();
				count=yahooapiList.size();
				for (int i = 0; i < yahooapiList.size(); i++) {
					StockYahooHistoryApi ya = yahooapiList.get(i);
					JSONObject itemObj = JSONObject.fromObject(ya);
					// add to array list
					arrayObj.add(itemObj);
				}

				yam.getStockLog();
			}
			else if(insertFlag==1){
				count=yam.insertHistory(yam.getStockSymbols());
			}else if(insertFlag==2){
				String stocksymbol=CUtils.nz(request.getParameter("code"),"");
				String stockname=CUtils.nz(request.getParameter("name"),"");
				String isactive=CUtils.nz(request.getParameter("isactive"),"");
				String stockexchange=CUtils.nz(request.getParameter("stockexchange"),"");
				count=yam.insertStockSymbols(stocksymbol,stockname,isactive,stockexchange);
			}
			else{
				String filename=request.getParameter("fileName");
				String stockexchange=CUtils.nz(request.getParameter("stockexchange"),"");
				count=yam.insertStockSymbolsFromFile(filename,stockexchange);
			}
			JSONObject myObj = new JSONObject();
			// sets success to true

			myObj.put("success", true);
			if(insertFlag==0){
				myObj.put("count", arrayObj);
				myObj.put("totalCount", count);
			}else{
			myObj.put("count", count);
			}

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
			int insertFlag=Integer.parseInt(CUtils.nz(request.getParameter("insertFlag"),"0"));
			int count=0;
			JSONArray arrayObj = new JSONArray();
			if(insertFlag==2){
				String stocksymbol=CUtils.nz(request.getParameter("code"),"");
				String stockname=CUtils.nz(request.getParameter("name"),"");
				String isactive=CUtils.nz(request.getParameter("isactive"),"");
				String stockexchange=CUtils.nz(request.getParameter("stockexchange"),"");
				count=yam.insertStockSymbols(stocksymbol,stockname,isactive,stockexchange);
			}
			else{
				String filename=CUtils.nz(request.getParameter("fileName"),"");
				String stockexchange=CUtils.nz(request.getParameter("stockexchange"),"");
				count=yam.insertStockSymbolsFromFile(filename,stockexchange);
			}
			JSONObject myObj = new JSONObject();
			// sets success to true
			myObj.put("success", true);

			myObj.put("count", count);
			out.println(myObj.toString());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
