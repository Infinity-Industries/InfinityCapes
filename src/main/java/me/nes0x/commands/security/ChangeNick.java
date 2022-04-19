package me.nes0x.commands.security;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class ChangeNick extends Command {
    private final UserService service;

    public ChangeNick(final UserService service) {
        this.service = service;
        name = "change-nick";
        arguments = "<nick>";
        help = "Zmiana nicku.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 2) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś nowego nicku!",
                            null)
            ).queue();
            return;
        }

        String newNick = args[1];

        try {
            if (service.changeNick(commandEvent.getAuthor().getId(), newNick)) {
                commandEvent.getTextChannel()
                        .sendMessageEmbeds(
                                Utils.createEmbed(
                                        "Sukces!",
                                        Color.GREEN,
                                        "Twój nick został zmieniony na: `" + newNick + "`!",
                                        null)
                        ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        commandEvent.getTextChannel()
                .sendMessageEmbeds(
                        Utils.createEmbed(
                                "Błąd!",
                                Color.RED,
                                "Nie masz konta, taki nick już jest zajęty lub wystąpił nieoczekiwany błąd!",
                                null
                        )
                ).queue();

    }
}
