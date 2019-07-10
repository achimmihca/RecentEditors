package de.achimmihca.recenteditors.models;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import de.achimmihca.recenteditors.utils.JsonUtils;
import de.achimmihca.recenteditors.utils.StringUtils;

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

	public IFile toIFile() {
		if( StringUtils.isNullOrEmpty( filePath ) ) {
			return null;
		}
		var pathHandle = Path.fromPortableString( filePath );
		var fileHandle = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation( pathHandle );
		return fileHandle;
	}
}
