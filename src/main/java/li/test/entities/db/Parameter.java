package li.test.entities.db;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="parameters")
public class Parameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Parameter_ID")
	private Integer parameter_ID;
	
	@Column(name = "StartTime")
	private Timestamp startTime;
	
	@Column(name = "StartAmount")
	private BigDecimal startAmount;
	
	@Column(name = "MaxOrderAmount")
	private BigDecimal maxOrderAmount;
	
	@Column(name = "MaxRunningOrders")
	private Integer maxRunningOrders;
	
	@Column(name = "TimeDifferenceBetweenOrders")
	private Long timeDifferenceBetweenOrders;
	
	@Column(name = "FeesPercentage")
	private BigDecimal feesPercentage;
	
	@Column(name = "DesiredGainPercentage")
	private BigDecimal desiredGainPercentage;

	@Column(name = "StopLossTriggerPercentage")
	private BigDecimal stopLossTriggerPercentage;

	public Parameter() {
		super();
	}

	public Parameter(Integer parameter_ID, Timestamp startTime, BigDecimal startAmount, BigDecimal maxOrderAmount,
			Integer maxRunningOrders, Long timeDifferenceBetweenOrders, BigDecimal feesPercentage,
			BigDecimal desiredGainPercentage, BigDecimal stopLossTriggerPercentage) {
		super();
		this.parameter_ID = parameter_ID;
		this.startTime = startTime;
		this.startAmount = startAmount;
		this.maxOrderAmount = maxOrderAmount;
		this.maxRunningOrders = maxRunningOrders;
		this.timeDifferenceBetweenOrders = timeDifferenceBetweenOrders;
		this.feesPercentage = feesPercentage;
		this.desiredGainPercentage = desiredGainPercentage;
		this.stopLossTriggerPercentage = stopLossTriggerPercentage;
	}

	public Integer getParameter_ID() {
		return parameter_ID;
	}

	public void setParameter_ID(Integer parameter_ID) {
		this.parameter_ID = parameter_ID;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(BigDecimal startAmount) {
		this.startAmount = startAmount;
	}

	public BigDecimal getMaxOrderAmount() {
		return maxOrderAmount;
	}

	public void setMaxOrderAmount(BigDecimal maxOrderAmount) {
		this.maxOrderAmount = maxOrderAmount;
	}

	public Integer getMaxRunningOrders() {
		return maxRunningOrders;
	}

	public void setMaxRunningOrders(Integer maxRunningOrders) {
		this.maxRunningOrders = maxRunningOrders;
	}

	public Long getTimeDifferenceBetweenOrders() {
		return timeDifferenceBetweenOrders;
	}

	public void setTimeDifferenceBetweenOrders(Long timeDifferenceBetweenOrders) {
		this.timeDifferenceBetweenOrders = timeDifferenceBetweenOrders;
	}

	public BigDecimal getFeesPercentage() {
		return feesPercentage;
	}

	public void setFeesPercentage(BigDecimal feesPercentage) {
		this.feesPercentage = feesPercentage;
	}

	public BigDecimal getDesiredGainPercentage() {
		return desiredGainPercentage;
	}

	public void setDesiredGainPercentage(BigDecimal desiredGainPercentage) {
		this.desiredGainPercentage = desiredGainPercentage;
	}

	public BigDecimal getStopLossTriggerPercentage() {
		return stopLossTriggerPercentage;
	}

	public void setStopLossTriggerPercentage(BigDecimal stopLossTriggerPercentage) {
		this.stopLossTriggerPercentage = stopLossTriggerPercentage;
	}

	@Override
	public String toString() {
		return "Parameter [parameter_ID=" + parameter_ID + ", startTime=" + startTime + ", startAmount=" + startAmount
				+ ", maxOrderAmount=" + maxOrderAmount + ", maxRunningOrders=" + maxRunningOrders
				+ ", timeDifferenceBetweenOrders=" + timeDifferenceBetweenOrders + ", feesPercentage=" + feesPercentage
				+ ", desiredGainPercentage=" + desiredGainPercentage + ", stopLossTriggerPercentage="
				+ stopLossTriggerPercentage + "]";
	}	

}
