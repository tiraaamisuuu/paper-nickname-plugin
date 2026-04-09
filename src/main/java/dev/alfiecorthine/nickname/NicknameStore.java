package dev.alfiecorthine.nickname;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class NicknameStore {
    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config = new YamlConfiguration();

    public NicknameStore(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "nicknames.yml");
    }

    public void load() {
        if (!this.plugin.getDataFolder().exists() && !this.plugin.getDataFolder().mkdirs()) {
            this.plugin.getLogger().warning("Could not create the plugin data folder.");
        }

        if (!this.file.exists()) {
            return;
        }

        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException exception) {
            throw new IllegalStateException("Could not load nicknames.yml", exception);
        }
    }

    public Optional<String> getNickname(UUID uniqueId) {
        return Optional.ofNullable(this.config.getString(path(uniqueId, "nickname")));
    }

    public Optional<String> getRealName(UUID uniqueId) {
        return Optional.ofNullable(this.config.getString(path(uniqueId, "real-name")));
    }

    public void setNickname(UUID uniqueId, String nickname) {
        this.config.set(path(uniqueId, "nickname"), nickname);
        save();
    }

    public void setRealName(UUID uniqueId, String realName) {
        this.config.set(path(uniqueId, "real-name"), realName);
        save();
    }

    public void clearNickname(UUID uniqueId) {
        this.config.set(path(uniqueId, "nickname"), null);

        ConfigurationSection playerSection = this.config.getConfigurationSection("players." + uniqueId);
        if (playerSection != null && playerSection.getKeys(false).isEmpty()) {
            this.config.set("players." + uniqueId, null);
        }

        save();
    }

    public boolean isNameTakenByAnother(UUID excludedUniqueId, String requestedName) {
        String normalizedName = requestedName.toLowerCase(Locale.ROOT);
        ConfigurationSection playersSection = this.config.getConfigurationSection("players");
        if (playersSection == null) {
            return false;
        }

        for (String key : playersSection.getKeys(false)) {
            UUID uniqueId;
            try {
                uniqueId = UUID.fromString(key);
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            if (excludedUniqueId.equals(uniqueId)) {
                continue;
            }

            Set<String> reservedNames = new HashSet<>();
            String realName = this.config.getString(path(uniqueId, "real-name"));
            String nickname = this.config.getString(path(uniqueId, "nickname"));

            if (realName != null) {
                reservedNames.add(realName.toLowerCase(Locale.ROOT));
            }

            if (nickname != null) {
                reservedNames.add(nickname.toLowerCase(Locale.ROOT));
            }

            if (reservedNames.contains(normalizedName)) {
                return true;
            }
        }

        return false;
    }

    private void save() {
        try {
            this.config.save(this.file);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save nicknames.yml", exception);
        }
    }

    private String path(UUID uniqueId, String key) {
        return "players." + uniqueId + "." + key;
    }
}
