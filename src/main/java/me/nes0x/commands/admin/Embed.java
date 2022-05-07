package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class Embed extends Command {
    public Embed(final Category category) {
        this.category = category;
        requiredRole = "*";
        name = "embed";
        arguments = "<skin/item/cape> <id> <#kanał> <url-obrazka>";
        help = "Tworzy embed z itemem/skinem/capem/kitem (id, obrazek).";
    }

    @Override
    protected void execute(final CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        if (args.length != 5 || commandEvent.getMessage().getMentionedChannels().size() == 0) {
            commandEvent.getTextChannel().sendMessageEmbeds(
                    BotUtils.createEmbed("Błąd!",
                            Color.RED,
                            "Nie podałeś argumentów!",
                            null)
            ).queue();
            return;
        }

        String type = args[1];
        String typeId = args[2];
        TextChannel channel = commandEvent.getMessage().getMentionedChannels().get(0);
        String url = args[4];
        String title, description;


        switch (type) {
            case "skin":
                title = "Skin " + typeId;
                description = "Aby mieć tego skina w grze nazwij go w kowadle: `" + typeId + "`";
                break;
            case "item":
                title = "Item " + typeId;
                description = "Aby nadać sobie ten item wpisz: `" + BotUtils.PREFIX + "item " + typeId + "`";
                break;
            case "cape":
                title = "Pelerynka " + typeId;
                description = "Aby nadać sobie tą pelerynke wpisz: `" + BotUtils.PREFIX + "cape " + typeId + "`";
                break;
            default:
                commandEvent.getTextChannel().sendMessage("Nie podałeś typu!").queue();
                return;
        }
        commandEvent.getGuild().getTextChannelById(channel.getId()).sendMessageEmbeds(
                BotUtils.createEmbed(title,
                        Color.CYAN,
                        description,
                        url)
        ).queue();

    }
}
