package ru.knowledgebase.dbmodule.storages;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by vova on 30.09.16.
 */
public class SectionRoleStorage {
    private static volatile SectionRoleStorage instance;

    private HashMap<Integer, HashSet <Integer>> userSections = new HashMap<>();


    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static SectionRoleStorage getInstance() {
        SectionRoleStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (SectionRoleStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SectionRoleStorage();
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

    public void delete(int userId, int sectionId) throws Exception{
        if (userSections.get(userId) != null) {
            userSections.get(userId).remove(sectionId);
        }
    }

    public HashSet<Integer> getSections(int userId) throws Exception{
        if (userSections.get(userId) == null) {
            return new HashSet<>();
        }
        return userSections.get(userId);
    }

    public void deleteAllSections(int userId){
        userSections.put(userId, new HashSet<>());
    }
}
