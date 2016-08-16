package ru.knowledgebase.authorizemodule.ldap;

import org.junit.*;

/**
 * Created by vova on 15.08.16.
 */
public class LdapControllerTest{

    @BeforeClass
    public static void createTestUser() throws Exception{
        LdapController.getInstance().createUser("testuser1", "testuser1", "User");
    }

    @Test
    public void authorize() throws Exception {
        LdapController.getInstance().authorize("testuser1", "testuser1");
    }

    @Test(expected = WrongUsernameException.class)
    public void authorizeWrongUser() throws Exception {
        LdapController.getInstance().authorize("testuser2", "testuser1");
    }

    @Test(expected = WrongUsernameException.class)
    public void authorizeWrongUser2() throws Exception {
        LdapController.getInstance().authorize("", "testuser1");
    }

    @Test(expected = WrongPasswordException.class)
    public void authorizeWrongPass() throws Exception {
        LdapController.getInstance().authorize("testuser1", "testuser2");
    }

    @Test(expected = WrongPasswordException.class)
    public void authorizeWrongPass2() throws Exception {
        LdapController.getInstance().authorize("testuser1", "");
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createUserExist() throws Exception {
        LdapController.getInstance().createUser("testuser1", "testuser1", "User");
    }

    @Test(expected = LdapException.class)
    public void createUser1() throws Exception {
        LdapController.getInstance().createUser("", "test", "User");
    }

    @Test(expected = LdapException.class)
    public void createUser2() throws Exception {
        LdapController.getInstance().createUser("testtest", "", "User");
    }

    @Test
    public void changePass() throws Exception {
        LdapController.getInstance().changePass("testuser1", "newpass");
        LdapController.getInstance().authorize("testuser1", "newpass");
        LdapController.getInstance().changePass("testuser1", "testuser1");
    }

    @Test(expected = WrongPasswordException.class)
    public void changePass2() throws Exception {
        LdapController.getInstance().changePass("testuser1", "");
    }

    @Test(expected = WrongUsernameException.class)
    public void deleteUser() throws Exception {
        LdapController.getInstance().deleteUser("nousername");
    }

    @AfterClass
    public static void deleteUser2() throws Exception {
        LdapController.getInstance().deleteUser("testuser1");
    }

}