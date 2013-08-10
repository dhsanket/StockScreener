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

public class StockExchangeMinMaxServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public StockExchangeMinMaxServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// HttpClient httpclient = new DefaultHttpClient();
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			int stckexchange=Integer.parseInt(request.getParameter("stckexchange"));
			StockYahooApiModel yam=new StockYahooApiModel();

			JSONArray arrayObj = new JSONArray();
			ArrayList<StockExchange> stckexchangeList = yam.getStockExchange();
			for (int i = 0; i < stckexchangeList.size(); i++) {
				StockExchange se =stckexchangeList.get(i);
				se.setMinmrktcap(yam.getMinMarketCap());
				se.setMaxmrktcap(yam.getMaxMarketCap());
				se.setMinpe(yam.getMinPe());
				se.setMaxpe(yam.getMaxPE());
				se.setMinvolume(yam.getMinVolume());
				se.setMaxvolume(yam.getMaxVolume());
				se.setStockexchange(se.getStockexchange());
				JSONObject itemObj = JSONObject.fromObject(se);
				// add to array list
				arrayObj.add(itemObj);
			}
			JSONObject myObj = new JSONObject();
			// sets success to true
			myObj.put("success", true);
			if(stckexchange==1){
				myObj.put("data",  yam.getStockExchange());
			}else{
				myObj.put("stockexchange", arrayObj);
			}
			out.println(myObj.toString());
			out.close();
		}catch(Exception e){

		}
}
}