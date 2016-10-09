package ru.knowledgebase.wrappermodule;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import javax.ws.rs.core.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.usermodule.UserController;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 31.08.16.
 */
public class ImageWrapper {

    private ImageController imageController = ImageController.getInstance();
    private UserController  userController  = UserController.getInstance();

    /**
     * Add image to hard drive and add record with path to DB.
     * Create response for web-server
     * @param token
     * @param userId
     * @param uploadedInputStream - stream with raw image
     * @param filename
     * @return response for web-service
     */
    public Response addImage(String token, int userId, InputStream uploadedInputStream,
                             String filename) {
        String id;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            id = imageController.addImage(uploadedInputStream, filename);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildImageCreatedResponse(id);
    }

    /**
     * Get image path by id
     * @param token
     * @param userId
     * @param id
     * @return
     */
    public Response getImage(String token, int userId, String id) {
        String path;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            path = imageController.getImagePath(id);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildImagePathResponse(path);
    }

    /**
     * @param token
     * @param userId
     * @return response with (ImageName, ImagePath)
     */
    public Response getAllImages(String token, int userId) {
        List<Image> images = new LinkedList<>();
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            images = imageController.getAllImages();
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildGetAllImagesResponse(images);
    }

    public Response deleteImage(String token, int userId, String imageId) {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            imageController.deleteImage(imageId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildImagesDeletedResponse();
    }

}
