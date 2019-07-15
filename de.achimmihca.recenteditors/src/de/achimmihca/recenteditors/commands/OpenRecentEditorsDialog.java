package de.achimmihca.recenteditors.commands;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import de.achimmihca.recenteditors.models.EditorModel;
import de.achimmihca.recenteditors.models.RecentEditorsComparator;

public class OpenRecentEditorsDialog extends FilteredResourcesSelectionDialog {

	private Map<IFile, EditorModel> fileToEditorModelMap = new HashMap<>();

	public OpenRecentEditorsDialog(Shell shell, Collection<EditorModel> recentEditors) {
		super( shell, true, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE );
		for( var editor : recentEditors ) {
			var fileHandle = editor.toIFile();
			this.fileToEditorModelMap.put( fileHandle, editor );
		}
		setTitle( "Open Recently Closed Files" );
		setInitialPattern( "**" );
		setSelectionHistory( new EmptySelectionHistory() );
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
	    IProgressMonitor progressMonitor) {
		if( itemsFilter instanceof ResourceFilter ) {
			for( var file : fileToEditorModelMap.keySet() ) {
				contentProvider.add( file, itemsFilter );
			}
		}
		progressMonitor.done();
	}

	@Override
	protected Comparator<Object> getItemsComparator() {
		var comparator = new Comparator<>() {
			RecentEditorsComparator editorComparator = new RecentEditorsComparator();

			@Override
			public int compare(Object o1, Object o2) {
				var editor1 = fileToEditorModelMap.get( (IFile) o1 );
				var editor2 = fileToEditorModelMap.get( (IFile) o2 );
				return editorComparator.compare( editor1, editor2 );
			}
		};
		return comparator;
	}

	private static class EmptySelectionHistory extends SelectionHistory {

		@Override
		protected void storeItemToMemento(Object item, IMemento memento) {

		}

		@Override
		protected Object restoreItemFromMemento(IMemento memento) {
			return null;
		}
	}
}
