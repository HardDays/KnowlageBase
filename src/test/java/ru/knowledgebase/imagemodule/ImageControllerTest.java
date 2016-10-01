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

    private static ImageController ic = ImageController.getInstance();

    @Transactional
    @Test
    public void addImage() throws Exception {
        String path = "path/newpath";
        Image img = new Image(path);
        img = ic.addImage(img);
        List<Image> imgs = ic.getImages(Arrays.asList(img.getId()));
        assertTrue(imgs.size() == 1 && imgs.get(0).getPath().equals(path));

        ic.deleteImage(img.getId());
    }

    @Transactional
    @Test
    public void getAllImages() throws Exception{
        String path1 = "path/newpath";
        Image img1 = new Image(path1);
        img1 = ic.addImage(img1);

        String path2 = "path/newpath2";
        Image img2 = new Image(path2);
        img2 = ic.addImage(img2);

        List<Image> images = ic.getAllImages();
        assertTrue(images.size() == 2);

        ic.deleteImage(img1.getId());
        ic.deleteImage(img2.getId());
    }

}