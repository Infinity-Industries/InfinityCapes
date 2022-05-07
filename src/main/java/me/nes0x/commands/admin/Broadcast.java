package me.nes0x.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.utils.BotUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Broadcast extends Command {

    public Broadcast(final Category category) {
        this.category = category;
        name = "broadcast";
        arguments = "<#kanał>";
        help = "Tworzy i wysyła ogłoszenie na podany kanał.";
        requiredRole = "*";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        MessageChannel channel = commandEvent.getChannel();
        User author = commandEvent.getAuthor();


        if (args.length != 2 || commandEvent.getMessage().getMentionedChannels().size() == 0) {
            channel.sendMessageEmbeds(BotUtils.createEmbed("Błąd!", Color.RED, "Nie podałeś kanału!", null)).queue();
            return;
        }
        TextChannel mentionedChannel = commandEvent.getMessage().getMentionedChannels().get(0);


        channel.sendMessage("Okej, podaj link do obrazka. Jeśli go nie chcesz wpisz `brak`")
                .queue(getImage -> BotUtils.EVENT_WAITER.waitForEvent(
                                GuildMessageReceivedEvent.class,
                                imageEvent -> {
                                    if (!imageEvent.getChannel().getId().equals(channel.getId())) {
                                        return false;
                                    }

                                    return imageEvent.getAuthor().getIdLong() == author.getIdLong();
                                },
                                imageEvent -> {
                                    String image = imageEvent.getMessage().getContentRaw();

                                    if (image.equalsIgnoreCase("brak")) {
                                        image = null;
                                    }

                                    String finalImage = image;
                                    channel.sendMessage("Podaj tytuł ogłoszenia. Jeśli go nie chcesz wpisz `brak`").queue(
                                            getTitle -> BotUtils.EVENT_WAITER.waitForEvent(
                                                    GuildMessageReceivedEvent.class,
                                                    titleEvent -> {
                                                        if (!titleEvent.getChannel().getId().equals(channel.getId())) {
                                                            return false;
                                                        }

                                                        return titleEvent.getAuthor().getIdLong() == author.getIdLong();
                                                    },
                                                    titleEvent -> {
                                                        String title = titleEvent.getMessage().getContentRaw();

                                                        if (title.equalsIgnoreCase("brak")) {
                                                            title = null;
                                                        }

                                                        String finalTitle = title;
                                                        channel.sendMessage("Podaj treść ogłoszenia. Jeśli go nie chcesz wpisz `brak`").queue(
                                                                getDescription -> BotUtils.EVENT_WAITER.waitForEvent(
                                                                        GuildMessageReceivedEvent.class,
                                                                        descriptionEvent -> {
                                                                            if (!descriptionEvent.getChannel().getId().equals(channel.getId())) {
                                                                                return false;
                                                                            }

                                                                            return descriptionEvent.getAuthor().getIdLong() == author.getIdLong();
                                                                        },
                                                                        descriptionEvent -> {
                                                                            String description = descriptionEvent.getMessage().getContentRaw();

                                                                            if (description.equalsIgnoreCase("brak")) {
                                                                                description = null;
                                                                            }

                                                                            channel.sendMessage("Tworzenie ogłoszenia...").queue();

                                                                            MessageEmbed embed = BotUtils.createEmbed(finalTitle, Color.CYAN, description, finalImage);
                                                                            commandEvent .getJDA().getTextChannelById(mentionedChannel.getId()).sendMessageEmbeds(embed).queue();
                                                                        },
                                                                        3, TimeUnit.MINUTES,
                                                                        () -> channel.sendMessage("Nie odpowiedziałeś w czasie!").queue()
                                                                )
                                                        );

                                                    },
                                                    3, TimeUnit.MINUTES,
                                                    () -> channel.sendMessage("Nie odpowiedziałeś w czasie!").queue()
                                            )
                                    );

                                },
                                3, TimeUnit.MINUTES,
                                () -> channel.sendMessage("Nie odpowiedziałeś w czasie!").queue()
                        )
                );


    }
}
