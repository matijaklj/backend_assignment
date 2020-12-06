package com.backend.backend.assigment.tracking.service;

import com.backend.backend.assigment.tracking.service.entities.Account;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class AccountService {

    @PersistenceContext
    private EntityManager em;

    public Account getAccount(String accountId) {
        return em.find(Account.class, accountId);
    }

    public List<Account> getAccounts() {
        List<Account> accounts = em
                .createNamedQuery("Account.findAccounts", Account.class)
                .getResultList();

        return accounts;
    }

    @Transactional
    public void saveAccount(Account account) {
        if (account != null) {
            em.persist(account);
        }

    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteAccount(String accountId) {
        Account account = em.find(Account.class, accountId);
        if (account != null) {
            em.remove(account);
        }
    }
}