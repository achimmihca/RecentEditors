package de.achimmihca.recenteditors.models;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import de.achimmihca.recenteditors.utils.JsonUtils;
import de.achimmihca.recenteditors.utils.StringUtils;

public class EditorModel {
	/**
	 * The file path identifies an EditorModel.
	 * Equality (and hashCode) is defined using this field.
	 */
	private String filePath;

	/**
	 * Timestamp of the last time the associated editor was closed.
	 */
	private Date lastCloseTime;

	/**
	 * The smallest currently visible line number.
	 */
	private int visibleLineIndex;

	/**
	 * Offset of the selection.
	 */
	private int selectionOffset;

	/**
	 * Length of the selection.
	 */
	private int selectionLength;

	public EditorModel() {
	}

	public EditorModel(String filePath) {
		this( filePath, null );
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

	@Override
	public int hashCode() {
		if( filePath == null ) {
			return 0;
		}
		return filePath.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if( !( other instanceof EditorModel ) ) {
			return false;
		}
		var otherEditorModel = (EditorModel) other;
		var otherFilePath = otherEditorModel.getFilePath();
		if( filePath == null ) {
			return otherFilePath == null;
		}
		return filePath.equals( otherFilePath );
	}

	public int getVisibleLineIndex() {
		return visibleLineIndex;
	}

	public void setVisibleLineIndex(int visibleLineIndex) {
		this.visibleLineIndex = visibleLineIndex;
	}

	public int getSelectionOffset() {
		return selectionOffset;
	}

	public void setSelectionOffset(int selectionOffset) {
		this.selectionOffset = selectionOffset;
	}

	public int getSelectionLength() {
		return selectionLength;
	}

	public void setSelectionLength(int selectionLength) {
		this.selectionLength = selectionLength;
	}
}
