package ru.knowledgebase.analyticsmodule;

import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.analyticsmodule.rank.OperationFrequency;
import ru.knowledgebase.analyticsmodule.rank.RequestRank;
import ru.knowledgebase.loggermodule.Log.LogReader;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.enums.OPERATION;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 01.10.16.
 */
public class LogAnalyser {
    private Analyser analyser = Analyser.getInstance();
    private List<ALogRecord> log;

    public  LogAnalyser() {
        log = LogReader.getInstance().getRecordsFromLog();
    }

    public List<ArticleRank> getPopularArticles() {
        return analyser.getPopularArticles(log);
    }

    public HashMap<String, List<ArticleRank>> getRelevantArticles() throws Exception {
        return analyser.getRelevantArticles(log);
    }

    public List<RequestRank> getPopularRequests() throws Exception {
        return analyser.getPopularRequests(log);
    }

    public HashMap<OPERATION, OperationFrequency> getOperationFrequency() {
        return analyser.getOperationFrequency(log);
    }

    public HashMap<OPERATION, Long> getAverageOperationTime() {
        return analyser.getAverageOperationTime(log);
    }

    public HashSet<Integer> getUploadedArticles() {
        return analyser.getUploadedArticles(log);
    }

    public Integer getSearchUsage() {
        return analyser.getSearchUsage(log);
    }

    public HashMap<Integer, LinkedList<String>> getUserRequests() {
        return analyser.getUserRequests(log);
    }

    public List<ArticleRank> getPopularArticles(Timestamp from, Timestamp to) throws Exception {
        return analyser.getPopularArticles(log, from, to);
    }

    public List<ArticleRank> getPopularArticles(int parentId, Timestamp from, Timestamp to) throws Exception {
        return analyser.getPopularArticles(log, parentId, from, to);
    }

    public List<ArticleRank> getPopularArticles(int parentId) throws Exception {
        return analyser.getPopularArticles(log, parentId);
    }

    public HashMap<String, List<ArticleRank>> getRelevantArticles(Timestamp from, Timestamp to) throws Exception {
        return analyser.getRelevantArticles(log, from, to);
    }

    public HashMap<String, List<ArticleRank>> getRelevantArticles(int parentId, Timestamp from, Timestamp to) throws Exception {
        return analyser.getRelevantArticles(log, parentId, from, to);
    }

    public HashMap<String, List<ArticleRank>> getRelevantArticles(int parentId) throws Exception {
        return analyser.getRelevantArticles(log, parentId);
    }

    public List<RequestRank> getPopularRequests(Timestamp from, Timestamp to) throws Exception {
        return analyser.getPopularRequests(log, from, to);
    }

    public HashMap<OPERATION, OperationFrequency> getOperationFrequency(Timestamp from, Timestamp to) throws Exception {
        return analyser.getOperationFrequency(log, from, to);
    }

    public HashMap<OPERATION, Long> getAverageOperationTime(Timestamp from, Timestamp to) throws Exception {
        return analyser.getAverageOperationTime(log, from, to);
    }

    public HashSet<Integer> getUploadedArticles(Timestamp from, Timestamp to) throws Exception {
        return analyser.getUploadedArticles(log, from, to);
    }

    public HashSet<Integer> getUploadedArticles(int parentId, Timestamp from, Timestamp to) throws Exception {
        return analyser.getUploadedArticles(log, parentId, from, to);
    }

    public HashSet<Integer> getUploadedArticles(int parentId) throws Exception {
        return analyser.getUploadedArticles(log, parentId);
    }

    public Integer getSearchUsage(int parentId, Timestamp from, Timestamp to) throws Exception {
        return analyser.getSearchUsage(log, parentId, from, to);
    }

    public Integer getSearchUsage(Timestamp from, Timestamp to) throws Exception {
        return analyser.getSearchUsage(log, from, to);
    }

    public Integer getSearchUsage(int parentId) throws Exception {
        return analyser.getSearchUsage(log, parentId);
    }

    public List<ArticleRank> getUserViews(int userId) {
        return analyser.getUserViews(log, userId);
    }

    public List<ArticleRank> getUserViews(int userId, Timestamp from, Timestamp to) {
        return analyser.getUserViews(log, userId, from, to);
    }

    public HashMap<Integer, LinkedList<String>> getUserRequests(int parentId) throws Exception {
        return analyser.getUserRequests(log, parentId);
    }

    public HashMap<Integer, LinkedList<String>> getUserRequests(int parentId, Timestamp from, Timestamp to) throws Exception {
        return analyser.getUserRequests(log, parentId, from, to);
    }

    public HashMap<Integer, LinkedList<String>> getUserRequests(Timestamp from, Timestamp to) throws Exception {
        return analyser.getUserRequests(log, from, to);
    }
}
