package ru.knowledgebase.rolemodule;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.knowledgebase.configmodule.Config;
import ru.knowledgebase.configmodule.ConfigReader;
import ru.knowledgebase.modelsmodule.rolemodels.Role;

import javax.json.JsonObject;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 07.10.16.
 */
public class RoleImporter {

    /** From JSON config to Role */
    public static List<Role> getRoles(){
        JSONArray jsonroles = ConfigReader.getInstance().getConfig().getRoles();

        LinkedList <Role> roles = new LinkedList();

        for (int i = 0; i < jsonroles.length(); i++){
            JSONObject obj = jsonroles.getJSONObject(i);
            Role role = new Role();
            if (obj.has("name"))
                role.setName(obj.getString("name"));
            if (obj.has("can_add_article"))
                role.setCanAddArticle(obj.getBoolean("can_add_article"));
            if (obj.has("can_add_mistakes"))
                role.setCanAddMistakes(obj.getBoolean("can_add_mistakes"));
            if (obj.has("can_add_news"))
                role.setCanAddNews(obj.getBoolean("can_add_news"));
            if (obj.has("can_delete_article"))
                role.setCanDeleteArticle(obj.getBoolean("can_delete_article"));
            if (obj.has("can_edit_article"))
                role.setCanEditArticle(obj.getBoolean("can_edit_article"));
            if (obj.has("can_get_employees_actions_reports"))
                role.setCanGetEmployeesActionsReports(obj.getBoolean("can_get_employees_actions_reports"));
            if (obj.has("can_get_notifications"))
                role.setCanGetNotifications(obj.getBoolean("can_get_notifications"));
            if (obj.has("can_get_search_operations_reports"))
                role.setCanGetSearchOperationsReports(obj.getBoolean("can_get_search_operations_reports"));
            if (obj.has("can_get_system_actions_reports"))
                role.setCanGetSystemActionsReports(obj.getBoolean("can_get_system_actions_reports"));
            if (obj.has("can_off_on_notifications"))
                role.setCanOnOffNotifications(obj.getBoolean("can_off_on_notifications"));
            if (obj.has("can_search"))
                role.setCanSearch(obj.getBoolean("can_search"));
            if (obj.has("can_view_mistakes"))
                role.setCanViewMistakes(obj.getBoolean("can_view_mistakes"));
            if (obj.has("can_view_articles"))
                role.setCanViewArticle(obj.getBoolean("can_view_articles"));
            if (obj.has("can_add_user"))
                role.setCanAddUser(obj.getBoolean("can_add_user"));
            if (obj.has("can_delete_user"))
                role.setCanDeleteUser(obj.getBoolean("can_delete_user"));
            if (obj.has("can_edit_user"))
                role.setCanEditUser(obj.getBoolean("can_edit_user"));
            if (obj.has("can_edit_user_role"))
                role.setCanEditUserRole(obj.getBoolean("can_edit_user_role"));
            if (obj.has("can_view_user"))
                role.setCanViewUser(obj.getBoolean("can_view_user"));
            if (obj.has("role_id"))
                role.setRoleId(obj.getInt("role_id"));
            if (obj.has("base_role"))
                role.setBaseRole(obj.getBoolean("base_role"));
            roles.add(role);
        }
        return roles;
    }
}
