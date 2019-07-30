package de.achimmihca.recenteditors;

import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.achimmihca.recenteditors.models.SettingsModel;
import de.achimmihca.recenteditors.services.SettingsService;

public class RecentEditorsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	SettingsService settingsService;

	Spinner maxRecentEditorsSpinner;
	Button restoreEditorStateCheckBox;

	@Override
	public void init(IWorkbench workbench) {
		settingsService = RecentEditorsActivator.getDefault().getSettingsService();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = createFilledComposite( parent );
		
		var row = createRow( composite );
		Label maxRecentEditorsLabel = new Label( row, SWT.NULL );
		maxRecentEditorsLabel.setText( "Maximum number of recent editors in history:" );
		maxRecentEditorsSpinner = new Spinner( row, SWT.NULL );
		maxRecentEditorsSpinner.setMinimum( 0 );

		restoreEditorStateCheckBox = new Button( composite, SWT.CHECK );
		restoreEditorStateCheckBox.setText( "Restore the scroll position and selection when re-opening text editors" );

		reloadControlValuesFromSettings();

		return composite;
	}

	@Override
	public boolean performOk() {
		SettingsModel settingsModel = settingsService.getSettings();
		settingsModel.setRestoreEditorState( restoreEditorStateCheckBox.getSelection() );
		settingsModel.setMaxRecentEditors( maxRecentEditorsSpinner.getSelection() );
		return true;
	}

	@Override
	public void performDefaults() {
		reloadControlValuesFromSettings();
	}

	private void reloadControlValuesFromSettings() {
		restoreEditorStateCheckBox.setSelection( settingsService.getSettings().isRestoreEditorState() );
		maxRecentEditorsSpinner.setSelection( settingsService.getSettings().getMaxRecentEditors() );
	}

	protected Composite createFilledComposite(Composite parent) {
		Composite composite = new Composite( parent, SWT.NULL );
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
		return composite;
	}

	protected Composite createRow(Composite parent) {
		Composite composite = new Composite( parent, SWT.NULL );
		RowLayout layout = RowLayoutFactory.swtDefaults().wrap( false ).create();
		composite.setLayout( layout );
		return composite;
	}
}
