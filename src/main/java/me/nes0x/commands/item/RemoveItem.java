package me.nes0x.commands.item;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class RemoveItem extends Command {
    private final UserService service;

    public RemoveItem(final UserService service) {
        this.service = service;
        name = "remove-item";
        arguments = "<id>";
        help = "Zdejmuje podany item.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 2) {
            commandEvent.getTextChannel().sendMessageEmbeds(
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
                commandEvent.getTextChannel().sendMessageEmbeds(
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

        commandEvent.getTextChannel().sendMessageEmbeds(
                Utils.createEmbed("Błąd!",
                        Color.RED,
                        "Nie masz założonego takiego itemu, nie masz założonego konta lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
