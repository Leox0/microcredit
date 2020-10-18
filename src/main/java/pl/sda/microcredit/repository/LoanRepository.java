package pl.sda.microcredit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.microcredit.model.LoanEntity;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

}
