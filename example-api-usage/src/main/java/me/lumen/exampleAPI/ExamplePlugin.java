package me.lumen.exampleAPI;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        //check the plugin exists before using it
        if (Bukkit.getPluginManager().isPluginEnabled("Map-Resetter")){
            /*
            you couldn't put the api tasks in here instead of a separate method if the dependency was optional, as the required classes may not exist,
            and you would get class def errors
            */
            ExampleAPIUsage.run(this);
        }
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
