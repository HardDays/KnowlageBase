package ru.knowledgebase.usermodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;

import static org.junit.Assert.*;

/**
 * Created by vova on 18.08.16.
 */
public class UserDeleteControllerTest {
    @BeforeClass
    public static void createUser() throws Exception{
        RegisterController.register("testuser1", "testuser1");
    }

    @Test(expected = UserNotFoundException.class)
    public void editPass() throws Exception{
        UserDeleteController.delete("testuser123");
    }

    @AfterClass
    public static void deleteUser() throws Exception{
        UserDeleteController.delete("testuser1");
    }
}