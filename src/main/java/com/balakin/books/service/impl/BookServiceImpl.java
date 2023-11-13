package com.balakin.books.service.impl;

import com.balakin.books.model.Book;
import com.balakin.books.repository.BookRepository;
import com.balakin.books.service.BookService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        return new ArrayList<>(
                bookRepository.findAll()
        );
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (final EmptyResultDataAccessException ex) {
            log.debug("Attempted to delete non-existing book", ex);
        }
    }

    @Override
    @Transactional
    public Optional<Book> findByAuthorAndTitle(String author, String title) {
        return bookRepository.findByAuthorAndTitle(author, title);
    }
}
