package io.tofpu.speedbridgeupdater.command;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridgeupdater.PterodactylApp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class PluginCommand implements CommandExecutor {
    private final @NotNull PterodactylApp pterodactylApp;

    public PluginCommand(final @NotNull JavaPlugin plugin,
            final @NotNull PterodactylApp pterodactylApp) {
        this.pterodactylApp = pterodactylApp;
        plugin.getCommand("sbupdate").setExecutor(this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("you need to be oped to run this command.");
            return false;
        }

        if (pterodactylApp.hasSentRequest()) {
            sender.sendMessage("You already sent an update request...");
            return false;
        }
        sender.sendMessage("Sending an update request now...");
        pterodactylApp.sendUpdateRequest(sender);
        return false;
    }
}
