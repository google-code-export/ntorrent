package ntorrent.torrentpeers.model;

import java.util.Comparator;

import ntorrent.torrenttable.model.Bit;
import ntorrent.torrenttable.model.Byte;

/**
 * @author Henry Zhou
 * 
 */
public class Peer implements Comparable<Peer>, Comparator<Peer> {
	private Bit upRate, downRate, peerRate;
	private Byte upTotal, downTotal, peerTotal;;
	private String clientVersion, address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Bit getUpRate() {
		return upRate;
	}

	public void setUpRate(long upRate) {
		this.upRate = new Bit(upRate);
	}

	public Bit getDownRate() {
		return downRate;
	}

	public void setDownRate(long downRate) {
		this.downRate = new Bit(downRate);
	}

	public Byte getUpTotal() {
		return upTotal;
	}

	public void setUpTotal(long upTotal) {
		this.upTotal = new Byte(upTotal);
	}

	public Byte getDownTotal() {
		return downTotal;
	}

	public void setDownTotal(long downTotal) {
		this.downTotal = new Byte(downTotal);
	}

	public Bit getPeerRate() {
		return peerRate;
	}

	public void setPeerRate(long peerRate) {
		this.peerRate = new Bit(peerRate);
	}

	public Byte getPeerTotal() {
		return peerTotal;
	}

	public void setPeerTotal(long peerTotal) {
		this.peerTotal = new Byte(peerTotal);
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	@Override
	public int compareTo(Peer o) {
		return getAddress().compareTo(o.getAddress());
	}

	@Override
	public int compare(Peer o1, Peer o2) {
		return o1.compareTo(o2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Peer other = (Peer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

}
