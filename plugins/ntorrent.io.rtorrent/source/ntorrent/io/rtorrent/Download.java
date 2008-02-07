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
 * d.
 */
public interface Download {
	Object check_hash();
	Object close();
	Object create_link();
	Object delete_link();
	Object delete_tied();
	Object erase();
	Object get_base_filename();
	Object get_base_path();
	Object get_bytes_done();
	Object get_chunk_size();
	Object get_chunks_hashed();
	Object get_complete();
	Object get_completed_bytes();
	Object get_completed_chunks();
	Object get_connection_current();
	Object get_connection_leech();
	Object get_connection_seed();
	Object get_creation_date();
	Object get_custom1(String hash);
	Object get_custom2();
	Object get_custom3();
	Object get_custom4();
	Object get_custom5();
	Object get_directory();
	Object get_down_rate();
	Object get_down_total();
	Object get_free_diskspace();
	Object get_hash();
	Object get_hashing();
	Object get_ignore_commands();
	Object get_left_bytes();
	Object get_local_id();
	Object get_local_id_html();
	Object get_max_file_size();
	Object get_max_peers();
	Object get_max_size_pex();
	Object get_max_uploads();
	Object get_message();
	Object get_min_peers();
	Object get_mode();
	Object get_name();
	Object get_peer_exchange();
	Object get_peers_accounted();
	Object get_peers_complete();
	Object get_peers_connected();
	Object get_peers_max();
	Object get_peers_min();
	Object get_peers_not_connected();
	Object get_priority();
	Object get_priority_str();
	Object get_ratio();
	Object get_size_bytes();
	Object get_size_chunks();
	Object get_size_files();
	Object get_size_pex();
	Object get_skip_rate();
	Object get_skip_total();
	Object get_state();
	Object get_state_changed();
	Object get_tied_to_file();
	Object get_tracker_focus();
	Object get_tracker_numwant();
	Object get_tracker_size();
	Object get_up_rate();
	Object get_up_total();
	Object get_uploads_max();
	Object is_active();
	Object is_hash_checked();
	Object is_hash_checking();
	Object is_multi_file();
	Object is_open();
	Object is_pex_active();
	Object is_private();
	Object multicall();
	Object open();
	Object set_connection_current();
	Object set_connection_leech();
	Object set_connection_seed();
	Object set_custom1();
	Object set_custom2();
	Object set_custom3();
	Object set_custom4();
	Object set_custom5();
	Object set_directory();
	Object set_ignore_commands();
	Object set_max_file_size();
	Object set_max_peers();
	Object set_max_uploads();
	Object set_message();
	Object set_min_peers();
	Object set_peer_exchange();
	Object set_peers_max();
	Object set_peers_min();
	Object set_priority();
	Object set_tracker_numwant();
	Object set_uploads_max();
	Object start();
	Object stop();
	Object update_priorities();
}
