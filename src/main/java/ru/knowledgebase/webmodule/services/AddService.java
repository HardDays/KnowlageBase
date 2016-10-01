package ru.knowledgebase.webmodule.services;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.wrappermodule.ArticleWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by root on 09.08.16.
 */
@Path("/add")
public class AddService {

    @POST
    @Path("/article")
    public Response addNewArticleP(String smg) {

        return Response.status(200).entity(smg).build();
    }

    @GET
    @Path("/article/{param}")
    public Response addNewArticleG(@PathParam("param") String smg) throws Exception{
        ArticleController w = new ArticleController();
        Article a = w.getArticle(123);
        return Response.status(200).entity(a.toString()).build();
    }

    @POST
    @Path("article/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        String fileLocation = "/home/pisatel/Documents/Work/Testing/Jersey/uploads/" + fileDetail.getFileName();
        //saving file
        try {
            File yourFile = new File(fileLocation);
            if(!yourFile.exists()) {
                yourFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(yourFile, false);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        String output = "File successfully uploaded to : " + fileLocation;
        return Response.status(200).entity(output).build();
    }
}
