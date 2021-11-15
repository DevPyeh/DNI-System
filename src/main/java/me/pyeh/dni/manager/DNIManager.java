package me.pyeh.dni.manager;

import me.pyeh.dni.DNI;
import me.pyeh.dni.command.DNICommand;
import me.pyeh.dni.userdata.Userdata;
import me.pyeh.dni.utils.ColorUtils;
import me.pyeh.dni.utils.ItemUtils;
import me.pyeh.dni.utils.TaskUtils;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public class DNIManager implements Listener {

    public final Map<UUID, Boolean> checkingCreateDNI;
    public final List<String> surname;
    public final List<String> date;
    public final List<String> nationality;

    public final Map<UUID, Boolean> check;
    public final Map<UUID, Boolean> move;

    public DNIManager() {
        this.checkingCreateDNI = new HashMap<>();
        this.surname = new ArrayList<>();
        this.date = new ArrayList<>();
        this.nationality = new ArrayList<>();

        this.check = new HashMap<>();
        this.move = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, DNI.getInstance());
        DNI.getInstance().getCommand("dni").setExecutor(new DNICommand());
    }

    public void disable() {
    }

    public void creatingDNI(Player sender) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 4, ColorUtils.translate("&eCreando tu DNI"));
        Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(sender);

        ItemStack sur_name = new ItemUtils(Material.LEATHER).setName(ColorUtils.translate("&6Seleccionar Nombre/Apellidos")).setLore(ColorUtils.translate("&eTu nombre es: &f" + userdata.getName_surname()), " ", ColorUtils.translate("&7&nClick derecho para modificar!")).build();
        ItemStack date = new ItemUtils(Material.NETHER_STAR).setName(ColorUtils.translate("&6Seleccionar Fecha de Nacimiento")).setLore(ColorUtils.translate("&eTu fecha es: &f" + userdata.getDate_of_birth()), " ", ColorUtils.translate("&7&nClick derecho para modificar!")).build();
        ItemStack nationality = new ItemUtils(Material.APPLE).setName(ColorUtils.translate("&6Seleccionar Nacionalidad")).setLore(ColorUtils.translate("&eTu nacionalidad es: &f" + userdata.getNationality()), " ", ColorUtils.translate("&7&nClick derecho para modificar!")).build();
        ItemStack gender = null;
        switch (userdata.getGender()) {
            case "none":
            case "Masculino":
                gender = new ItemUtils(Material.NAME_TAG).setName(ColorUtils.translate("&6Seleccionar Genero")).setLore(ColorUtils.translate("  &6&l♦ &aMasculino"), ColorUtils.translate("  &cFemenino"), ColorUtils.translate("  &cSin especificar"), " ", ColorUtils.translate("&7&nClick derecho para modificar!")).build();
                break;
            case "Femenino":
                gender = new ItemUtils(Material.NAME_TAG).setName(ColorUtils.translate("&6Seleccionar Genero")).setLore(ColorUtils.translate("  &cMasculino"), ColorUtils.translate("  &6&l♦ &aFemenino"), ColorUtils.translate("  &cSin especificar"), " ", ColorUtils.translate("&7&nClick derecho para modificar!")).build();
                break;
            case "Sin Especificar":
                gender = new ItemUtils(Material.NAME_TAG).setName(ColorUtils.translate("&6Seleccionar Genero")).setLore(ColorUtils.translate("  &cMasculino"), ColorUtils.translate("  &cFemenino"), ColorUtils.translate("  &6&l♦ &aSin especificar"), " ", ColorUtils.translate("&7&nClick derecho para modificar!")).build();
                break;
        }
        ItemStack placeholder_save = new ItemUtils(Material.LIME_DYE).setName(ColorUtils.translate("&aGuardad DNI!")).build();
        ItemStack placeholder_close = new ItemUtils(Material.REDSTONE).setName(ColorUtils.translate("&cCerrar inventario!")).build();
        inventory.setItem(11, sur_name);
        inventory.setItem(12, date);
        inventory.setItem(14, nationality);
        inventory.setItem(15, gender);
        IntStream.rangeClosed(27, 28).forEach(i -> inventory.setItem(i, placeholder_close));
        IntStream.rangeClosed(34, 35).forEach(i -> inventory.setItem(i, placeholder_save));

        this.checkingCreateDNI.put(sender.getUniqueId(), true);
        sender.openInventory(inventory);
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!player.getOpenInventory().getTitle().equals(ColorUtils.translate("&eCreando tu DNI"))) return;

        if (this.checkingCreateDNI.get(player.getUniqueId())) {
            player.sendMessage(ColorUtils.translate("&cLos datos no fueron guardados satisfactoriamente, en breve se abrirá nuevamente el menú!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            TaskUtils.aSyncDelayed(() -> this.creatingDNI(player), 40L);
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (!player.getOpenInventory().getTitle().equals(ColorUtils.translate("&eCreando tu DNI"))) return;
        if(event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) return;
        event.setCancelled(true);
        Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(player);
        switch (slot) {
            case 11: {
                this.surname.add(player.getName());
                this.checkingCreateDNI.put(player.getUniqueId(), false);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
                player.sendMessage(ColorUtils.translate("  "));
                player.sendMessage(ColorUtils.translate("&eDebes escribir tu nombre y apellido, si colocas '&ccancel&e' en el chat se cancelará el proceso y serás regresado al formulario."));
                player.sendMessage(ColorUtils.translate("  "));
                player.closeInventory();
                break;
            }
            case 12: {
                this.date.add(player.getName());
                this.checkingCreateDNI.put(player.getUniqueId(), false);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
                player.sendMessage(ColorUtils.translate("  "));
                player.sendMessage(ColorUtils.translate("&eDebes escribir tu fecha, si colocas '&ccancel&e' en el chat se cancelará el proceso y serás regresado al formulario."));
                player.sendMessage(ColorUtils.translate("&cFormato: <dd/mm/aaaa>"));
                player.sendMessage(ColorUtils.translate("  "));
                player.closeInventory();
                break;
            }
            case 14: {
                this.nationality.add(player.getName());
                this.checkingCreateDNI.put(player.getUniqueId(), false);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
                player.sendMessage(ColorUtils.translate("  "));
                player.sendMessage(ColorUtils.translate("&eDebes escribir tu nacionalidad, si colocas '&ccancel&e' en el chat se cancelará el proceso y serás regresado al formulario."));
                player.sendMessage(ColorUtils.translate("  "));
                player.closeInventory();
                break;
            }
            case 15: {
                switch (userdata.getGender()) {
                    case "none":
                    case "Masculino":
                        userdata.setGender("Femenino");
                        this.checkingCreateDNI.put(player.getUniqueId(), false);
                        player.closeInventory();
                        this.creatingDNI(player);
                        break;
                    case "Femenino":
                        userdata.setGender("Sin Especificar");
                        this.checkingCreateDNI.put(player.getUniqueId(), false);
                        player.closeInventory();
                        this.creatingDNI(player);
                        break;
                    case "Sin Especificar":
                        userdata.setGender("Masculino");
                        this.checkingCreateDNI.put(player.getUniqueId(), false);
                        player.closeInventory();
                        this.creatingDNI(player);
                        break;
                }
                break;
            }
            case 27:
            case 28: {
                player.sendMessage(ColorUtils.translate("&cAcabas de cerrar el inventario de registro de tu DNI, puedes volver a interactuar con el NPC!"));
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURREOW, 1.0f, 1.0f);
                this.checkingCreateDNI.put(player.getUniqueId(), false);
                userdata.setName_surname("none");
                userdata.setDate_of_birth("none");
                userdata.setNationality("none");
                userdata.setGender("Unspecified");
                userdata.setCheck(false);
                player.closeInventory();
                break;
            }
            case 34:
            case 35: {
                player.sendMessage(ColorUtils.translate("&aAcabas de terminar de registrar tu DNI!"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                this.checkingCreateDNI.put(player.getUniqueId(), false);
                player.closeInventory();
                this.move.put(player.getUniqueId(), false);
                TaskUtils.aSyncDelayed( () -> player.sendMessage(ColorUtils.translate("&e<&6Ayudante DNI&e> &eQue rápido eres &f" + player.getName() + "!&e tu registro quedó listo y ahora eres un ciudadano")), 0L);
                TaskUtils.aSyncDelayed( () -> player.sendMessage(ColorUtils.translate("&e<&6Ayudante DNI&e> &eRecuerda seguir las normas. En fin, eso es todo conmigo. Distruta tu estadía!")), 100L);
            }
        }
    }

    public void viewDNI(Player sender, Player target) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3 , ColorUtils.translate("&7Documento de identidad"));
        Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(target);
        ItemStack itemStack = new ItemUtils(Material.WRITABLE_BOOK).setName("&6Información Personal ✔").setLore(ColorUtils.translate("  &6&l♦ &eNombre Completo: &f" + userdata.getName_surname()), ColorUtils.translate("  &6&l♦ &eFecha de Nacimiento: &f" + userdata.getDate_of_birth()),
                ColorUtils.translate("  &6&l♦ &eNacionalidad: &f" + userdata.getNationality()), ColorUtils.translate("  &6&l♦ &eGénero: &f" + userdata.getGender()), ColorUtils.translate("  &6&l♦ &eTrabajo: &f" + ColorUtils.getPrefix(target)),
                ColorUtils.translate("  &6&l♦ &eEstado Civil: &f" + ColorUtils.getCivil(target))).build();
        ItemStack verifiqued = new ItemUtils(Material.NAME_TAG).setName(ColorUtils.translate("&aEste documento es verídico ✔")).build();

        ItemStack panel_lime = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemStack panel_yellow = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemStack panel_marrow = new ItemStack(Material.BROWN_STAINED_GLASS_PANE, 1);

        inventory.setItem(0, panel_marrow);
        IntStream.rangeClosed(1, 7).forEach(i -> inventory.setItem(i, panel_yellow));
        inventory.setItem(8, panel_marrow);
        inventory.setItem(9, panel_marrow);
        inventory.setItem(10, panel_yellow);
        inventory.setItem(11, panel_lime);
        inventory.setItem(12, itemStack);
        inventory.setItem(13, panel_lime);
        inventory.setItem(14, verifiqued);
        inventory.setItem(15, panel_lime);
        inventory.setItem(16, panel_yellow);
        inventory.setItem(17, panel_marrow);
        inventory.setItem(18, panel_marrow);
        IntStream.rangeClosed(19, 25).forEach(i -> inventory.setItem(i, panel_yellow));
        inventory.setItem(26, panel_marrow);
        sender.openInventory(inventory);
    }

    @EventHandler
    public void onClickInventoryView(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!player.getOpenInventory().getTitle().equals(ColorUtils.translate("&7Documento de identidad"))) return;
        if (event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) return;
        event.setCancelled(true);
    }

    public void showOtherPlayer(Player player) {
        List<Entity> nearby = player.getNearbyEntities(5,5,5);
        player.sendMessage(ColorUtils.translate("&aAcabas de enviar la invitación para que vean el DNI!"));
        nearby.forEach(entity -> {
            if (entity instanceof Player) {
                if (entity.hasPermission("dni.permission.show-other")) {
                    Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(player);
                    if (!userdata.isCheck()) {
                        entity.sendMessage("  ");
                        entity.sendMessage("  ");
                        entity.sendMessage(ColorUtils.translate("&cEl usuario &f" + player.getName() + " &cno tiene DNI registrado!"));
                        entity.sendMessage("  ");
                        entity.sendMessage("  ");
                        return;
                    }
                    ComponentBuilder message = new ComponentBuilder(ColorUtils.translate("  &eEl usuario &f" + player.getName() + " &eacaba de aceptar la visualización de su dni, interactua para verlo!"));
                    String hoverText = ColorUtils.translate("&7Click derecho para ver el DNI!");
                    message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create())).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dni adminview " + player.getName()));

                    entity.sendMessage("  ");
                    entity.sendMessage("  ");
                    entity.spigot().sendMessage(message.create());
                    entity.sendMessage("  ");
                    entity.sendMessage("  ");
                }
            }
        });
    }

    @EventHandler
    public void onInteractiveNPC(NPCRightClickEvent event) {
        Player player = event.getClicker();
        String name = event.getNPC().getName();
        if (name.equals(ColorUtils.translate("&6Ayudante DNI"))) {
            Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(player);
            if (userdata.isCheck()) {
                player.sendMessage(ColorUtils.translate("&e<&6Ayudante DNI&e> &eOh vaya, por lo que veo, ya has creado tu DNI, no hace falta interactuar conmigo!"));
                return;
            }
            userdata.setCheck(true);
            this.move.put(player.getUniqueId(), true);
            TaskUtils.aSyncDelayed( () -> player.sendMessage(ColorUtils.translate("&e<&6Ayudante DNI&e> &eOh vaya, bienvenido &f" + player.getName() + "&e a nuestro aeropuerto!")), 0L);
            TaskUtils.aSyncDelayed( () -> player.sendMessage(ColorUtils.translate("&a<You> &aHola, acabo de notar que puedo crear mi DNI con tu ayuda, ¿es verdad?")), 60L);
            TaskUtils.aSyncDelayed( () -> player.sendMessage(ColorUtils.translate("&e<&6Ayudante DNI&e> &ePor supuesto, ahora mismo te entrego el formulario!")), 120L);
            TaskUtils.aSyncDelayed( () -> this.creatingDNI(player), 180L);
        }
    }

    @EventHandler
    public void onChatSurName(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (this.surname.contains(player.getName())) {
            event.setCancelled(true);
            String message = event.getMessage();
            if (message.equalsIgnoreCase("cancel")) {
                this.surname.remove(player.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage(ColorUtils.translate("&cEl proceso ha sido cancelado!"));
                this.check.put(player.getUniqueId(), true);
                return;
            }
            this.surname.remove(player.getName());
            Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(player);
            userdata.setName_surname(message);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ColorUtils.translate("&aSe registró tu nombre correctamente!"));
            this.check.put(player.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onChatDate(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (this.date.contains(player.getName())) {
            event.setCancelled(true);
            String message = event.getMessage();
            if (message.equalsIgnoreCase("cancel")) {
                this.date.remove(player.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage(ColorUtils.translate("&cEl proceso ha sido cancelado!"));
                this.check.put(player.getUniqueId(), true);
                return;
            }
            this.date.remove(player.getName());
            Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ColorUtils.translate("&aSe registró tus datos correctamente!"));
            userdata.setDate_of_birth(message);
            this.check.put(player.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onChatNationality(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (this.nationality.contains(player.getName())) {
            event.setCancelled(true);
            String message = event.getMessage();
            if (message.equalsIgnoreCase("cancel")) {
                this.nationality.remove(player.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage(ColorUtils.translate("&cEl proceso ha sido cancelado!"));
                this.check.put(player.getUniqueId(), true);
                return;
            }
            this.nationality.remove(player.getName());
            Userdata userdata = DNI.getInstance().getUserdataManager().getUserdata(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ColorUtils.translate("&aSe registró tu nacinalidad correctamente!"));
            userdata.setNationality(message);
            this.check.put(player.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onMovePlayer(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.check.get(player.getUniqueId())) {
            this.creatingDNI(player);
            this.check.put(player.getUniqueId(), false);
        }
    }

    @EventHandler
    public void onMoveInteractiveCreateDNI(PlayerMoveEvent event ) {
        Player player = event.getPlayer();
        if (this.move.get(player.getUniqueId())) {
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.check.put(event.getPlayer().getUniqueId(), false);
        this.move.put(event.getPlayer().getUniqueId(), false);
    }
}
