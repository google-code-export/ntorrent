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
package ntorrent.gui.profile;

import javax.swing.JComponent;

import ntorrent.gui.profile.model.ClientProfileInterface;
import ntorrent.gui.profile.model.HttpProfileModel;
import ntorrent.gui.profile.model.LocalProfileModel;
import ntorrent.gui.profile.model.SshProfileModel;
import ntorrent.gui.profile.view.AbstractClientProfileView;
import ntorrent.gui.profile.view.ClientProfileView;
import ntorrent.gui.profile.view.HttpProfileView;
import ntorrent.gui.profile.view.LocalProfileView;
import ntorrent.gui.profile.view.SshProfileView;



public class ClientProfileController {
	LocalProfileModel localModel = new LocalProfileModel();
	SshProfileModel sshModel = new SshProfileModel();
	HttpProfileModel httpModel = new HttpProfileModel();
	
	LocalProfileView localView = new LocalProfileView(localModel);
	SshProfileView sshView = new SshProfileView(sshModel);
	HttpProfileView httpView = new HttpProfileView(httpModel);
	ClientProfileView mainView;
	
	ProfileRequester req;
	
	public ClientProfileController(ProfileRequester r) {
		req = r;
		AbstractClientProfileView[] views = {localView,sshView,httpView};
		mainView = new ClientProfileView(views,this);
	}
	
	
	public JComponent getDisplay() {
		return mainView.getDisplay();
	}

	public void connectEvent(ClientProfileInterface model) {
		req.sendProfile(model);
	}

}
