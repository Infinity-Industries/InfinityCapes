package me.nes0x;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.nes0x.commands.admin.*;
import me.nes0x.commands.cape.Cape;
import me.nes0x.commands.cape.RemoveCape;
import me.nes0x.commands.item.Item;
import me.nes0x.commands.item.RemoveItem;
import me.nes0x.commands.other.*;
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
                //cape
                new Cape(service, Utils.CAPES_CATEGORY),
                new RemoveCape(service, Utils.CAPES_CATEGORY),
                //item
                new Item(service, Utils.ITEMS_CATEGORY),
                new RemoveItem(service, Utils.ITEMS_CATEGORY),
                //other
                new Register(service, Utils.OTHER_CATEGORY),
                new Unregister(service, Utils.OTHER_CATEGORY),
                new ChangeNick(service, Utils.OTHER_CATEGORY),
                new DropList(service, Utils.OTHER_CATEGORY),
                new Info(service, Utils.OTHER_CATEGORY),
                new Voucher(service, Utils.OTHER_CATEGORY),
                //administration
                new UnregisterAdmin(service, Utils.ADMIN_CATEGORY),
                new Embed(Utils.ADMIN_CATEGORY),
                new CreateVoucher(service, Utils.ADMIN_CATEGORY),
                new RemoveVoucher(service, Utils.ADMIN_CATEGORY),
                new VoucherList(service, Utils.ADMIN_CATEGORY),
                new Broadcast(Utils.ADMIN_CATEGORY),
                new AddCape(Utils.ADMIN_CATEGORY),
                new AddItem(Utils.ADMIN_CATEGORY),
                new ChangeDrop(service, Utils.ADMIN_CATEGORY)
        );
        client.setCoOwnerIds("520174583593304065", "893863477423259671");
        client.setActivity(Activity.playing("https://infinity.lisianora.xyz"));
        client.setStatus(OnlineStatus.DO_NOT_DISTURB);

        JDABuilder.createLight(System.getenv("BOT_TOKEN"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(
                        client.build(),
                        new BotMention(service),
                        new InfinityCommands(),
                        new Drop(service),
                        Utils.EVENT_WAITER
                )
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();


    }
}
