package me.pyeh.dni.command;

import me.pyeh.dni.DNI;
import me.pyeh.dni.userdata.Userdata;
import me.pyeh.dni.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class DNICommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            if (!sender.hasPermission("dni.permission.admin")) {
                this.usageNormal(sender, label);
                return false;
            }
            this.usageAdmin(sender, label);
            return false;
        }

        if (args[0].equalsIgnoreCase("ver")) {
            Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata((Player) sender);
            if (!userdata.isCheck()) {
                sender.sendMessage(ColorUtils.translate("&cOh no, tu no tienes DNI, busca al npc &6Ayudante DNI &c y podrás obtener uno!"));
                return false;
            }
            DNI.getInstance().getDniManager().viewDNI((Player) sender,(Player) sender);

        } else if (args[0].equalsIgnoreCase("mostrar")) {
            DNI.getInstance().getDniManager().showOtherPlayer((Player) sender);
        } else if (args[0].equalsIgnoreCase("reset")) {
            if (!sender.hasPermission("dni.permission.admin")) {
                sender.sendMessage(ColorUtils.translate("&cNo permission!"));
                return false;
            }

            if (args.length == 1) {
                sender.sendMessage(ColorUtils.translate("&cUsage: /dni reset <player>"));
                return false;
            }
            if (args[1] == null) {
                sender.sendMessage(ColorUtils.translate("&cPlayer &f" + args[1] + "&c not found!"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[1]);
            Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(target);

            userdata.setName_surname("none");
            userdata.setDate_of_birth("none");
            userdata.setNationality("none");
            userdata.setGender("none");
            userdata.setCheck(false);
            sender.sendMessage(ColorUtils.translate("&aYou have just reset the DNI of the user " + target.getName()));

        } else if (args[0].equalsIgnoreCase("alldelete")) {
            if (!sender.hasPermission("dni.permission.admin")) {
                sender.sendMessage(ColorUtils.translate("&cNo permission!"));
                return false;
            }
            sender.sendMessage(ColorUtils.translate("&aYou just removed all the DNI data"));
            DNI.getInstance().getUserdataManager().deleteAllUserdata();

        } else if (args[0].equalsIgnoreCase("adminview")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ColorUtils.translate("&cPlayer &f" + args[1] + "&c not found!"));
                return false;
            }
            DNI.getInstance().getDniManager().viewDNI((Player) sender, target);

        } else {
            sender.sendMessage(ColorUtils.translate("&cArgumento no encontrado!"));
        }
        return false;
    }

    private void usageNormal(CommandSender sender, String label) {
        sender.sendMessage(ColorUtils.translate(" "));
        sender.sendMessage(ColorUtils.translate("&6&lDNI System"));
        sender.sendMessage(ColorUtils.translate("  &6&l♦ &f/" + label + " ver"));
        sender.sendMessage(ColorUtils.translate("  &6&l♦ &f/" + label + " mostrar"));
        sender.sendMessage(ColorUtils.translate(" "));
    }

    private void usageAdmin(CommandSender sender, String label) {
        sender.sendMessage(ColorUtils.translate(" "));
        sender.sendMessage(ColorUtils.translate("&6&lDNI System"));
        sender.sendMessage(ColorUtils.translate("  &6&l♦ &f/" + label + " ver"));
        sender.sendMessage(ColorUtils.translate("  &6&l♦ &f/" + label + " mostrar"));
        sender.sendMessage(ColorUtils.translate("  &6&l♦ &f/" + label + " reset <player>"));
        sender.sendMessage(ColorUtils.translate("  &6&l♦ &f/" + label + " alldelete"));
        sender.sendMessage(ColorUtils.translate(" "));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {

        if (strings.length == 1 && sender.hasPermission("dni.permission.admin")) {
            return ColorUtils.getCompletions(strings, Arrays.asList("ver", "mostrar", "reset", "alldelete"));
        } else if (strings.length == 1 && !sender.hasPermission("dni.permission")) {
            return ColorUtils.getCompletions(strings, Arrays.asList("ver", "mostrar"));
        }
        return null;
    }
}
