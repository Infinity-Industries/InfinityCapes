package me.nes0x.commands.cape;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class RemoveCape extends Command {
    private final UserService service;

    public RemoveCape(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "remove-cape";
        help = "Usuwa pelerynke którą aktualnie używasz.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        TextChannel channel = commandEvent.getTextChannel();
        try {
            if (service.removeCape(commandEvent.getAuthor().getId())) {
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed(
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


        channel.sendMessageEmbeds(
                BotUtils.createEmbed(
                        "Błąd!",
                        Color.RED,
                        "Nie posiadasz konta, nie masz założonej pelerynki lub wystąpił nieoczekiwany błąd!",
                        null
                )
        ).queue();

    }
}
