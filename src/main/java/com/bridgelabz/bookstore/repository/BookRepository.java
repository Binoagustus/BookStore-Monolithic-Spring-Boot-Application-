package com.bridgelabz.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.BookModel;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Integer>{

	Optional<BookModel> deleteByBookName(String bookName);
	
	Optional<BookModel> findByBookName(String bookName);
}
