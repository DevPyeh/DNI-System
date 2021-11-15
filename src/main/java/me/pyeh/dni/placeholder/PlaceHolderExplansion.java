package me.pyeh.dni.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pyeh.dni.DNI;
import org.bukkit.entity.Player;

public class PlaceHolderExplansion extends PlaceholderExpansion {

    private final DNI plugin;

    public PlaceHolderExplansion(final DNI plugin) {
        this.plugin = plugin;
    }

    public String getIdentifier() {
        return "dni";
    }

    public String getAuthor() {
        return "Pyeh";
    }

    @Deprecated
    public String getPlugin() {
        return null;
    }

    public String getVersion() {
        return "0.0.1";
    }

    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (identifier.equalsIgnoreCase("display_name")) {
            return this.plugin.getUserdataManager().getUserdata(player.getUniqueId()).getName_surname();
        } else if (identifier.equalsIgnoreCase("gender")) {
            return this.plugin.getUserdataManager().getUserdata(player.getUniqueId()).getGender();
        } else if (identifier.equalsIgnoreCase("nationality")) {
            return this.plugin.getUserdataManager().getUserdata(player.getUniqueId()).getNationality();
        } else {
            if (!identifier.equalsIgnoreCase("date_of_birth")) {
                return null;
            }
            return this.plugin.getUserdataManager().getUserdata(player.getUniqueId()).getDate_of_birth();
        }
    }

}
