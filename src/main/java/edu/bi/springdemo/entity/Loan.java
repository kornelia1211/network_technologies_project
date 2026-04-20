package edu.bi.springdemo.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer loanId;

    @ManyToOne
    @JoinColumn(name = "book_id") //foreign key
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id") //foreign key
    private User user;

    @Column
    private Date loanDate;

    @Column
    private Date dueDate;

    @Column
    private Date returnDate;

    @Column
    private String status; // REQUESTED, BORROWED, RETURN_REQUESTED, RETURNED


    //Getters and setters
    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
