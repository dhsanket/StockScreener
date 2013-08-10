package com.stutzen.stock.yapi;

import java.math.BigDecimal;

public class StockExchange {
int id=0;
	public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

	Double minvolume=null;
	Double maxvolume=null;
	Double minpe=null;
	Double maxpe=null;
	BigDecimal minmrktcap=null;
	BigDecimal maxmrktcap=null;

	String stockexchange=null;

	public Double getMinvolume() {
		return minvolume;
	}

	public void setMinvolume(Double minvolume) {
		this.minvolume = minvolume;
	}

	public Double getMaxvolume() {
		return maxvolume;
	}

	public void setMaxvolume(Double maxvolume) {
		this.maxvolume = maxvolume;
	}

	public Double getMinpe() {
		return minpe;
	}

	public void setMinpe(Double minpe) {
		this.minpe = minpe;
	}

	public Double getMaxpe() {
		return maxpe;
	}

	public void setMaxpe(Double maxpe) {
		this.maxpe = maxpe;
	}

	public BigDecimal getMinmrktcap() {
		return minmrktcap;
	}

	public void setMinmrktcap(BigDecimal minmrktcap) {
		this.minmrktcap = minmrktcap;
	}

	public BigDecimal getMaxmrktcap() {
		return maxmrktcap;
	}

	public void setMaxmrktcap(BigDecimal maxmrktcap) {
		this.maxmrktcap = maxmrktcap;
	}

	public String getStockexchange() {
		return stockexchange;
	}

	public void setStockexchange(String stockexchange) {
		this.stockexchange = stockexchange;
	}

}