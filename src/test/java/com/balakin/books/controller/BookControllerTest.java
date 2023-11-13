package com.balakin.books.controller;

import com.balakin.books.model.Book;
import com.balakin.books.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private List<Book> getBooks() {
        return List.of(
                Book
                        .builder()
                        .author("Edgar Allan Po")
                        .title("Black cat")
                        .numPages(200)
                        .build(),
                Book
                        .builder()
                        .author("Lev Tolstoy")
                        .title("Voina i mir. Tom 1")
                        .numPages(400)
                        .build());

    }

    @Test
    public void testPostInsertNewBookReturnsHTTP201() throws Exception {
        Book testBook = getBooks().get(0);

        Mockito.when(bookService.findByAuthorAndTitle(eq(testBook.getAuthor()), eq(testBook.getTitle())))
                .thenReturn(Optional.empty());

        Mockito.when(bookService.save(any(Book.class)))
                .thenReturn(testBook);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/books")
                        .content(new ObjectMapper().writeValueAsString(testBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.numPages").value(testBook.getNumPages()));
    }

    @Test
    public void testPostInsertExistingByAuthorAndNameBookReturnsHTTP302() throws Exception {
        Book testBook = getBooks().get(0);

        Mockito.when(bookService.findByAuthorAndTitle(eq(testBook.getAuthor()), eq(testBook.getTitle())))
                .thenReturn(Optional.of(testBook));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/books")
                        .content(new ObjectMapper().writeValueAsString(testBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.numPages").value(testBook.getNumPages()));
    }

    @Test
    public void testPutUpdateExistingBookReturnsHTTP200() throws Exception {
        Book testBook = getBooks().get(0);
        long id = 100L;
        String newTitle = "White cat";

        testBook.setTitle(newTitle);

        Mockito.when(bookService.findByAuthorAndTitle(eq(testBook.getAuthor()),
                        eq(testBook.getTitle())))
                .thenReturn(Optional.empty());

        Mockito.when(bookService.update(eq(testBook)))
                .thenReturn(testBook);

        Mockito.when(bookService.findById(eq(id)))
                .thenReturn(Optional.of(testBook));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/books/" + id)
                        .content(new ObjectMapper().writeValueAsString(testBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.numPages").value(testBook.getNumPages()));
    }

    @Test
    public void testPutUpdateNotExistingByIdBookReturnsHTTP404() throws Exception {
        Book testBook = getBooks().get(0);
        long id = 100L;
        String newTitle = "White cat";

        testBook.setTitle(newTitle);

        Mockito.when(bookService.findById(eq(id)))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/books/" + id)
                        .content(new ObjectMapper().writeValueAsString(testBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPutUpdateExistingBookByAlreadyExistingAuthorAndTitleReturnsHTTP302() throws Exception {
        Book testBook = getBooks().get(0);
        long id = 100L;
        String newTitle = "White cat";

        testBook.setTitle(newTitle);

        Mockito.when(bookService.findById(eq(id)))
                .thenReturn(Optional.of(testBook));

        Mockito.when(bookService.findByAuthorAndTitle(eq(testBook.getAuthor()), eq(testBook.getTitle())))
                .thenReturn(Optional.of(testBook));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/books/" + id)
                        .content(new ObjectMapper().writeValueAsString(testBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.numPages").value(testBook.getNumPages()));
    }

    @Test
    void testGetAllBooksReturnsHTTP200() throws Exception {
        Mockito.when(bookService.findAll()).thenReturn(getBooks());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetExistingBookReturnsHTTP200() throws Exception {
        Book testBook = getBooks().get(0);
        long id = 100L;

        Mockito.when(bookService.findById(eq(id))).thenReturn(Optional.of(testBook));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.numPages").value(testBook.getNumPages()));
    }

    @Test
    public void testGetNotExistingBookReturnsHTTP404() throws Exception {
        long id = 100L;

        Mockito.when(bookService.findById(eq(id))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletingExistingBookReturnsHTTP204() throws Exception {
        Book testBook = getBooks().get(0);
        long id = 100L;

        Mockito.when(bookService.findById(eq(id))).thenReturn(Optional.of(testBook));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/books/" + id))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBookById(eq(id));
    }
}
