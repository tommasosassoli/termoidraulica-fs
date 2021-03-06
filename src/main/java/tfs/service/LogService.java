package tfs.service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import tfs.gui.view.ViewManager;

/**
 * Wrapper Class for Log4j2 plugin
 *
 * levels:
 * -> OFF	0
 * -> FATAL	100
 * -> ERROR	200
 * -> WARN	300
 * -> INFO	400
 * -> DEBUG	500
 * -> TRACE	600
 * -> ALL	Integer.MAX_VALUE
 *
 * */
public class LogService {
	public static <T> void error(Class<T> classToLog, String msg) {
		LogService.error(classToLog, msg, false);
	}

	public static <T> void error(Class<T> classToLog, String msg, boolean reportToUser) {
		LogService.error(classToLog, msg, reportToUser, null);
	}

	public static <T> void error(Class<T> classToLog, String msg, boolean reportToUser, Throwable thrown) {
		Logger log = LogManager.getLogger(classToLog.getName());
		log.error(msg, thrown);

		try {
			if (reportToUser)
				ViewManager.launchErrorDialog(msg);
		} catch (ExceptionInInitializerError e) {
			System.err.println("Cannot open error dialog, maybe you are in test mode...");
		}
	}

	public static <T> void warning(Class<T> classToLog, String msg) {
		LogService.warning(classToLog, msg, false);
	}

	public static <T> void warning(Class<T> classToLog, String msg, boolean reportToUser) {
		LogService.warning(classToLog, msg, reportToUser, null);
	}

	public static <T> void warning(Class<T> classToLog, String msg, boolean reportToUser, Throwable thrown) {
		Logger log = LogManager.getLogger(classToLog.getName());
		log.warn(msg, thrown);

		try {
			if (reportToUser)
				ViewManager.launchWarningDialog(msg);
		} catch (ExceptionInInitializerError e) {
			System.err.println("Cannot open warning dialog, maybe you are in test mode...");
		}
	}

	public static void info(Class<?> classToLog, String msg) {
		LogService.info(classToLog, msg, false);
	}

	public static void info(Class<?> classToLog, String msg, boolean reportToUser) {
		Logger log = LogManager.getLogger(classToLog.getName());
		log.info(msg);

		try {
			if (reportToUser)
				ViewManager.launchInfoDialog(msg);
		} catch (ExceptionInInitializerError e) {
			System.err.println("Cannot open info dialog, maybe you are in test mode...");
		}
	}

	public static void debug(Class<?> classToLog, String msg) {
		Logger log = LogManager.getLogger(classToLog.getName());
		log.debug(msg);
	}

	public static void trace(Class<?> classToLog, String msg) {
		Logger log = LogManager.getLogger(classToLog.getName());
		log.trace(msg);
	}
}
