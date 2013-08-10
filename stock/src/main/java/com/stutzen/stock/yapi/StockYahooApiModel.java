package com.stutzen.stock.yapi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class StockYahooApiModel {

	Connection conn = null;
	PreparedStatement stmt = null;
	String sql = null;

	public ArrayList getStockSymbols() {
		ArrayList sb = new ArrayList();
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st
					.executeQuery("select * from stocksymbol_tbl left join stockhistory_tbl on stocksymbol=stockhistsymbol and stockhistdate='"
							+ getYesterdaDateString()
							+ "' where stockhistsymbol is null and isactive is true ");
			while (rs.next()) {

				sb.add(rs.getString("stocksymbol").trim());
			}
			rs.close();
			st.close();
			st = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	public ArrayList getAllStockSymbols(String stockexchange,int start,int limit,int sector,String  srchvalues,int srchFlag)
	 {
		ArrayList sb = new ArrayList();
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("GMT"));
			int cali=1;
			 cali+=cal.get(Calendar.MONTH);
			String curmonth = "" + cali;
			//System.out.println("cur month-->"+curmonth);
			cal.add(Calendar.YEAR, 0);
			String curYear = getDateYear(cal);
			StringBuffer sqlQry=new StringBuffer();
			//sqlQry.append("select d.stocksymbol from stocksymbol_tbl a ");
			sqlQry.append("select d.stocksymbol as \"Ticker\",b.stockhistadjclose as \"Price Sold\",c.stockhistadjclose as \"Purchase Price\",((b.stockhistadjclose-c.stockhistadjclose)/c.stockhistadjclose)*100 as \"Gain / Loss%\"  from stockinfo_tbl a ");
			switch(sector){
		case 1:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
			sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e order by e.stockhistdate desc limit 1) ");
			sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e order by e.stockhistdate desc limit 1 offset 2)  ");
			break;
		case 2:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
			sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 2) ");
			sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 6)  ");
			break;
		case 3:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
			sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 6) ");
			sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e order by e.stockhistdate desc limit 1 offset 13)  ");
			break;
		case 4:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
			sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 13) ");
			sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 30)  ");
			break;
		case 5:

			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
					+ curmonth
					+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
					+ curmonth
					+ "' and   extract(year from stockhistdate)='"+curYear+"' order by e.stockhistdate asc limit 1)  ");
			break;
		case 6:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval))  "
					+ " order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval)) "
					+ " order by e.stockhistdate asc limit 1)  ");
			break;
		case 7:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval))  "
					+ " order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '2 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '2 month'::interval))  "
					+ " order by e.stockhistdate asc limit 1)  ");
			break;
		case 8:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
					+ curmonth
					+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '2 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '2 month'::interval))  "
					+ " order by e.stockhistdate asc limit 1)  ");
			break;
		case 9:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
					+ curmonth
					+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '3 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '3 month'::interval))  "
					+ " order by e.stockhistdate asc limit 1)  ");
			break;
		case 10:
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
					+ curmonth
					+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
					+ " extract (month from (now() - '6 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '6 month'::interval))  "
					+ " order by e.stockhistdate asc limit 1)  ");
			break;
		case 11:
			cal.add(Calendar.YEAR, -1);
			String prevYear = getDateYear(cal);
			sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
					+ curmonth
					+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e  "
					//"extract(year from stockhistdate)='"+ prevYear
					+ " order by e.stockhistdate desc limit 1 offset 249)  ");
			break;

		default:
			sqlQry.append(" select * from stocksymbol_tbl where isactive is true ");
			sqlQry.append(" and stocksymbol in (select distinct stockhistsymbol from stockhistory_tbl) ");

			break;
		}
			double minvolume = 0;
			double maxvolume = 0;
			BigDecimal minmarketcap = null;
			BigDecimal maxmarketcap = null;
			double minpe = 0;
			double maxpe = 0;
			sqlQry.append("join   stocksymbol_tbl d on a.stocksymbol=d.stocksymbol  ");
			sqlQry.append(" where d.isactive is true ");
			if (srchFlag == 1) {
				String delimiter = "@";
				/*
				 * given string will be split by the argument delimiter
				 * provided.
				 */
				String temp[] = srchvalues.split(delimiter);
				/* print substrings */
				String minmaxvol[] = temp[0].split(" ");
				minvolume = Double.parseDouble(minmaxvol[0]);
				maxvolume =Double.parseDouble(minmaxvol[1]);
				//System.out.println("minmaxmcap[0][1]"+temp[1]);
				String minmaxmcap[] = temp[1].split(" ");
				//System.out.println("minmaxmcap[0][1]"+minmaxmcap[0]+"&&"+minmaxmcap[1]);
				minmarketcap = new BigDecimal(minmaxmcap[0]);
				maxmarketcap = new BigDecimal(minmaxmcap[1]);

				String minmaxpe[] = temp[2].split(" ");
				minpe = Double.parseDouble(minmaxpe[0]);
				maxpe = Double.parseDouble(minmaxpe[1]);

				sqlQry.append(" and   round(coalesce(a.volume,0)) >= "
						+ minvolume
						+ " and  round(coalesce(a.volume,0)) <= "
						+ maxvolume);
				sqlQry.append(" and  round(coalesce(a.pe,0)) >= "
						+ minpe
						+ " and round(coalesce(a.pe,0)) <=  "
						+ maxpe);

				//sqlQry.append(" and case when a.marketcap = 'N/A' then '0' else case when a.marketcap like '%M' then replace(a.marketcap,'M','')::real*1000000 else case when a.marketcap like '%B' then replace(a.marketcap,'B','')::real*1000000000 else case when a.marketcap like '%T' then replace(a.marketcap,'T','')::real*1000000000000 end end end end ::real >= "
					//	+ minmarketcap);
				//sqlQry.append(" and case when a.marketcap = 'N/A' then '0' else case when a.marketcap like '%M' then replace(a.marketcap,'M','')::real*1000000 else case when a.marketcap like '%B' then replace(a.marketcap,'B','')::real*1000000000 else case when a.marketcap like '%T' then replace(a.marketcap,'T','')::real*1000000000000 end end end end ::real <= "
					//	+ maxmarketcap);
				sqlQry.append(" and  round(coalesce(a.marketcap,0)) >= "
						+ minmarketcap);
				sqlQry.append(" and round(coalesce(a.marketcap,0)) <= "
						+ maxmarketcap);
			}
			if(stockexchange!=null && stockexchange !=""){
				sqlQry.append("  and d.stockexchange like '"+stockexchange.toUpperCase().trim()+"%' ");
				sqlQry.append(" limit  "+limit);
			}else{
				sqlQry.append(" order by \"Gain / Loss%\"  desc limit  "+limit+" offset "+start);
			}
			System.out.println("stck qry-->"+sqlQry);
			System.out.println("stck sector-->"+sector);

			ResultSet rs = st
					.executeQuery(sqlQry.toString());
			while (rs.next()) {

				sb.add(rs.getString(1).trim());
			}
			rs.close();
			st.close();
			st = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	public int insertStockSymbols(String stocksymbol, String stockname,
			String isactive,String stockexchange) throws Exception {
		int insertedcount = 0;
		try {
			System.out.println("stocksymbol"+stocksymbol+"stockname"+stockname+"isactive"+isactive+"stockexchange"+stockexchange);
			if(stocksymbol!=""){
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			// ps.executeUpdate();
			int temp = 0;
			boolean isact=true;
			if(isactive.equals("true")){
				isact=true;
			}else{
				isact=false;
			}
			ResultSet rs = st
					.executeQuery("select * from stocksymbol_tbl where stocksymbol like '"
							+ stocksymbol.toUpperCase().trim() + "'");
			System.out.println("temp val b select"+temp);
			while (rs.next()) {
				temp = 1;
				if (isactive != "") {
					PreparedStatement pst = conn
							.prepareStatement("update stocksymbol_tbl set isactive="
									+ isact
									+ " where stocksymbol like '"
									+ stocksymbol + "'");
					System.out.println("iupdate qry-->"+"update stocksymbol_tbl set isactive="+ isactive+ " where stocksymbol like '"+ stocksymbol + "'");
					pst.executeUpdate();
					pst.close();
				}
			}
			System.out.println("t-emp->" + temp);
			if (temp == 0) {
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO stocksymbol_tbl (stocksymbol,stockname,isactive,stockexchange) VALUES (?,?,?,?)");

				ps.setString(1, stocksymbol.toUpperCase());
				ps.setString(2, stockname);

				ps.setBoolean(3, isact);
				ps.setString(4, stockexchange);
				insertedcount += ps.executeUpdate();
				System.out.println("insert qry");
				ps.close();
			}
			st.close();
			st = null;
			conn.close();
			conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return insertedcount;
	}

	public int insertStockSymbolsFromFile(String filename,String stockexchange)
			throws FileNotFoundException {
		int insertedcount = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(filename));
			String line = null;
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO stocksymbol_tbl (stocksymbol,stockexchange) VALUES (?,?)");
			// ps.executeUpdate();
			int omitheader = 0;
			int temp = 0;
			while ((line = input.readLine()) != null) {
				// while(inp.hasMoreTokens()){

				String[] fields = parseCSVLine(line);
				System.out.println("fields-->" + fields[1]);
				ResultSet rs = st
						.executeQuery("select * from stocksymbol_tbl where stocksymbol like '"
								+ fields[0] + "'");
				while (rs.next()) {
					temp = 1;
				}
				System.out.println("t-emp->" + temp + "fields" + fields[0]);
				if ((temp == 0) && (omitheader == 1)) {
					ps.setString(1, fields[0]);
					ps.setString(2, stockexchange);
					insertedcount += ps.executeUpdate();
				}
				omitheader = 1;
				temp = 0;
			}
			st.close();
			st = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return insertedcount;
	}

	public ArrayList getStockLog() {
		ArrayList sb = new ArrayList();
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st
					.executeQuery("select to_char(stockhistdate,'MM/DD/YYYY'),count(*) as count from stockhistory_tbl group by stockhistdate ORDER BY stockhistdate DESC limit 10 ");
			while (rs.next()) {
				StockYahooHistoryApi sha = new StockYahooHistoryApi();
				sha.setHistDate(rs.getString(1));
				sha.setCount(Integer.parseInt(rs.getString(2)));
				sb.add(sha);
			}
			rs.close();
			st.close();
			st = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;

	}

	public synchronized int insertHistory(ArrayList sb) {
		HttpClient httpclient = new DefaultHttpClient();
		String symb = "";
		int insertedcount = 0;
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			Connection con = ((DataSource) ctx.lookup("jdbc/mydb2"))
					.getConnection();

			String enddate = getYesterdaDateString();

			//System.out.println("enddate date--->" + enddate);

			String[] edate = enddate.split("/");
			int emon = Integer.parseInt(edate[1]) - 1;
			int smon =0;
			String responseBodyHist = "";
			String[] sdate =null;

			for (int s = 0; s < sb.size(); s++) {
				String paramQry="http://ichart.finance.yahoo.com/table.csv?s="+ sb.get(s);
				String startdate = getHistoryStartEndDate(sb.get(s).toString()
						.toUpperCase().trim());
				//System.out.println("strat date"+startdate);
				if(startdate.equals("")){
					 sdate = getLastYearDateString().split("/");
				}else{
					sdate = startdate.split("-");

				}
				//System.out.println("strat date length-->"+sdate.length);
				if(sdate.length>1){
				 smon = Integer.parseInt(sdate[1]) - 1;
				}
				paramQry+="&a=" + smon + "&b="
						+ sdate[2] + "&c=" + sdate[0] + "&d="
						+ emon + "&e=" + edate[2] + "&f="
						+ edate[0] + "&g=d";

				// System.out.println("s value--->" + s + "mod val-->" + s % 199
				// + "symb-->" + symb);
				try {
					HttpGet httpgethist = new HttpGet(
							paramQry);
					System.out.println("executing request "
							+ httpgethist.getURI());

					// Create a response handler
					ResponseHandler<String> responseHandlerhist = new BasicResponseHandler();
					responseBodyHist = httpclient.execute(httpgethist,
							responseHandlerhist);
					System.out.println("responseBody request "
							+ responseBodyHist);

					PreparedStatement pshist = con
							.prepareStatement("INSERT INTO stockhistory_tbl(stockhistsymbol,stockhistopen,stockhisthigh,stockhistlow,stockhistclose,stockhistvolume,stockhistadjclose,stockhistdate)  VALUES (?,?,?,?,?,?,?,?)");
					StringTokenizer inphist = new StringTokenizer(
							responseBodyHist, "\n");
					int histtemp = 0;
					String ssymb="";
					while (inphist.hasMoreTokens()) {
						//System.out.println("in token-->"+inphist.countTokens()+"hasmoretokens->"+inphist.hasMoreTokens());
						String[] fields = parseCSVLine(inphist.nextToken());
						//System.out.println("fields.length-->"+fields.length);
						if (histtemp == 1) {
							ssymb=fields[1];
							pshist.setString(1, sb.get(s).toString());
							pshist.setFloat(2, Float.parseFloat(fields[1]));
							pshist.setFloat(3, Float.parseFloat(fields[2]));
							pshist.setFloat(4, Float.parseFloat(fields[3]));
							pshist.setFloat(5, Float.parseFloat(fields[4]));
							pshist.setFloat(7, Float.parseFloat(fields[6]));
							pshist.setFloat(6, Long.parseLong(fields[5]));

							pshist.setDate(8, getCurrentDate(fields[0]));
							insertedcount += pshist.executeUpdate();
						}
						histtemp = 1;
						//System.out.println("in token-->"+inphist.countTokens()+"hasmoretokens->"+inphist.hasMoreTokens());
						if(inphist.countTokens()==0){
							ArrayList ssymbol=new ArrayList();
							ssymbol.add(sb.get(s).toString());
							getStockInfo(ssymbol);
						}
					}
					pshist.close();
					pshist = null;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}

			con.close();
			con = null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return insertedcount;
	}

	public int checkStockInfoSymbol(String symb){
		int count=0;
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			StringBuffer stckQry=new StringBuffer();
			stckQry.append(" select count(stocksymbol) from stockinfo_tbl where stocksymbol like '"+symb+"' ");
			//system.out.println("stck qry-->"+stckQry);

			ResultSet rs = st
					.executeQuery(stckQry.toString());
			while (rs.next()) {
				count=Integer.parseInt(rs.getString(1));
			}
			rs.close();
			st.close();
			st = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//system.out.println("count-->"+count);
		return count;

	}

	public void getStockInfo(ArrayList sb) {

		HttpClient httpclient = new DefaultHttpClient();
		String symb = "";

		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			//system.out.println("sb sixe-->"+sb.size());
			for (int s = 0; s < sb.size(); s++) {
//system.out.println("************************************88");
				if ((s > 0 || sb.size() == 1) && (s % 199 == 0 || sb.size() == 1 || s == sb.size() - 1)) {
					//system.out.println("Inside loop");
					symb += sb.get(s).toString().trim();
					try {
						HttpGet httpget = new HttpGet(
								"http://download.finance.yahoo.com/d/tablequotes.csv?s="
										+ symb + "&f=nsd1l1vj1r");
						String qsymb = symb;
						symb = "";
						// Create a response handler
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String responseBody = httpclient.execute(httpget,
								responseHandler);

						// PreparedStatement ps = conn.prepareStatement(
						// "INSERT INTO stockinfo_tbl(curdate,volume,marketcap,pe,price,stockname,stocksymbol)  VALUES (?,?,?,?,?,?,?)");
						// ps.executeUpdate();
						StringTokenizer inp = new StringTokenizer(responseBody,
								"\n");
						String line = null;
						int j = 0;
						// while (( line = inp) != null) {
						while (inp.hasMoreTokens()) {
							String[] fields = parseCSVLine(inp.nextToken());
							// j=1;
							System.out.println("fields"+fields);
							//int chckSymb=checkStockInfoSymbol(fields[1].toUpperCase().trim());
							int count=0;
							Statement st = conn.createStatement();
							StringBuffer stckQry=new StringBuffer();
							stckQry.append(" select count(stocksymbol) from stockinfo_tbl where stocksymbol like '"+fields[1].toUpperCase().trim()+"' ");
							System.out.println("stck qry-->"+stckQry);

							ResultSet rs = st
									.executeQuery(stckQry.toString());
							while (rs.next()) {
								count=Integer.parseInt(rs.getString(1));
							}
							String mcap=fields[5];
							System.out.println("count-->"+count);
							BigDecimal mcapVal=null;
							if(mcap.contains("M")){
								 mcapVal=new BigDecimal(mcap.replaceAll("M","")).multiply(new BigDecimal(1000000));
							}else if(mcap.contains("B")){
								 mcapVal=new BigDecimal(mcap.replaceAll("B","")).multiply(new BigDecimal(1000000000));
								 //mcapVal=Double.parseDouble(mcap.replaceAll("B",""))*1000000000;
							}else if(mcap.contains("T")){
								 mcapVal=new BigDecimal(mcap.replaceAll("T","")).multiply(new BigDecimal(1000000000));
								/// mcapVal=Double.parseDouble(mcap.replaceAll("T",""))*1000000000;
							}else if(mcap.contains("e+")){
								String[] mrktval=mcap.split("e+");
								//system.out.println("e value-->"+mrktval[1]);
								Double  exp=Math.pow(10, Double.parseDouble(mrktval[1]));
								//system.out.println("e exp-->"+exp);
								 mcapVal=new BigDecimal(mrktval[0]).multiply(new BigDecimal(exp));
								 //system.out.println("mcapVal-->"+mcapVal);
							}else if(mcap.contains("E+")){
								String[] mrktval=mcap.split("E+");
								Double  exp=Math.pow(10, Double.parseDouble(mrktval[1]));
								 mcapVal=new BigDecimal(mrktval[0]).multiply(new BigDecimal(exp));
							}else if(mcap.equals("N/A")){
								mcapVal=new BigDecimal(0);

							}else{
								mcapVal=new BigDecimal(fields[5]);
							}
							double pe=0;
							String per=fields[6].replaceAll("\\r", "");
							per=per.replaceAll("\\n", "");
							//system.out.println("pe-->"+fields[6]+"@@");
							//system.out.println("per-->"+per+"@@");
							if(per.equals("N/A")||per.equals("")){
								pe=0;
							}else{
								pe=Double.parseDouble(per);
							}
							long volume=0;
							//system.out.println("volume-->"+fields[4]+"@@");
							if(fields[4].equals("N/A")||fields[4].equals("")){
								volume=0;
							}else{
								volume=Long.parseLong(fields[4]);
							}
							if(mcapVal.equals(0)){
								mcapVal=mcapVal;
							}else{
								mcapVal=mcapVal.divide(new BigDecimal(1000000));
							}
							//system.out.println("mcapVal-->"+mcapVal+"@@");
							//system.out.println("volume-->"+volume+"@@");
							if(count==0){
							String insertQry="Insert into  stockinfo_tbl(curdate,volume,marketcap,pe,price,stockname,stocksymbol) values(?,?,?,?,?,?,?) ";
							PreparedStatement pss = conn
									.prepareStatement(insertQry);
							pss.setString(1, fields[2]);
							pss.setLong(2,volume);
							pss.setBigDecimal(3, mcapVal);
							pss.setDouble(4,pe);
							//pss.setString(2, fields[4]);
							//pss.setString(3, ""+mcapVal);
							//pss.setString(4, fields[6]);
							pss.setString(5, fields[3]);
							pss.setString(6, fields[0]);
							pss.setString(7, fields[1]);
							pss.executeUpdate();
							pss.close();
							}else{
							String updatesql = "UPDATE stockinfo_tbl set curdate='"
									+ fields[2]
									+ "',volume="
									+ volume
									+ ",marketcap="
									+ mcapVal
									+ ",pe="
									+ pe
									+ ",price='"
									+ fields[3]
									+ "' where stocksymbol='" + fields[1] + "'";
							PreparedStatement ps = conn
									.prepareStatement(updatesql);
							ps.executeUpdate();
							ps.close();
							}
							j++;
						}
						symb = "";
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					symb += sb.get(s) + "+";
				}
			}
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static java.sql.Date getCurrentDate(String pdate)
			throws ParseException {

		// String startDate="01-02-2013";
		DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date cdate = sdf1.parse(pdate);
		return new java.sql.Date(cdate.getTime());

		// java.util.Date today = new java.util.Date();
		// return new java.sql.Date(today.getTime());
	}

	public static String[] parseCSVLine(String line) {
		// Create a pattern to match breaks
		Pattern p = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
		// Split input with the pattern
		String[] fields = p.split(line);
		for (int i = 0; i < fields.length; i++) {
			// Get rid of residual double quotes
			fields[i] = fields[i].replace("\"", "");
		}
		return fields;
	}

	public ArrayList<StockYahooApi> getSectors(int sector, int srchFlag,
			String srchvalues, ArrayList symbl, int start, int limit) {
		ArrayList<StockYahooApi> yahooapiList = new ArrayList<StockYahooApi>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		int cali=1;
		 cali+=cal.get(Calendar.MONTH);
		String curmonth = "" + cali;
		System.out.println("cur month-->"+curmonth);
		cal.add(Calendar.YEAR, 0);
		String curYear = getDateYear(cal);
		String stcksymbol="";
		String sym = "";
		for (int i = 0; i < symbl.size(); i++) {
			//stcksymbol="'" + symbl.get(i).toString().toUpperCase().trim() + "'";
			sym += "'" + symbl.get(i) + "'";
			if (i != symbl.size() - 1) {
				sym += ",";
			}
		}
		try {
			if(symbl.size()>0){
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			StringBuffer sqlQry = new StringBuffer();
			sqlQry.append("select d.stockid as \"StockId\",a.stockname as \"Name\",a.stocksymbol as \"Ticker\",a.curdate as \"Date\",a.volume as \"Volume\",a.marketcap as \"MarketCap\",a.pe as \"P/E\",'$'|| a.price as \"Current Price\",b.stockhistadjclose as \"Price Sold\",c.stockhistadjclose as \"Purchase Price\",((b.stockhistadjclose-c.stockhistadjclose)/c.stockhistadjclose)*100 as \"Gain / Loss%\" ,d.isactive as isactive,d.stockexchange as stockexchange,to_char(b.stockhistdate,'MM/DD/YYYY') as startdate,to_char(c.stockhistdate,'MM/DD/YYYY') as enddate from stockinfo_tbl a ");
			switch (sector) {

			case 1:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym + ") order by e.stockhistdate desc limit 1) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 2)  ");
				break;
			case 2:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 2) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 6)  ");
				break;
			case 3:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 6) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 13)  ");
				break;
			case 4:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 13) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 30)  ");
				break;
			case 5:

				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' and   extract(year from stockhistdate)='"+curYear+"' order by e.stockhistdate asc limit 1)  ");
				break;
			case 6:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 7:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '2 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '2 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 8:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '2 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '2 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 9:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '3 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '3 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 10:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '6 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '6 month'::interval)) and e.stockhistsymbol in ("
						+ sym
						+ ") "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 11:
				cal.add(Calendar.YEAR, -1);
				String prevYear = getDateYear(cal);
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where " +
						//"extract(year from stockhistdate)='"+ prevYear	+ "'" + and
								"  e.stockhistsymbol in ("
						+ sym
						+ ") order by e.stockhistdate desc limit 1 offset 249)  ");
				break;

			default:
				sqlQry.append("");
				break;
			}
			sqlQry.append("join   stocksymbol_tbl d on a.stocksymbol=d.stocksymbol  ");

			double minvolume = 0;
			double maxvolume = 0;
			BigDecimal minmarketcap = null;
			BigDecimal maxmarketcap = null;
			double minpe = 0;
			double maxpe = 0;

			sqlQry.append(" where d.isactive is true and a.stocksymbol in ("
					+ sym + ")");
			if (srchFlag == 1) {
				String delimiter = "@";
				/*
				 * given string will be split by the argument delimiter
				 * provided.
				 */
				String temp[] = srchvalues.split(delimiter);
				/* print substrings */
				String minmaxvol[] = temp[0].split(" ");
				minvolume = Double.parseDouble(minmaxvol[0]);
				maxvolume =Double.parseDouble(minmaxvol[1]);
				//System.out.println("minmaxmcap[0][1]"+temp[1]);
				String minmaxmcap[] = temp[1].split(" ");
				//System.out.println("minmaxmcap[0][1]"+minmaxmcap[0]+"&&"+minmaxmcap[1]);
				minmarketcap = new BigDecimal(minmaxmcap[0]);
				maxmarketcap = new BigDecimal(minmaxmcap[1]);

				String minmaxpe[] = temp[2].split(" ");
				minpe = Double.parseDouble(minmaxpe[0]);
				maxpe = Double.parseDouble(minmaxpe[1]);



				sqlQry.append(" and  round(coalesce(a.volume,0))  >= "
						+ minvolume
						+ " and round(coalesce(a.volume,0))  <= "
						+ maxvolume);
				sqlQry.append(" and  round(coalesce(a.pe,0))  >= "
						+ minpe
						+ " and  round(coalesce(a.pe,0)) <=  "
						+ maxpe);

				//sqlQry.append(" and case when a.marketcap = 'N/A' then '0' else case when a.marketcap like '%M' then replace(a.marketcap,'M','')::real*1000000 else case when a.marketcap like '%B' then replace(a.marketcap,'B','')::real*1000000000 else case when a.marketcap like '%T' then replace(a.marketcap,'T','')::real*1000000000000 end end end end ::real >= "
					//	+ minmarketcap);
				//sqlQry.append(" and case when a.marketcap = 'N/A' then '0' else case when a.marketcap like '%M' then replace(a.marketcap,'M','')::real*1000000 else case when a.marketcap like '%B' then replace(a.marketcap,'B','')::real*1000000000 else case when a.marketcap like '%T' then replace(a.marketcap,'T','')::real*1000000000000 end end end end ::real <= "
					//	+ maxmarketcap);
				sqlQry.append(" and   round(coalesce(a.marketcap,0))  >= "
						+ minmarketcap);
				sqlQry.append(" and   round(coalesce(a.marketcap,0))  <= "
						+ maxmarketcap);
			}
			sqlQry.append("  order by \"Gain / Loss%\"  desc  limit "+limit);
			System.out.println("sql--->" + sqlQry.toString());
			stmt = conn.prepareStatement(sqlQry.toString(),
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();
			int rank = start;
			while (rs.next()) {
				//System.out.println("reault");
				rank = rank + 1;
				StockYahooApi ya = new StockYahooApi();
				ya.setId(Integer.parseInt(rs.getString(1)));
				ya.setName(rs.getString(2));
				ya.setCode(rs.getString(3));
				ya.setCurdate(rs.getString(4));
				ya.setVolume(rs.getString(5));
				ya.setMktcap(rs.getString(6));
				ya.setPe(rs.getString(7));
				ya.setPrice(rs.getString(8));
				ya.setValue1(rs.getString(9));
				ya.setValue2(rs.getString(10));
				ya.setGainpercent(rs.getString(11));
				if(rs.getString(12).equals("t")){
					ya.setIsactive(true);
				}else{
					ya.setIsactive(false);
				}
				ya.setStockexchange(rs.getString(13));
				ya.setStartdate(rs.getString(14));
				ya.setEnddate(rs.getString(15));
				ya.setType(sector);
				ya.setRank(rank);
				ya.setSector(sector);
				yahooapiList.add(ya);

			}
			rs.close();
			stmt.close();
			stmt = null;
			conn.close();
			conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlex) {
				}

				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqlex) {
				}

				conn = null;
			}
		}
		return yahooapiList;
	}

	public int getTotalSectors(int sector, int srchFlag,
			String srchvalues, ArrayList symbl, int start, int limit) {
		int totalcount = 0;
		ArrayList<StockYahooApi> yahooapiList = new ArrayList<StockYahooApi>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		int cali=1;
		 cali+=cal.get(Calendar.MONTH);
		String curmonth = "" + cali;
		//System.out.println("cur month-->"+curmonth);
		cal.add(Calendar.YEAR, 0);
		String curYear = getDateYear(cal);
		String stcksymbol="";
		String sym = "";
		for (int i = 0; i < symbl.size(); i++) {
			//stcksymbol="'" + symbl.get(i).toString().toUpperCase().trim() + "'";
			sym += "'" + symbl.get(i) + "'";
			if (i != symbl.size() - 1) {
				sym += ",";
			}
		}
		try {
			if(symbl.size()>0){
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			StringBuffer sqlQry = new StringBuffer();
			sqlQry.append("select count(*) from stockinfo_tbl a ");
			switch (sector) {

			case 1:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e order by e.stockhistdate desc limit 1) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e order by e.stockhistdate desc limit 1 offset 2)  ");
				break;
			case 2:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 2) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 6)  ");
				break;
			case 3:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 6) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e order by e.stockhistdate desc limit 1 offset 13)  ");
				break;
			case 4:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in ");
				sqlQry.append("(select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 13) ");
				sqlQry.append(" join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e  order by e.stockhistdate desc limit 1 offset 30)  ");
				break;
			case 5:

				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' and   extract(year from stockhistdate)='"+curYear+"' order by e.stockhistdate asc limit 1)  ");
				break;
			case 6:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval))  "
						+ " order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval)) "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 7:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '1 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '1 month'::interval))  "
						+ " order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '2 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '2 month'::interval))  "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 8:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '2 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '2 month'::interval))  "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 9:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '3 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '3 month'::interval))  "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 10:
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)="
						+ " extract (month from (now() - '6 month'::interval)) and extract(year from stockhistdate)=extract (year from (now() - '6 month'::interval))  "
						+ " order by e.stockhistdate asc limit 1)  ");
				break;
			case 11:
				cal.add(Calendar.YEAR, -1);
				String prevYear = getDateYear(cal);
				sqlQry.append(" join  stockhistory_tbl b on a.stocksymbol=b.stockhistsymbol  and b.stockhistdate in (select e.stockhistdate from stockhistory_tbl e where extract(month from stockhistdate)='"
						+ curmonth
						+ "' order by e.stockhistdate desc limit 1) join stockhistory_tbl c on a.stocksymbol=c.stockhistsymbol and c.stockhistdate in (select distinct e.stockhistdate from stockhistory_tbl e  "
						//"extract(year from stockhistdate)='"+ prevYear
						+ " order by e.stockhistdate desc limit 1 offset 249)  ");
				break;

			default:
				sqlQry.append("");
				break;
			}
			sqlQry.append("join   stocksymbol_tbl d on a.stocksymbol=d.stocksymbol  ");

			double minvolume = 0;
			double maxvolume = 0;
			BigDecimal minmarketcap = null;
			BigDecimal maxmarketcap = null;
			double minpe = 0;
			double maxpe = 0;

			sqlQry.append(" where d.isactive is true ");
			if (srchFlag == 1) {
				String delimiter = "@";
				/*
				 * given string will be split by the argument delimiter
				 * provided.
				 */
				String temp[] = srchvalues.split(delimiter);
				/* print substrings */
				String minmaxvol[] = temp[0].split(" ");
				minvolume = Double.parseDouble(minmaxvol[0]);
				maxvolume =Double.parseDouble(minmaxvol[1]);
				//System.out.println("minmaxmcap[0][1]"+temp[1]);
				String minmaxmcap[] = temp[1].split(" ");
				//System.out.println("minmaxmcap[0][1]"+minmaxmcap[0]+"&&"+minmaxmcap[1]);
				minmarketcap = new BigDecimal(minmaxmcap[0]);
				maxmarketcap = new BigDecimal(minmaxmcap[1]);

				String minmaxpe[] = temp[2].split(" ");
				minpe = Double.parseDouble(minmaxpe[0]);
				maxpe = Double.parseDouble(minmaxpe[1]);

				sqlQry.append(" and   round(coalesce(a.volume,0)) >= "
						+ minvolume
						+ " and  round(coalesce(a.volume,0)) <= "
						+ maxvolume);
				sqlQry.append(" and  round(coalesce(a.pe,0)) >= "
						+ minpe
						+ " and round(coalesce(a.pe,0)) <=  "
						+ maxpe);

				//sqlQry.append(" and case when a.marketcap = 'N/A' then '0' else case when a.marketcap like '%M' then replace(a.marketcap,'M','')::real*1000000 else case when a.marketcap like '%B' then replace(a.marketcap,'B','')::real*1000000000 else case when a.marketcap like '%T' then replace(a.marketcap,'T','')::real*1000000000000 end end end end ::real >= "
					//	+ minmarketcap);
				//sqlQry.append(" and case when a.marketcap = 'N/A' then '0' else case when a.marketcap like '%M' then replace(a.marketcap,'M','')::real*1000000 else case when a.marketcap like '%B' then replace(a.marketcap,'B','')::real*1000000000 else case when a.marketcap like '%T' then replace(a.marketcap,'T','')::real*1000000000000 end end end end ::real <= "
					//	+ maxmarketcap);
				sqlQry.append(" and  round(coalesce(a.marketcap,0)) >= "
						+ minmarketcap);
				sqlQry.append(" and round(coalesce(a.marketcap,0)) <= "
						+ maxmarketcap);
			}
			System.out.println("sql--->" + sqlQry.toString());
			stmt = conn.prepareStatement(sqlQry.toString(),
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				//System.out.println("reault");
				totalcount=Integer.parseInt(rs.getString(1));


			}
			rs.close();
			stmt.close();
			stmt = null;
			conn.close();
			conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlex) {
				}

				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqlex) {
				}

				conn = null;
			}
		}
		return totalcount;
	}

	public static String getDate(Calendar cal) {
		return "" + cal.get(Calendar.MONTH) + "/"
				+ (cal.get(Calendar.DATE) + 1) + "/" + cal.get(Calendar.YEAR);
	}

	public static String getDateMon(Calendar cal) {
		return "" + cal.get(Calendar.MONTH);
	}

	public static String getDateYear(Calendar cal) {
		return "" + cal.get(Calendar.YEAR);
	}

	public String getHistoryStartEndDate(String symb) {
		String startdate = "";
		//System.out.println("hus start date-->"+symb);
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st
					.executeQuery(" Select stockhistdate::date+'1'::integer as startdate from stockhistory_tbl inner join stocksymbol_tbl on stocksymbol=stockhistsymbol where stocksymbol = '"
							+ symb
							+ "' and isactive is true order by stockhistdate desc limit 1");
			while (rs.next()) {
				startdate = rs.getString(1);
			}
			rs.close();
			st.close();
			st = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return startdate;
	}

	private static String getYesterdaDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}

	private static String getLastYearDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return dateFormat.format(cal.getTime());
	}

	public ArrayList<StockSymbol> getStockSymbolsList(int start,int limit) throws Exception{
		ArrayList<StockSymbol> stcksymbList = new ArrayList<StockSymbol>();
		try{
		Context ctx = (Context) new InitialContext()
		.lookup("java:comp/env");
conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
stmt = conn.prepareStatement("select * from stocksymbol_tbl limit "+limit+" offset "+start);
ResultSet rs = stmt.executeQuery();
while (rs.next()) {
	//System.out.println("reault");
	StockSymbol ya = new StockSymbol();
	ya.setId(Integer.parseInt(rs.getString(1)));
	ya.setName(rs.getString(3));
	ya.setCode(rs.getString(2));
	if(rs.getString(4).equals("t")){
		ya.setIsactive(true);
	}else{
		ya.setIsactive(false);
	}
	ya.setStockexchange(rs.getString(5));
	stcksymbList.add(ya);

}
rs.close();
stmt.close();
stmt = null;
conn.close();
conn = null;

		}catch(Exception e){
			e.printStackTrace();
		}
		return stcksymbList;

	}
	public int getStockSymbolCount() throws Exception{
		int totalCnt=0;
		try{
		Context ctx = (Context) new InitialContext()
		.lookup("java:comp/env");
conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
stmt = conn.prepareStatement("select count(*) from stocksymbol_tbl ");
ResultSet rs = stmt.executeQuery();
while (rs.next()) {
	//System.out.println("reault");
	totalCnt=Integer.parseInt(rs.getString(1));

}
rs.close();
stmt.close();
stmt = null;
conn.close();
conn = null;

		}catch(Exception e){
			e.printStackTrace();
		}
		return totalCnt;

	}



	public BigDecimal getMaxMarketCap()throws Exception{
		BigDecimal maxmarketcap=null;
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select max(marketcap) from stockinfo_tbl ");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
		//System.out.println("reault");
		maxmarketcap=(new BigDecimal(rs.getString(1)));

	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return maxmarketcap;
	}


	public BigDecimal getMinMarketCap()throws Exception{
		BigDecimal minmarketcap=null;
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select min(marketcap) from stockinfo_tbl ");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
		//System.out.println("reault");
		minmarketcap=(new BigDecimal(rs.getString(1)));

	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return minmarketcap;
	}

	public double getMaxVolume()throws Exception{
		double maxvolume=0;
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select max(volume) from stockinfo_tbl ");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
		//System.out.println("reault");
		maxvolume=Double.parseDouble(rs.getString(1));

	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return maxvolume;
	}


	public double getMinVolume()throws Exception{
		double minvolume=0;
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select min(volume) from stockinfo_tbl ");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
		//System.out.println("reault");
		minvolume=Double.parseDouble(rs.getString(1));

	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return minvolume;
	}

	public double getMaxPE()throws Exception{
		double maxpe=0;
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select max(pe) from stockinfo_tbl ");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
		maxpe=Double.parseDouble(rs.getString(1));

	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return maxpe;
	}


	public double getMinPe()throws Exception{
		double minpe=0;
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select min(pe) from stockinfo_tbl ");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
		minpe=Double.parseDouble(rs.getString(1));

	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return minpe;
	}


	public ArrayList<StockExchange> getStockExchange()throws Exception{
		ArrayList<StockExchange>  selist=new ArrayList<StockExchange>();
		try{
			Context ctx = (Context) new InitialContext()
			.lookup("java:comp/env");
	conn = ((DataSource) ctx.lookup("jdbc/mydb2")).getConnection();
	stmt = conn.prepareStatement("select distinct stockexchange from stocksymbol_tbl ");
	ResultSet rs = stmt.executeQuery();
	int id=0;
	while (rs.next()) {
		StockExchange se=new StockExchange();
		se.setId(id);
		se.setStockexchange(rs.getString(1));
		selist.add(se);
		id=id+1;
	}
	rs.close();
	stmt.close();
	stmt = null;
	conn.close();
	conn = null;

			}catch(Exception e){
				e.printStackTrace();
			}
		return selist;
	}



}


