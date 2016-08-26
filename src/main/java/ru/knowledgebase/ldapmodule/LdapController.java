package ru.knowledgebase.ldapmodule;

import ru.knowledgebase.exceptionmodule.ldapexceptions.LdapConnectionException;
import ru.knowledgebase.exceptionmodule.ldapexceptions.LdapException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleDeleteException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongUserDataException;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LdapController {

    //params for LDAP
    private String ldapURI = "ldap://localhost";
    private String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private String adminName = "admin";
    private String adminPass = "12345";
    private String domain = "dc=db,dc=test";
    private String defaultRole = "User";

    private static volatile LdapController instance;

    /**
     * Get instance of a class
     * @return instance of a class
    */
    public static LdapController getInstance() {
        LdapController localInstance = instance;
        if (localInstance == null) {
            synchronized (LdapController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new LdapController();
                }
            }
        }
        return localInstance;
    }
    /**
     * Form environment for context
     * @return environment hashtable
     */
    private Hashtable<String,String> formEnvironment() throws Exception {
        Hashtable<String,String> env = new Hashtable <String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        env.put(Context.PROVIDER_URL, ldapURI);
        return env;
    }
    /**
     * Form context from environment
     * @param env hashtable with environment
     * @return context for ldapmodule
     */
    private DirContext formContext(Hashtable<String, String> env) throws Exception {
        DirContext ctx = null;
        ctx = new InitialDirContext(env);
        return ctx;
    }
    /**
     * Form context with admin authorization parameters
     * @return context with admin parameters
     */
    private DirContext formAdminContext() throws Exception {
        Hashtable<String,String> env = formEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=" + adminName + "," + domain);
        env.put(Context.SECURITY_CREDENTIALS, adminPass);
        return formContext(env);
    }
    /**
     * Form context with user authorization parameters
     * @param domain domain with user id, domains etc
     * @param password user password
     * @return context with user parameters
     */
    private DirContext formAuthContext(String domain, String password) throws Exception {
        Hashtable<String,String> env = formEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, domain);
        env.put(Context.SECURITY_CREDENTIALS, password);
        return formContext(env);
    }
    /**
     * Find domain by given filter
     * @param filter filter
     * @return domain string
     */
    private String findDomain(String filter) throws Exception{
        DirContext ctx = formContext(formEnvironment());
        //search element with given filter
        SearchControls ctrl = new SearchControls();
        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration answer = ctx.search(domain, filter, ctrl);
        //go through result
        String resDomain = null;
        if (answer.hasMore()) {
            SearchResult result = (SearchResult) answer.next();
            resDomain = result.getNameInNamespace();
        }
        answer.close();
        return resDomain;
    }
    /**
     * Find domain by user id
     * @param login user id
     * @return domain string
     */
    private String findUserDomain(String login) throws Exception {
        return findDomain("(uid=" + login + ")");
    }
    /**
     * Check is user exits
     * @param login user id
     * @return true if exists, else false
     */
    public boolean isUserExists(String login) throws Exception{
        return findUserDomain(login) != null;
    }
    /**
     * Find domain role name
     * @param role role name
     * @return domain string
     */
    private String findRoleDomain(String role) throws Exception {
        return findDomain("(ou=" + role + ")");
    }
    /**
     * Check is role exists
     * @param role role name
     * @return true if role exists, else false
     */
    public boolean isRoleExists(String role) throws Exception{
        return findRoleDomain(role) != null;
    }
    /**
     * Authorize user with login and password
     * @param login user id
     * @param password user password
     */
    public void authorize(String login, String password) throws Exception{
        //find user domain
        String domain = findUserDomain(login);
        if (domain == null)
            throw new UserNotFoundException();
        //try authorize
        try {
            formAuthContext(domain, password);
        }catch (javax.naming.AuthenticationException e) {
            throw new WrongPasswordException();
        }catch (javax.naming.OperationNotSupportedException e){
            throw new WrongPasswordException();
        }catch (javax.naming.CommunicationException e){
            throw new LdapConnectionException();
        }catch (Exception e){
            throw new LdapException();
        }
    }
    /**
     * Create new user
     * @param login user id
     * @param password user password
     */
    public void createUser(String login, String password) throws Exception{
        if ((password.length() == 0) || (login.length() == 0)){
            throw new WrongUserDataException();
        }
        String userDomain = "uid=" + login + ",ou=" + defaultRole + "," + domain;
        //make attributes
        Attribute loginAttr = new BasicAttribute("uid", login);
        Attribute passAttr = new BasicAttribute("userPassword", password);
        Attribute ocAttr = new BasicAttribute("objectClass");
        //role of object
        ocAttr.add("account");
        ocAttr.add("simpleSecurityObject");

        DirContext ctx = null;
        try {
            ctx = formAdminContext();
            //make entry
            BasicAttributes entry = new BasicAttributes();
            entry.put(loginAttr);
            entry.put(passAttr);
            entry.put(ocAttr);
            //create subcontext from entry
            ctx.createSubcontext(userDomain, entry);
            ctx.close();
        }catch (javax.naming.NameAlreadyBoundException e){
            throw new UserAlreadyExistsException();
        }catch (javax.naming.CommunicationException e) {
            throw new LdapConnectionException();
        }catch(javax.naming.NameNotFoundException e){
            throw new RoleNotFoundException();
        }catch (Exception e) {
            throw new LdapException();
        }
    }
    /**
     * Delete user with login
     * @param login user id
     */
    public void deleteUser(String login) throws Exception {
        //find domain
        String domain = findUserDomain(login);
        if (domain == null)
            throw new UserNotFoundException();

        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            //delete user
            ctx.destroySubcontext(domain);
            ctx.close();
        }catch (javax.naming.CommunicationException e){
            throw new LdapConnectionException();
        }catch (Exception e) {
            throw new LdapException();
        }
    }
    /**
     * Change user password
     * @param login user id
     * @param password new user password
     */
    public void changePassword(String login, String password) throws Exception{
        if (password.length() == 0){
            throw new WrongUserDataException();
        }
        //form user domain
        String domain = findUserDomain(login);
        if (domain == null)
            throw new UserNotFoundException();

        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            //change password
            Attribute newPass = new BasicAttribute("userPassword", password);
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, newPass);
            ctx.modifyAttributes(domain, mods);
            ctx.close();
        }catch (javax.naming.CommunicationException e){
            throw new LdapConnectionException();
        }catch (Exception e){
             throw new LdapException();
        }
    }
    /**
     * Change user role
     * @param login user id
     * @param role role name
     */
    public void changeRole(String login, String role) throws Exception {
        if (role.length() == 0) {
            throw new WrongUserDataException();
        }
        //form user domain
        String domain = findUserDomain(login);
        if (domain == null)
            throw new UserNotFoundException();
        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            Pattern pattern = Pattern.compile("ou=[a-zA-Z0-9]*");
            Matcher matcher = pattern.matcher(domain);
            if (matcher.find()) {
                String newDomain = domain.replace(matcher.group(0), "ou=" + role);
                ctx.rename(domain, newDomain);
                ctx.close();
            } else {
                throw new LdapException();
            }
        } catch (javax.naming.NameNotFoundException e) {
            throw new RoleNotFoundException();
        } catch (javax.naming.CommunicationException e) {
            throw new LdapConnectionException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new LdapException();
        }
    }
    /**
     * Create new role
     * @param name role name
     */
    public void createRole(String name) throws Exception{
        if (name.length() == 0){
            throw new WrongUserDataException();
        }
        String roleDomain = "ou=" + name + "," + domain;
        //make attributes
        Attribute nameAttr = new BasicAttribute("ou", name);
        Attribute ocAttr = new BasicAttribute("objectClass", "organizationalUnit");

        DirContext ctx = null;
        try {
            ctx = formAdminContext();
            //make entry
            BasicAttributes entry = new BasicAttributes();
            entry.put(nameAttr);
            entry.put(ocAttr);
            //create subcontext from entry
            ctx.createSubcontext(roleDomain, entry);
            ctx.close();
        }catch (javax.naming.NameAlreadyBoundException e){
            throw new RoleAlreadyExistsException();
        }catch (javax.naming.CommunicationException e){
            throw new LdapConnectionException();
        }catch (Exception e) {
            throw new LdapException();
        }
    }
    /**
     * Delete role
     * @param role role name
     */
    public void deleteRole(String role) throws Exception {
        //find domain
        String domain = findRoleDomain(role);
        if (domain == null)
            throw new RoleNotFoundException();
        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            //delete role
            ctx.destroySubcontext(domain);
            ctx.close();
        }catch (javax.naming.CommunicationException e) {
            throw new LdapConnectionException();
        } catch(javax.naming.ContextNotEmptyException e){
            throw new RoleDeleteException();
        }catch (Exception e) {
            throw new LdapException();
        }
    }
    /**
     * Change user login name
     * @param login user login name
     * @param newlogin new login name
     */
    public void changeLogin(String login, String newlogin) throws Exception{
        if (login.length() == 0) {
            throw new WrongUserDataException();
        }
        //form user domain
        String domain = findUserDomain(login);
        if (domain == null)
            throw new UserNotFoundException();
        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            Pattern pattern = Pattern.compile("uid=[a-zA-Z0-9]*");
            Matcher matcher = pattern.matcher(domain);
            if (matcher.find()) {
                String newDomain = domain.replace(matcher.group(0), "uid=" + newlogin);
                ctx.rename(domain, newDomain);
                ctx.close();
            } else {
                throw new LdapException();
            }
        } catch (javax.naming.NameNotFoundException e) {
            throw new RoleNotFoundException();
        } catch (javax.naming.CommunicationException e) {
            throw new LdapConnectionException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new LdapException();
        }
    }


    public String getLdapURI() {
        return ldapURI;
    }

    public void setLdapURI(String ldapURI) {
        this.ldapURI = ldapURI;
    }

    public String getContextFactory() {
        return contextFactory;
    }

    public void setContextFactory(String contextFactory) {
        this.contextFactory = contextFactory;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

}


