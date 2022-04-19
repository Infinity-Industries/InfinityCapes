package me.nes0x.commands.security;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class UnregisterAdmin extends Command {
    private final UserService service;

    public UnregisterAdmin(final UserService service) {
        this.service = service;
        requiredRole = "*";
        name = "unregister-admin";
        arguments = "<discord-id>";
        help = "Komenda dla administracji. Umożliwia odrejestrownia użytkownika.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 2) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś id użytkownika!",
                            null)
            ).queue();
            return;
        }
        String discordId = args[1];

        try {
            if (service.unregister(discordId)) {
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed(
                                "Sukces!",
                                Color.GREEN,
                                "Usunąłeś użytkownika o id: `" + discordId + "`!",
                                null)
                ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        commandEvent.getTextChannel().sendMessageEmbeds(
                Utils.createEmbed(
                        "Błąd!",
                        Color.RED,
                        "Taki użytkownik nie istnieje lub wystąpił nieoczekiwany błąd!",
                        null
                )
        ).queue();

    }
}
