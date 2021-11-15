package me.pyeh.dni;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.pyeh.dni.manager.DNIManager;
import me.pyeh.dni.placeholder.PlaceHolderExplansion;
import me.pyeh.dni.placeholder.PlaceHolderReplacer;
import me.pyeh.dni.userdata.UserdataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Modifier;

public class DNI extends JavaPlugin {

    @Getter public static DNI instance;

    @Getter private Gson gson;
    @Getter private UserdataManager userdataManager;
    @Getter private DNIManager dniManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();
        this.registerGson();
        this.registerExtension();
        this.userdataManager = new UserdataManager();
        this.dniManager = new DNIManager();
    }

    @Override
    public void onDisable() {
        this.userdataManager.disable();
        this.dniManager.disable();
        instance = null;
    }

    private void registerGson() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
                .enableComplexMapKeySerialization().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC).create();
    }


    private void registerExtension() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderExplansion(this).register();
        }
        if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
            new PlaceHolderReplacer("dni_display_name").register();
            new PlaceHolderReplacer("dni_gender").register();
            new PlaceHolderReplacer("dni_nationality").register();
            new PlaceHolderReplacer("dni_date_of_birth").register();
        }
    }
}
