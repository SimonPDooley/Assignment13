package com.coderscampus.assignment13.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

		Set<Account> findByUsersIn(List<User> users);

		@Query("select a from Account a where accountId = :accountId")
		Account findAccountById(Long accountId);
}
