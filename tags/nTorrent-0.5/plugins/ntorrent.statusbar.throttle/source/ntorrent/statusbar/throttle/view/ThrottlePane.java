/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ntorrent.statusbar.throttle.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ntorrent.locale.ResourcePool;

/**
 * @author Kim Eik
 *
 */
public class ThrottlePane extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JSpinner up,down;
		
	public ThrottlePane(SpinnerNumberModel upModel, SpinnerNumberModel downModel) {
		up = new JSpinner(upModel);
		down = new JSpinner(downModel);
		
		up.setPreferredSize(new Dimension(60,20));
		down.setPreferredSize(new Dimension(60,20));
		
		up.setToolTipText(ResourcePool.getString("upload", "locale", this));
		down.setToolTipText(ResourcePool.getString("download", "locale", this));
		
		upModel.setMinimum(0);
		downModel.setMinimum(0);
		
		add(up);
		add(down);
	}

	public JSpinner getUp() {
		return up;
	}
	
	public JSpinner getDown() {
		return down;
	}
}
