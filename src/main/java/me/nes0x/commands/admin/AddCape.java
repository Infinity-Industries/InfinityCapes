package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.util.List;

public class AddCape extends Command {
    public AddCape(final Category category) {
        this.category = category;
        name = "add-cape";
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
                BotUtils.createEmbed("Błąd!", Color.RED, "Nie dodałeś peleryny do komendy!", null)
            ).queue();
            return;
        }

        attachments.forEach(
                attachment -> {
                    if (!attachment.getFileExtension().equalsIgnoreCase("png")) {
                        channel.sendMessageEmbeds(
                                BotUtils.createEmbed("Błąd!", Color.RED, "Podałeś plik który nie jest peleryną!", null)
                        ).queue();
                    } else {
                        File cape = new File("./capes/" + attachment.getFileName());
                        if (!cape.exists()) {
                            attachment.downloadToFile(cape);
                            channel.sendMessageEmbeds(
                                            BotUtils.createEmbed("Sukces!",
                                                    Color.GREEN,
                                                    "Dodałeś peleryne o id `"
                                                            + attachment.getFileName().replace(".png", "") + "`", null))
                                    .queue();
                        } else {
                            channel.sendMessageEmbeds(
                                            BotUtils.createEmbed("Błąd!",
                                                    Color.RED,
                                                    "Taka peleryna już istnieje!",
                                                            null))
                                    .queue();
                        }


                    }
                }
        );

    }
}
