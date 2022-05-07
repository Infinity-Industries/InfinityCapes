package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;

public class VoucherList extends Command {
    private final UserService service;

    public VoucherList(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        name = "vouchers-list";
        requiredRole = "*";
        help = "Wyświetla wszystkie aktualne vouchery.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        TextChannel channel = commandEvent.getTextChannel();
        try {
            channel.sendMessageEmbeds(service.vouchersList()).queue();
            return;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(
                BotUtils.createEmbed(
                        "Błąd!", Color.RED, "Wystąpił nieoczekiwany błąd!", null
                ) ).queue();
    }
}
