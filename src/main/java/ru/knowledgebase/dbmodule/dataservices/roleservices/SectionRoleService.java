package ru.knowledgebase.dbmodule.dataservices.roleservices;

import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by vova on 30.09.16.
 */
public class SectionRoleService {
    private static volatile SectionRoleService instance;

    private HashMap<Integer, HashSet <Integer>> userSections = new HashMap<>();


    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static SectionRoleService getInstance() {
        SectionRoleService localInstance = instance;
        if (localInstance == null) {
            synchronized (SectionRoleService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SectionRoleService();
                }
            }
        }
        return localInstance;
    }

    public void add(int userId, int sectionId) throws Exception{
        if (userSections.get(userId) == null) {
            userSections.put(userId, new HashSet<>());
        }
        userSections.get(userId).add(sectionId);
    }

    public HashSet<Integer> getSections(int userId) throws Exception{
        if (userSections.get(userId) == null) {
            return new HashSet<>();
        }
        return userSections.get(userId);
    }
}
