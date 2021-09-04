package com.coderscampus.assignment13.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		
		model.put("user", new User());
		model.put("adddress", new Address());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		
		return "users";
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);		
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		return "users";
	}
	
	@PostMapping("/users/{userId}")
	public String postOneUser (User user) {
		Set<Account> accounts = accountService.getUserAccounts(user);
		user.setAccounts(accounts);
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
	@GetMapping("/users/{userId}/accounts")
	public String getUserAccounts (ModelMap model, @PathVariable Long userId, User user, Address address) {
		
		System.out.println(address);
		Account account = accountService.createNewAccount(user);
		Set<Account> accounts = user.getAccounts();
		user.setAccounts(accounts);
		user.setAddress(address);
		System.out.println(user.getAccounts().toString());
		model.put("address", address);
		model.put("accounts", accounts);
		model.put("account", account);
		model.put("user", user);
			
		return "redirect:/users/"+user.getUserId() + "/accounts/" + account.getAccountId();
	}
	
	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getUserAccount (ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		
		User user = userService.findById(userId);	
		Account account = accountService.findById(accountId);
		Set<Account> accounts = accountService.getUserAccounts(user);
		Address address = user.getAddress();
		user.setAccounts(accounts);
		System.out.println(accountId);
		userService.saveUser(user);

		model.put("address", address);
		model.put("accounts", accounts);
		model.put("account", account);
		model.put("user", user);
		
		return "accounts";
	}
	
	@PostMapping("/users/{userId}/accounts")
	public String createNewBankAccount (User user, Address address, ModelMap model) {
		System.out.println(address);
		Account account = accountService.createNewAccount(user);
		Set<Account> accounts = user.getAccounts();
		user.setAccounts(accounts);
		user.setAddress(address);
		System.out.println(user.getAccounts().toString());
		userService.saveUser(user);
		model.put("account", account);
			
		return "redirect:/users/"+user.getUserId() + "/accounts/" + account.getAccountId();
	}
	
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String postUserAccount (User user, Account account, Address address) {
		System.out.println(address);
		Set<Account> accounts = user.getAccounts();
		accounts = accountService.updateAccount(accounts, account);
		user.setAccounts(accounts);
		user.setAddress(address);
		System.out.println(user.getAccounts().toString());
		userService.saveUser(user);
			
		return "redirect:/users/"+user.getUserId() + "/accounts/" + account.getAccountId();
	}
}
