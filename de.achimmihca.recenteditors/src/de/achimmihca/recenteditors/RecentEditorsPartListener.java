package de.achimmihca.recenteditors;

import java.util.Date;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.EditorPart;

import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.models.EditorModel;
import de.achimmihca.recenteditors.services.SettingsService;

public class RecentEditorsPartListener implements IPartListener {

	LogWrapper log = new LogWrapper( RecentEditorsPartListener.class );

	SettingsService settingsService;

	public RecentEditorsPartListener(SettingsService settingsService) {
		this.settingsService = settingsService;
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if( part instanceof EditorPart ) {
			var editorModel = createEditorModel( (EditorPart) part, new Date() );
			if( editorModel == null ) {
				return;
			}
			//			log.info( "Updating lastCloseTime of the editor: " + editorModel.getFilePath() );

			settingsService.updateEditorModel( editorModel );
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if( part instanceof EditorPart ) {
			var editorModel = createEditorModel( (EditorPart) part, null );
			if( editorModel == null ) {
				return;
			}
			//			log.info(
			//			    "Removing now open editor from the list of recently closed editors: " + editorModel.getFilePath() );

			settingsService.removeEditorModel( editorModel.getFilePath() );
		}
	}

	private EditorModel createEditorModel(EditorPart editorPart, Date lastCloseTime) {
		var editorInput = editorPart.getEditorInput();
		if( editorInput == null ) {
			return null;
		}

		var file = ResourceUtil.getFile( editorInput );
		if( file == null || !file.exists() ) {
			return null;
		}

		var filePath = file.getLocation().toPortableString();
		var editorModel = new EditorModel( filePath, lastCloseTime );
		return editorModel;
	}
}
