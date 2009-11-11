package ntorrent.torrenttable.model;

public class TorrentEvent {
	public static final int UNKNOWN = -1;
	public static final int TORRENT_OPEN = 100;
	public static final int TORRENT_START = 1 + TORRENT_OPEN;
	public static final int TORRENT_STOP = 1 + TORRENT_START;
	public static final int TORRENT_CLOSE = 1 + TORRENT_STOP;
	public static final int TORRENT_ERASE = 1 + TORRENT_CLOSE;
	
	protected transient Torrent source;
	protected int oldState;
	protected int newState;
	
	public TorrentEvent(Torrent source, int oldState, int newState) {
		this.source = source;
		this.oldState = oldState;
		this.newState = newState;
	}
	
	public Torrent getSource() {
		return this.source;
	}
}
