package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.util.List;

public class CapeAdd extends Command {
    public CapeAdd(final Category category) {
        this.category = category;
        name = "cape-add";
        requiredRole = "*";
        arguments = "<dołącz pelerynke do komendy>";
        help = "Dodaje na serwer peleryne dołączoną do komendy.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        List<Message.Attachment> attachments = commandEvent.getMessage().getAttachments();
        TextChannel channel = commandEvent.getTextChannel();

        if (attachments.isEmpty()) {
            channel.sendMessageEmbeds(
                Utils.createEmbed("Błąd!", Color.RED, "Nie dodałeś peleryny do komendy!", null)
            ).queue();
            return;
        }

        attachments.forEach(
                attachment -> {
                    if (!attachment.getFileExtension().equalsIgnoreCase("png")) {
                        channel.sendMessageEmbeds(
                                Utils.createEmbed("Błąd!", Color.RED, "Podałeś plik który nie jest peleryną!", null)
                        ).queue();
                    } else {
                        attachment.downloadToFile(new File("./capes/" + attachment.getFileName()));
                        channel.sendMessageEmbeds(
                                        Utils.createEmbed("Sukces!",
                                                Color.GREEN,
                                                "Dodałeś peleryne o id `"
                                                        + attachment.getFileName().replace(".png", "") + "`", null))
                                .queue();
                    }
                }
        );

    }
}
