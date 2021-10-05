package com.vkstech.saga.payment.repository;

import com.vkstech.saga.payment.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {
}
