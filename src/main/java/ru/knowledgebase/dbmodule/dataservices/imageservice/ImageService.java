package ru.knowledgebase.dbmodule.dataservices.imageservice;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ImageRepository;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.util.List;

/**
 * Created by root on 17.08.16.
 */

@Service("imageService")
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image create(Image token) {
        return imageRepository.save(token);
    }

    public Image find(String id) {
        return imageRepository.findOne(id);
    }

    public boolean exist(Image token) {
        return imageRepository.exists(token.getId());
    }

    public void delete(String id) {
        imageRepository.delete(id);
    }

    public List<Image> getAllImages() {
        Iterable<Image> imgs = imageRepository.findAll();
        return Lists.newLinkedList(imgs);
    }
}
