package ru.knowledgebase.usermodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongPasswordException;

/**
 * Created by vova on 18.08.16.
 */
public class UserControllerTest {
    @BeforeClass
    public static void createUser() throws Exception{
        UserController.register("testuser1", "testuser1");
    }

    @Test
    public void testAuthorize() throws Exception{
        UserController.authorize("testuser1", "testuser1");
    }

    @Test
    public void testAuthorize2() throws Exception{
        UserController.authorizeLdap("testuser1", "testuser1");
    }

    @Test(expected = UserNotFoundException.class)
    public void testAuthorizeWrongUser() throws Exception{
        UserController.authorize("testuser123", "testuser1");
    }

    @Test(expected = UserNotFoundException.class)
    public void testAuthorizeWrongUser2() throws Exception{
        UserController.authorizeLdap("testuser123", "testuser1");
    }

    @Test(expected = WrongPasswordException.class)
    public void testAuthorizeWrongPass() throws Exception{
        UserController.authorize("testuser1", "testuser123");
    }

    @Test(expected = WrongPasswordException.class)
    public void testAuthorizeWrongPass2() throws Exception{
        UserController.authorizeLdap("testuser1", "testuser123");
    }

    @AfterClass
    public static void deleteUser() throws Exception{
        UserController.delete("testuser1");
    }


}