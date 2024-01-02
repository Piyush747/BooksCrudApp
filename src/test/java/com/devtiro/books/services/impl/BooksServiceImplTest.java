package com.devtiro.books.services.impl;

import com.devtiro.books.TestData;
import com.devtiro.books.domain.Book;
import com.devtiro.books.domain.BooksEntity;
import com.devtiro.books.repositories.BooksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BooksServiceImplTest {
    @Mock
    private BooksRepository booksRepository;
    @InjectMocks
    private BooksServiceImpl underTest;

    @Test
    public void testThatBookIsSaved(){

        final Book book = TestData.testBook();

        final BooksEntity booksEntity = TestData.testBookEntity();

        when(booksRepository.save(eq(booksEntity))).thenReturn(booksEntity);

        final Book result = underTest.save(book);
        assertEquals(book, result);

    }

    @Test
    public void testThatFindByIdReturnsEmptyWhenNoBook(){
        final String isbn = "123445678";
        when(booksRepository.findById(eq(isbn))).thenReturn(Optional.empty());

        final Optional<Book> book = underTest.findById(isbn);

        assertEquals(Optional.empty(),book);
    }

    @Test
    public void testThatFindByIdReturnsBookWhenPresent(){
        final Book book = TestData.testBook();
        final BooksEntity booksEntity = TestData.testBookEntity();
        when(booksRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(booksEntity));

        final Optional<Book> result = underTest.findById(book.getIsbn());

        assertEquals(Optional.of(book),result);
    }

    @Test
    public void testThatListBooksReturnsEmptyListWhenNoBooksFound(){
        when(booksRepository.findAll()).thenReturn(new ArrayList<>());
        final List<Book> result = underTest.listBooks();
        assertEquals(0,result.size());
    }

    @Test
    public void testThatListBooksReturnsBookListWhenBooksFound(){
        final BooksEntity booksEntity = TestData.testBookEntity();
        when(booksRepository.findAll()).thenReturn(List.of(booksEntity));
        final List<Book> result = underTest.listBooks();
        assertEquals(1,result.size());
    }

    @Test
    public void testThatBooksExistsReturnsFalseWhenBookNotExists(){
        when(booksRepository.existsById(any())).thenReturn(false);

        assertEquals(false, underTest.isBookExists(TestData.testBook()));
    }

    @Test
    public void testThatBooksExistsReturnsTrueWhenBookExists(){
        when(booksRepository.existsById(any())).thenReturn(true);

        assertEquals(true, underTest.isBookExists(TestData.testBook()));
    }

    @Test
    public void testThatBookIsDeletedIfExists(){
        String isbn = "12345678";

        underTest.deleteBookById(isbn);
        verify(booksRepository,times(1)).deleteById(isbn);
    }

}
