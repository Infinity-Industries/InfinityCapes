package me.nes0x.commands.security;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;

public class Register extends Command {
    private final UserService service;

    public Register(final UserService service) {
        this.service = service;
        name = "register";
        arguments = "<nick>";
        help = "Rejestracja do systemu pelerynek InfinityCapes.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");

        if (args.length != 2) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś nicku!",
                            null)
            ).queue();
            return;
        }
        String nick = args[1];

        try {
            if (service.register(nick, commandEvent.getAuthor().getId())) {
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Założyłeś konto na nick: `" + nick + "`",
                                null)
                ).queue();
                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        commandEvent.getTextChannel().sendMessageEmbeds(
                Utils.createEmbed(
                        "Błąd!",
                        Color.RED,
                        "Masz już założone konto, taki nick jest już zajęty lub wystąpił nieoczekiwany błąd!",
                        null
                )).queue();


    }
}
