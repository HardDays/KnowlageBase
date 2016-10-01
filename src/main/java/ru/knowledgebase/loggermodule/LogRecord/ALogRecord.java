package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.Constants.CONSTANTS;
import ru.knowledgebase.loggermodule.enums.OPERATION;

import java.sql.Timestamp;

/**
 * Class {@code ALogRecord} represents an abstract class of records which could be written to Log.
 */
public abstract class ALogRecord {
	protected OPERATION operationType;
	protected Timestamp time;
	protected int userID;

	@Override
	public abstract String toString();

	/**
	 * Adds spacial separator to the end of input part of record {@code word} to separate parts of the the
	 * record from each other.
	 * @param word
	 * @return
     */
	protected String addRecordSeparator(String word){ return word + CONSTANTS.INSIDE_RECORD_SEPARATOR; }

	public OPERATION getOperationType() {
		return operationType;
	}

	public void setOperationType(OPERATION operationType) {
		this.operationType = operationType;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * Creates a {@code StringBuilder} of class' fields {@code operationType}, {@code time} and
	 * {@code userID}.
	 * @return
     */
	protected StringBuilder getRecordBase() {
		StringBuilder recordBase = new StringBuilder();
		recordBase.append(addRecordSeparator(operationType.toString()));
		recordBase.append(addRecordSeparator(time.toString()));
		recordBase.append(addRecordSeparator(Integer.toString(userID)));
		return recordBase;
	}
}