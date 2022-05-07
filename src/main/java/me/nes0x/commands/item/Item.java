package me.nes0x.commands.item;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class Item extends Command {
    private final UserService service;

    public Item(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "item";
        arguments = "<id>";
        help = "Nakłada podany item.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
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
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Pomyślnie nałożyłeś item: `" + id + "`!",
                                null)
                ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                BotUtils.createEmbed("Błąd!",
                        Color.RED,
                        "Nie masz założonego konta, nie posiadasz tego itemu, masz go już nałożonego, taki item nie istnieje lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
