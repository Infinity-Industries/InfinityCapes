package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class UnregisterAdmin extends Command {
    private final UserService service;

    public UnregisterAdmin(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        requiredRole = "*";
        name = "unregister-admin";
        arguments = "<discord-id>";
        help = "Umożliwia odrejestrownia użytkownika.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś id użytkownika!",
                            null)
            ).queue();
            return;
        }
        String discordId = args[1];

        try {
            if (service.unregister(discordId)) {
                channel.sendMessageEmbeds(BotUtils.createEmbed(
                                "Sukces!",
                                Color.GREEN,
                                "Usunąłeś użytkownika o id: `" + discordId + "`!",
                                null)).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                BotUtils.createEmbed(
                        "Błąd!",
                        Color.RED,
                        "Taki użytkownik nie istnieje lub wystąpił nieoczekiwany błąd!",
                        null
                )
        ).queue();

    }
}
