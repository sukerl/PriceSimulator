package li.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import li.test.entities.db.Symbol;

@Repository
public interface SymbolsRepository extends JpaRepository<Symbol, Integer> {

	Symbol findBySymbol(String symbol);

}
