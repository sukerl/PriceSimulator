package li.test.entities.db;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="prices")
public class Price {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Price_ID")
	private Integer price_ID;
	
	@ManyToOne
	@JoinColumn(name="Symbol_ID", referencedColumnName = "Symbol_ID")
	private Symbol symbol;
	
	@Column(name = "PriceTime")
	private Timestamp priceTime;
	
	@Column(name = "AvgOpenClosePrice")
	private BigDecimal avgOpenClosePrice;

	@Column(name = "LowPrice")
	private BigDecimal lowPrice;

	public Price() {
		super();
	}

	public Price(Integer price_ID, Symbol symbol, Timestamp priceTime, BigDecimal avgOpenClosePrice,
			BigDecimal lowPrice) {
		super();
		this.price_ID = price_ID;
		this.symbol = symbol;
		this.priceTime = priceTime;
		this.avgOpenClosePrice = avgOpenClosePrice;
		this.lowPrice = lowPrice;
	}

	public Integer getPrice_ID() {
		return price_ID;
	}

	public void setPrice_ID(Integer price_ID) {
		this.price_ID = price_ID;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Timestamp getPriceTime() {
		return priceTime;
	}

	public void setPriceTime(Timestamp priceTime) {
		this.priceTime = priceTime;
	}

	public BigDecimal getAvgOpenClosePrice() {
		return avgOpenClosePrice;
	}

	public void setAvgOpenClosePrice(BigDecimal avgOpenClosePrice) {
		this.avgOpenClosePrice = avgOpenClosePrice;
	}

	public BigDecimal getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(BigDecimal lowPrice) {
		this.lowPrice = lowPrice;
	}

	@Override
	public String toString() {
		return "Price [price_ID=" + price_ID + ", symbol=" + symbol + ", priceTime=" + priceTime
				+ ", avgOpenClosePrice=" + avgOpenClosePrice + ", lowPrice=" + lowPrice + "]";
	}
}
