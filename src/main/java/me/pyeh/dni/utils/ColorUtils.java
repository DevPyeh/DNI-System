package me.pyeh.dni.utils;

import com.google.common.base.Preconditions;
import com.lenis0012.bukkit.marriage2.MarriageAPI;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ColorUtils {

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String getPrefix(Player player) {
        return LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
    }

    public static String getCivil(Player player) {
        String str;
        boolean value = MarriageAPI.getInstance().getMPlayer(player).isMarried();
        if (value) {
            str = "&aCasado";
        } else {
            str = "&cSoltero";
        }
        return str;
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        return getCompletions(args, input, 80);
    }

    public static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull(args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length()))
                .limit(limit).collect(Collectors.toList());
    }
}
