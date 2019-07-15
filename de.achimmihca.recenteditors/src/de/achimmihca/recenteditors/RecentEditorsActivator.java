package de.achimmihca.recenteditors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.achimmihca.recenteditors.logging.LogWrapper;
import de.achimmihca.recenteditors.services.SettingsService;

/**
 * The activator class controls the plug-in life cycle
 */
public class RecentEditorsActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.achimmihca.recenteditors";

	private static RecentEditorsActivator plugin;

	private SettingsService settingsService;
	private RecentEditorsPartListener partListener;

	private LogWrapper log;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start( context );
		plugin = this;

		log = new LogWrapper( RecentEditorsActivator.class, getLog() );
		settingsService = new SettingsService();
		partListener = new RecentEditorsPartListener( settingsService );
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop( context );

		// Remove listener
		try {
			for( var workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows() ) {
				workbenchWindow.getPartService().removePartListener( partListener );
			}
		} catch(Exception e) {
			log.error( e );
		}

		// Save settings
		try {
			settingsService.save();
		} catch( Exception e ) {
			log.error( e );
		}
	}

	public static RecentEditorsActivator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin( PLUGIN_ID, path );
	}

	public SettingsService getSettingsService() {
		return settingsService;
	}

	public RecentEditorsPartListener getPartListener() {
		return partListener;
	}
}
