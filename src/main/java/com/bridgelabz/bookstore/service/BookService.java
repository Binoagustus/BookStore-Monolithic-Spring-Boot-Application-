package com.bridgelabz.bookstore.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.DTO.BookDTO;
import com.bridgelabz.bookstore.exceptions.BookStoreException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.repository.BookRepository;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.utility.JWTUtils;

@Service
public class BookService implements IBookService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	BookRepository bookRepo;

	@Autowired
	JWTUtils jwtUtils;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public void addBook(String token, BookModel book) {
		Optional<UserModel> user = getUserByToken(token);

		if (user.get().getRole().equals("admin")) {

			if (bookRepo.findByBookName(book.getBookName()).isPresent()) {
				throw new BookStoreException("Book Already exists");
			} else
				bookRepo.save(book);

		} else
			throw new BookStoreException("You are not admin");
	}

	@Override
	public void removeBook(String token, String bookName) {
		Optional<UserModel> user = getUserByToken(token);

		if (user.get().getRole().equals("admin")) {

			if (bookRepo.findByBookName(bookName).isPresent()) {
				bookRepo.deleteByBookName(bookName);
			} else
				throw new BookStoreException("Book Already exists");

		} else
			throw new BookStoreException("You are not Admin");
	}

	@Override
	public BookDTO updateBookDetails(String token, BookModel book) {

		Optional<UserModel> user = getUserByToken(token);

		if (user.get().getRole().equals("admin")) {

			Optional<BookModel> foundBook = bookRepo.findByBookName(book.getBookName());
			if (foundBook.isPresent()) {
				if (book.getBookAuthor() == null) {
					foundBook.get().setBookAuthor(foundBook.get().getBookAuthor());
				} else
					foundBook.get().setBookAuthor(book.getBookAuthor());

				if (book.getBookId() == 0) {
					foundBook.get().setBookId(foundBook.get().getBookId());
				} else
					foundBook.get().setBookId(book.getBookId());

				if (book.getBookName() == null) {
					foundBook.get().setBookName(foundBook.get().getBookName());
				} else
					foundBook.get().setBookName(book.getBookName());

				if (book.getBookPrice() == 0) {
					foundBook.get().setBookPrice(foundBook.get().getBookPrice());
				} else
					foundBook.get().setBookPrice(book.getBookPrice());

				if (book.getQuantity() == 0) {
					foundBook.get().setQuantity(foundBook.get().getQuantity());
				} else
					foundBook.get().setQuantity(book.getQuantity());

				bookRepo.save(foundBook.get());
				return modelMapper.map(foundBook.get(), BookDTO.class);
			} else
				throw new BookStoreException("Book doesn't exist");

		} else
			throw new BookStoreException("You are not Admin");
	}

	@Override
	public List<BookDTO> fetchAllBooks() {
		List<BookModel> books = bookRepo.findAll();
		List<BookDTO> bookDTO = books.stream().map(book -> modelMapper.map(book, BookDTO.class))
				.collect(Collectors.toList());
		return bookDTO;
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
}
