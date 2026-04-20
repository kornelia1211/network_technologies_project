package edu.bi.springdemo.service;

import edu.bi.springdemo.Repositories.BookRepository;
import edu.bi.springdemo.Repositories.LoanRepository;
import edu.bi.springdemo.Repositories.UserRepository;
import edu.bi.springdemo.entity.Book;
import edu.bi.springdemo.entity.Loan;
import edu.bi.springdemo.entity.User;
import edu.bi.springdemo.exception.InvalidDataException;
import edu.bi.springdemo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Loan addLoan(Integer bookId, String username){

        // find user by username
        User user = userRepository.findUserByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> NotFoundException.create("User not found"));

        // find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> NotFoundException.create("Book not found"));

        // check availability
        if(book.getAvailableCopies() == null || book.getAvailableCopies() <= 0){
            throw InvalidDataException.create("No available copies");
        }

        // decrease number of books
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // create loan
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);

        // set dates automatically
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        loan.setLoanDate(today);

        java.sql.Date dueDate = new java.sql.Date(
                System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
        );
        loan.setDueDate(dueDate);

        loan.setReturnDate(null);

        return loanRepository.save(loan);
    }

    public Loan returnBook(Integer loanId){

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> NotFoundException.create("Loan not found"));

        if(loan.getReturnDate() != null){
            throw InvalidDataException.create("Book already returned");
        }

        loan.setReturnDate(new java.sql.Date(System.currentTimeMillis()));

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public Iterable<Loan> getAllLoans(){
        return loanRepository.findAll();
    }

    public Iterable<Loan> getLoansByUser(Integer userId){
        if(!userRepository.existsById(userId)){
            throw NotFoundException.create("User not found");
        }

        return loanRepository.findByUserId(userId);
    }
}