package com.bridgelabz.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.CartModel;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long>{

}
