package dev.alfiecorthine.nickname;

import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NicknameService {
    private final JavaPlugin plugin;
    private final NicknameStore nicknameStore;

    public NicknameService(JavaPlugin plugin, NicknameStore nicknameStore) {
        this.plugin = plugin;
        this.nicknameStore = nicknameStore;
    }

    public Optional<String> getNickname(UUID uniqueId) {
        return this.nicknameStore.getNickname(uniqueId);
    }

    public Optional<String> getRealName(Player player) {
        return this.nicknameStore.getRealName(player.getUniqueId());
    }

    public void setNickname(Player player, String nickname) {
        UUID uniqueId = player.getUniqueId();
        this.nicknameStore.getRealName(uniqueId)
            .orElseGet(() -> {
                this.nicknameStore.setRealName(uniqueId, player.getName());
                return player.getName();
            });

        this.nicknameStore.setNickname(uniqueId, nickname);
        applyProfileName(player, nickname);
    }

    public boolean resetNickname(Player player) {
        UUID uniqueId = player.getUniqueId();
        Optional<String> realName = this.nicknameStore.getRealName(uniqueId);
        if (realName.isEmpty()) {
            return false;
        }

        this.nicknameStore.clearNickname(uniqueId);
        applyProfileName(player, realName.get());
        return true;
    }

    public boolean applySavedNickname(Player player) {
        Optional<String> nickname = this.nicknameStore.getNickname(player.getUniqueId());
        if (nickname.isEmpty()) {
            return false;
        }

        applyProfileName(player, nickname.get());
        return true;
    }

    public boolean isNameTaken(Player player, String requestedName) {
        String normalizedName = requestedName.toLowerCase(Locale.ROOT);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }

            String currentProfileName = onlinePlayer.getPlayerProfile().getName();
            if (onlinePlayer.getName().equalsIgnoreCase(requestedName)
                || (currentProfileName != null && currentProfileName.equalsIgnoreCase(normalizedName))) {
                return true;
            }
        }

        return this.nicknameStore.isNameTakenByAnother(player.getUniqueId(), requestedName);
    }

    private void applyProfileName(Player player, String name) {
        PlayerProfile profile = Bukkit.createProfileExact(player.getUniqueId(), name);
        profile.setProperties(player.getPlayerProfile().getProperties());
        player.setPlayerProfile(profile);

        Component component = Component.text(name);
        player.displayName(component);
        player.playerListName(component);

        this.plugin.getLogger().info("Applied nickname '" + name + "' to " + player.getUniqueId() + ".");
    }
}
