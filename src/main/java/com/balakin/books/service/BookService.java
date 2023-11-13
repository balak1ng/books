package com.balakin.books.service;

import com.balakin.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Book update(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();

    void deleteBookById(Long id);

    Optional<Book> findByAuthorAndTitle(String author, String title);
}
