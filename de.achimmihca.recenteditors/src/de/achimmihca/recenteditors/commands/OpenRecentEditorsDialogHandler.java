package de.achimmihca.recenteditors.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.achimmihca.recenteditors.logging.LogWrapper;

/**
 * Opens the recent editors dialog
 */
public class OpenRecentEditorsDialogHandler extends AbstractOpenRecentEditorHandler {

	private static LogWrapper log = new LogWrapper( OpenRecentEditorsDialogHandler.class );

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		log.info( "open recent editors dialog" );
		return null;
	}
}