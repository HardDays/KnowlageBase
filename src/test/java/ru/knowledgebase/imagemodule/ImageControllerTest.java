package ru.knowledgebase.imagemodule;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by root on 26.08.16.
 */
public class ImageControllerTest {

    private static ImageController ic = new ImageController();

    @Test
    public void getImages() throws Exception {

    }

    @Transactional
    @Test
    public void addImage() throws Exception {
        String path = "path/newpath";
        Image img = new Image(path);
        img = ic.addImage(img);
        List<Image> imgs = ic.getImages(Arrays.asList(img.getId()));
        assertTrue(imgs.size() == 1 && imgs.get(0).getPath().equals(path));
    }

}