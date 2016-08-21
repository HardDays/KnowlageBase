package ru.knowledgebase.ldapmodule.exceptions;

import ru.knowledgebase.ldapmodule.LdapException;
import ru.knowledgebase.rolemodule.exceptions.RoleAlreadyExistsException;
import ru.knowledgebase.rolemodule.exceptions.RoleNotFoundException;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongPasswordException;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LdapController {

    private static volatile LdapController instance;

    //params
    private final String ldapURI = "ldap://localhost";
    private final String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private final String adminName = "admin";
    private final String adminPass = "12345";
    private final String domain = "dc=db,dc=test";
    private final String defaultRole = "User";
    
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
     * Find domain by user id
     * @param uid user id
     * @return domain string
     */
    private String findUserDomain(String uid) throws Exception {
        DirContext ctx = formContext(formEnvironment());
        //can be changed to cn
        String filter = "(uid=" + uid + ")";
        //search user with uid
        SearchControls ctrl = new SearchControls();
        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration answer = ctx.search(domain, filter, ctrl);
        //go through result
        String resDomain;
        if (answer.hasMore()) {
            SearchResult result = (SearchResult) answer.next();
            resDomain = result.getNameInNamespace();
        }
        else {
            throw new WrongPasswordException();
        }
        answer.close();
        return resDomain;
    }
    /**
     * Authorize user with uid and password
     * @param uid user id
     * @param password user password
     */
    public void authorize(String uid, String password) throws Exception{
        //find user domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        }catch (Exception e){
            throw new UserNotFoundException();
        }
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
     * @param uid user id
     * @param password user password
     * @param type category of user
     */
    public void createUser(String uid, String password, String type) throws Exception{
        if ((password.length() == 0) || (uid.length() == 0)){
            throw new WrongUserDataException();
        }
        String userDomain = "uid=" + uid + ",ou=" + type + "," + domain;
        //make attributes
        Attribute uidAttr = new BasicAttribute("uid", uid);
        Attribute passAttr = new BasicAttribute("userPassword", password);
        Attribute ocAttr = new BasicAttribute("objectClass");
        //type of object
        ocAttr.add("account");
        ocAttr.add("simpleSecurityObject");

        DirContext ctx = null;
        try {
            ctx = formAdminContext();
            //make entry
            BasicAttributes entry = new BasicAttributes();
            entry.put(uidAttr);
            entry.put(passAttr);
            entry.put(ocAttr);
            //create subcontext from entry
            ctx.createSubcontext(userDomain, entry);
            ctx.close();
        }catch (javax.naming.NameAlreadyBoundException e){
            throw new UserAlreadyExistsException();
        }catch (javax.naming.CommunicationException e){
            throw new LdapConnectionException();
        }catch (Exception e) {
            e.printStackTrace();
            throw new LdapException();
        }
    }
    /**
     * Delete user with uid
     * @param uid user id
     */
    public void deleteUser(String uid) throws Exception {
        //find domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        }catch (Exception e){
            throw new UserNotFoundException();
        }
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
     * @param uid user id
     * @param password new user password
     */
    public void changePass(String uid, String password) throws Exception{
        if (password.length() == 0){
            throw new WrongUserDataException();
        }
        //form user domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        }catch (Exception e){
            throw new UserNotFoundException();
        }
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

    public void changeRole(String uid, String role) throws Exception {
        if (role.length() == 0) {
            throw new WrongUserDataException();
        }
        //form user domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
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
}


