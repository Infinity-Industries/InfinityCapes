package me.nes0x.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.nes0x.Bot;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Broadcast extends Command {

    public Broadcast() {
        name = "broadcast";
        arguments = "<id-kanału>";
        help = "Tworzenie ogłoszeń.";
        requiredRole = "*";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split("\\s+");
        MessageChannel channel = commandEvent.getChannel();
        User author = commandEvent.getAuthor();


        if (args.length != 2) {
            return;
        }
        String id = args[1];


        channel.sendMessage("Okej, podaj link do obrazka. Jeśli chcesz anulować wpisz `anuluj` jeśli go nie chcesz wpisz `brak`")
                .queue(getImage -> Utils.EVENT_WAITER.waitForEvent(
                                GuildMessageReceivedEvent.class,
                                imageEvent -> {
                                    if (!imageEvent.getChannel().getId().equals(channel.getId())) {
                                        return false;
                                    }

                                    return imageEvent.getAuthor().getIdLong() == author.getIdLong();
                                },
                                imageEvent -> {
                                    String image = imageEvent.getMessage().getContentRaw();

                                    if (image.equalsIgnoreCase("anuluj")) {
                                        channel.sendMessage("Anulowano tworzenie embeda!").queue();
                                        return;
                                    } else if (image.equalsIgnoreCase("brak")) {
                                        image = null;
                                    }

                                    String finalImage = image;
                                    channel.sendMessage("Podaj tytuł ogłoszenia. Jeśli chcesz anulować wpisz `anuluj` jeśli go nie chcesz wpisz `brak`").queue(
                                            getTitle -> Utils.EVENT_WAITER.waitForEvent(
                                                    GuildMessageReceivedEvent.class,
                                                    titleEvent -> {
                                                        if (!titleEvent.getChannel().getId().equals(channel.getId())) {
                                                            return false;
                                                        }

                                                        return titleEvent.getAuthor().getIdLong() == author.getIdLong();
                                                    },
                                                    titleEvent -> {
                                                        String title = titleEvent.getMessage().getContentRaw();

                                                        if (title.equalsIgnoreCase("anuluj")) {
                                                            channel.sendMessage("Anulowano tworzenie embeda!").queue();
                                                            return;
                                                        } else if (title.equalsIgnoreCase("brak")) {
                                                            title = null;
                                                        }

                                                        String finalTitle = title;
                                                        channel.sendMessage("Podaj treść ogłoszenia. Jeśli chcesz anulować wpisz `anuluj` jeśli go nie chcesz wpisz `brak`").queue(
                                                                getDescription -> Utils.EVENT_WAITER.waitForEvent(
                                                                        GuildMessageReceivedEvent.class,
                                                                        descriptionEvent -> {
                                                                            if (!descriptionEvent.getChannel().getId().equals(channel.getId())) {
                                                                                return false;
                                                                            }

                                                                            return descriptionEvent.getAuthor().getIdLong() == author.getIdLong();
                                                                        },
                                                                        descriptionEvent -> {
                                                                            String description = descriptionEvent.getMessage().getContentRaw();

                                                                            if (description.equalsIgnoreCase("anuluj")) {
                                                                                channel.sendMessage("Anulowano tworzenie embeda!").queue();
                                                                                return;
                                                                            } else if (description.equalsIgnoreCase("brak")) {
                                                                                description = null;
                                                                            }

                                                                            channel.sendMessage("Tworzenie ogłoszenia...").queue();

                                                                            MessageEmbed embed = Utils.createEmbed(finalTitle, Color.CYAN, description, finalImage);
                                                                            commandEvent .getJDA().getTextChannelById(id).sendMessageEmbeds(embed).queue();
                                                                        },
                                                                        1, TimeUnit.MINUTES,
                                                                        () -> {
                                                                            channel.sendMessage("Nie odpowiedziałeś w czasie!").queue();
                                                                        }
                                                                )
                                                        );

                                                    },
                                                    1, TimeUnit.MINUTES,
                                                    () -> {
                                                        channel.sendMessage("Nie odpowiedziałeś w czasie!").queue();
                                                    }
                                            )
                                    );

                                },
                                1, TimeUnit.MINUTES,
                                () -> {
                                    channel.sendMessage("Nie odpowiedziałeś w czasie!").queue();
                                }
                        )
                );


    }
}
