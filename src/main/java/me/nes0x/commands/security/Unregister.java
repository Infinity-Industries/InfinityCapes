package me.nes0x.commands.security;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class Unregister extends Command {
    private final UserService service;

    public Unregister(final UserService service) {
        this.service = service;
        name = "unregister";
        help = "Usuwa z systemu pelerynek InfinityCapes. **(UWAGA, STRACISZ WSZYSTKIE ODBLOKOWANE PELERYNY I ITEMY!)**";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {

        try {
            if (service.unregister(commandEvent.getAuthor().getId())) {
                commandEvent.getTextChannel()
                        .sendMessageEmbeds(
                                Utils.createEmbed(
                                        "Sukces!",
                                        Color.GREEN,
                                        "Pomyślnie się odrejestrowałeś!",
                                        null
                                )
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
                                "Nie masz założonego konta lub wystąpił nieoczekiwany błąd!",
                                null
                        )).queue();
    }
}
