package de.achimmihca.recenteditors;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

/**
 * IStartup implementation to register the RecentEditorsPartListener.
 * The listener updates the lastCloseTime when an EditorPart is closed.
 */
public class RecentEditorsPluginStartup implements IStartup {

	@Override
	public void earlyStartup() {
		var partListener = RecentEditorsActivator.getDefault().getPartListener();
		for( var workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows() ) {
			workbenchWindow.getPartService().addPartListener( partListener );
		}
	}

}
