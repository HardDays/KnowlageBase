package ru.knowledgebase.ldapmodule;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;

public class LdapController {

    private static volatile LdapController instance;

    //params
    private final String ldapURI = "ldapmodule://localhost";
    private final String contextFactory = "com.sun.jndi.ldapmodule.LdapCtxFactory";
    private final String adminName = "admin";
    private final String adminPass = "12345";
    private final String domain = "dc=db,dc=test";
    
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
            throw new Exception("No user!");
        }
        answer.close();
        return resDomain;
    }
    /**
     * Authorize user with uid and password
     * @param uid user id
     * @param password user password
     * @return answer with result
     */
    public LdapAnswer authorize(String uid, String password){
        //find user domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        }catch (Exception e){
            return LdapAnswer.WRONG_UID;
        }
        //try authorize
        try {
            formAuthContext(domain, password);
        }catch (javax.naming.AuthenticationException e) {
            return LdapAnswer.WRONG_PASSWORD;
        }catch (javax.naming.OperationNotSupportedException e){
            return LdapAnswer.EMPTY_PASSWORD;
        }catch (javax.naming.CommunicationException e){
            return LdapAnswer.CONNECTION_ERROR;
        }catch (Exception e){
            return LdapAnswer.UNKNOWN_ERROR;
        }
        return LdapAnswer.OK;
    }
    /**
     * Create new user
     * @param uid user id
     * @param password user password
     * @param type category of user
     * @return answer with result
     */
    public LdapAnswer createUser(String uid, String password, String type) {
        if (password.length() == 0){
            return LdapAnswer.EMPTY_PASSWORD;
        }else if (uid.length() == 0){
            return LdapAnswer.EMPTY_UID;
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
            return LdapAnswer.USER_ALREADY_EXISTS;
        }catch (Exception e) {
            return LdapAnswer.UNKNOWN_ERROR;
        }
        return LdapAnswer.OK;
    }
    /**
     * Delete user with uid
     * @param uid user id
     * @return answer with result
     */
    public LdapAnswer deleteUser(String uid){
        //find domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        }catch (Exception e){
            return LdapAnswer.WRONG_UID;
        }
        DirContext ctx = null;
        try {
            //authorize as admin
            ctx = formAdminContext();
            //delete user
            ctx.destroySubcontext(domain);
            ctx.close();
        }catch (Exception e){
            return LdapAnswer.UNKNOWN_ERROR;
        }
        return LdapAnswer.OK;
    }
    /**
     * Change user password
     * @param uid user id
     * @param password new user password
     * @return answer with result
     */
    public LdapAnswer changePass(String uid, String password) {
        if (password.length() == 0){
            return LdapAnswer.EMPTY_PASSWORD    ;
        }
        //form user domain
        String domain = null;
        try {
            domain = findUserDomain(uid);
        }catch (Exception e){
            return LdapAnswer.WRONG_UID;
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
            return LdapAnswer.UNKNOWN_ERROR;
        }
        return LdapAnswer.OK;
    }
}


