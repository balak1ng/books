package com.balakin.books.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "title")
    private String title;

    @Column(name = "num_pages")
    private int numPages;
}
