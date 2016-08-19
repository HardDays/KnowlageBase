package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.SectionRoleRepository;
import ru.knowledgebase.modelsmodule.SectionRole;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
@Service("sectionRoleService")
public class SectionRoleService {

    @Autowired
    private SectionRoleRepository sectionRoleRepository;

    @Transactional
    public void create(SectionRole sectionRole) throws Exception{
        sectionRoleRepository.save(sectionRole);
    }

    public List<SectionRole> getAll() throws Exception{
        return sectionRoleRepository.getAll();
    }
}
