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

        User user = userRepository.findUserByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> NotFoundException.create("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> NotFoundException.create("Book not found"));

        if(book.getAvailableCopies() == null || book.getAvailableCopies() <= 0){
            throw InvalidDataException.create("No available copies");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);

        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        loan.setLoanDate(today);
        loan.setStatus("REQUESTED");

        java.sql.Date dueDate = new java.sql.Date(
                System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
        );
        loan.setDueDate(dueDate);
        loan.setReturnDate(null);

        return loanRepository.save(loan);
    }

    public Loan approveLoan(Integer loanId){

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> NotFoundException.create("Loan not found"));

        if(!loan.getStatus().equals("REQUESTED")){
            throw InvalidDataException.create("Loan not in REQUESTED state");
        }

        Book book = loan.getBook();

        if(book.getAvailableCopies() <= 0){
            throw InvalidDataException.create("No available copies");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        loan.setStatus("BORROWED");

        return loanRepository.save(loan);
    }

    public Loan requestReturn(Integer loanId){

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> NotFoundException.create("Loan not found"));

        loan.setStatus("RETURN_REQUESTED");

        return loanRepository.save(loan);
    }

    public Loan approveReturn(Integer loanId){

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> NotFoundException.create("Loan not found"));

        if(!loan.getStatus().equals("RETURN_REQUESTED")){
            throw InvalidDataException.create("Return not requested");
        }

        loan.setReturnDate(new java.sql.Date(System.currentTimeMillis()));

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        loan.setStatus("RETURNED");

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