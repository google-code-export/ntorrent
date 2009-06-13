package ntorrent.error;

import javax.swing.JOptionPane;

import org.java.plugin.boot.BootErrorHandler;
import org.java.plugin.registry.IntegrityCheckReport;
import org.java.plugin.registry.IntegrityCheckReport.ReportItem;

public class NtorrentErrorHandler implements BootErrorHandler {
	@Override
	public boolean handleError(String message, Exception e) {
		System.out.println(message);
		e.printStackTrace();
		return JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
	}

	@Override
	public boolean handleError(String message,
			IntegrityCheckReport integrityCheckReport) {
		System.out.println(message);
		for(ReportItem item : integrityCheckReport.getItems()){
			System.out.println(item.getMessage());
			System.out.println(item.getSource());
		}
		return JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
	}

	@Override
	public void handleFatalError(String message) {
		JOptionPane.showMessageDialog(null, message);
		System.out.println(message);
	}

	@Override
	public void handleFatalError(String message, Throwable t) {
		JOptionPane.showMessageDialog(null, message);
		System.out.println(message);
		t.printStackTrace();
	}

}
