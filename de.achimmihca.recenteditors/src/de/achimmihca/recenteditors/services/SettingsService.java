package de.achimmihca.recenteditors.services;

import java.util.Optional;

import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.models.EditorModel;
import de.achimmihca.recenteditors.models.SettingsModel;
import de.achimmihca.recenteditors.persistence.IPersistenceService;
import de.achimmihca.recenteditors.persistence.RecentEditorsPluginPersistenceService;
import de.achimmihca.recenteditors.utils.ListUtils;

/**
 * Queries, updates and persists settings.
 * @author andre
 *
 */
public class SettingsService {
	private static LogWrapper log = new LogWrapper( SettingsService.class );

	private static final String SETTINGS_FILENAME = "settings.json";

	private IPersistenceService persistenceService = new RecentEditorsPluginPersistenceService();
	private SettingsModel settings;

	/**
	 * Returns the current settings model. It will be loaded from the file system if not done yet.
	 * If there is no persisted settings model that can be loaded,
	 * then a new default settings model is returned.
	 * <br><br>
	 * This method is intended for reading the settings. To change settings use {@link #editAndSave}.
	 * @return
	 */
	public SettingsModel getSettings() {
		if( settings == null ) {
			Optional<SettingsModel> loadedSettings = persistenceService.load( SETTINGS_FILENAME, SettingsModel.class );
			settings = loadedSettings.orElseGet( () -> {
				log.info( "Loaded default settings model" );
				return new SettingsModel();
			} );
			settings.removeNonExistingEditors();
			log.info( "Loaded settings model " + settings );
		}
		return settings;
	}

	public void updateEditorModel(EditorModel newEditorModel) {
		var oldRecentEditors = getSettings().getRecentEditors();
		var existingEditor =
		    ListUtils.findFirst( oldRecentEditors, (it) -> it.getFilePath().equals( newEditorModel.getFilePath() ) )
		        .orElse( null );
		if( existingEditor != null ) {
			existingEditor.setLastCloseTime( newEditorModel.getLastCloseTime() );
			save();
		} else {
			getSettings().addRecentEditor( newEditorModel );
			save();
		}
		//		log.info( "Saved changed settings model with updated editor: " + getSettings() );
	}

	public void removeEditorModel(String filePath) {
		var editorOptional =
		    ListUtils.findFirst( getSettings().getRecentEditors(), (it) -> it.getFilePath().equals( filePath ) );
		editorOptional.ifPresent( (it) -> getSettings().removeEditor( it ) );
		//		log.info( "Saved settings model without the editor '" + filePath + "': " + getSettings() );
	}

	private void save() {
		persistenceService.save( SETTINGS_FILENAME, getSettings() );
	}
}
