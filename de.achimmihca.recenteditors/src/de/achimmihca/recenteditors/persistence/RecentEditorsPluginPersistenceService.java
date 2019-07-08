package de.achimmihca.recenteditors.persistence;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.achimmihca.recenteditors.RecentEditorsActivator;

public class RecentEditorsPluginPersistenceService extends FilePersistenceService {

	public RecentEditorsPluginPersistenceService() {
		String persistentDataPathLocation;
		if( RecentEditorsActivator.getDefault() != null ) {
			persistentDataPathLocation =
			    RecentEditorsActivator.getDefault().getStateLocation().makeAbsolute().toOSString();
		} else {
			persistentDataPathLocation = "persistentData";
		}
		Path persistentDataPath = Paths.get( persistentDataPathLocation );
		setPersistentDataPath( persistentDataPath );
	}
}
