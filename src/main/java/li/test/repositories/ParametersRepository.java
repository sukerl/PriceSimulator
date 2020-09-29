package li.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import li.test.entities.db.Parameter;

@Repository
public interface ParametersRepository extends JpaRepository<Parameter, Integer> {


}
