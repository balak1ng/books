package com.balakin.books.service;

import com.balakin.books.model.Book;
import com.balakin.books.repository.BookRepository;
import com.balakin.books.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook() {
        return Book.builder()
                .author("Edgar Allan Po")
                .title("Black cat")
                .numPages(200)
                .build();
    }

    @Test
    public void testSavingBook() {
        final Book book = testBook();
        bookService.save(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdatingBook() {
        final Book book = testBook();
        bookService.update(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testFindExistingBook() {
        final Book book = testBook();

        when(bookRepository.findById(eq(book.getId()))).thenReturn(Optional.of(book));

        final Optional<Book> result = bookService.findById(book.getId());
        assertEquals(Optional.of(book), result);
    }


    @Test
    public void testFindNotExistingBook() {
        final Book book = testBook();
        when(bookRepository.findById(eq(book.getId()))).thenReturn(Optional.empty());

        final Optional<Book> result = bookService.findById(book.getId());
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testReturnEmptyListIfNoBooks() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        final List<Book> result = bookService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    public void testReturnListIfBooksExist() {
        final Book book = testBook();
        when(bookRepository.findAll()).thenReturn(List.of(book));
        final List<Book> result = bookService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteExistingBook() {
        final Book book = testBook();
        bookService.deleteBookById(book.getId());
        verify(bookRepository, times(1)).deleteById(eq(book.getId()));
    }

    @Test
    public void testFindBookByAuthorAndTitle() {
        final Book book = testBook();
        when(bookRepository.findByAuthorAndTitle(eq(book.getAuthor()), eq(book.getTitle())))
                .thenReturn(Optional.of(book));
        Optional<Book> foundBook = bookService.findByAuthorAndTitle(book.getAuthor(), book.getTitle());
        assertTrue(foundBook.isPresent());
        assertEquals(foundBook.get(), book);
    }

}
