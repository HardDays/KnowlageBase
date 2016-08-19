package ru.knowledgebase.dbmodule.repositories;

/**
 * Created by vova on 19.08.16.
 */
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.SectionRole;


public interface SectionRoleRepository extends CrudRepository<SectionRole, Integer> {

    @Query("from SectionRole")
    public List<SectionRole> getAll() throws Exception;

}