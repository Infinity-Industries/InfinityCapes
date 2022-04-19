package me.nes0x;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.nes0x.commands.*;
import me.nes0x.commands.cape.Cape;
import me.nes0x.commands.cape.RemoveCape;
import me.nes0x.commands.item.Item;
import me.nes0x.commands.item.RemoveItem;
import me.nes0x.commands.security.ChangeNick;
import me.nes0x.commands.security.Register;
import me.nes0x.commands.security.Unregister;
import me.nes0x.commands.security.UnregisterAdmin;
import me.nes0x.commands.voucher.CreateVoucher;
import me.nes0x.commands.voucher.Voucher;
import me.nes0x.events.BotMention;
import me.nes0x.events.Drop;
import me.nes0x.events.InfinityCommands;
import me.nes0x.utils.UserService;
import me.nes0x.utils.Utils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Arrays;

public class Bot {
    public static void main(String[] args) throws LoginException {
        File[] files = new File[]{
                new File("./capes"),
                new File("./users")};
        Arrays.stream(files).forEach(
                file -> {
                    if (!file.exists()) {
                        file.mkdir();
                    }
                }
        );

        UserService service = new UserService();
        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId("920440877594316840");
        client.setPrefix(Utils.PREFIX);
        client.addCommands(
                new Register(service),
                new Unregister(service),
                new Cape(service),
                new ChangeNick(service),
                new UnregisterAdmin(service),
                new RemoveCape(service),
                new Embed(),
                new Info(service),
                new CreateVoucher(service),
                new Voucher(service),
                new Item(service),
                new RemoveItem(service)
        );
        client.setCoOwnerIds("520174583593304065", "893863477423259671");
        client.setActivity(Activity.playing("https://infinity.lisianora.xyz"));
        client.setStatus(OnlineStatus.DO_NOT_DISTURB);

        JDABuilder.createLight(System.getenv("BOT_TOKEN"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(
                        client.build(),
                        new BotMention(),
                        new InfinityCommands(),
                        new Drop(service)
                )
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();


    }
}
