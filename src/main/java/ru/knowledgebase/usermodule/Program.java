package ru.knowledgebase.usermodule;

/**
 * Created by vova on 18.08.16.
 */
public class Program {
    public static void main(String args[]){
        try {
        // System.out.println(AuthorizeController.authorizeLdap("userNew2", "12345").getToken());
          //
            // RegisterController.register("userNew2", "userNew1");
          //  UserEditController.changePassword("userNew2", "12345");
            UserDeleteController.delete("userNew1");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
