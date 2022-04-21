package me.nes0x.commands.other;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class ChangeNick extends Command {
    private final UserService service;

    public ChangeNick(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "change-nick";
        arguments = "<nick>";
        help = "Zmiana nicku.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 2) {
            channel.sendMessageEmbeds(
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
                channel
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

        channel
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
