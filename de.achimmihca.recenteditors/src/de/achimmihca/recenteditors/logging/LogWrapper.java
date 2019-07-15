package de.achimmihca.recenteditors.logging;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import de.achimmihca.recenteditors.RecentEditorsActivator;

public class LogWrapper {
	private ILog log;
	private Class<?> clazz;

	public LogWrapper(Class<?> clazz) {
		if( RecentEditorsActivator.getDefault() != null ) {
			log = RecentEditorsActivator.getDefault().getLog();
		} else {
			log = new LogToStdStream();
		}
		this.clazz = clazz;
	}

	public LogWrapper(Class<?> clazz, ILog log) {
		this.log = log;
		this.clazz = clazz;
	}

	public void info(Exception e) {
		info( e, e.getMessage() );
	}

	public void info(String message, Object... messageVariables) {
		info( null, message, messageVariables );
	}

	public void info(Exception e, String message, Object... messageVariables) {
		if( messageVariables != null && messageVariables.length > 0 ) {
			message = String.format( message, messageVariables );
		}
		Status status =
		    new Status( IStatus.INFO, RecentEditorsActivator.PLUGIN_ID, clazz.getName() + ":" + message, e );
		log.log( status );
	}

	public void warn(Exception e) {
		warn( e, e.getMessage() );
	}

	public void warn(String message, Object... messageVariables) {
		warn( null, message, messageVariables );
	}

	public void warn(Exception e, String message, Object... messageVariables) {
		if( messageVariables != null && messageVariables.length > 0 ) {
			message = String.format( message, messageVariables );
		}
		Status status =
		    new Status( IStatus.WARNING, RecentEditorsActivator.PLUGIN_ID, clazz.getName() + ":" + message, e );
		log.log( status );
	}

	public void error(Exception e) {
		error( e, e.getMessage() );
	}

	public void error(String message, Object... messageVariables) {
		error( null, message, messageVariables );
	}

	public void error(Exception e, String message, Object... messageVariables) {
		if( messageVariables != null && messageVariables.length > 0 ) {
			message = String.format( message, messageVariables );
		}
		Status status =
		    new Status( IStatus.ERROR, RecentEditorsActivator.PLUGIN_ID, clazz.getName() + ":" + message, e );
		log.log( status );
	}

}
