package me.nes0x.events;

import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class BotMention extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        RuntimeMXBean uptimeMs = ManagementFactory.getRuntimeMXBean();
        String message = event.getMessage().getContentRaw();
        if (message.equalsIgnoreCase("<@!961255531694133248>")
                || message.equalsIgnoreCase("<@961255531694133248>")
                && !event.getAuthor().isBot()) {
            int time = (int) (uptimeMs.getUptime() / 60000);
            MessageEmbed botMentioned = Utils.createEmbed("Hejka, jestem InfinityCapes",
                    Color.CYAN,
                    "Zostałem zrobiony przez <@!920440877594316840> " +
                            "\nMój prefix to: `" + Utils.PREFIX + "`"
                            + "\nWpisz `" + Utils.PREFIX + "help` aby poznać listę komend"
                            + "\nDziałam od:" + Utils.minutesToTime(time), null);
            event.getTextChannel().sendMessageEmbeds(botMentioned).queue();
        }


    }

}
