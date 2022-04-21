package me.nes0x.commands.item;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class RemoveItem extends Command {
    private final UserService service;

    public RemoveItem(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "remove-item";
        arguments = "<id>";
        help = "Zdejmuje podany item.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś id itemu!",
                            null)
            ).queue();
            return;
        }
        String id = args[1];

        try {
            if (service.removeItem(id, commandEvent.getAuthor().getId())) {
                channel.sendMessageEmbeds(
                        Utils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Pomyślnie zdjąłeś item: `" + id +"`!",
                                null)
                ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                Utils.createEmbed("Błąd!",
                        Color.RED,
                        "Nie masz założonego takiego itemu, nie masz założonego konta lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
