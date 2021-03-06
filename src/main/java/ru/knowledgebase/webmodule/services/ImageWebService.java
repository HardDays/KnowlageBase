package ru.knowledgebase.webmodule.services;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ru.knowledgebase.wrappermodule.ImageWrapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.Timestamp;

/**
 * Created by root on 08.10.16.
 */

@Path("/images")
public class ImageWebService {

    private ImageWrapper imageWrapper = new ImageWrapper();

    @POST
    @Path("/add_image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addImage(@FormParam(value = "author_id") int authorId,
                               @FormParam(value = "token") String userToken,
                             @FormDataParam("file") InputStream uploadedInputStream,
                             @FormDataParam("file") FormDataContentDisposition fileDetail) {
        return imageWrapper.addImage(userToken, authorId, uploadedInputStream, fileDetail.getFileName());
    }

    @POST
    @Path("/get_image")
    public Response getImage(@FormParam(value = "author_id") int authorId,
                             @FormParam(value = "token") String userToken,
                             @FormParam(value = "id") String imageId) {
        return imageWrapper.getImage(userToken, authorId, imageId);
    }

    @POST
    @Path("/get_all_image")
    public Response getImage(@FormParam(value = "author_id") int authorId,
                             @FormParam(value = "token") String userToken) {
        return imageWrapper.getAllImages(userToken, authorId);
    }

    @POST
    @Path("/delete_image")
    public Response deleteImage(@FormParam(value = "author_id") int authorId,
                             @FormParam(value = "token") String userToken,
                               @FormParam(value = "token") String imageId) {
        return imageWrapper.deleteImage(userToken, authorId, imageId);
    }



}
