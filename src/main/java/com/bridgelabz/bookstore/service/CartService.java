package com.bridgelabz.bookstore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.DTO.BookDTO;
import com.bridgelabz.bookstore.DTO.ViewCart;
import com.bridgelabz.bookstore.exceptions.BookStoreException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.CartAndBooksModel;
import com.bridgelabz.bookstore.model.CartModel;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.repository.BookRepository;
import com.bridgelabz.bookstore.repository.CartAndBookRepository;
import com.bridgelabz.bookstore.repository.CartRepository;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.utility.JWTUtils;

@Service
public class CartService implements ICartService {

	@Autowired
	JWTUtils jwtUtils;

	@Autowired
	UserRepository userRepo;

	@Autowired
	BookRepository bookRepo;

	@Autowired
	CartRepository cartRepo;
	
	@Autowired
	CartAndBookRepository cartBookRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public CartAndBooksModel addBooksToCart(String token, String bookName) {
		Optional<UserModel> user = getUserByToken(token);
		Optional<BookModel> book = bookRepo.findByBookName(bookName);

		if (user.get().isLogin()) {
			if (book.isPresent() && book.get().getQuantity() >= 1) {
				CartModel cart = user.get().getCart();
				if(cartBookRepo.findByCartIdAndBookId(cart.getCartId(), book.get().getBookId()).isPresent()) {
					throw new BookStoreException("Book is already available in the cart");
				} else {
					CartAndBooksModel cartAndBook = new CartAndBooksModel();
					cartAndBook.setQuantity(1);
					cartAndBook.setBooks(book.get());
					cartAndBook.setCart(cart);
					cartAndBook.setPrice(book.get().getBookPrice());
					return cartBookRepo.save(cartAndBook);
				}
			} else
				throw new BookStoreException("Book Out Of Stock");
		} else
			throw new BookStoreException("Login first");
	}

	@Override
	public void removeBooksFromCart(String token, int bookId) {
		Optional<UserModel> user = getUserByToken(token);

		if (user.get().isLogin()) {
			CartModel cart = user.get().getCart();
			Optional<CartAndBooksModel> cartBook = cartBookRepo.findByCartIdAndBookId(cart.getCartId(), bookId);
			if (cartBook.isPresent()) {
				cartBookRepo.deleteById(cartBook.get().getCartBookId());
			} else
				throw new BookStoreException("Book is not in your cart");

		} else
			throw new BookStoreException("Login first");
	}
	

	@Override
	public String addQuantityOfBooks(String token, int increaseQuantity, int bookId) {
		Optional<UserModel> user = getUserByToken(token);
		if (user.get().isLogin()) {
			CartModel cart = user.get().getCart();
			Optional<CartAndBooksModel> cartBook = cartBookRepo.findByCartIdAndBookId(cart.getCartId(), bookId);
			if(cartBook.isPresent()) {
				int quantity = cartBook.get().getQuantity();
				quantity = quantity + increaseQuantity;
				cartBook.get().setQuantity(quantity);
				cartBookRepo.save(cartBook.get());
				return "Quantity added" + quantity;
			} else
				throw new BookStoreException("Book not in your cart");
		} else
			throw new BookStoreException("User is not loggeed in");
	}

	@Override
	public String subtractQuantityOfBooks(String token, int decreaseQuantity, int bookId) {
		Optional<UserModel> user = getUserByToken(token);
		if (user.get().isLogin()) {
			CartModel cart = user.get().getCart();
			Optional<CartAndBooksModel> cartBook = cartBookRepo.findByCartIdAndBookId(cart.getCartId(), bookId);
			if(cartBook.isPresent()) {
				int quantity = cartBook.get().getQuantity();
				if(quantity == 1 || quantity - decreaseQuantity == 0) {
					throw new BookStoreException("Quantity cannot be zero. Try deleting the book");
				} else {
					quantity = quantity - decreaseQuantity;
					return "Quantity decreased " + quantity;
				}
			} else
				throw new BookStoreException("Book not in your cart");
		} else
			throw new BookStoreException("User is not loggeed in");
	}

	private Optional<UserModel> getUserByToken(String token) {
		String email = jwtUtils.getEmailFromToken(token);
		Optional<UserModel> getByEmail = userRepo.findByEmail(email);
		Optional<UserModel> getByEmailAndPassword = userRepo.findByEmailAndPassword(email,
				getByEmail.get().getPassword());

		if (getByEmail.isPresent()) {
			if (getByEmailAndPassword.isPresent()) {
				return getByEmail;
			} else
				throw new BookStoreException("Password is incorrect");
		} else
			throw new BookStoreException("Email id not exists");
	}

	@Override
	public List<ViewCart> viewAllBooksFromCart(String token) {
		Optional<UserModel> user = getUserByToken(token);
		long cartId = user.get().getCart().getCartId();
		List<CartAndBooksModel> cartBook = cartBookRepo.findByCartId(cartId);
		List<ViewCart> listView = new ArrayList<>();
		
		for(int i = 0; i < cartBook.size(); i++) {
			ViewCart cartview = new ViewCart();
			cartview.setBook((modelMapper.map(cartBook.get(i).getBooks(), BookDTO.class))); ;
			cartview.setQuantity(cartBook.get(i).getQuantity());
			listView.add(cartview);
			System.out.println(cartview.toString());
		}
		return listView;
	}
}
