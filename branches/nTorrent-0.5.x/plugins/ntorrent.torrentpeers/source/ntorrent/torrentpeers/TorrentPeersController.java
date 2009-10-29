package ntorrent.torrentpeers;

import ntorrent.session.ConnectionSession;
import ntorrent.session.DefaultSessionExtension;

/**
 * @author Henry Zhou
 *
 */
public class TorrentPeersController extends DefaultSessionExtension<TorrentPeersInstance> {

	@Override
	protected TorrentPeersInstance getNewSessionInstance(
			ConnectionSession session) {
		return new TorrentPeersInstance(session);
	}

}
