package me.nes0x.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class UserService {


    private boolean save(File file, String json) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.write(json);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private JSONObject read(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buffer = new StringBuffer();
        String text;

        while ((text = reader.readLine()) != null) {
            buffer.append(text);
        }
        reader.close();
        String jsonText = buffer.toString();
        return new JSONObject(jsonText);
    }

    private boolean checkNick(String nick) throws IOException {
        Stream<Path> path = Files.walk(Path.of("./users"));
        return path
                .filter(Files::isRegularFile)
                .anyMatch(file -> {
                            try {
                                JSONObject json = read(file.toFile());
                                if (json.getString("nick").equalsIgnoreCase(nick)) {
                                    return true;
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            return false;
                        }
                );
    }

    public boolean register(String nick, String discordId) throws Exception {
        File user = new File("./users/" + discordId + ".json");

        if (user.exists()) {
            return false;
        }

        if (checkNick(nick)) {
            return false;
        }

        if (user.createNewFile()) {
            JSONObject json = new JSONObject();
            JSONObject items = new JSONObject();
            items.put("items", (Collection<String>) null);
            json.put("nick", nick);
            json.put("active-cape", "");
            json.put("active-items", (Collection<String>) null);
            json.put("capes", (Collection<String>) null);
            json.put("items", (Collection<String>) null);
            json.put("drop", "");
            return save(user, json.toString()) && save(new File("/var/www/html/users/" + nick + ".cfg"), items.toString());
        }
        return false;
    }


    public boolean unregister(String discordId) throws IOException {
        File user = new File("./users/" + discordId + ".json");
        if (!user.exists()) {
            return false;
        }

        JSONObject json = read(user);

        File cape = new File("/var/www/html/capes/" + json.getString("nick") + ".png");
        File items = new File("/var/www/html/users/" + json.getString("nick") + ".cfg");

        if (cape.exists()) {
            if (!cape.delete()) {
                return false;
            }
        }
        if (items.exists()) {
            if (!items.delete()) {
                return false;
            }
        }
        return user.delete();
    }

    public boolean changeCape(String discordId, String id, List<Role> roles, Guild guild) throws IOException {
        File user = new File("./users/" + discordId + ".json");

        if (!user.exists()) {
            return false;
        }

        JSONObject json = read(user);
        String nick = json.getString("nick");

        if (id.startsWith("a_") || id.startsWith("p_")) {
            if (!json.getJSONArray("capes").toList().contains(id) && !roles.contains(guild.getRolesByName("*", true).get(0))) {
                return false;
            }
        }

        Stream<Path> path = Files.walk(Path.of("./capes"));
        return path
                .filter(Files::isRegularFile)
                .anyMatch(file -> {
                    if (file.toFile().getName().replace(".png", "").equalsIgnoreCase(id)) {
                        File cape = new File("/var/www/html/capes/" + nick + ".png");
                        if (cape.exists()) {
                            cape.delete();
                        }
                        File newCape = new File("/var/www/html/capes/");
                        try {
                            FileUtils.copyFileToDirectory(new File("./capes/" + id + ".png"), newCape);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        cape = new File("/var/www/html/capes/" + id + ".png");
                        json.put("active-cape", id);
                        save(user, json.toString());
                        return cape.renameTo(new File("/var/www/html/capes/" + nick + ".png"));
                    }
                    return false;
                });
    }

    public boolean getInformation(String nick, TextChannel channel) throws IOException {
        Stream<Path> path = Files.walk(Path.of("./users"));

        return path.filter(Files::isRegularFile)
                .anyMatch(file -> {
                    try {
                        JSONObject json = read(file.toFile());
                        if (json.getString("nick").equalsIgnoreCase(nick)) {

                            String activeCape = json.getString("active-cape").isBlank()
                                    ? "`brak`" : "`" + json.getString("active-cape") + "`";
                            String activeItems = json.getJSONArray("active-items").isEmpty()
                                    ? "`brak`" : "`" + json.getJSONArray("active-items") + "`";
                            String capes = json.getJSONArray("capes").isEmpty()
                                    ? "`brak`" : "`" + json.getJSONArray("capes") + "`";
                            String items = json.getJSONArray("items").isEmpty()
                                    ? "`brak`" : "`" + json.getJSONArray("items") + "`";
                            channel.sendMessageEmbeds(
                                    Utils.createEmbed(
                                            "Sukces!",
                                            Color.GREEN,
                                            "Nick: `" + nick + "`"
                                                    + "\nId discord: `" + file.toFile().getName().replace(".json", "") + "`"
                                                    + "\nAktualnie założona pelerynka: " + activeCape
                                                    + "\nAktualnie założone itemy: " + activeItems
                                                    + "\nDostępne dodatkowe peleryny: " + capes
                                                    + "\nDostępne dodatkowe itemy: " + items
                                            ,
                                            null
                                    )
                            ).queue();
                            return true;
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    return false;
                });
    }

    public boolean changeNick(String discordId, String newNick) throws IOException {
        File user = new File("./users/" + discordId + ".json");

        if (!user.exists()) {
            return false;
        }

        if (checkNick(newNick)) {
            return false;
        }

        JSONObject json = read(user);
        File cape = new File("/var/www/html/capes/" + json.getString("nick") + ".png");
        File items = new File("/var/www/html/users/" + json.getString("nick") + ".cfg");
        boolean isCapeChanged = true, isItemsChanged = true;
        if (cape.exists()) {
            isCapeChanged = cape.renameTo(new File("/var/www/html/capes/" + newNick + ".png"));
        }
        if (items.exists()) {
            isItemsChanged = items.renameTo(new File("/var/www/html/users/" + newNick + ".cfg"));
        }

        json.put("nick", newNick);
        return isCapeChanged && isItemsChanged && save(user, json.toString());
    }

    public boolean removeCape(String discordId) throws IOException {
        File user = new File("./users/" + discordId + ".json");

        if (!user.exists()) {
            return false;
        }
        JSONObject json = read(user);
        File cape = new File("/var/www/html/capes/" + json.getString("nick") + ".png");

        if (cape.exists() && cape.delete()) {
            json.put("active-cape", "");
            return save(user, json.toString());
        }
        return false;
    }

    public String createVoucher(String type, String id) throws IOException {
        File vouchersFile = new File("./vouchers-" + type + ".json");
        String voucher = Long.toHexString(Double.doubleToLongBits(Math.random()));

        JSONObject json = read(vouchersFile);
        json.put(voucher, id);
        if (save(vouchersFile, json.toString())) {
            return voucher;
        }
        return null;
    }

    public boolean applyVoucher(String type, String voucher, String discordId) throws IOException {
        File vouchersFile = new File("./vouchers-" + type + ".json");
        File user = new File("./users/" + discordId + ".json");

        if (!user.exists()) {
            return false;
        }

        JSONObject jsonVouchers = read(vouchersFile);
        JSONObject jsonUser = read(user);
        String voucherValue;

        try {
            voucherValue = jsonVouchers.getString(voucher);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }


        if (jsonUser.getJSONArray(type + "s").toList().contains(voucher)) {
            return false;
        }

        List<Object> values = new ArrayList<>(jsonUser.getJSONArray(type + "s").toList());
        values.add(voucherValue);
        jsonVouchers.remove(voucher);
        jsonUser.put(type + "s", values);
        return save(vouchersFile, jsonVouchers.toString()) && save(user, jsonUser.toString());
    }


    public boolean addItem(String id, String discordId, List<Role> roles, Role requiredRole) throws IOException {
        File user = new File("./users/" + discordId + ".json");
        if (!user.exists()) {
            return false;
        }

        JSONObject json = read(user);
        List<Object> items = json.getJSONArray("items").toList();
        List<Object> userActiveItems = json.getJSONArray("active-items").toList();

        if (!items.contains(id) && !roles.contains(requiredRole)) {
            return false;
        }

        if (userActiveItems.contains(id)) {
            return false;
        }

        Stream<Path> path = Files.walk(Path.of("/var/www/html/items"));
        boolean result = path
                .filter(Files::isDirectory)
                .anyMatch(file -> file.toFile().getName().equalsIgnoreCase(id));

        if (!result) {
            return false;
        }

        File userItems = new File("/var/www/html/users/" + json.getString("nick") + ".cfg");
        JSONObject jsonItems = read(userItems);
        List<Object> activeItems = jsonItems.getJSONArray("items").toList();
        JSONObject item = new JSONObject();
        item.put("type", "custom");
        item.put("model", "items/" + id + "/model.cfg");
        item.put("texture", "items/" + id + "/texture.png");
        item.put("active", "true");
        activeItems.add(item);
        jsonItems.put("items", activeItems);


        userActiveItems.add(id);
        json.put("active-items", userActiveItems);
        return save(user, json.toString()) && save(userItems, jsonItems.toString());
    }

    public boolean removeItem(String id, String discordId) throws IOException {
        File user = new File("./users/" + discordId + ".json");

        if (!user.exists()) {
            return false;
        }

        JSONObject json = read(user);
        List<Object> items = json.getJSONArray("active-items").toList();

        if (!items.contains(id)) {
            return false;
        }

        File userItems = new File("/var/www/html/users/" + json.getString("nick") + ".cfg");
        JSONObject jsonItems = read(userItems);
        List<Object> userActiveItems = jsonItems.getJSONArray("items").toList();
        userActiveItems.removeIf(item -> item.toString().contains(id));

        jsonItems.put("items", userActiveItems);
        items.remove(id);
        json.put("active-items", items);
        return save(user, json.toString()) && save(userItems, jsonItems.toString());
    }

    public String checkDrop(User member) throws IOException {
        File user = new File("./users/" + member.getId() + ".json");
        if (!user.exists()) {
            return "Nie masz konta.";
        }

        JSONObject json = read(user);
        long now = System.currentTimeMillis();
        if (json.get("drop").toString().isEmpty()) {
            json.put("drop", now);
            save(user, json.toString());
            return "Nie udało Ci się tym razem wygrać, spróbuj ponownie za godzinę.";
        }


        Timestamp stamp = new Timestamp(json.getLong("drop"));
        long diffInMillis = (new Date()).getTime() - (new Date(stamp.getTime())).getTime();
        long minutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if (minutes >= 60) {
            json.put("drop", now);
            save(user, json.toString());
            Random random = new Random();
            JSONObject drop = read(new File("./drop.json"));
            List<String> typeList = Arrays.asList("cape", "item");
            String type = typeList.get(random.nextInt(typeList.size()));
            List<Object> selectedList = drop.getJSONArray(type).toList();
            String selected = String.valueOf(selectedList.get(random.nextInt(selectedList.size())));

            if (true) {
                member.openPrivateChannel().queue(
                        channel -> {
                            try {
                                String voucher = createVoucher(type, selected);
                                channel.sendMessageEmbeds(Utils.createEmbed(
                                        "Gratulacje!",
                                        Color.GREEN,
                                        "Udało Ci się wygrać " + type + " o id: `" +  selected + "`!\nWpisz `"
                                        + Utils.PREFIX + "voucher " + type + " " + voucher + "` aby odebrać nagrodę (Pamiętaj, że może to być duplikat)!" ,
                                        null
                                )).queue();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                );
                return "Gratulacje, wygrałeś! Sprawdź wiadomość prywatną od bota.";
            } else {
                return "Nie udało Ci się tym razem wygrać, spróbuj ponownie za godzinę.";
            }
        } else {
            return "Musisz poczekać jeszcze " + (60 - minutes) + " minut.";
        }
    }

}
