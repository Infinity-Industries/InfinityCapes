package me.nes0x.commands.other;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class Unregister extends Command {
    private final UserService service;

    public Unregister(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "unregister";
        help = "Odrejestrowanie z systemu InfinityCapes. **(UWAGA, STRACISZ WSZYSTKIE ODBLOKOWANE PELERYNY I ITEMY!)**";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        TextChannel channel = commandEvent.getTextChannel();
        try {
            if (service.unregister(commandEvent.getAuthor().getId())) {
                channel.sendMessageEmbeds(
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


        channel.sendMessageEmbeds(
                        Utils.createEmbed(
                                "Błąd!",
                                Color.RED,
                                "Nie masz założonego konta lub wystąpił nieoczekiwany błąd!",
                                null
                        )).queue();
    }
}
