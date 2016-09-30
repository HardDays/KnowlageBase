package ru.knowledgebase.imagemodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 25.08.16.
 */
public class ImageController {

    private DataCollector dataCollector = new DataCollector();

    private static ImageController instance;

    /**
     * Controller as thread-safe singeleton
     * @return
     */
    public static ImageController getInstance() {
        ImageController localInstance = instance;
        if (localInstance == null) {
            synchronized (ImageController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ImageController();
                }
            }
        }
        return localInstance;
    }

    /**
     * Get image objects from DB by list of id
     * @param imagesId
     * @return - list with images
     */
    public List<Image> getImages(List<String> imagesId) {
        List<Image> images = new LinkedList<Image>();
        for (String id : imagesId) {
            Image img = dataCollector.findImage(id);
            images.add(img);
        }
        return images;
    }

    public Image addImage(Image image) {
        return dataCollector.addImage(image);
    }

    public void deleteImage(String id) {
        dataCollector.deleteImage(id);
    }
}
