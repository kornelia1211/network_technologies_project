package edu.bi.springdemo.Repositories;

import edu.bi.springdemo.entity.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Integer> {

    @Query("SELECT l FROM Loan l WHERE l.user.userId = :userId")
    Iterable<Loan> findByUserId(Integer userId);
}
