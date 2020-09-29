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
@Table(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Order_ID")
	private Integer order_ID;
	
	@ManyToOne
	@JoinColumn(name="Symbol_ID", referencedColumnName = "Symbol_ID")
	private Symbol symbol;
	
	@ManyToOne
	@JoinColumn(name="Parameter_ID", referencedColumnName = "Parameter_ID")
	private Parameter parameter;
	
	@Column(name = "OrderTime")
	private Timestamp orderTime;
	
	@Column(name = "OrderPrice")
	private BigDecimal orderPrice;

	@Column(name = "OrderAmount")
	private BigDecimal orderAmount;
	
	@Column(name = "SettlementTime")
	private Timestamp settlementTime;
	
	@Column(name = "SettlementPrice")
	private BigDecimal settlementPrice;

	@Column(name = "Profit")
	private BigDecimal profit;

	public Order() {
		super();
	}

	public Order(Integer order_ID, Symbol symbol, Parameter parameter, Timestamp orderTime, BigDecimal orderPrice,
			BigDecimal orderAmount, Timestamp settlementTime, BigDecimal settlementPrice, BigDecimal profit) {
		super();
		this.order_ID = order_ID;
		this.symbol = symbol;
		this.parameter = parameter;
		this.orderTime = orderTime;
		this.orderPrice = orderPrice;
		this.orderAmount = orderAmount;
		this.settlementTime = settlementTime;
		this.settlementPrice = settlementPrice;
		this.profit = profit;
	}

	public Integer getOrder_ID() {
		return order_ID;
	}

	public void setOrder_ID(Integer order_ID) {
		this.order_ID = order_ID;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Timestamp getSettlementTime() {
		return settlementTime;
	}

	public void setSettlementTime(Timestamp settlementTime) {
		this.settlementTime = settlementTime;
	}

	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	@Override
	public String toString() {
		return "Order [order_ID=" + order_ID + ", symbol=" + symbol + ", parameter=" + parameter + ", orderTime="
				+ orderTime + ", orderPrice=" + orderPrice + ", orderAmount=" + orderAmount + ", settlementTime="
				+ settlementTime + ", settlementPrice=" + settlementPrice + ", profit=" + profit + "]";
	}	
	
}
