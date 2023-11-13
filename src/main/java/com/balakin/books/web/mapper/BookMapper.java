package com.balakin.books.web.mapper;

import com.balakin.books.model.Book;
import com.balakin.books.web.dto.BookDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends Mappable<Book, BookDto> {
}
