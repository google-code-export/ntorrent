package ntorrent;

import java.net.URL;

import ntorrent.splash.NtorrentSplashHandler;

import org.java.plugin.boot.Application;
import org.java.plugin.boot.Boot;
import org.java.plugin.boot.BootErrorHandler;
import org.java.plugin.boot.DefaultApplicationInitializer;
import org.java.plugin.boot.SplashHandler;

public class NtorrentApplicationInitializer extends DefaultApplicationInitializer {
	
	
@Override
public Application initApplication(BootErrorHandler errorHandler,String[] args) throws Exception {
		SplashHandler splash = null;
		if(args.length == 0){
			splash = new NtorrentSplashHandler();
			String cwd = System.getProperty("user.dir");
			splash.setImage(new URL("file:"+cwd+"/plugins/ntorrent/splash.png"));
			Boot.setSplashHandler(splash);
			splash.setVisible(true);
		}
		Application app = super.initApplication(errorHandler, args);
		if(splash != null)
			splash.setVisible(false);
		return app;
	}

}
