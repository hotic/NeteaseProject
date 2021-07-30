package me.asgard.neteaseproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.asgard.neteaseproject.config.Config;
import me.asgard.neteaseproject.config.NeteaseConfig;
import me.asgard.neteaseproject.module.BaseModule;
import me.asgard.neteaseproject.service.OrderService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class NeteaseProject extends JavaPlugin {
    @Inject
    private ScheduledExecutorService scheduledExecutorService;
    @Inject
    private OrderService orderService;
    @Inject
    private Config config;
    @Inject
    @Named("yamlObjectMapper")
    private ObjectMapper objectMapper;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Injector injector = Guice.createInjector(new BaseModule(this));
        injector.injectMembers(this);
        loadConfig();
//        System.out.println(this.neteaseConfig);
        start();
        //injector.getInstance(NeteaseProject.class).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void start() {
        scheduledExecutorService.scheduleAtFixedRate(()-> Bukkit.getOnlinePlayers().forEach(p-> orderService.shipItem(p)), config.getConfig().getOrderPostInterval(), config.getConfig().getOrderPostInterval(), TimeUnit.SECONDS);
    }

    private void loadConfig () {
        try {
            this.config.setConfig(objectMapper.readValue(new File(getDataFolder(), "config.yml"), NeteaseConfig.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();
        if ("np".equals(cmd)) {
            if (sender.isOp()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        loadConfig();
                        sender.sendMessage("§7§l[§f§lAsgard§7§l] §7NeteaseProject 重载成功");
                    }
                }
            }
        }
        return false;
    }
}
