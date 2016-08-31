package ru.knowledgebase.wrappermodule;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.responsemodule.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.usermodule.UserController;

import java.io.InputStream;

/**
 * Created by root on 31.08.16.
 */
public class ImageWrapper {

    private ImageController imageController = ImageController.getInstance();
    private UserController  userController  = UserController.getInstance();

    /**
     * Add image to hadr drive and add record with path to DB.
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
        return ResponseBuilder.buildImageCreatedResponse(path);
    }

}
