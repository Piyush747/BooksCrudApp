package com.devtiro.books.services.impl;

import com.devtiro.books.domain.Book;
import com.devtiro.books.domain.BooksEntity;
import com.devtiro.books.repositories.BooksRepository;
import com.devtiro.books.services.BooksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public boolean isBookExists(Book book) {
        return booksRepository.existsById(book.getIsbn());
    }

    @Override
    public Book save(final Book book) {
        final BooksEntity booksEntity = bookToBookEntity(book);
        BooksEntity savedBooksEntity = booksRepository.save(booksEntity);
        return bookEntityToBook(savedBooksEntity);
    }

    @Override
    public Optional<Book> findById(String isbn) {
        final Optional<BooksEntity> foundBook = booksRepository.findById(isbn);
        return foundBook.map(book -> bookEntityToBook(book));
    }

    @Override
    public List<Book> listBooks() {
        final List<BooksEntity> foundBooks = booksRepository.findAll();
        return foundBooks.stream().map(book -> bookEntityToBook(book)).collect(Collectors.toList());
    }

    @Override
    public void deleteBookById(String isbn) {
        try{
            booksRepository.deleteById(isbn);
        } catch (final EmptyResultDataAccessException e){
            log.debug("Attempted to delete non existing book "+e);
        }
    }

    private BooksEntity bookToBookEntity(Book book){
        return BooksEntity.builder().isbn(book.getIsbn()).title(book.getTitle()).author(book.getAuthor()).build();
    }

    private Book bookEntityToBook(BooksEntity booksEntity){
        return Book.builder().isbn(booksEntity.getIsbn()).title(booksEntity.getTitle()).author(booksEntity.getAuthor()).build();
    }
}
