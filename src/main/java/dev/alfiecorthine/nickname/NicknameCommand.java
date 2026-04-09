package dev.alfiecorthine.nickname;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class NicknameCommand implements CommandExecutor, TabCompleter {
    private static final Pattern VALID_NICKNAME = Pattern.compile("^[A-Za-z0-9_]{1,16}$");

    private final NicknameService nicknameService;

    public NicknameCommand(NicknameService nicknameService) {
        this.nicknameService = nicknameService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("Usage: /" + label + " <name|reset>", NamedTextColor.YELLOW));
            return true;
        }

        String input = args[0].trim();
        if (input.isEmpty()) {
            player.sendMessage(Component.text("Your nickname cannot be empty.", NamedTextColor.RED));
            return true;
        }

        String loweredInput = input.toLowerCase(Locale.ROOT);
        if (loweredInput.equals("reset") || loweredInput.equals("clear") || loweredInput.equals("off")) {
            if (!this.nicknameService.resetNickname(player)) {
                player.sendMessage(Component.text("You do not have a nickname set.", NamedTextColor.YELLOW));
                return true;
            }

            String realName = this.nicknameService.getRealName(player)
                .orElse(player.getName());
            player.sendMessage(Component.text(
                "Your nickname was reset. Other players now see " + realName + ".",
                NamedTextColor.GREEN
            ));
            return true;
        }

        if (!VALID_NICKNAME.matcher(input).matches()) {
            player.sendMessage(Component.text(
                "Nicknames must be 1-16 characters and only use letters, numbers, or underscores.",
                NamedTextColor.RED
            ));
            return true;
        }

        if (this.nicknameService.isNameTaken(player, input)) {
            player.sendMessage(Component.text(
                "That nickname is already in use by another tracked player.",
                NamedTextColor.RED
            ));
            return true;
        }

        this.nicknameService.setNickname(player, input);
        player.sendMessage(Component.text(
            "Your nickname is now " + input + ".",
            NamedTextColor.GREEN
        ));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && "reset".startsWith(args[0].toLowerCase(Locale.ROOT))) {
            return List.of("reset");
        }

        return List.of();
    }
}

