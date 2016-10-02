package ru.knowledgebase.imagemodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.imageexceptions.ImageCantBeSavedException;
import ru.knowledgebase.exceptionmodule.imageexceptions.ImageNotFoundException;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 25.08.16.
 */
public class ImageController {

    private DataCollector dataCollector = DataCollector.getInstance();

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
    public List<Image> getImages(List<String> imagesId) throws Exception{
        List<Image> images = new LinkedList<Image>();
        for (String id : imagesId) {
            Image img;
            try {
                img = dataCollector.findImage(id);
            }
            catch (Exception ex) {
                throw new DataBaseException();
            }
            images.add(img);
        }
        return images;
    }

    public String getImagePath(String id) throws Exception{
        Image img;
        try {
            img = dataCollector.findImage(id);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        if (img == null) {
            throw new ImageNotFoundException();
        }
        return img.getPath();
    }

    public Image addImage(Image image) throws Exception{
        Image img = null;
        try {
            img = dataCollector.addImage(image);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        return img;
    }

    public String addImage(InputStream uploadedInputStream, String filename) throws Exception{
        String path;
        try {
            path = ImageProcessor.saveImage(uploadedInputStream, filename);
        }
        catch (Exception ex) {
            throw new ImageCantBeSavedException();
        }
        return addImage(new Image(path, filename)).getId();
    }

    public void deleteImage(String id) throws Exception{
        try {
            dataCollector.deleteImage(id);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
    }

    public List<Image> getAllImages() throws Exception{
        List<Image> images = new LinkedList<>();
        try {
            images = dataCollector.addAllImages();
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        return images;
    }
}
