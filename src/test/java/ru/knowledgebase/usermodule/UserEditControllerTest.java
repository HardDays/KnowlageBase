package ru.knowledgebase.usermodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;

import static org.junit.Assert.*;

/**
 * Created by vova on 18.08.16.
 */
public class UserEditControllerTest {
    @BeforeClass
    public static void createUser() throws Exception{
        RegisterController.register("testuser1", "testuser1");
    }

    @Test
    public void editPass() throws Exception{
        UserEditController.changePassword("testuser1", "newpass");
        AuthorizeController.authorizeLdap("testuser1", "newpass");
    }

    @Test(expected = WrongUserDataException.class)
    public void editPassWrong() throws Exception{
        UserEditController.changePassword("testuser1", "");
    }

    @Test(expected = UserNotFoundException.class)
    public void editUserWrong() throws Exception{
        UserEditController.changePassword("testuser123", "pass");
    }

    @AfterClass
    public static void deleteUser() throws Exception{
        UserDeleteController.delete("testuser1");
    }
}