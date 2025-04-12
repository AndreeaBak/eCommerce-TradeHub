package com.ase.service;

import com.ase.model.Order;
import com.ase.model.Seller;
import com.ase.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySeller(Seller seller);
    List<Transaction>getAllTransactions();
}
