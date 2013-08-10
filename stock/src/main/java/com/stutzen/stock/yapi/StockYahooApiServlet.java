package com.stutzen.stock.yapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StockYahooApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public StockYahooApiServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// HttpClient httpclient = new DefaultHttpClient();
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String values = CUtils.nz(request.getParameter("values"),"");
			int sectorFlag = Integer.parseInt( CUtils.nz(request.getParameter("sector"),"0"));
			String volume =  CUtils.nz(request.getParameter("volume"),"");
			String marketcap =  CUtils.nz(request.getParameter("marketcap"),"");
			String pe =  CUtils.nz(request.getParameter("pe"),"");
			String stockexchange = "";
			stockexchange= CUtils.nz(request.getParameter("stockexchange"),"");
			int srchFlag = Integer.parseInt( CUtils.nz(request.getParameter("srchFlag"),"0"));
			int start=Integer.parseInt(request.getParameter("start"));
			int limit=Integer.parseInt(request.getParameter("limit"));
			StockYahooApiModel yam = new StockYahooApiModel();
			String srchvalues = "";
			if(srchFlag==1)
			{srchvalues=volume + "@" + marketcap + "@" + pe;
			//start=0;
			}
			ArrayList stckSymb=yam.getAllStockSymbols(stockexchange,start,limit,sectorFlag,srchvalues,srchFlag);
			yam.getStockInfo(stckSymb);

			JSONArray arrayObj = new JSONArray();
			//System.out.println("srch val-->"+srchvalues);
			//System.out.println("stckSymb val-->"+stckSymb);
			ArrayList<StockYahooApi> yahooapiList = yam.getSectors(sectorFlag,
					srchFlag, srchvalues,stckSymb,start,limit);
			for (int i = 0; i < yahooapiList.size(); i++) {
				StockYahooApi ya = yahooapiList.get(i);
				JSONObject itemObj = JSONObject.fromObject(ya);
				// add to array list
				arrayObj.add(itemObj);
			}
			JSONObject myObj = new JSONObject();
			// sets success to true
			myObj.put("success", true);
			// set the JSON root to items
			myObj.put("sectors", arrayObj);
			//if(srchFlag==1)
			//{
				//myObj.put("totalCount",yahooapiList.size());
			//}else{

			myObj.put("totalCount", yam.getTotalSectors(sectorFlag,
					srchFlag, srchvalues,stckSymb,start,limit));
			//}
			out.println(myObj.toString());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
