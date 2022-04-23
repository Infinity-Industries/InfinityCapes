package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class RemoveVoucher extends Command {
    private final UserService service;

    public RemoveVoucher(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        requiredRole = "*";
        name = "remove-voucher";
        arguments = "<item/cape> <code>";
        help = "Usuwa podany voucher.";
    }


    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();

        if (args.length != 3) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś argumentów!",
                            null)
            ).queue();
            return;
        }
        String type = args[1].toLowerCase();
        String code = args[2];

        try {
            if (service.removeVoucher(code, type)) {
                channel.sendMessageEmbeds(Utils.createEmbed(
                        "Sukces!",
                        Color.GREEN,
                        "Pomyślnie usunąłeś voucher `" + code + "`",
                        null
                )).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        channel.sendMessageEmbeds(Utils.createEmbed(
                "Błąd!",
                Color.RED,
                "Taki voucher nie istnieje, nie podałeś dobrego typu lub wystąpił nieoczekiwany błąd!",
                null
        )).queue();

    }
}
