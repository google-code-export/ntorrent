package ntorrent.torrenttable.model;

public interface TorrentStateListener {
	public void torrentStateChanged(TorrentEvent event);
}
