package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.ImageRepository;
import ru.knowledgebase.modelsmodule.Image;

/**
 * Created by root on 17.08.16.
 */

@Service("imageService")
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void create(Image token) {
        imageRepository.save(token);
    }

    public boolean exist(Image token) {
        return imageRepository.exists(token.getId());
    }
}
