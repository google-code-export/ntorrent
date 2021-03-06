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
package ntorrent.io.rtorrent;

/**
 * This interface governs the f.* commands as written in rtorrent.
 * @author Kim Eik
 *
 */
public interface File {
	Object get_completed_chunks();	 
  	Object get_frozen_path();
  	Object get_is_created();
  	Object get_is_open();
  	Object get_last_touched();
  	Object get_match_depth_next();
  	Object get_match_depth_prev();
  	Object get_offset();
  	Object get_path(String hash, int l);
  	Object get_path_components();
  	Object get_path_depth();
  	Object get_priority();
  	Object get_range_first();
  	Object get_range_second();
  	Object get_size_bytes();
  	Object get_size_chunks();
  	Object multicall();
  	Object set_priority(String hash, int offset, int value);
}
