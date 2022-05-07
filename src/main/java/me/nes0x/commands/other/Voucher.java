package me.nes0x.commands.other;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class Voucher extends Command {
    private final UserService service;

    public Voucher(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "voucher";
        arguments = "<item/cape> <id>";
        help = "Możliwość odebrania peleryny/itemu.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        if (args.length != 3) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś argumentów!",
                            null)
            ).queue();
            return;
        }


        String type = args[1].toLowerCase();
        String voucher = args[2];

        if (!type.equalsIgnoreCase("cape") && !type.equalsIgnoreCase("item")) {
            channel.sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
                            Color.RED,
                            "Musisz wybrać pomiędzy itemem a peleryną!",
                            null)
            ).queue();
            return;
        }

        try {
            if (service.applyVoucher(type, voucher, commandEvent.getAuthor().getId())) {
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Odebrałeś voucher.",
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
                        "Taki voucher nie istnieje, nie masz założonego konta lub wystąpił nieoczekiwany błąd!",
                        null)
        ).queue();

    }
}
