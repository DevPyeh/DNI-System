package me.pyeh.dni.userdata;

import me.pyeh.dni.DNI;
import me.pyeh.dni.utils.ColorUtils;
import me.pyeh.dni.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserdataManager implements Listener {

    protected final Map<UUID, Userdata> userdata;

    public UserdataManager() {
        this.userdata = new ConcurrentHashMap<>();

        Bukkit.getOnlinePlayers().forEach(this::loadUserdata);
        Bukkit.getPluginManager().registerEvents(this, DNI.getInstance());
    }

    public void disable() {
        this.userdata.keySet().forEach(uuid -> this.saveUserdata(uuid, false));
        this.userdata.clear();
    }

    private void loadUserdata(Player player) {
        this.loadUserdata(player.getUniqueId(), player.getName());
    }

    protected void loadUserdata(UUID uuid, String name) {
        if (this.userdata.containsKey(uuid)) return;

        File file = new File(new File(DNI.getInstance().getDataFolder(), "userdata"),uuid + ".json");

        if (!file.exists()) {
            this.userdata.put(uuid, new Userdata(uuid, name));
            return;
        }

        String content = FileUtils.readWholeFile(file);
        if (content == null) return;

        this.userdata.put(uuid, DNI.getInstance().getGson().fromJson(content, Userdata.class));
    }

    private void saveUserdata(Player player) {
        this.saveUserdata(player.getUniqueId(), true);
    }

    private void saveUserdata(UUID uuid, boolean remove) {
        Userdata userdata = this.getUserdata(uuid);
        if (userdata == null) return;

        File file = FileUtils.getOrCreateFile(new File(DNI.getInstance().getDataFolder(), "userdata") , uuid + ".json");

        FileUtils.writeString(file, DNI.getInstance().getGson().toJson(userdata, Userdata.class));
        if(remove) this.userdata.remove(uuid);
    }

    public void saveUserdata(Userdata userdata) {
        if (userdata == null) return;

        File file = FileUtils.getOrCreateFile(new File(DNI.getInstance().getDataFolder(), "userdata") , userdata.getUuid() + ".json");
        FileUtils.writeString(file, DNI.getInstance().getGson().toJson(userdata, Userdata.class));
    }

    public Userdata getUserdata(Player player) {
        return this.getUserdata(player.getUniqueId());
    }

    public Userdata getUserdata(UUID uuid) {
        return this.userdata.get(uuid);
    }

    public Userdata getUserdata(OfflinePlayer player) {
        if(this.userdata.containsKey(player.getUniqueId())) return this.getUserdata(player.getUniqueId());

        File file = new File(new File(DNI.getInstance().getDataFolder(), "userdata"), player.getUniqueId() + ".json");
        if(!file.exists()) return null;

        String content = FileUtils.readWholeFile(file);
        if(content == null) return null;

        Userdata userdata = DNI.getInstance().getGson().fromJson(content, Userdata.class);
        this.userdata.put(player.getUniqueId(), userdata);

        return userdata;
    }

    public void deleteAllUserdata() {
        File file = new File(DNI.getInstance().getDataFolder(), "userdata");
        file.delete();
        this.userdata.clear();

        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();
            this.userdata.put(uuid, new Userdata(uuid, player.getName()));
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        this.loadUserdata(event.getUniqueId(), event.getName());

        if(this.getUserdata(event.getUniqueId()) == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ColorUtils.translate("&cError 001 - Communicate the error to the operator (Pyeh)"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.saveUserdata(event.getPlayer());
    }

}
