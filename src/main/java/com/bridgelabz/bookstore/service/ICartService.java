package com.bridgelabz.bookstore.service;

import java.util.List;

import com.bridgelabz.bookstore.DTO.BookDTO;
import com.bridgelabz.bookstore.DTO.ViewCart;
import com.bridgelabz.bookstore.model.CartAndBooksModel;

public interface ICartService {

	CartAndBooksModel addBooksToCart(String token, String bookName);

	void removeBooksFromCart(String token, int bookId);

	String addQuantityOfBooks(String token, int quantity, int bookId);

	String subtractQuantityOfBooks(String token, int decreaseQuantity, int bookId);

	List<ViewCart> viewAllBooksFromCart(String token);
}
