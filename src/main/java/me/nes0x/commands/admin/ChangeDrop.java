package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class ChangeDrop extends Command {
    private final UserService service;

    public ChangeDrop(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "change-drop";
        requiredRole = "*";
        arguments = "<add/remove> <cape/item> <id>";
        help = "Dodaje podany item/peleryne do nagrod z dropu.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        TextChannel channel = commandEvent.getTextChannel();
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 4) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed(
                            "Błąd",
                            Color.RED,
                            "Nie podałeś argumentów!",
                            null
                    )
            ).queue();
            return;
        }

        String action = args[1];
        String type = args[2];
        String id = args[3];

        if (!action.equalsIgnoreCase("remove") && !action.equalsIgnoreCase("add")) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed(
                            "Błąd",
                            Color.RED,
                            "Musisz wybrać czy chcesz usunąć czy dodać!",
                            null
                    )
            ).queue();
            return;
        }

        try {
            if (service.changeDrop(type, id, action.equalsIgnoreCase("remove"))) {
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Pomyślnie dokonałeś akcje.",
                                null)
                ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                BotUtils.createEmbed("Błąd!",
                        Color.RED,
                        "Taka rzecz już jest dodana, nie ma jej lub wystąpił nieoczekiwany błąd.",
                        null)
        ).queue();


    }
}
