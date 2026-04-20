package edu.bi.springdemo.Controller;

import edu.bi.springdemo.DTO.LoanRequestDTO;
import edu.bi.springdemo.Repositories.LoanRepository;
import edu.bi.springdemo.entity.Loan;
import edu.bi.springdemo.exception.InvalidDataException;
import edu.bi.springdemo.exception.NotFoundException;
import edu.bi.springdemo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED) //code 201 - means that website is successfully created
    public Loan addLoan(@RequestBody LoanRequestDTO dto,
                        Authentication authentication){

        return loanService.addLoan(dto.getBookId(), authentication.getName());
    }

    @PutMapping("/return/{loanId}")
    public Loan returnBook(@PathVariable Integer loanId){
        return loanService.returnBook(loanId);
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
