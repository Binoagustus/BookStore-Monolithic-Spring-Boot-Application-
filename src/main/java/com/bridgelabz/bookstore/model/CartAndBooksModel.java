package com.bridgelabz.bookstore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CartAndBooksModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartBookId;

	@ManyToOne
	private BookModel books;

	@ManyToOne
	private CartModel cart;

	private int quantity;
	
	private float price;

	public int getCartBookId() {
		return cartBookId;
	}

	public void setId(int cartBookId) {
		this.cartBookId = cartBookId;
	}

	public BookModel getBooks() {
		return books;
	}

	public void setBooks(BookModel books) {
		this.books = books;
	}

	public CartModel getCart() {
		return cart;
	}

	public void setCart(CartModel cart) {
		this.cart = cart;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
