package dev.alfiecorthine.nickname;

import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
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
        if (!this.nicknameStore.getNickname(uniqueId).isPresent()) {
            return false;
        }

        Optional<String> realName = this.nicknameStore.getRealName(uniqueId);
        if (!realName.isPresent()) {
            return false;
        }

        this.nicknameStore.clearNickname(uniqueId);
        applyProfileName(player, realName.get());
        return true;
    }

    public boolean applySavedNickname(Player player) {
        Optional<String> nickname = this.nicknameStore.getNickname(player.getUniqueId());
        if (!nickname.isPresent()) {
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
        PlayerProfile profile = createExactProfile(player.getUniqueId(), name);
        profile.setProperties(player.getPlayerProfile().getProperties());
        player.setPlayerProfile(profile);

        player.setDisplayName(name);
        player.setPlayerListName(name);

        this.plugin.getLogger().info("Applied nickname '" + name + "' to " + player.getUniqueId() + ".");
    }

    private PlayerProfile createExactProfile(UUID uniqueId, String name) {
        try {
            // Paper 1.18+ provides this API and does not replace the supplied
            // nickname with a cached online-player profile.
            return (PlayerProfile) Bukkit.class
                .getMethod("createProfileExact", UUID.class, String.class)
                .invoke(null, uniqueId, name);
        } catch (NoSuchMethodException ignored) {
            // Paper 1.16 and 1.17 require the older mutable-profile API.
            PlayerProfile profile = Bukkit.createProfile(uniqueId, name);
            try {
                PlayerProfile.class.getMethod("setName", String.class).invoke(profile, name);
                return profile;
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Could not set the player profile name.", exception);
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not create an exact player profile.", exception);
        }
    }
}
