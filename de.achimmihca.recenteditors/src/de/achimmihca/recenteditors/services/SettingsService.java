package de.achimmihca.recenteditors.services;

import java.util.Optional;
import java.util.function.Consumer;

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
			//			log.info( "Loaded settings model " + settings );
		}
		return settings;
	}

	public void editSettings(Consumer<SettingsModel> consumer) {
		consumer.accept( getSettings() );
	}

	public Optional<EditorModel> findEditorModel(String filePath) {
		var allRecentEditors = getSettings().getRecentEditors();
		var matchingRecentEditor = ListUtils.findFirst( allRecentEditors, (it) -> it.getFilePath().equals( filePath ) );
		return matchingRecentEditor;
	}

	public void save() {
		persistenceService.save( SETTINGS_FILENAME, getSettings() );
		//		log.info( "Saved settings model " + getSettings() );
	}
}
