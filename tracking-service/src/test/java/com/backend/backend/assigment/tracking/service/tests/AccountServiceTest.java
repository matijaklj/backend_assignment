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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        Account acc1 = accountService.getAccount("test-0");
        Assert.assertNotNull(acc1);
        Assert.assertTrue(acc1.isActive());

        Account acc2 = accountService.getAccount("test-1");
        Assert.assertNotNull(acc2);
        Assert.assertFalse(acc2.isActive());
    }

    @Test
    public void testGetAccounts() {
        List<Account> accountList = accountService.getAccounts();
        Assert.assertNotNull(accountList);
        Assert.assertEquals(accountList.size(), 3);
    }

    @Test
    public void testSaveAccount() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = formatter.format(new Date());
        Account account = new Account("test-" + date, "Test Account", false);
        this.accountService.saveAccount(account);

        Account savedAccount = this.accountService.getAccount("test-" + date);
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
