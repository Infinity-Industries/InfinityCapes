package me.nes0x.events;

import me.nes0x.utils.UserService;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Drop extends ListenerAdapter {
    private final UserService service;

    public Drop(final UserService service) {
        this.service = service;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getChannel().getId().equalsIgnoreCase("965638065521578084")) {
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            if (event.getMessage().getContentRaw().equalsIgnoreCase(";drop")) {
                try {
                    String message = service.checkDrop(event.getAuthor());
                        event.getTextChannel().sendMessageEmbeds(
                                BotUtils.createEmbed(
                                        "Drop!",
                                        Color.CYAN,
                                        message,
                                        null
                                )
                        ).queue();

                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }

    }

}
