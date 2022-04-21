package me.nes0x.commands.other;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class Info extends Command {
    private final UserService service;

    public Info(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "info";
        arguments = "<nick>";
        help = "Wyświetla informacje o danej osobie.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś nicku użytkownika!",
                            null)
            ).queue();
            return;
        }
        String nick = args[1];

        try {
            if (!service.getInformation(nick, channel, commandEvent.getGuild())) {
                channel.sendMessageEmbeds(
                        Utils.createEmbed(
                                "Błąd!",
                                Color.RED,
                                "Nie ma takiego konta lub wystąpił nieoczekiwany błąd!",
                                null
                        )
                ).queue();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }
}
