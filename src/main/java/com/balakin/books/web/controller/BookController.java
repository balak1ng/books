package com.balakin.books.web.controller;

import com.balakin.books.model.Book;
import com.balakin.books.service.BookService;
import com.balakin.books.web.dto.BookDto;
import com.balakin.books.web.mapper.BookMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @PostMapping()
    public ResponseEntity<Book> createNewBook(@RequestBody @Validated final BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        Optional<Book> findByAuthorAndTitle = bookService.findByAuthorAndTitle(book.getAuthor(), book.getTitle());

        if (findByAuthorAndTitle.isPresent()) {
            return new ResponseEntity<>(findByAuthorAndTitle.get(), HttpStatus.FOUND);
        }

        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable final Long id,
                                               @RequestBody @Validated final BookDto bookDto) {
        Optional<Book> foundBookById = bookService.findById(id);

        if (foundBookById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Book newBook = bookMapper.toEntity(bookDto);

        Optional<Book> foundBookByAuthorAndTitle =
                bookService.findByAuthorAndTitle(newBook.getAuthor(), newBook.getTitle());

        if (foundBookByAuthorAndTitle.isPresent()) {
            return new ResponseEntity<>(foundBookByAuthorAndTitle.get(), HttpStatus.FOUND);
        }

        Book oldBook = foundBookById.get();
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setTitle(newBook.getTitle());
        oldBook.setNumPages(newBook.getNumPages());

        Book savedBook = bookService.update(oldBook);

        return new ResponseEntity<>(savedBook, HttpStatus.OK);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Book> getBook(@PathVariable final Long id) {
        final Optional<Book> foundBook = bookService.findById(id);
        return foundBook
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Book>> listBooks() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable final Long id) {
        Optional<Book> foundBookById = bookService.findById(id);

        if (foundBookById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
