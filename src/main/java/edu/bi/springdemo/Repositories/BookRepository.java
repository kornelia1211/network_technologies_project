package edu.bi.springdemo.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.bi.springdemo.entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> { // <Book, type of primary key of Book>

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Iterable<Book> findByTitle(String title);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    Iterable<Book> findByAuthor(String author);

}
