package ru.knowledgebase.webmodule.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/hello")
public class HelloWorldService {
    @GET
    @Path("/say/{param}")
    public Response getMessage(@PathParam("param") String message) {
<<<<<<< .merge_file_eXFfV6
        String output = "Jersey says: " + message;
=======
        String output = "Jersey says " + message;
>>>>>>> .merge_file_RKZN46
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/getSomeNumbers/{param}")
    public Response getNewMessage(@PathParam("param") String message) {
        ArrayList<Integer> arr= new ArrayList<Integer>();
        arr.add(7);
        arr.add(7);
        arr.add(7);
        return Response.status(200).entity(arr.toString()).build();
    }



}