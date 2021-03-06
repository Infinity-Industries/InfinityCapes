package me.nes0x.utils;

import net.dv8tion.jda.api.entities.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class UserService {


    private boolean save(File file, JSONObject json) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.write(json.toString());
            writer.flush();
            writer.close();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private JSONObject read(File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String text;
            while ((text = reader.readLine()) != null) {
                builder.append(text);
            }
            reader.close();
            return new JSONObject(builder.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return null;
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

        if (user.exists() || checkNick(nick)) {
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
            return save(user, json) && save(new File("/var/www/html/users/" + nick + ".cfg"), items);
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

        if (id.toLowerCase().startsWith("p_")) {
            if (!json.getJSONArray("capes").toList().contains(id)
                    && !roles.contains(guild.getRolesByName("*", true).get(0))
                    && !roles.contains(guild.getRoleById("813562303358173294"))) {
                return false;
            }
        } else if (id.toLowerCase().startsWith("a_")) {
            if (!json.getJSONArray("capes").toList().contains(id)
                    && !roles.contains(guild.getRolesByName("*", true).get(0))) {
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
                        save(user, json);
                        return cape.renameTo(new File("/var/www/html/capes/" + nick + ".png"));
                    }
                    return false;
                });
    }

    public boolean getInformation(String nick, TextChannel channel, Guild guild) throws IOException {
        Stream<Path> path = Files.walk(Path.of("./users"));

        return path.filter(Files::isRegularFile)
                .anyMatch(file -> {
                    try {
                        JSONObject json = read(file.toFile());
                        if (json.getString("nick").equalsIgnoreCase(nick)) {

                            String nickFromJson = "`" + json.getString("nick") + "`";
                            String activeCape = json.getString("active-cape").isBlank()
                                    ? "`brak`" : "`" + json.getString("active-cape") + "`";
                            String activeItems = json.getJSONArray("active-items").isEmpty()
                                    ? "`brak`" : "`" + json.getJSONArray("active-items") + "`";
                            String capes = json.getJSONArray("capes").isEmpty()
                                    ? "`brak`" : "`" + json.getJSONArray("capes") + "`";
                            String items = json.getJSONArray("items").isEmpty()
                                    ? "`brak`" : "`" + json.getJSONArray("items") + "`";
                            String discordId = file.toFile().getName().replace(".json", "");

                            Member member = guild.retrieveMemberById(discordId).complete();
                            if (member.getRoles().contains(
                                    guild.getRolesByName("*", true).get(0))) {
                                capes = "`wszystkie`";
                                items = "`wszystkie`";
                            } else if (member.getRoles().contains(guild.getRoleById("813562303358173294"))) {
                                capes = capes.equalsIgnoreCase("`brak`") ? "`premium`" : "`premium`, " + capes;
                            }

                            channel.sendMessageEmbeds(
                                    BotUtils.createEmbed(
                                            "Sukces!",
                                            Color.GREEN,
                                            "Nick: " + nickFromJson
                                                    + "\nId discord: `" + discordId + "`"
                                                    + "\nAktualnie za??o??ona pelerynka: " + activeCape
                                                    + "\nAktualnie za??o??one itemy: " + activeItems
                                                    + "\nDost??pne dodatkowe peleryny: " + capes
                                                    + "\nDost??pne dodatkowe itemy: " + items
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
        return isCapeChanged && isItemsChanged && save(user, json);
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
            return save(user, json);
        }
        return false;
    }

    public String createVoucher(String type, String id) throws IOException {
        File vouchersFile = new File("./vouchers-" + type + ".json");

        if (!vouchersFile.exists()) {
            return null;
        }

        String voucher = Long.toHexString(Double.doubleToLongBits(Math.random()));

        JSONObject json = read(vouchersFile);
        json.put(voucher, id);
        if (save(vouchersFile, json)) {
            return voucher;
        }
        return null;
    }

    public boolean removeVoucher(String code, String type) throws IOException {
        File vouchersFile = new File("./vouchers-" + type + ".json");

        if (!vouchersFile.exists()) {
            return false;
        }

        JSONObject json = read(vouchersFile);

        if (json.getString(code) == null) {
            return false;
        }
        json.remove(code);
        return save(vouchersFile, json);
    }

    public MessageEmbed dropList() throws IOException {
        File drops = new File("./drop.json");

        if (!drops.exists()) {
            return null;
        }

        JSONObject json = read(drops);

        return BotUtils.createEmbed("Aktualny drop to:", Color.CYAN,
                "Itemy: `" + json.getJSONArray("item").toList() + "`\nPeleryny: `"
                        + json.getJSONArray("cape").toList() + "`", null);
    }

    public boolean applyVoucher(String type, String voucher, String discordId) throws IOException {
        File vouchersFile = new File("./vouchers-" + type + ".json");
        File user = new File("./users/" + discordId + ".json");

        if (!user.exists()) {
            return false;
        }
        if (!vouchersFile.exists()) {
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
        return save(vouchersFile, jsonVouchers) && save(user, jsonUser);
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
        return save(user, json) && save(userItems, jsonItems);
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
        return save(user, json) && save(userItems, jsonItems);
    }

    public String checkDrop(User member) throws IOException {
        File user = new File("./users/" + member.getId() + ".json");
        if (!user.exists()) {
            return "Nie masz konta.";
        }

        JSONObject jsonUser = read(user);
        long nowTime = System.currentTimeMillis();
        if (jsonUser.get("drop").toString().isEmpty()) {
            jsonUser.put("drop", nowTime);
            save(user, jsonUser);
            return "Nie uda??o Ci si?? tym razem wygra??, spr??buj ponownie za godzin??.";
        }


        Timestamp stamp = new Timestamp(jsonUser.getLong("drop"));
        long diffInMillis = (new Date()).getTime() - (new Date(stamp.getTime())).getTime();
        long minutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if (minutes >= 60) {
            jsonUser.put("drop", nowTime);
            save(user, jsonUser);
            Random random = new Random();
            File dropFile = new File("./drop.json");
            JSONObject drop = read(dropFile);
            List<String> typeList = Arrays.asList("cape", "item");
            String type = typeList.get(random.nextInt(typeList.size()));
            List<Object> selectedList = drop.getJSONArray(type).toList();
            String selected = String.valueOf(selectedList.get(random.nextInt(selectedList.size())));

            if (random.nextInt(251) >= 242) {
                member.openPrivateChannel().queue(
                        channel -> {
                            try {
                                String voucher = createVoucher(type, selected);
                                channel.sendMessageEmbeds(BotUtils.createEmbed(
                                        "Gratulacje!",
                                        Color.GREEN,
                                        "Uda??o Ci si?? wygra?? " + type + " o id: `" + selected + "`!\nWpisz `"
                                                + BotUtils.PREFIX + "voucher " + type + " " + voucher +
                                                "` aby odebra?? nagrod?? (Pami??taj, ??e mo??e to by?? duplikat)!",
                                        null
                                )).queue();
                                drop.put("winners", drop.getInt("winners") + 1);
                                save(dropFile, drop);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                );
                return "Gratulacje, wygra??e??! Sprawd?? wiadomo???? prywatn?? od bota.";
            } else {
                return "Nie uda??o Ci si?? tym razem wygra??, spr??buj ponownie za godzin??.";
            }
        } else {
            return "Musisz poczeka?? jeszcze " + (60 - minutes) + " minut.";
        }
    }

    public boolean changeDrop(String type, String id, boolean remove) throws IOException {
        File drop = new File("./drop.json");

        if (!drop.exists()) {
            return false;
        }

        JSONObject json = read(drop);
        List<Object> drops = json.getJSONArray(type).toList();


        if (remove) {
            if (!drops.contains(id)) {
                return false;
            }
            drops.remove(id);
        } else {
            if (drops.contains(id)) {
                return false;
            }
            drops.add(id);
        }

        json.put(type, drops);
        return save(drop, json);
    }

    public int winnersNumber() throws IOException {
        File drop = new File("./drop.json");
        if (!drop.exists()) {
            return 0;
        }
        JSONObject json = read(drop);
        return json.getInt("winners");
    }

    public MessageEmbed vouchersList() throws IOException {
        File vouchersCapeFile = new File("./vouchers-cape.json");
        File vouchersItemFile = new File("./vouchers-item.json");

        if (!vouchersCapeFile.exists()) {
            return null;
        }
        if (!vouchersItemFile.exists()) {
            return null;
        }

        JSONObject cape = read(vouchersCapeFile);
        JSONObject item = read(vouchersItemFile);

        Map<String, Object> capeMap = cape.toMap();
        Map<String, Object> itemMap = item.toMap();

        return BotUtils.createEmbed("Aktualne vouchery to:",
                Color.CYAN,
                "Peleryny: `" + capeMap + "`\nItemy: `" + itemMap + "`",
                null);

    }

}
