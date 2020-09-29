package li.test.entities.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="symbols")
public class Symbol {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Symbol_ID")
	private Integer symbol_ID;
	
	@Column(name = "Symbol")
	private String symbol;

	public Symbol() {
		super();
	}

	public Symbol(Integer symbol_ID, String symbol) {
		super();
		this.symbol_ID = symbol_ID;
		this.symbol = symbol;
	}

	public Integer getSymbol_ID() {
		return symbol_ID;
	}

	public void setSymbol_ID(Integer symbol_ID) {
		this.symbol_ID = symbol_ID;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
