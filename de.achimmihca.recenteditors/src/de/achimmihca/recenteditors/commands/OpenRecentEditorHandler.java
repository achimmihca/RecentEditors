package de.achimmihca.recenteditors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import de.achimmihca.recenteditors.RecentEditorsActivator;
import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.models.EditorModel;
import de.achimmihca.recenteditors.models.RecentEditorsComparator;
import de.achimmihca.recenteditors.utils.ListUtils;

/**
 * Re-opens the last closed editor.
 */
public class OpenRecentEditorHandler extends AbstractHandler {

	private static LogWrapper log = new LogWrapper( OpenRecentEditorHandler.class );

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//		log.info( "re-opening last closed editor" );
		var settingsService = RecentEditorsActivator.getDefault().getSettingsService();
		var editors = settingsService.getSettings().getRecentEditors();
		if( !ListUtils.isNullOrEmpty( editors ) ) {
			var comparator = new RecentEditorsComparator();
			var editorsList = ListUtils.copy( editors );
			editorsList.sort( comparator );
			var lastClosedEditor = editorsList.get( 0 );
			//			log.info( "Re-opening editor:" + lastClosedEditor );
			openEditor( event, lastClosedEditor );
		} else {
			log.info( "Found no recently closed editors to re-open" );
		}
		return null;
	}

	/**
	 * Opens the Eclipse editor that is associated with the EditorModel.
	 */
	private void openEditor(ExecutionEvent event, EditorModel editor) {
		try {
			var file = editor.toIFile();
			if( file != null ) {
				var workbenchPage = HandlerUtil.getActiveWorkbenchWindow( event ).getActivePage();
				IDE.openEditor( workbenchPage, file );
			}
		} catch( Exception e ) {
			log.error( e, "Could not open editor: " + editor.getFilePath() );
		}
	}
}