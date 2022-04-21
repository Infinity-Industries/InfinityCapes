package me.nes0x.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class InfinityCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getChannel().getId().equalsIgnoreCase("963103413174956092")) {
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }
}