package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.IOException;

public class CreateVoucher extends Command {
    private final UserService service;

    public CreateVoucher(final UserService service, final Category category) {
        this.service = service;
        this.category = category;
        requiredRole = "*";
        name = "create-voucher";
        arguments = "<item/cape> <id>";
        help = "Tworzy voucher z itemem/peleryną.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = commandEvent.getTextChannel();
        User user = commandEvent.getAuthor();
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
        String id = args[2];

        try {
            String voucher = service.createVoucher(type, id);
            if (voucher != null) {
                user.openPrivateChannel().queue(userChannel -> {
                    userChannel.sendMessageEmbeds(BotUtils.createEmbed(
                            "Sukces!",
                            Color.GREEN,
                            "Wygenerowany voucher " + type + " dla `" + id + "` to: `" + voucher + "`",
                            null
                    )).queue();
                });
                channel.sendMessageEmbeds(
                        BotUtils.createEmbed("Sukces!",
                                Color.GREEN,
                                "Sprawdź wiadomość prywatną z voucherem od bota!",
                                null)
                ).queue();
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        channel.sendMessageEmbeds(BotUtils.createEmbed(
                "Błąd!",
                Color.RED,
                "Nie wybrałeś odpowiedniego typu vouchera lub wystąpił nieoczekiwany błąd!",
                null
        )).queue();

    }
}
