package com.backend.backend.assigment.tracking.service.tests;

import com.backend.backend.assigment.tracking.service.AccountService;
import com.backend.backend.assigment.tracking.service.entities.Account;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

public class AccountServiceTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(AccountService.class)
                .addClass(Account.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("config.yml", "../config.yml")
                .addAsManifestResource("META-INF/data/accounts.sql", "data/accounts.sql");
    }

    @Inject
    private AccountService accountService;

    @Test
    public void testAcountServiceInjectable() {
        Assert.assertNotNull(accountService);
    }

    @Test
    public void testGetAccountById() {
        Account acc1 = accountService.getAccount("0");
        Assert.assertNotNull(acc1);
        Assert.assertTrue(acc1.isActive());

        Account acc2 = accountService.getAccount("1");
        Assert.assertNotNull(acc2);
        Assert.assertFalse(acc2.isActive());
    }

    @Test
    public void testGetAccounts() {
        List<Account> accountList = accountService.getAccounts();
        Assert.assertNotNull(accountList);
        Assert.assertEquals(accountList.size(), 2);
    }

    @Test
    public void testSaveAccount() {
        Account account = new Account("3", "Test Account", false);
        this.accountService.saveAccount(account);

        Account savedAccount = this.accountService.getAccount("3");
        Assert.assertNotNull(savedAccount);
        Assert.assertEquals(account.getId(), savedAccount.getId());
        Assert.assertFalse(savedAccount.isActive());
    }

    @Test
    public void testDeleteAccount() {
        this.accountService.deleteAccount("2");

        Account deletedAccount = this.accountService.getAccount("2");
        Assert.assertNull(deletedAccount);
    }
}