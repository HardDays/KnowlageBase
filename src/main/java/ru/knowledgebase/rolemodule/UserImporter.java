package ru.knowledgebase.rolemodule;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.knowledgebase.configmodule.Configurations;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 08.10.16.
 */
public class UserImporter{
        public static List<User> getUsers(){
            JSONArray jsonroles = Configurations.getUsers();

            LinkedList<User> users = new LinkedList();

            for (int i = 0; i < jsonroles.length(); i++){
                JSONObject obj = jsonroles.getJSONObject(i);
                User user = new User();
                if (obj.has("login"))
                    user.setLogin(obj.getString("login"));
                if (obj.has("password"))
                    user.setPassword(obj.getString("password"));
                users.add(user);
            }
            return users;
        }
}
