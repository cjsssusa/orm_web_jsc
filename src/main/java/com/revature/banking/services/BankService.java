package com.revature.banking.services;

import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.orm.utils.CrudORM;

import java.util.Map;
import java.util.UUID;

public class BankService {

//    private final UserService userService;
    private final CrudORM crudORM;

    public BankService( UserService userService, CrudORM crudORM) {
//        this.userService = userService;
        this.crudORM = crudORM;
    }

//    public boolean isBankAccountValid(BankAccount bankAccount) {
//        if (bankAccount == null) return false;
//        if (bankAccount.getAccount_name() == null || bankAccount.getAccount_name().trim().equals("")) return false;
//        if (bankAccount.getAccount_type() == null || bankAccount.getAccount_type().trim().equals("")) return false;
//        return true;
//    }

    public boolean openBankAccount(BankAccount bankAccount) {
        bankAccount.setBank_account_id(UUID.randomUUID().toString());
        BankAccount registeredBankAccount = crudORM.insertTable(bankAccount);

        if (registeredBankAccount == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return true;
    }

    public boolean update(BankAccount bankAccount, Map<String, Map<String, String>> whereOderBy) {

        bankAccount = crudORM.updateTable(bankAccount, whereOderBy);
        System.out.println(bankAccount);

        if (bankAccount == null) {
            throw new ResourcePersistenceException("The transaction could not be persisted to the datasource!");
        }

        return true;
    }

    public boolean delete(BankAccount bankAccount, Map<String, Map<String, String>> whereOderBy) {

        bankAccount = crudORM.deletTable(bankAccount, whereOderBy);
        System.out.println(bankAccount);

        if (bankAccount == null) {
            throw new ResourcePersistenceException("The transaction could not be persisted to the datasource!");
        }

        return true;
    }
}
