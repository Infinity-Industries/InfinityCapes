package me.nes0x.commands.other;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class Register extends Command {
    private final UserService service;

    public Register(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "register";
        arguments = "<nick>";
        help = "Rejestracja do systemu InfinityCapes.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();

        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś nicku!",
                            null)
            ).queue();
            return;
        }
        String nick = args[1];

        try {
            if (service.register(nick, commandEvent.getAuthor().getId())) {
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Założyłeś konto na nick: `" + nick + "`",
                                null)
                ).queue();
                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                BotUtils.createEmbed(
                        "Błąd!",
                        Color.RED,
                        "Masz już założone konto, taki nick jest już zajęty lub wystąpił nieoczekiwany błąd!",
                        null
                )).queue();


    }
}
