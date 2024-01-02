package com.devtiro.books;

import com.devtiro.books.domain.Book;
import com.devtiro.books.domain.BooksEntity;

public final class TestData {

    private TestData(){}

    public static Book testBook(){
        return Book.builder().isbn("90237429").title("Test Book").author("Test Author").build();
    }

    public static BooksEntity testBookEntity(){
        return BooksEntity.builder().isbn("90237429").title("Test Book").author("Test Author").build();
    }

}
