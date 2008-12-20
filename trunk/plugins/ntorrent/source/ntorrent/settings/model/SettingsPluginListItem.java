package ntorrent.settings.model;

public class SettingsPluginListItem {
	private final String label;
	private final SettingsExtension plugin;

	public SettingsPluginListItem(final String label, final SettingsExtension plugin) {
		this.label = label;
		this.plugin = plugin;
	}
	
	public SettingsExtension getPlugin() {
		return this.plugin;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
}
