package com.devtiro.books.controllers;

import com.devtiro.books.TestData;
import com.devtiro.books.domain.Book;
import com.devtiro.books.services.BooksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BooksControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BooksService booksService;

    @Test
    public void testThatBookIsCreated() throws Exception {
        final Book book = TestData.testBook();

        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/"+book.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatBookIsUpdated() throws Exception {
        final Book book = TestData.testBook();
        booksService.save(book);

        book.setAuthor("updated author name");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/"+book.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("updated author name"));
    }


    @Test
    public void testThatRetrieveBookReturns404WhenBookNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/12345678")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveBookReturnsBookWhenFound() throws Exception {
        final Book book = TestData.testBook();
        booksService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/"+book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatListBooksReturns200WhenNoBooksFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListBooksReturnsEmptyResponseWhenNoBooksFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testThatListBooksReturnsBooksWhenFound() throws Exception {
        Book book = TestData.testBook();
        booksService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(book.getAuthor()));
    }

    @Test
    public void testThatDeleteBookReturns204WhenBookNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/533242412")).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteBookDeletesBookWhenExists() throws Exception {
        Book book = TestData.testBook();
        booksService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+book.getIsbn())).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
