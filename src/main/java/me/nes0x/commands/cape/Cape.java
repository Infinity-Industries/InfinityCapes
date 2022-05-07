package me.nes0x.commands.cape;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class Cape extends Command {
    private final UserService service;

    public Cape(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "cape";
        arguments = "<id>";
        help = "Zmienia pelerynke.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś id peleryny!",
                            null)
            ).queue();
            return;
        }
        String id = args[1];

        try {
            if (service.changeCape(commandEvent.getAuthor().getId(), id, commandEvent.getMember().getRoles(), commandEvent.getGuild())) {
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed(
                                "Sukces!",
                                Color.GREEN,
                                "Zmieniłeś swoją pelerynke na: `" + id + "`",
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
                        "Nie masz założonego konta, taka peleryna nie istnieje lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
