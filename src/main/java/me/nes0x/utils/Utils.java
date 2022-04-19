package me.nes0x.utils;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Utils {
    public final static String PREFIX = ";";
    public final static EventWaiter EVENT_WAITER = new EventWaiter();

    public static MessageEmbed createEmbed(String title, Color color, String description, String imageURL) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setColor(color);
        if (imageURL != null) {
            embed.setImage(imageURL);
        }

        embed.setDescription(description);
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedUTC = localDateTime.atZone(ZoneId.of("GMT"));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String date = dateTimeFormatter.format(zonedUTC);
        embed.setFooter("InfinityCapes - " + date);
        return embed.build();
    }

    public static String minutesToTime(int time) {
        StringBuilder timeToReturn = new StringBuilder();

        if (!(time / 60 > 0)) {
            timeToReturn.append("\n").append(time).append(" minut/y");
        }

        if (time / 60 > 0 && !(time / 1440 > 0)) {
            timeToReturn.append("\n").append(time / 60).append(" godzin/y");
        }

        if (time / 1440 > 0) {
            timeToReturn.append("\n").append(time / 1440).append(" dni");
        }


        return timeToReturn.toString();
    }
}
