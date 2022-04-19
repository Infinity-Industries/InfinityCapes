package me.nes0x.commands.cape;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class RemoveCape extends Command {
    private final UserService service;

    public RemoveCape(final UserService service) {
        this.service = service;
        name = "remove-cape";
        help = "Usuwa pelerynke którą aktualnie używasz.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        try {
            if (service.removeCape(commandEvent.getAuthor().getId())) {
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed(
                                "Sukces!",
                                Color.GREEN,
                                "Usunąłeś aktualnie założoną pelerynke!",
                                null
                        )
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
                        "Nie posiadasz konta, nie masz założonej pelerynki lub wystąpił nieoczekiwany błąd!",
                        null
                )
        ).queue();

    }
}
