package ru.knowledgebase.usermodule.ldapmodule;

import org.junit.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 15.08.16.
 */
public class LdapControllerTest{
    /*
    @BeforeClass
    public static void createTestUser() throws Exception{
        LdapController.getInstance().createUser("testuser1", "testuser1", "User");
    }

    @Test
    public void authorize() throws Exception {
        assertTrue(LdapController.getInstance().authorize("testuser1", "testuser1") == LdapAnswer.OK);
    }

    @Test
    public void authorizeWrongUser() throws Exception {
        assertTrue(LdapController.getInstance().authorize("testuser2", "testuser1") == LdapAnswer.WRONG_UID);
    }

    @Test
    public void authorizeWrongUser2() throws Exception {
        assertTrue(LdapController.getInstance().authorize("", "testuser1") == LdapAnswer.WRONG_UID);
    }

    @Test
    public void authorizeWrongPass() throws Exception {
        assertTrue(LdapController.getInstance().authorize("testuser1", "testuser2") == LdapAnswer.WRONG_PASSWORD);
    }

    @Test
    public void authorizeWrongPass2() throws Exception {
         assertTrue(LdapController.getInstance().authorize("testuser1", "") == LdapAnswer.EMPTY_PASSWORD);
    }

    @Test
    public void createUserExist() throws Exception {
         assertTrue(LdapController.getInstance().createUser("testuser1", "testuser1", "User") == LdapAnswer.USER_ALREADY_EXISTS);
    }

    @Test
    public void createWrongUser1() throws Exception {
         assertTrue(LdapController.getInstance().createUser("", "test", "User") == LdapAnswer.EMPTY_UID);
    }

    @Test
    public void createWrongUser2() throws Exception {
         assertTrue(LdapController.getInstance().createUser("testtest", "", "User") == LdapAnswer.EMPTY_PASSWORD);
    }

    @Test
    public void changePass() throws Exception {
         assertTrue(LdapController.getInstance().changePass("testuser1", "newpass") == LdapAnswer.OK);
         assertTrue(LdapController.getInstance().authorize("testuser1", "newpass") == LdapAnswer.OK);
         assertTrue(LdapController.getInstance().changePass("testuser1", "testuser1") == LdapAnswer.OK);
    }

    @Test
    public void changePass2() throws Exception {
        assertTrue(LdapController.getInstance().changePass("testuser1", "") == LdapAnswer.EMPTY_PASSWORD);
    }

    @Test
    public void deleteUser() throws Exception {
         assertTrue(LdapController.getInstance().deleteUser("nousername") == LdapAnswer.WRONG_UID);
    }

    @AfterClass
    public static void deleteUser2() throws Exception {
        assertTrue(LdapController.getInstance().deleteUser("testuser1") == LdapAnswer.OK);
    }
    */
}