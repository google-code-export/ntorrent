package ntorrent;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import ntorrent.connection.model.ConnectionProfile;
import ntorrent.gui.MainWindow;
import ntorrent.io.rtorrent.Global;
import ntorrent.locale.ResourcePool;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.settings.view.SettingsComponentFactory;
import ntorrent.tools.Serializer;

import org.apache.log4j.Logger;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.util.ExtendedProperties;

public class NtorrentApplication extends ApplicationPlugin implements Application, SettingsExtension {


	/** PluginManager **/
	public static PluginManager MANAGER;
	
	/** GUI **/
	public static MainWindow MAIN_WINDOW;
	
	/** Sessions **/
	public static final Vector<Session> SESSIONS = new Vector<Session>();
	
	/** Settings **/
	public static NtorrentSettingsModel SETTINGS = new NtorrentSettingsModel();
	
	/** Settings facilitator **/
	public static SettingsComponentFactory scf;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(NtorrentApplication.class);
	
	@Override
	protected Application initApplication(ExtendedProperties config,String[] args) throws Exception {
		log.info("initApplication() called");
		try{
			log.info("restoring settings from NtorrentSettingsModel");
			NtorrentSettingsModel model = Serializer.deserialize(NtorrentSettingsModel.class);
			if(model != null)
				SETTINGS = model;
		}catch(Exception x){
			log.debug("could not restore ntorrent settings", x);
		}
		
		log.info("setting public PluginManager");
		MANAGER = getManager();
		
		log.info("init SettingsComponentFactory");
		scf = new SettingsComponentFactory(SETTINGS);
		
		log.info("init MainWindow");
		MAIN_WINDOW = new MainWindow();
		
		//TODO restore language
		//ResourcePool.setLocale(Environment.getUserLanguage(),Environment.getUserCountry());
		
		/** Loading environment **/

		/*File ntorrent = Environment.getNtorrentDir();
		if(!(ntorrent.isDirectory() || ntorrent.mkdir()))
			Logger.global.log(Level.WARNING,"Could not create directory:"+ntorrent);*/
		
		/** Load logging **/
		/*try{
			if(Environment.isLogging())
				new SystemLog();
		}catch(Exception x){
			x.printStackTrace();
		}*/
		
		
		/** UImanager, set look and feel **/
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		/** restore plugins **/
		//errors with plugin framework if this isn't threaded
		/*new Thread(){
			public void run(){
				main.getJpf().restore();
			}
		}.start();*/
		return this;
	}

	@Override
	protected void doStart() throws Exception {
		log.info(this.getClass()+": doStart() called");
	}

	@Override
	protected void doStop() throws Exception {
		log.info(this.getClass()+": doStop() called");
	}

	@Override
	public void startApplication() throws Exception {
		log.info(this.getClass()+": startApplication() called");
		
		/** License **/
		log.info("printing license");
		System.out.println(ResourcePool.getString("lic",this));
		
		/** autoconnect **/
		/*try{
			for(ClientProfileInterface p : Serializer.deserialize(ClientProfileListModel.class, Environment.getNtorrentDir()))
				if(p.isAutoConnect())
					newSession(p);
		}catch(Exception e){
			Logger.global.log(Level.WARNING,e.getMessage(),e);
		}
		
		if(!(SESSIONS.size() > 0)){
			newSession();
		}
		*/
		/** Start socket server **/
		/*try{
			new Server().start();
			new Thread(){
				public void run(){
					for(String line : Environment.getArgs()){
						clientSoConn(line);
						break; //break since its not possible to add torrent without being connected, just show message.
					}
				}
			}.start();
		}catch(IOException e){
			Logger.global.info(e.getMessage());
			try {
				new Client(Environment.getArgs());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}*/
		
		/** Draw Gui **/
		MAIN_WINDOW.drawWindow();
		
		/** Start a new session **/
		MAIN_WINDOW.newSession();
	}
	
	private void newSession(ConnectionProfile p) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Sends a raw byte for byte torrent file to a session and starts the torrent.
	 * @param torrent
	 * @param target
	 */
	private static void sendTorrentFileToSession(File torrent,Session target){
		if(target != null && torrent.exists() && torrent.isFile()){
			Global global = target.getConnection().getGlobalClient();
			try {
				byte[] source = new byte[(int)torrent.length()];
				FileInputStream reader = new FileInputStream(torrent);
				reader.read(source, 0, source.length);					
				global.load_raw_start(source);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Sends a string representing a torrent url to a session and calls the load_start method.
	 * @param url
	 * @param target
	 */
	private static void sendTorrentUrlToSession(String url, Session target){
		if(url != null && target != null){
			Global global = target.getConnection().getGlobalClient();
			global.load_start(url);
		}
	}

	/**
	 * Handles the dialogue between the user when adding a torrent.
	 * Three outcomes are possible. there is only one session, therefore
	 * automatically loaded to that session. There can be several sessions,
	 * then the user must choose which to add the torrent to.
	 * and third, no sessions are connected, in that case return false.
	 * @param line
	 * @return
	 */
	public static boolean addTorrentToSession(final String line){
		File f = new File(line);
		log.info(line);
		Vector<Session> sessionList = new Vector<Session>();
			for(Session s : SESSIONS){
				if(s.isConnected())
					sessionList.add(s);
			}
		
		Session target = null;
		if(sessionList.size() > 1){
			target = (Session) JOptionPane.showInputDialog(
					MAIN_WINDOW,
					null, 
					null, 
					JOptionPane.PLAIN_MESSAGE,
					null, 
					sessionList.toArray(),
					null
					);
		}else if(sessionList.size() == 1){
			target = sessionList.get(0);
		}
		
		if(f.exists() && f.isFile()){
			sendTorrentFileToSession(f, target);
		}else{
			sendTorrentUrlToSession(line, target);
		}
		
		return target != null;
	}
	
	/**
	 * The first method being called when adding a torrent to the session.
	 * The string parameter can be either an torrent url, or a file path to a torrent.
	 * @param line
	 */
	public static void clientSoConn(final String line) {
		if(!addTorrentToSession(line)){
			JOptionPane.showMessageDialog(MAIN_WINDOW, "The torrent "+line+" will be added once you connect.");
			//TODO redo this to instead checking every few seconds. listen for session list changes.
			new Thread(){
				@Override
				public void run() {
					while(!addTorrentToSession(line)){
						try {
							sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}

	@Override
	public Component getSettingsDisplay() {
		return scf.getDisplay();
	}

	@Override
	public void saveActionPerformedOnSettings() throws Exception {
		scf.restoreToModel();
		Serializer.serialize(SETTINGS);
	}

	@Override
	public String getSettingsDisplayName() {
		return this.getDescriptor().getId();
	}

}
