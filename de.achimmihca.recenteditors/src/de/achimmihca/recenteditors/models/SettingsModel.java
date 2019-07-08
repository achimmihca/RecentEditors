package de.achimmihca.recenteditors.models;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.achimmihca.recenteditors.utils.JsonUtils;
import de.achimmihca.recenteditors.utils.ListUtils;

public class SettingsModel {
	private int maxRecentEditors = 20;
	private Set<EditorModel> recentEditors = new HashSet<>();

	public List<EditorModel> getRecentEditors() {
		return ListUtils.copy( recentEditors );
	}

	public void setRecentEditors(Set<EditorModel> recentEditors) {
		this.recentEditors = recentEditors;
	}

	public void addRecentEditor(EditorModel newEditorModel) {
		if( !recentEditors.contains( newEditorModel ) ) {
			recentEditors.add( newEditorModel );
			// Remove oldest editor if limit reached.
			if( recentEditors.size() > maxRecentEditors ) {
				removeOldestEditor();
			}
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
			if( oldestEditor == null || editor.getLastCloseTime().before( oldestEditor.getLastCloseTime() ) ) {
				oldestEditor = editor;
			}
		}
		removeEditor( oldestEditor );
	}
}