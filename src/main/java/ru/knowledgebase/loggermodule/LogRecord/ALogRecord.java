package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.logenums.CONSTANTS;
import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.sql.Timestamp;

public abstract class ALogRecord {
	protected OPERATION operationType;
	protected Timestamp time;
	protected int userID;

	@Override
	public abstract String toString();

	protected String addRecordDelimiter(String word){ return word + CONSTANTS.INSIDE_RECORD_SEPARATOR; }

	protected StringBuilder getRecordBase() {
		StringBuilder record_base = new StringBuilder();
		record_base.append(addRecordDelimiter(operationType.toString()));
		record_base.append(addRecordDelimiter(time.toString()));
		record_base.append(addRecordDelimiter(Integer.toString(userID)));
		return record_base;
	}

	public OPERATION getOperationType() {
		return operationType;
	}

	public Timestamp getTime() {
		return time;
	}

	public int getUserID() {
		return userID;
	}
}