package com.bridgelabz.bookstore.DTO;

import java.util.List;

public class UserOrderView {

	private int orderId;
	private List<ViewCart> viewBooks;
	private String name;
	private String address;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public List<ViewCart> getViewBooks() {
		return viewBooks;
	}

	public void setViewBooks(List<ViewCart> viewBooks) {
		this.viewBooks = viewBooks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "[orderId=" + orderId + ", viewBooks=" + viewBooks + ", name=" + name + ", address="
				+ address + "]";
	}
}
