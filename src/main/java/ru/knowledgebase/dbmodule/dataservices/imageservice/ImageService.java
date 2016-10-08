package ru.knowledgebase.dbmodule.dataservices.imageservice;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ImageRepository;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by root on 17.08.16.
 */

@Service("imageService")
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image create(Image token) throws Exception {
        return imageRepository.save(token);
    }

    public Image find(String id) throws Exception {
        return imageRepository.findOne(id);
    }

    public boolean exist(Image token) throws Exception {
        return imageRepository.exists(token.getId());
    }

    @Transactional
    public void delete(String id) throws Exception {
        Image img = find(id);
        if (Files.exists(Paths.get(img.getPath()))) {
            Files.delete(Paths.get(img.getPath()));
        }
        imageRepository.delete(id);
    }

    public List<Image> getAllImages() throws Exception {
        Iterable<Image> imgs = imageRepository.findAll();
        return Lists.newLinkedList(imgs);
    }
}
