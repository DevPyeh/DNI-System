package me.pyeh.dni.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ItemUtils {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemUtils(Material material) {
        this(material, 1, 0);
    }

    public ItemUtils(Material material, int amount, int durability) {
        this.itemStack = new ItemStack(material, amount, (short) durability);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemUtils setName(String name) {
        this.itemMeta.setDisplayName(ColorUtils.translate(name));
        return this;
    }

    public ItemUtils setLore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemUtils setSkullOwner(String owner) {
        try {
            ((SkullMeta) this.itemMeta).setOwner(owner);
        } catch(ClassCastException ignored) { }

        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

}
