package com.bridgelabz.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Integer>{

	@Query(value = "SELECT * FROM bookstoresystem.order_model where cart_cart_id= :cartId", nativeQuery = true)
	Optional<OrderModel> findByCartId(long cartId);
	
	@Query(value = "SELECT * FROM bookstoresystem.order_model where cart_cart_id= :cartId", nativeQuery = true)
	List<OrderModel> findAllByCartId(long cartId);
}
