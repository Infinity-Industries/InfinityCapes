package me.nes0x.commands.item;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.io.IOException;

public class Item extends Command {
    private final UserService service;

    public Item(final UserService service) {
        this.service = service;
        name = "item";
        arguments = "<id>";
        help = "Nakłada podany item.";
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
        Role role = commandEvent.getGuild().getRolesByName("*", true).get(0);

        try {
            if (service.addItem(id, commandEvent.getAuthor().getId(), commandEvent.getMember().getRoles(), role)) {
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Pomyślnie nałożyłeś item: `" + id + "`!",
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
                        "Nie masz założonego konta, nie posiadasz tego itemu, masz go już nałożonego, taki item nie istnieje lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
