package ntorrent;

import java.io.File;
import java.net.URL;

import ntorrent.splash.NtorrentSplashHandler;

import org.apache.log4j.Logger;
import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.boot.Boot;
import org.java.plugin.boot.SplashHandler;
import org.java.plugin.util.ExtendedProperties;

public class NtorrentApplicationPlugin extends ApplicationPlugin {

	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(NtorrentApplication.class);
	
	@Override
	protected Application initApplication(ExtendedProperties config,String[] args) throws Exception {
		System.out.println(getDescriptor().getExtensionPoints());
		return new NtorrentApplication(getManager());
	}

	@Override
	protected void doStart() throws Exception {
		log.info(this.getClass()+": doStart() called");
	}

	@Override
	protected void doStop() throws Exception {
		log.info(this.getClass()+": doStop() called");
	}

}
