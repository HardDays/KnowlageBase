package ru.knowledgebase.dbmodule.repositories.JPA.articlerepository;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

public interface ImageRepository extends CrudRepository<Image, String> {

}