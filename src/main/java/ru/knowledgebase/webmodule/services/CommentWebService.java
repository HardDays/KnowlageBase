package ru.knowledgebase.webmodule.services;

import org.json.JSONObject;
import ru.knowledgebase.wrappermodule.CommentWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by vova on 02.10.16.
 */
@Path("/comments")
public class CommentWebService {

    private CommentWrapper commentWrapper = new CommentWrapper();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(String param) {
        try {
            JSONObject obj = new JSONObject(param);
            return commentWrapper.add(obj.getInt("user_id"),
                                      obj.getString("user_token"),
                                      obj.getInt("article_id"),
                                      obj.getString("comment"),
                                      obj.getString("article_text"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_list")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getList(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return commentWrapper.get(obj.getInt("user_id"),
                    obj.getString("user_token"),
                    obj.getInt("offset"),
                    obj.getInt("limit"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return commentWrapper.delete(obj.getInt("user_id"),
                    obj.getString("user_token"),
                    obj.getInt("comment_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }
}
