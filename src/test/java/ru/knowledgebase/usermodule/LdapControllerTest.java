package ru.knowledgebase.usermodule;

import org.junit.*;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.ldapmodule.LdapController;

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
    private LdapController ldapController = LdapController.getInstance();

    @Before
    public void prepareUser() throws Exception{
        if (ldapController.isUserExists(login1))
            ldapController.deleteUser(login1);
        if (ldapController.isUserExists(login2))
            ldapController.deleteUser(login2);
    }

    @Before
    public void prepareRole() throws Exception{
        if (!ldapController.isRoleExists(roleName))
            ldapController.createRole(roleName);
    }

    @Test
    public void register1() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        assertTrue(ldapController.isUserExists(login1));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void register2() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.createUser(login1, password1, roleName);
    }

    @Test(expected = RoleNotFoundException.class)
    public void register3() throws Exception{
        ldapController.createUser(login1, password1, roleName + "0");
    }

    @Test
    public void authorize1() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.authorize(login1, password1);
    }

    @Test(expected = UserNotFoundException.class)
    public void authorize2() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.authorize(login2, password2);
    }

    @Test(expected = WrongPasswordException.class)
    public void authorize3() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.authorize(login1, password2);
    }

    @Test
    public void changePass1() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.changePassword(login1, password2);
        ldapController.authorize(login1, password2);
    }

    @Test(expected = UserNotFoundException.class)
    public void changePass2() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.changePassword(login2, password2);
    }

    @Test
    public void changeLogin1() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.changeLogin(login1, login2);
        ldapController.authorize(login2, password1);
   }

    @Test(expected = UserNotFoundException.class)
    public void changeLogin2() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.changeLogin(login2, login1);
    }

    @Test
    public void delete1() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.deleteUser(login1);
        assertTrue(!ldapController.isUserExists(login1));
    }

    @Test(expected = UserNotFoundException.class)
    public void delete2() throws Exception{
        ldapController.createUser(login1, password1, roleName);
        ldapController.deleteUser(login2);
    }

}