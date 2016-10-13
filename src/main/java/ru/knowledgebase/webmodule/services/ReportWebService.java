package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.ReportWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Мария on 09.10.2016.
 */
@Path("/report")
public class ReportWebService {
    ReportWrapper reportWrapper = new ReportWrapper();

    @POST
    @Path("/get_system_actions_report")
    public Response getSystemActionsReport(
            @FormParam(value = "user_id") int userID,
            @FormParam(value = "token") String token,
            @FormParam(value = "from_time") Timestamp from,
            @FormParam(value = "to_time") Timestamp to,
            @FormParam(value = "list_of_sections") List<Integer> sections){
        return reportWrapper.getSystemActionsReport(userID, token, from, to, sections);
    }

    @POST
    @Path("/get_employees_actions_report")
    public Response getEmployeesActionsReport(
            @FormParam(value = "supervisor_id") int supervisorID,
            @FormParam(value = "token") String token,
            @FormParam(value = "from_time") Timestamp from,
            @FormParam(value = "to_time") Timestamp to,
            @FormParam(value = "list_of_sections") List<Integer> sections){
        return reportWrapper.getEmployeesActionsReport(supervisorID, token, from, to, sections);
    }

    @POST
    @Path("/get_search_actions_report")
    public Response getSearchActionsReport(
            @FormParam(value = "user_id") int userID,
            @FormParam(value = "token") String token,
            @FormParam(value = "from_time") Timestamp from,
            @FormParam(value = "to_time") Timestamp to,
            @FormParam(value = "list_of_sections") List<Integer> sections){
        return reportWrapper.getSearchActionsReport(userID, token, from, to, sections);
    }
}
