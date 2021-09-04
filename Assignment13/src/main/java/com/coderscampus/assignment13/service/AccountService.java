package com.coderscampus.assignment13.service;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private UserRepository userRepo;

	public Set<Account> getUserAccounts(User user) {
		return accountRepo.findByUsersIn(Arrays.asList(user));
	}

	public Account findById(Long accountId) {
		return accountRepo.findAccountById(accountId);
	}

	public Account createNewAccount(User user) {
		Set<Account> userAccounts = getUserAccounts(user);
		Account account = new Account();
		account.setAccountId((long) userAccounts.size()+1);
		account.setAccountName("Account #" + account.getAccountId());
		account.getUsers().add(user);
		userAccounts.add(account);
		user.setAccounts(userAccounts);
		user.getAccounts().add(account);
		accountRepo.saveAll(userAccounts);
		return account;
	}

	public Set<Account> updateAccount(Set<Account> accounts, Account account) {
		
		for (Account acc : accounts) {
			if (acc.getAccountId().equals(account.getAccountId())) {
				accounts.remove(acc);
				accounts.add(account);
				accountRepo.saveAll(accounts);
				return accounts;
			}
		}
		accounts.add(account);
		accountRepo.saveAll(accounts);
		userRepo.saveAll(account.getUsers());
		return accounts;
	}
}
