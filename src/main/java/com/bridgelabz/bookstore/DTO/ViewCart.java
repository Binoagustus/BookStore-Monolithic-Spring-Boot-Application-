package com.bridgelabz.bookstore.DTO;

public class ViewCart {

	private BookDTO book;
	private int quantity;

	public BookDTO getBook() {
		return book;
	}

	public void setBook(BookDTO book) {
		this.book = book;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "[book=" + book + ", quantity=" + quantity + "]";
	}
}
