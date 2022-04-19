package me.nes0x.commands.voucher;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class Voucher extends Command {
    private final UserService service;

    public Voucher(final UserService service) {
        this.service = service;
        name = "voucher";
        arguments = "<item/cape> <id>";
        help = "Możliwość odebrania peleryny/itemu.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 3) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś argumentów!",
                            null)
            ).queue();
            return;
        }


        String type = args[1].toLowerCase();
        String voucher = args[2];

        if (!type.equalsIgnoreCase("cape") && !type.equalsIgnoreCase("item")) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Musisz wybrać pomiędzy itemem a peleryną!",
                            null)
            ).queue();
            return;
        }

        try {
            if (service.applyVoucher(type, voucher, commandEvent.getAuthor().getId())) {
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Odebrałeś voucher.",
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
                        "Taki voucher nie istnieje, nie masz założonego konta lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
