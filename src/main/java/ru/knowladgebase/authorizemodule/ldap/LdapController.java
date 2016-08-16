package ru.knowladgebase.authorizemodule.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;

public class LdapController {

    private static volatile LdapController instance;

    //params
    private final String ldapURI = "ldap://localhost";
    private final String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private final String adminName = "admin";
    private final String adminPass = "12345";
    private final String domain = "dc=db,dc=test";

    //singleton
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

    //form environment for context
    private Hashtable<String,String> formEnvironment() throws Exception {
        Hashtable<String,String> env = new Hashtable <String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        env.put(Context.PROVIDER_URL, ldapURI);
        return env;
    }

    //form context from environment
    private DirContext formContext(Hashtable<String, String> env) throws Exception {
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
        }catch(javax.naming.CommunicationException e){
            throw new NoConnectionException();
        }
        return ctx;
    }

    //form context with admin authorization
    private DirContext formAdminContext() throws Exception {
        Hashtable<String,String> env = formEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=" + adminName + "," + domain);
        env.put(Context.SECURITY_CREDENTIALS, adminPass);
        return formContext(env);
    }

    //form context with user authorization
    private DirContext formAuthContext(String domain, String password) throws Exception {
        Hashtable<String,String> env = formEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, domain);
        env.put(Context.SECURITY_CREDENTIALS, password);
        return formContext(env);
    }

    //find user domain by id
    private String getUserDomain(String uid) throws Exception {
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
            resDomain = null;
        }

        answer.close();
        return resDomain;
    }

    //authorize user
    public void authorize(String uid, String password) throws Exception {
        //find user domain
        String domain = getUserDomain(uid);
        if (domain == null){
            throw new WrongUsernameException();
        }
        //try authorize
        try {
            formAuthContext(domain, password);
        }catch (javax.naming.AuthenticationException e) {
            throw new WrongPasswordException();
        }catch (javax.naming.OperationNotSupportedException e){
            throw new WrongPasswordException();
        }
    }

    //create new user
    public void createUser(String uid, String password, String type) throws Exception{
        if (password.length() == 0){
            throw new LdapException("Cannot create new user!");
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
        }catch (Exception e) {
            throw new LdapException("Cannot create new user!");
        }
    }

    //delete user
    public void deleteUser(String uid) throws Exception{
        //find domain
        String domain = getUserDomain(uid);
        if (domain == null){
            throw new WrongUsernameException();
        }
        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            //delete user
            ctx.destroySubcontext(domain);
            ctx.close();
        }catch (Exception e){
            throw new LdapException("Cannot delete user!");
        }
    }
    //change user password
    public void changePass(String uid, String password) throws Exception{
        if (password.length() == 0){
            throw new WrongPasswordException();
        }
        //form user domain
        String domain = getUserDomain(uid);
        if (domain == null){
            throw new WrongUsernameException();
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
        }catch (Exception e){
            throw new LdapException("Cannot change password!");
        }
    }
}


