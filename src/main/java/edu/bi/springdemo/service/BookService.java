package edu.bi.springdemo.service;

import edu.bi.springdemo.Repositories.BookRepository;
import edu.bi.springdemo.entity.Book;
import edu.bi.springdemo.exception.InvalidDataException;
import edu.bi.springdemo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {

        if (book.getYear() != null && book.getYear() < 0) {
            throw InvalidDataException.create("Year cannot be negative");
        }

        if (book.getAvailableCopies() != null && book.getAvailableCopies() < 0) {
            throw InvalidDataException.create("Available copies cannot be negative");
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Integer id, Book updatedBook) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> NotFoundException.create("Book not found"));

        if (updatedBook.getTitle() != null) {
            book.setTitle(updatedBook.getTitle());
        }

        if (updatedBook.getAuthor() != null) {
            book.setAuthor(updatedBook.getAuthor());
        }

        if (updatedBook.getPublisher() != null) {
            book.setPublisher(updatedBook.getPublisher());
        }

        if (updatedBook.getYear() != null) {
            if (updatedBook.getYear() < 0) {
                throw InvalidDataException.create("Year cannot be negative");
            }
            book.setYear(updatedBook.getYear());
        }

        if (updatedBook.getAvailableCopies() != null) {
            if (updatedBook.getAvailableCopies() < 0) {
                throw InvalidDataException.create("Available copies cannot be negative");
            }
            book.setAvailableCopies(updatedBook.getAvailableCopies());
        }

        if (updatedBook.getIsbn() != null) {
            book.setIsbn(updatedBook.getIsbn());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw NotFoundException.create("Book not found");
        }
        bookRepository.deleteById(id);
    }

    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Iterable<Book> searchByTitle(String title) {
        Iterable<Book> books = bookRepository.findByTitle(title);

        if (!books.iterator().hasNext()) {
            throw NotFoundException.create("No books found with given title");
        }

        return books;
    }

    public Iterable<Book> searchByAuthor(String author) {
        Iterable<Book> books = bookRepository.findByAuthor(author);

        if (!books.iterator().hasNext()) {
            throw NotFoundException.create("No books found with given author");
        }
        return books;
    }
}