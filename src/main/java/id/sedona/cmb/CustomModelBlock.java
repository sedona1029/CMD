package id.sedona.cmb;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomModelBlock extends JavaPlugin {
    public static CustomModelBlock plugin;

    @Override
    public void onEnable() {
        plugin = this;

        setupNoteBlockEvent();

        getLogger().info("CMB enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("CMB disabled");
    }

    private void setupNoteBlockEvent() {
        Events(new NoteBlockEvent());
    }

    private void Events(Listener... listeners) {
        for (Listener listener : listeners)
            Bukkit.getPluginManager().registerEvents(listener, this);

    }
}
