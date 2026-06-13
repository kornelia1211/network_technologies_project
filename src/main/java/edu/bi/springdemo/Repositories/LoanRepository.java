package edu.bi.springdemo.Repositories;

import edu.bi.springdemo.entity.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Integer> {

    @Query("SELECT l FROM Loan l WHERE l.user.userId = :userId")
    Iterable<Loan> findByUserId(Integer userId);

    @Query("""
    SELECT COUNT(l) > 0
    FROM Loan l
    WHERE l.book.bookId = :bookId
    AND l.status <> 'RETURNED'
""")
    boolean hasActiveLoans(Integer bookId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM Loan l
    WHERE l.book.bookId = :bookId
""")
    void deleteByBookId(Integer bookId);

    @Query("""
    SELECT COUNT(l) > 0
    FROM Loan l
    WHERE l.user.userId = :userId
    AND l.status <> 'RETURNED'
""")
    boolean hasActiveLoansByUser(Integer userId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM Loan l
    WHERE l.user.userId = :userId
""")
    void deleteByUserId(Integer userId);
}
