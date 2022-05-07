package me.nes0x.commands.other;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class DropList extends Command {
    private final UserService service;

    public DropList(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "drop-list";
        help = "Wyświetla co aktualnie można dostać z dropu.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        TextChannel channel = commandEvent.getTextChannel();
        try {
            channel.sendMessageEmbeds(service.dropList()).queue();
            return;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                BotUtils.createEmbed(
                        "Błąd!", Color.RED, "Wystąpił nieoczekiwany błąd!", null
                ) ).queue();
    }
}
