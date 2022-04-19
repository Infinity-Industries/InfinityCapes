package me.nes0x.commands.cape;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class Cape extends Command {
    private final UserService service;

    public Cape(final UserService service) {
        this.service = service;
        name = "cape";
        arguments = "<id>";
        help = "Zmienia pelerynke.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 2) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś id peleryny!",
                            null)
            ).queue();
            return;
        }
        String id = args[1];

        try {
            if (service.changeCape(commandEvent.getAuthor().getId(), id, commandEvent.getMember().getRoles(), commandEvent.getGuild())) {
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed(
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


        commandEvent.getTextChannel().sendMessageEmbeds(
                Utils.createEmbed("Błąd!",
                        Color.RED,
                        "Nie masz założonego konta, taka peleryna nie istnieje lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
