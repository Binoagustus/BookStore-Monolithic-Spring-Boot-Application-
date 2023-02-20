package com.bridgelabz.bookstore.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BookModel {

	@Id
	private int bookId;
	private String bookName;
	private String bookAuthor;
	private int quantity;
	private float bookPrice;

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void increaseQuantityOfBooks(int quantity) {
		this.quantity = this.quantity + quantity;
	}

	public void decreaseQuantityOfBooks(int quantity) {
		if (this.quantity > 1) {
			this.quantity = this.quantity - quantity;
		}
	}

	public float getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(float bookPrice) {
		this.bookPrice = bookPrice;
	}

}
