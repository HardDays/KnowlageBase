package ru.knowledgebase.usermodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;

/**
 * Created by vova on 18.08.16.
 */
public class UserEditControllerTest {
    User user = null;
    @BeforeClass
    public static void createUser() throws Exception{
        UserController.register("testuser1", "testuser1");
    }

    @Test
    public void editPass() throws Exception{
        UserController.changePassword("testuser1", "newpass");
        UserController.authorizeLdap("testuser1", "newpass");
    }

    @Test(expected = WrongUserDataException.class)
    public void editPassWrong() throws Exception{
        UserController.changePassword("testuser1", "");
    }

    @Test(expected = UserNotFoundException.class)
    public void editUserWrong() throws Exception{
        UserController.changePassword("testuser123", "pass");
    }

    @AfterClass
    public static void deleteUser() throws Exception{
        UserController.delete("testuser1");
    }
}