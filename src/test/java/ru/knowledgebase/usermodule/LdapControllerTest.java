package ru.knowledgebase.usermodule;

import org.junit.*;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.ldapmodule.LdapWorker;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 15.08.16.
 */
public class LdapControllerTest{

    private final String login1 = "testlogin1";
    private final String login2 = "testlogin2";

    private final String password1 = "testpassword1";
    private final String password2 = "testpassword2";

    private final int articleId = 1;
    private final String articleName = "testarticle";

    private final int roleId = 1;
    private final String roleName = "User";
    private LdapWorker ldapWorker = LdapWorker.getInstance();

    @Before
    public void prepareUser() throws Exception{
        if (ldapWorker.isUserExists(login1))
            ldapWorker.deleteUser(login1);
        if (ldapWorker.isUserExists(login2))
            ldapWorker.deleteUser(login2);
    }

    @Before
    public void prepareRole() throws Exception{
        if (!ldapWorker.isRoleExists(roleName))
            ldapWorker.createRole(roleName);
    }

    @Test
    public void register1() throws Exception{
        ldapWorker.createUser(login1, password1);
        assertTrue(ldapWorker.isUserExists(login1));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void register2() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.createUser(login1, password1);
    }

    @Test
    public void authorize1() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.authorize(login1, password1);
    }

    @Test(expected = UserNotFoundException.class)
    public void authorize2() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.authorize(login2, password2);
    }

    @Test(expected = WrongPasswordException.class)
    public void authorize3() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.authorize(login1, password2);
    }

    @Test
    public void changePass1() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.changePassword(login1, password2);
        ldapWorker.authorize(login1, password2);
    }

    @Test(expected = UserNotFoundException.class)
    public void changePass2() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.changePassword(login2, password2);
    }

    @Test
    public void changeLogin1() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.changeLogin(login1, login2);
        ldapWorker.authorize(login2, password1);
   }

    @Test(expected = UserNotFoundException.class)
    public void changeLogin2() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.changeLogin(login2, login1);
    }

    @Test
    public void delete1() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.deleteUser(login1);
        assertTrue(!ldapWorker.isUserExists(login1));
    }

    @Test(expected = UserNotFoundException.class)
    public void delete2() throws Exception{
        ldapWorker.createUser(login1, password1);
        ldapWorker.deleteUser(login2);
    }

}