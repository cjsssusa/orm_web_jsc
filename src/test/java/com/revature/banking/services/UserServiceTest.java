package com.revature.banking.services;

import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.AppUser;
import com.revature.banking.orm.OrmMain;
import com.revature.banking.orm.models.AppUserORM;
import com.revature.banking.orm.models.BankAccountORM;
import com.revature.banking.orm.utils.CrudORM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserService sut;
    BankService but;
    AppUserORM mockUserORM;
    BankAccountORM mockBankAccountORM;
    CrudORM mockCrudORM;
    OrmMain mockORMMain;
    UserService mockUserService;


    @Before
    public void testCaseSetup() {
        mockCrudORM = mock(CrudORM.class);
        mockUserService = mock(UserService.class);
        mockUserORM = mock(AppUserORM.class);
        mockBankAccountORM = mock(BankAccountORM.class);
        mockORMMain = mock(OrmMain.class);
        sut = new UserService(mockCrudORM);
        but = new BankService(sut, mockCrudORM);
    }

    @After
    public void testCaseCleanUp() {
        sut = null;
    }


    @Test
    public void test_isUserValid_returnsTrue_givenValidUser() {

        // AAA pattern: Arrange, Act, Assert

        // Arrange
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");

        // Act
        boolean actualResult = sut.isUserValid(validUser);

        // Assert
        Assert.assertTrue("Expected user to be considered valid", actualResult);

    }

    @Test
    public void test_isUserValid_returnsFalse_givenUserWithInvalidFirstName() {

        // Arrange
        AppUser invalidUser_1 = new AppUser(null, "valid", "valid", "valid", "valid");
        AppUser invalidUser_2 = new AppUser("", "valid", "valid", "valid", "valid");
        AppUser invalidUser_3 = new AppUser("             ", "valid", "valid", "valid", "valid");

        // Act
        boolean actualResult_1 = sut.isUserValid(invalidUser_1);
        boolean actualResult_2 = sut.isUserValid(invalidUser_2);
        boolean actualResult_3 = sut.isUserValid(invalidUser_3);

        // Assert
        Assert.assertFalse("Expected user to be considered false.", actualResult_1);
        Assert.assertFalse("Expected user to be considered false.", actualResult_2);
        Assert.assertFalse("Expected user to be considered false.", actualResult_3);

    }


    @Test
    public void test_registerNewUser_returnsTrue_givenValidUser() {

        // Arrange
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        List<AppUser> appUserList = new ArrayList<>();
        appUserList.add(validUser);
        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("username", "valid");
        whereOderBy.put("where", where);

        when(mockCrudORM.readTable(validUser, whereOderBy, AppUser.class)).thenReturn(null);
        when(mockCrudORM.insertTable(validUser)).thenReturn(validUser);

        // Act
        boolean actualResult = sut.registerNewUser(validUser);

        // Assert
        Assert.assertTrue("Expected result to be true with valid user provided.", actualResult);
        verify(mockCrudORM, times(1)).insertTable(validUser);

    }


    @Test(expected = InvalidRequestException.class)
    public void test_registerNewUser_throwsInvalidRequestException_givenInvalidUser() {
        // Arrange
        AppUser InvalidUser = new AppUser("  ", "valid", "valid", "valid", "valid");
        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("username", "valid");
        whereOderBy.put("where", where);

        when(mockCrudORM.readTable(InvalidUser, whereOderBy, AppUser.class)).thenReturn(null);
        when(mockCrudORM.insertTable(InvalidUser)).thenReturn(InvalidUser);
        // Act
        try {
            boolean actualResult = sut.registerNewUser(InvalidUser);
        } finally {
            // Assert
            verify(mockCrudORM, times(0)).insertTable(InvalidUser);
        }
    }


//    @Test
//    public void test_authenticateUser__returnsTrue_givenValidUsernameValidPassword() {
//        // Arrange
//        String ValidUsername = "valid", ValidEmail = "valid";
//
//        AppUser ValidUser = new AppUser("valid", "valid", "valid", "valid", "valid");
//
//        List<AppUser> appUserList = new ArrayList<>();
//        appUserList.add(ValidUser);
//
//        Map<String, Map<String, String>> whereOderBy = new HashMap<>();
//
//        Map<String, String> where = new HashMap<>();
//        where.put("username", "a");
//        where.put("email", "a");
//        whereOderBy.put("where", where);
//
//        when(mockCrudORM.readTable(ValidUser, whereOderBy, AppUser.class)).thenReturn(appUserList);
//        System.out.println(appUserList);
//
//        // Act
//        try {
//            sut.authenticateUser(ValidUsername, ValidEmail);
//        } finally {
//            // Assert
//        }
//    }

    @Test(expected = InvalidRequestException.class)
    public void test_authenticateUser_throwsInvalidRequestException_givenInvalidUsernameOrInvalidPassword() {
        // Arrange
        AppUser ValidUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        String invalidUsername = " ", invalidEmail = " ";
        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("username", "valid");
        whereOderBy.put("where", where);

        when(mockCrudORM.readTable(ValidUser, whereOderBy, AppUser.class)).thenReturn(null);

        // Act
        try {
            sut.authenticateUser(invalidUsername, invalidEmail);
        } finally {
            // Assert
        }
    }
}