package de.achimmihca.recenteditors;

import java.util.Date;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.models.EditorModel;
import de.achimmihca.recenteditors.models.SettingsModel;
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
			var editorPart = (EditorPart) part;
			var filePath = getFilePath( editorPart );
			if( filePath == null ) {
				return;
			}
			
			var editorModel = settingsService.findEditorModel( filePath ).orElseGet( () -> {
				var newEditorModel = new EditorModel( filePath );
				settingsService.getSettings().addRecentEditor( newEditorModel );
				return newEditorModel;
			} );

			editorModel.setLastCloseTime( new Date() );

			// Save the visible area and cursor position of text editors, to restore them when the editor is re-opened.
			saveEditorState( editorPart, editorModel );
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if( part instanceof EditorPart ) {
			var editorPart = (EditorPart) part;
			var filePath = getFilePath( editorPart );
			if( filePath == null ) {
				return;
			}

			var editorModelOptional = settingsService.findEditorModel( filePath );
			editorModelOptional.ifPresent(
			    (editorModel) -> {
			        // Restore the visible area and cursor position, in case a text editor has been loaded
			        if( settingsService.getSettings().isRestoreEditorState() ) {
					    restoreEditorState( editorPart, editorModel );
				    }

			        // Remove editor model from list of recently closed editors
			        settingsService.editSettings( (SettingsModel settings) -> {
				        settings.removeEditor( editorModel );
			        } );
			    } );
		}
	}

	/**
	 * Returns the associated file path of the editor, or null if none.
	 */
	private String getFilePath(EditorPart editorPart) {
		var editorInput = editorPart.getEditorInput();
		if( editorInput == null ) {
			return null;
		}

		var file = ResourceUtil.getFile( editorInput );
		if( file == null || !file.exists() ) {
			return null;
		}
		var filePath = file.getLocation().toPortableString();
		return filePath;
	}

	/**
	 * Stores the current state of the EditorPart in the EditorModel.
	 * This is the visible area and caret position.
	 */
	private void saveEditorState(EditorPart editorPart, EditorModel editorModel) {
		var textViewer = getTextViewer( editorPart );
		if( textViewer != null ) {
			var visibleLineIndex = textViewer.getTopIndex();
			editorModel.setVisibleLineIndex( visibleLineIndex );

			var selectedRange = textViewer.getSelectedRange();
			editorModel.setSelectionOffset( selectedRange.x );
			editorModel.setSelectionLength( selectedRange.y );
		}
	}

	/**
	 * Restores the state of the EditorPart from the values saved in the EditorModel.
	 * This is the visible area and caret position.
	 */
	private void restoreEditorState(EditorPart editorPart, EditorModel editorModel) {
		var textViewer = getTextViewer( editorPart );
		if( textViewer != null ) {
			var visibleLineIndex = editorModel.getVisibleLineIndex();
			if( visibleLineIndex > 0 ) {
				textViewer.setTopIndex( visibleLineIndex );
			}

			var selectionOffset = editorModel.getSelectionOffset();
			var selectionLength = editorModel.getSelectionLength();
			if( selectionOffset > 0 || selectionLength > 0 ) {
				textViewer.setSelectedRange( selectionOffset, selectionLength );
			}

		}
	}

	/**
	 * Returns the associated ITextViewer of the editor, or null if none.
	 */
	private ITextViewer getTextViewer(EditorPart editorPart) {
		if( editorPart instanceof ITextEditor ) {
			var textEditor = (ITextEditor) editorPart;
			ITextOperationTarget target = (ITextOperationTarget) textEditor.getAdapter( ITextOperationTarget.class );
			if( target instanceof ITextViewer ) {
				ITextViewer viewer = (ITextViewer) target;
				return viewer;
			}
		}
		return null;
	}
}
