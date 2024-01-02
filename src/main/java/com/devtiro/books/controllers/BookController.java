package com.devtiro.books.controllers;

import com.devtiro.books.domain.Book;
import com.devtiro.books.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final BooksService booksService;

    @Autowired
    public BookController(BooksService booksService) {
        this.booksService = booksService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> createUpdateBook(@PathVariable final String isbn, @RequestBody final Book book){

        book.setIsbn(isbn);
        final boolean isBookExists = booksService.isBookExists(book);
        final Book savedBook = booksService.save(book);

        if(isBookExists){
            return new ResponseEntity<>(savedBook, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        }
    }

    @GetMapping(path="/books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable final String isbn){

        final Optional<Book> foundBook = booksService.findById(isbn);

        return foundBook.map(book -> new ResponseEntity(book, HttpStatus.OK)).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path="/books")
    public ResponseEntity<List<Book>> listBook(){
        return new ResponseEntity<>(booksService.listBooks(),HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable final String isbn){
        booksService.deleteBookById(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

}
