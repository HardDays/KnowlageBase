package ru.knowledgebase.dbmodule.repositories.rolerepositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
public interface ImageRepository extends CrudRepository<Image, String> {

}