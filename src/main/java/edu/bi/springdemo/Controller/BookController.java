package edu.bi.springdemo.Controller;

import edu.bi.springdemo.entity.Book;
import edu.bi.springdemo.exception.InvalidDataException;
import edu.bi.springdemo.exception.NotFoundException;
import edu.bi.springdemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED) //code 201 - means that website is successfully created
    public Book addBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

    @PatchMapping("/{id}")
    public Book updateBook(@PathVariable Integer id, @RequestBody Book book){
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id){
        bookService.deleteBook(id);
    }

    @GetMapping("/getAll")
    public Iterable<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/search/title")
    public Iterable<Book> searchByTitle(@RequestParam String title){
        return bookService.searchByTitle(title);
    }

    @GetMapping("/search/author")
    public Iterable<Book> searchByAuthor(@RequestParam String author){
        return bookService.searchByAuthor(author);
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
