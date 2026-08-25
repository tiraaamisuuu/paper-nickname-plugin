package dev.alfiecorthine.nickname;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " <name|reset>");
            return true;
        }

        String input = args[0].trim();
        if (input.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Your nickname cannot be empty.");
            return true;
        }

        String loweredInput = input.toLowerCase(Locale.ROOT);
        if (loweredInput.equals("reset") || loweredInput.equals("clear") || loweredInput.equals("off")) {
            if (!this.nicknameService.resetNickname(player)) {
                player.sendMessage(ChatColor.YELLOW + "You do not have a nickname set.");
                return true;
            }

            String realName = this.nicknameService.getRealName(player)
                .orElse(player.getName());
            player.sendMessage(ChatColor.GREEN
                + "Your nickname was reset. Other players now see " + realName + ".");
            return true;
        }

        if (!VALID_NICKNAME.matcher(input).matches()) {
            player.sendMessage(ChatColor.RED
                + "Nicknames must be 1-16 characters and only use letters, numbers, or underscores.");
            return true;
        }

        if (this.nicknameService.isNameTaken(player, input)) {
            player.sendMessage(ChatColor.RED
                + "That nickname is already in use by another tracked player.");
            return true;
        }

        this.nicknameService.setNickname(player, input);
        player.sendMessage(ChatColor.GREEN + "Your nickname is now " + input + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && "reset".startsWith(args[0].toLowerCase(Locale.ROOT))) {
            return Collections.singletonList("reset");
        }

        return Collections.emptyList();
    }
}

