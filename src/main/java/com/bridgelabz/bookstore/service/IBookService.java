package com.bridgelabz.bookstore.service;

import java.util.List;

import com.bridgelabz.bookstore.DTO.BookDTO;
import com.bridgelabz.bookstore.model.BookModel;

public interface IBookService {
	
	void addBook(String token, BookModel book);
	
	void removeBook(String token, String bookName);
	
	BookDTO updateBookDetails(String token, BookModel book);
	
	List<BookDTO> fetchAllBooks();
	
}
