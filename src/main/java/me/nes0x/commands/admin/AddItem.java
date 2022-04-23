package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.util.List;

public class AddItem extends Command {
    public AddItem(final Category category) {
        this.category = category;
        name = "add-item";
        requiredRole = "*";
        arguments = "<id> <dołącz model.cfg, texture.png - w takiej kolejności do komendy>";
        help = "Dodaje na serwer item dołączony do komendy.";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        List<Message.Attachment> attachments = commandEvent.getMessage().getAttachments();
        TextChannel channel = commandEvent.getTextChannel();
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");

        if (args.length != 2) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!", Color.RED, "Nie podałeś jaki ten item ma mieć id!", null)
            ).queue();
            return;
        }

        if (attachments.size() != 2) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!", Color.RED, "Nie dodałeś wymaganych plików!", null)
            ).queue();
            return;
        }
        Message.Attachment model = attachments.get(0);
        Message.Attachment texture = attachments.get(1);
        String id = args[1];

        if (!model.getFileExtension().equalsIgnoreCase("cfg")
                && !texture.getFileExtension().equalsIgnoreCase("png")) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!", Color.RED, "Dodałeś złe pliki!", null)
            ).queue();
            return;
        }

        File itemDir = new File("/var/www/html/items/" + id);

        if (itemDir.exists()) {
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Błąd!", Color.RED, "Taki item już istnieje!", null)
            ).queue();
            return;
        }
        if (itemDir.mkdir()) {
            model.downloadToFile(new File("/var/www/html/items/" + id + "/model.cfg"));
            texture.downloadToFile(new File("/var/www/html/items/" + id + "/texture.png"));
            channel.sendMessageEmbeds(
                    Utils.createEmbed("Sukces!", Color.GREEN, "Dodałeś item o id `" + id + "`!", null)
            ).queue();
            return;
        }

        channel.sendMessageEmbeds(
                Utils.createEmbed("Błąd!", Color.RED, "Wystąpił nieoczekiwany błąd!", null)
        ).queue();
    }
}
