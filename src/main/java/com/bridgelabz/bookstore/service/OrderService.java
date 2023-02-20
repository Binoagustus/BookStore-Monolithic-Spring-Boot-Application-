package com.bridgelabz.bookstore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.DTO.BookDTO;
import com.bridgelabz.bookstore.DTO.OrderDTO;
import com.bridgelabz.bookstore.DTO.UserOrderView;
import com.bridgelabz.bookstore.DTO.ViewCart;
import com.bridgelabz.bookstore.exceptions.BookStoreException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.CartAndBooksModel;
import com.bridgelabz.bookstore.model.OrderModel;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.repository.BookRepository;
import com.bridgelabz.bookstore.repository.CartAndBookRepository;
import com.bridgelabz.bookstore.repository.CartRepository;
import com.bridgelabz.bookstore.repository.OrderRepository;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.utility.JWTUtils;

@Service
public class OrderService implements IOrderService {

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	JWTUtils jwtUtils;

	@Autowired
	UserRepository userRepo;

	@Autowired
	CartRepository cartRepo;

	@Autowired
	BookRepository bookRepo;

	@Autowired
	CartAndBookRepository cartBookRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public OrderModel placeOrder(String token, OrderDTO orderDTO) {
		Optional<UserModel> user = getUserByToken(token);

		if (user.get().isLogin()) {
			long cartId = user.get().getCart().getCartId();
			List<CartAndBooksModel> cartBook = cartBookRepo.findByCartId(cartId);
			
			if (cartBook.size() == 0) {
				throw new BookStoreException("Cart is empty");
			} else {
				double price = 0;
				int quantity = 0;
				for (int i = 0; i < cartBook.size(); i++) {
					price = price + (double) cartBook.get(i).getPrice();
					quantity = quantity + cartBook.get(i).getQuantity();
					System.out.println(cartBook.get(i).getPrice());
				}
				OrderModel order = modelMapper.map(orderDTO, OrderModel.class);
				order.setTotalPrice(price);
				order.setCart(user.get().getCart());
				order.setTotalQuantity(quantity);
				orderRepo.save(order);

				/* Decrease Quantity in inventory */
				for (int i = 0; i < cartBook.size(); i++) {

					int userQuantity = cartBookRepo.findQuantityByCartIdAndBookId(cartId,
							cartBook.get(i).getBooks().getBookId());
					Optional<BookModel> book = bookRepo.findById(cartBook.get(i).getBooks().getBookId());
					int totalBookQuantity = book.get().getQuantity();
					totalBookQuantity = totalBookQuantity - userQuantity;
					book.get().setQuantity(totalBookQuantity);
					bookRepo.save(book.get());
				}
				return order;
			}
		} else
			throw new BookStoreException("User is not logged in");
	}

	@Override
	public List<UserOrderView> getOrders(String token) {
		Optional<UserModel> user = getUserByToken(token);

		if (user.get().isLogin()) {
			long cartId = user.get().getCart().getCartId();
			List<OrderModel> orderModel = orderRepo.findAllByCartId(cartId);
			List<CartAndBooksModel> cartBook = cartBookRepo.findByCartId(cartId);
//			orderModel.stream().forEach(order -> System.out.println(order.toString()));
			List<UserOrderView> orderList = new ArrayList<>();	
			
			for(int i = 0; i < orderModel.size(); i++) {
				List<ViewCart> listView = new ArrayList<>();
				
				for (int j = 0; j < cartBook.size(); j++) {
					ViewCart cartview = new ViewCart();
					cartview.setBook((modelMapper.map(cartBook.get(j).getBooks(), BookDTO.class)));
					cartview.setQuantity(cartBook.get(j).getQuantity());
					listView.add(cartview);
					System.out.println(listView.toString());
				}
				UserOrderView userOrderView = new UserOrderView();
				userOrderView.setAddress(orderModel.get(i).getAddress());
				userOrderView.setViewBooks(listView);
				userOrderView.setName(orderModel.get(i).getUserName());
				userOrderView.setOrderId(orderModel.get(i).getOrderId());
				orderList.add(userOrderView);
				System.out.println(userOrderView.toString());
			}
			orderList.stream().forEach(order -> System.out.println("order List" + order.toString()));
			return orderList;
		} else
			throw new BookStoreException("User is not logged in");
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
