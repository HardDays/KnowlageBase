package ru.knowledgebase.usermodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongPasswordException;

import static org.junit.Assert.*;

/**
 * Created by vova on 18.08.16.
 */
public class AuthorizeControllerTest {
    @BeforeClass
    public static void createUser() throws Exception{
        RegisterController.register("testuser1", "testuser1");
    }

    @Test
    public void testAuthorize() throws Exception{
        AuthorizeController.authorize("testuser1", "testuser1");
    }

    @Test
    public void testAuthorize2() throws Exception{
        AuthorizeController.authorizeLdap("testuser1", "testuser1");
    }

    @Test(expected = UserNotFoundException.class)
    public void testAuthorizeWrongUser() throws Exception{
        AuthorizeController.authorize("testuser123", "testuser1");
    }

    @Test(expected = UserNotFoundException.class)
    public void testAuthorizeWrongUser2() throws Exception{
        AuthorizeController.authorizeLdap("testuser123", "testuser1");
    }

    @Test(expected = WrongPasswordException.class)
    public void testAuthorizeWrongPass() throws Exception{
        AuthorizeController.authorize("testuser1", "testuser123");
    }

    @Test(expected = WrongPasswordException.class)
    public void testAuthorizeWrongPass2() throws Exception{
        AuthorizeController.authorizeLdap("testuser1", "testuser123");
    }

    @AfterClass
    public static void deleteUser() throws Exception{
        UserDeleteController.delete("testuser1");
    }


}