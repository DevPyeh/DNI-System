package me.pyeh.dni.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.pyeh.dni.DNI;
import org.bukkit.entity.Player;

public class PlaceHolderReplacer implements PlaceholderReplacer {

    private final String identifier;

    public PlaceHolderReplacer(final String identifier) {
        this.identifier = identifier;
    }

    public void register() {
        PlaceholderAPI.registerPlaceholder(DNI.getInstance(), this.identifier, this);
    }

    public String onPlaceholderReplace(final PlaceholderReplaceEvent event) {
        if (!event.isOnline()) {
            return  "";
        }

        if (event.getPlayer() == null) {
            return "";
        }

        final Player player = event.getPlayer();
        if (this.identifier.equalsIgnoreCase("dni_display_name")) {
            return DNI.getInstance().getUserdataManager().getUserdata(player.getUniqueId()).getName_surname();
        } else if (identifier.equalsIgnoreCase("dni_gender")) {
            return DNI.getInstance().getUserdataManager().getUserdata(player.getUniqueId()).getGender();
        } else if (identifier.equalsIgnoreCase("dni_nationality")) {
            return DNI.getInstance().getUserdataManager().getUserdata(player.getUniqueId()).getNationality();
        } else {
            if (!identifier.equalsIgnoreCase("dni_date_of_birth")) {
                return null;
            }
            return DNI.getInstance().getUserdataManager().getUserdata(player.getUniqueId()).getDate_of_birth();
        }
    }


}