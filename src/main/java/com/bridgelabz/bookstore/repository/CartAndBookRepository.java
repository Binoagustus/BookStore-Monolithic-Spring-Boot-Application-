package com.bridgelabz.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.CartAndBooksModel;

@Repository
public interface CartAndBookRepository extends JpaRepository<CartAndBooksModel, Integer>{

	@Query(value = "select * from bookstoresystem.cart_and_books_model where cart_cart_id= :cartId and books_book_id= :bookId", nativeQuery = true)
	Optional<CartAndBooksModel> findByCartIdAndBookId(long cartId, int bookId);
	
	@Query(value = "SELECT * FROM bookstoresystem.cart_and_books_model where cart_cart_id= :cartId", nativeQuery = true)
	List<CartAndBooksModel> findByCartId(long cartId);

	@Query(value = "select quantity from bookstoresystem.cart_and_books_model where cart_cart_id= :cartId and books_book_id= :bookId", nativeQuery = true)
	int findQuantityByCartIdAndBookId(long cartId, int bookId);
}
