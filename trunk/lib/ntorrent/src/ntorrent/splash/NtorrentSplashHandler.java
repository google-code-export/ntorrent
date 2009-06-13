package ntorrent.splash;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.java.plugin.boot.SplashHandler;
import org.java.plugin.util.ExtendedProperties;

public class NtorrentSplashHandler implements SplashHandler {

	private final static Frame frame = new Frame();
	private URL imageUrl;
	

	public NtorrentSplashHandler() {
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setFocusable(false);
		frame.setVisible(false);
	}
	
	@Override
	public void configure(ExtendedProperties config) {
		
	}

	@Override
	public URL getImage() {
		return imageUrl;
	}

	@Override
	public Object getImplementation() {
		return null;
	}

	@Override
	public float getProgress() {
		return 0;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public boolean isVisible() {
		return frame.isVisible();
	}

	@Override
	public void setImage(URL value) {
		this.imageUrl = value;
		Image image = Toolkit.getDefaultToolkit().getImage(value);
		frame.setSize(image.getWidth(null),image.getHeight(null));
		frame.add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	@Override
	public void setProgress(float value) {
		
	}

	@Override
	public void setText(String value) {
		
	}

	@Override
	public void setVisible(boolean value) {
		frame.setVisible(value);
	}


}
