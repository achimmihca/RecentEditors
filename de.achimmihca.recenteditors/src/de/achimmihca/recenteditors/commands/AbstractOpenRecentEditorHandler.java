package de.achimmihca.recenteditors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.models.EditorModel;

abstract public class AbstractOpenRecentEditorHandler extends AbstractHandler {
	private static LogWrapper log = new LogWrapper( AbstractOpenRecentEditorHandler.class );

	/**
	 * Opens the Eclipse editor that is associated with the EditorModel.
	 */
	public void openEditor(ExecutionEvent event, EditorModel editor) {
		try {
			var fileLocation = editor.getFilePath();
			var filePath = Path.fromPortableString( fileLocation );
			var file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation( filePath );
			var workbenchPage = HandlerUtil.getActiveWorkbenchWindow( event ).getActivePage();
			IDE.openEditor( workbenchPage, file );
		} catch( Exception e ) {
			log.error( e, "Could not open editor: " + editor.getFilePath() );
		}
	}
}
