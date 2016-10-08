package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.exceptionmodule.reportexception.UnableToCreateReportExeption;
import ru.knowledgebase.reportmodule.ReportController;

import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.usermodule.UserController;


import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Мария on 01.10.2016.
 */
public class ReportWrapper {
    private UserController userController = UserController.getInstance();
    private ResponseBuilder responseBuilder  = new ResponseBuilder();
    private ReportController reportController = ReportController.getInstance();
    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();

    public Response getSystemActionsReport(int userID, String token, Timestamp from, Timestamp to,
                                           List<Integer> sections){
        String reportType = "Отчет о действиях в системе";
        try{
        boolean okToken = userController.checkUserToken(userID, token);
        boolean hasRights = articleRoleController.hasAccessToSections(userID, sections);
        boolean canMakeThatReport = true;
            for (Integer section : sections) {
                canMakeThatReport = canMakeThatReport &&
                        articleRoleController.canGetSystemActionsReports(userID, section);
            }
        if (!okToken)
            return ResponseBuilder.buildWrongTokenResponse();
        if(!hasRights)
            return responseBuilder.buildNoAccessResponse();

        return responseBuilder.buildReportResponse(
                    reportController.getSystemActionsReport(userID, from, to, sections), reportType);
        }catch(Exception ex){
            return ResponseBuilder.buildResponse(ex);
        }
    }

    public Response getEmployeesActionsReport(int supervisorID, String token, Timestamp from, Timestamp to,
                                              List<Integer> sections) {
        String reportType = "Отчет о действиях подчиненных";
        try {
            boolean okToken = userController.checkUserToken(supervisorID, token);
            boolean hasRights = articleRoleController.hasAccessToSections(supervisorID, sections);
            boolean canMakeThatReport = true;
            for (Integer section : sections) {
                canMakeThatReport = canMakeThatReport &&
                        articleRoleController.canGetEmployeesActionsReports(supervisorID, section);
            }
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!(hasRights && canMakeThatReport))
                return responseBuilder.buildNoAccessResponse();

            return responseBuilder.buildReportResponse(
                    reportController.getEmployeesActionsReport(supervisorID, from, to, sections), reportType);
        } catch (UnableToCreateReportExeption ex){
            return responseBuilder.buildResponse(ex);
        } catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
    }

    public Response getSearchActionsReport(int userID, String token, Timestamp from, Timestamp to,
                                              List<Integer> sections){
        String reportType = "Отчео о поиске в системе";
        try{
            boolean okToken = userController.checkUserToken(userID, token);
            boolean hasRights = articleRoleController.hasAccessToSections(userID, sections);
            boolean canMakeThatReport = true;
            for (Integer section : sections) {
                canMakeThatReport = canMakeThatReport &&
                        articleRoleController.canGetSearchOperationsReports(userID, section);
            }
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if(!(hasRights && canMakeThatReport))
                return responseBuilder.buildNoAccessResponse();

            return responseBuilder.buildReportResponse(
                    reportController.getSearchActionsReport(userID, from, to, sections), reportType);
        }catch(Exception ex){
            return ResponseBuilder.buildResponse(ex);
        }
    }

}
