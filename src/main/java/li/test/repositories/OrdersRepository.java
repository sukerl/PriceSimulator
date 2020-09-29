package li.test.repositories;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import li.test.entities.db.Order;
import li.test.entities.db.Parameter;
import li.test.entities.db.Symbol;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer> {
	
	Integer countBySymbolAndParameter(Symbol symbol, Parameter parameter);
	
	@Query(value = "SELECT * " + 
			"FROM Orders " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} " +
			"AND Parameter_ID = :#{#parameter.Parameter_ID} " +
			"AND SettlementTime IS NULL ", nativeQuery = true)
	ArrayList<Order> findOpenOrdersBySymbolAndParameter(@Param("symbol") Symbol symbol, @Param("parameter") Parameter parameter);
	
	@Query(value = "SELECT MIN(OrderTime) " + 
			"FROM Orders " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} " +
			"AND Parameter_ID = :#{#parameter.Parameter_ID} " +
			"AND SettlementTime IS NULL ", nativeQuery = true)
	Timestamp findMinOrderTimeOpenOrdersBySymbolAndParameter(@Param("symbol") Symbol symbol, @Param("parameter") Parameter parameter);
	
	@Query(value = "SELECT MAX(OrderTime) " + 
			"FROM Orders " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} " +
			"AND Parameter_ID = :#{#parameter.Parameter_ID} " +
			"AND SettlementTime IS NULL", nativeQuery = true)
	Timestamp findMaxOrderTimeOpenOrdersBySymbolAndParameter(@Param("symbol") Symbol symbol, @Param("parameter") Parameter parameter);
	
	@Query(value = "SELECT COUNT(*) " + 
			"FROM Orders " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} " +
			"AND Parameter_ID = :#{#parameter.Parameter_ID} " +
			"AND SettlementTime IS NULL", nativeQuery = true)
	Integer getCountOpenOrdersBySymbolAndParameter(@Param("symbol") Symbol symbol, @Param("parameter") Parameter parameter);

	@Query(value = "SELECT SUM(Profit) " + 
			"FROM Orders " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} " +
			"AND Parameter_ID = :#{#parameter.Parameter_ID} " +
			"AND SettlementTime IS NOT NULL ", nativeQuery = true)
	BigDecimal getSumProfitForFinishedOrdersBySymbolAndParameter(@Param("symbol") Symbol symbol, @Param("parameter") Parameter parameter);
	
	@Query(value = "SELECT MAX(OrderTime) " + 
			"FROM Orders " + 
			"WHERE Symbol_ID = :#{#symbol.Symbol_ID} " +
			"AND Parameter_ID = :#{#parameter.Parameter_ID} ", nativeQuery = true)
	Timestamp findMaxOrderTimeBySymbolAndParameter(@Param("symbol") Symbol symbol, @Param("parameter") Parameter parameter);

}
