package dev.alfiecorthine.nickname;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NicknamePlugin extends JavaPlugin implements Listener {
    private final Map<UUID, String> pendingRealNames = new ConcurrentHashMap<>();

    private NicknameStore nicknameStore;
    private NicknameService nicknameService;

    @Override
    public void onEnable() {
        this.nicknameStore = new NicknameStore(this);
        this.nicknameStore.load();
        this.nicknameService = new NicknameService(this, this.nicknameStore);

        PluginCommand nicknameCommand = getCommand("nickname");
        if (nicknameCommand == null) {
            throw new IllegalStateException("The nickname command is missing from plugin.yml.");
        }

        NicknameCommand executor = new NicknameCommand(this.nicknameService);
        nicknameCommand.setExecutor(executor);
        nicknameCommand.setTabCompleter(executor);

        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uniqueId = player.getUniqueId();
                if (this.nicknameStore.getRealName(uniqueId).isEmpty()
                    && this.nicknameStore.getNickname(uniqueId).isEmpty()) {
                    this.nicknameStore.setRealName(uniqueId, player.getName());
                }

                this.nicknameService.applySavedNickname(player);
            }
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        this.pendingRealNames.put(event.getUniqueId(), event.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        String realName = this.pendingRealNames.remove(uniqueId);

        if (realName != null) {
            this.nicknameStore.setRealName(uniqueId, realName);
        } else if (this.nicknameStore.getRealName(uniqueId).isEmpty()
            && this.nicknameStore.getNickname(uniqueId).isEmpty()) {
            this.nicknameStore.setRealName(uniqueId, player.getName());
        }

        Bukkit.getScheduler().runTask(this, () -> this.nicknameService.applySavedNickname(player));
    }
}

