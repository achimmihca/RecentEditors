package de.achimmihca.recenteditors.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.achimmihca.recenteditors.RecentEditorsActivator;
import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.models.RecentEditorsComparator;
import de.achimmihca.recenteditors.utils.ListUtils;

/**
 * Re-opens the last closed editor.
 */
public class OpenRecentEditorHandler extends AbstractOpenRecentEditorHandler {

	private static LogWrapper log = new LogWrapper( OpenRecentEditorHandler.class );

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		log.info( "opening last closed editor" );
		var settingsService = RecentEditorsActivator.getDefault().getSettingsService();
		var editors = settingsService.getSettings().getRecentEditors();
		if( !ListUtils.isNullOrEmpty( editors ) ) {
			var comparator = new RecentEditorsComparator();
			editors.sort( comparator );
			var lastClosedEditor = editors.get( 0 );
			log.info( "Re-opening editor:" + lastClosedEditor );
			openEditor( event, lastClosedEditor );
		} else {
			log.info( "Found no recently closed editors to re-open" );
		}
		return null;
	}
}