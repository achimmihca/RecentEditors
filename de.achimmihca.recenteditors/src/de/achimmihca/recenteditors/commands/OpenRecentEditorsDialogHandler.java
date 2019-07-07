package de.achimmihca.recenteditors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.achimmihca.recenteditors.logging.LogWrapper;

public class OpenRecentEditorsDialogHandler extends AbstractHandler {

	private static LogWrapper log = new LogWrapper( OpenRecentEditorsDialogHandler.class );

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		log.info( "open recent editors dialog" );
		return null;
	}
}