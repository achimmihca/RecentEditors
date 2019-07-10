package de.achimmihca.recenteditors.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import de.achimmihca.recenteditors.RecentEditorsActivator;
import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.utils.ListUtils;

/**
 * Opens the recent editors dialog
 */
public class OpenRecentEditorsDialogHandler extends AbstractHandler {

	@SuppressWarnings("unused")
	private static LogWrapper log = new LogWrapper( OpenRecentEditorsDialogHandler.class );

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//		log.info( "opening recently closed files dialog" );

		// Prompt the user to select files
		final List<IFile> files = queryFileResource();

		// Open the selected files
		if( !ListUtils.isNullOrEmpty( files ) ) {

			final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if( window == null ) {
				throw new ExecutionException( "no active workbench window" ); //$NON-NLS-1$
			}

			final IWorkbenchPage page = window.getActivePage();
			if( page == null ) {
				throw new ExecutionException( "no active workbench page" ); //$NON-NLS-1$
			}

			try {
				for( Iterator<IFile> it = files.iterator(); it.hasNext(); ) {
					IDE.openEditor( page, it.next(), true );
				}
			} catch( final PartInitException e ) {
				throw new ExecutionException( "error opening file in editor", e ); //$NON-NLS-1$
			}
		}

		return null;
	}

	/**
	 * Query the user for the resources that should be opened
	 *
	 * @return the resources that should be opened.
	 */
	private final List<IFile> queryFileResource() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if( window == null ) {
			return null;
		}

		var shell = window.getShell();
		var settingsService = RecentEditorsActivator.getDefault().getSettingsService();
		var recentEditors = settingsService.getSettings().getRecentEditors();

		final var dialog = new OpenRecentEditorsDialog( shell, recentEditors );
		final int resultCode = dialog.open();
		if( resultCode != Window.OK ) {
			return null;
		}

		final Object[] result = dialog.getResult();
		if( result != null ) {
			List<IFile> files = new ArrayList<>();
			for( Object fileResource : result ) {
				if( fileResource instanceof IFile ) {
					files.add( (IFile) fileResource );
				}
			}
			return files;
		}
		return null;
	}
}