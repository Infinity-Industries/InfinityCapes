package me.nes0x.commands.voucher;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.io.IOException;

public class CreateVoucher extends Command {
    private final UserService service;

    public CreateVoucher(final UserService service) {
        this.service = service;
        requiredRole = "*";
        name = "create-voucher";
        arguments = "<item/cape> <id>";
        help = "Komenda dla administracji. Tworzy voucher z itemem/peleryną";
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
        String id = args[2];

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
            String voucher = service.createVoucher(type, id);
            if (voucher != null) {
                commandEvent.getAuthor().openPrivateChannel().queue(channel -> {
                    channel.sendMessageEmbeds(Utils.createEmbed(
                            "Sukces!",
                            Color.GREEN,
                            "Wygenerowany voucher " + type + " dla `" + id + "` to: `" + voucher + "`",
                            null
                    )).queue();
                });
                commandEvent.getTextChannel().sendMessageEmbeds(
                        Utils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Sprawdź wiadomość prywatną z voucherem od bota!",
                                null)
                ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        commandEvent.getAuthor().openPrivateChannel().queue(channel -> {
            channel.sendMessageEmbeds(Utils.createEmbed(
                    "Błąd!",
                    Color.RED,
                    "Wystąpił nieoczekiwany błąd przy tworzeniu vouchera!",
                    null
            )).queue();
        });


    }
}
