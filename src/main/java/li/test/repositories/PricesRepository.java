package li.test.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import li.test.entities.db.Price;
import li.test.entities.db.Symbol;

@Repository
public interface PricesRepository extends JpaRepository<Price, Integer> {
	
	Integer countBySymbol(Symbol symbol);
	
	Price findByPriceTime(Timestamp priceTime);
	
	@Query(value = "SELECT * " + 
			"FROM prices " + 
			"WHERE PriceTime = (SELECT MAX(PriceTime) FROM prices WHERE Symbol_ID = :#{#symbol.Symbol_ID}) " +
			"AND Symbol_ID = :#{#symbol.Symbol_ID} ", nativeQuery = true)
	Price findLatestPriceBySymbol(@Param("symbol") Symbol symbol);
	
	@Query(value = "SELECT MIN(PriceTime) " + 
			"FROM prices " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} ", nativeQuery = true)
	Timestamp getFirstPriceTimeBySymbol(@Param("symbol") Symbol symbol);
	
	@Query(value = "SELECT MAX(PriceTime) " + 
			"FROM prices " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} ", nativeQuery = true)
	Timestamp getLatestPriceTimeBySymbol(@Param("symbol") Symbol symbol);
	
	@Query(value = "SELECT PriceTime "
			+ "FROM Prices "
			+ "WHERE PriceTIME>=:startTime "
			+ "AND Symbol_ID = :#{#symbol.Symbol_ID} " 
			+ "ORDER BY PriceTime ASC ", nativeQuery = true)
	ArrayList<Timestamp> getAllPriceTimesBySymbolAndStartTime(@Param("symbol") Symbol symbol, @Param("startTime") Timestamp startTime);
	
	@Query(value = "SELECT * "
			+ "FROM Prices "
			+ "WHERE PriceTIME>=:startTime "
			+ "AND Symbol_ID = :#{#symbol.Symbol_ID} " 
			+ "ORDER BY PriceTime ASC ", nativeQuery = true)
	ArrayList<Price> getAllPricesBySymbolAndStartTime(@Param("symbol") Symbol symbol, @Param("startTime") Timestamp startTime);

}
