package de.achimmihca.recenteditors.models;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.achimmihca.recenteditors.utils.JsonUtils;
import de.achimmihca.recenteditors.utils.ListUtils;

public class SettingsModel {
	/**
	 * Maximum number of editors that should be persisted.
	 */
	private int maxRecentEditors = 50;

	/**
	 * Determines whether the last persisted state of an editor should be restored when the editor is re-opened.
	 * The editor state includes the last scroll position and selection.
	 */
	private boolean restoreEditorState = true;

	private Set<EditorModel> recentEditors = new HashSet<>();

	public Set<EditorModel> getRecentEditors() {
		return recentEditors;
	}

	public void setRecentEditors(Set<EditorModel> recentEditors) {
		this.recentEditors = recentEditors;
	}

	public void addRecentEditor(EditorModel newEditorModel) {
		if( !recentEditors.contains( newEditorModel ) ) {
			// Remove oldest editor if limit reached.
			if( recentEditors.size() >= maxRecentEditors ) {
				removeOldestEditor();
			}
			recentEditors.add( newEditorModel );
		}
	}

	public int getMaxRecentEditors() {
		return maxRecentEditors;
	}

	public void setMaxRecentEditors(int maxRecentEditors) {
		this.maxRecentEditors = maxRecentEditors;
	}

	@Override
	public String toString() {
		return JsonUtils.toJsonPrettyPrint( this );
	}

	public void removeEditor(EditorModel editor) {
		recentEditors.remove( editor );
	}

	public void removeNonExistingEditors() {
		for( var editor : ListUtils.copy( recentEditors ) ) {
			var filePath = editor.getFilePath();
			if( filePath == null || ( !new File( filePath ).exists() ) ) {
				removeEditor( editor );
			}
		}
	}

	private void removeOldestEditor() {
		EditorModel oldestEditor = null;
		for( var editor : recentEditors ) {
			var lastCloseTime = editor.getLastCloseTime();
			if( oldestEditor == null || lastCloseTime == null
			    || isBefore( lastCloseTime, oldestEditor.getLastCloseTime() ) ) {
				oldestEditor = editor;
			}
		}
		removeEditor( oldestEditor );
	}

	private boolean isBefore(Date date1, Date date2) {
		// null values are considered to be older than non-null values.
		if( date1 == null ) {
			return ( date2 != null );
		} else if( date2 == null ) {
			return false;
		}
		return date1.before( date2 );
	}

	public boolean isRestoreEditorState() {
		return restoreEditorState;
	}

	public void setRestoreEditorState(boolean restoreEditorState) {
		this.restoreEditorState = restoreEditorState;
	}
}