package edu.bi.springdemo.Controller;

import edu.bi.springdemo.DTO.LoanRequestDTO;
import edu.bi.springdemo.entity.Loan;
import edu.bi.springdemo.exception.InvalidDataException;
import edu.bi.springdemo.exception.NotFoundException;
import edu.bi.springdemo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    @ResponseStatus(HttpStatus.CREATED)
    public Loan addLoan(@RequestBody LoanRequestDTO dto,
                        Authentication authentication){

        return loanService.addLoan(dto.getBookId(), authentication.getName());
    }


    @PutMapping("/approve/{loanId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public Loan approveLoan(@PathVariable Integer loanId){
        return loanService.approveLoan(loanId);
    }

    @PutMapping("/request-return/{loanId}")
    @PreAuthorize("hasRole('READER')")
    public Loan requestReturn(@PathVariable Integer loanId){
        return loanService.requestReturn(loanId);
    }

    @PutMapping("/approve-return/{loanId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public Loan approveReturn(@PathVariable Integer loanId){
        return loanService.approveReturn(loanId);
    }

    @GetMapping("/getAll")
    public Iterable<Loan> getAllLoans(){
        return loanService.getAllLoans();
    }

    @GetMapping("/user/{userId}")
    public Iterable<Loan> getLoansByUser(@PathVariable Integer userId){
        return loanService.getLoansByUser(userId);
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidData(InvalidDataException e){
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return map;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundData(NotFoundException e){
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return map;
    }
}