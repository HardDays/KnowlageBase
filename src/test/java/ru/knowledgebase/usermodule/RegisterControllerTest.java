package ru.knowledgebase.usermodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;

import static org.junit.Assert.*;

/**
 * Created by vova on 18.08.16.
 */
public class RegisterControllerTest {
    @BeforeClass
    public static void createUser() throws Exception{
        UserController.register("testuser1", "testuser1");
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createExistsUser() throws Exception{
        UserController.register("testuser1", "testuser1");
    }

    @Test(expected = WrongUserDataException.class)
    public void createUserWrong() throws Exception{
        UserController.register("", "testuser1");
    }

    @Test(expected = WrongUserDataException.class)
    public void createUserWrong2() throws Exception{
        UserController.register("ttttt", "");
    }

    @AfterClass
    public static void deleteUser() throws Exception{
        UserController.delete("testuser1");
    }
}