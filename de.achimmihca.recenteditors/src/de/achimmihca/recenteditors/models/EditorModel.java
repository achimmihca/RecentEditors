package de.achimmihca.recenteditors.models;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import de.achimmihca.recenteditors.utils.JsonUtils;

public class EditorModel {
	private String filePath;
	private Date lastCloseTime;

	public EditorModel() {
	}

	public EditorModel(String filePath, Date lastCloseTime) {
		this.filePath = filePath;
		this.lastCloseTime = lastCloseTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJsonPrettyPrint( this );
	}

	///////////////////////////////////////
	// Getters and Setters

	public String getFilePath() {
		return filePath;
	}

	public String getName() {
		return FilenameUtils.getName( filePath );
	}

	public Date getLastCloseTime() {
		return lastCloseTime;
	}

	public void setLastCloseTime(Date lastCloseTime) {
		this.lastCloseTime = lastCloseTime;
	}
}
