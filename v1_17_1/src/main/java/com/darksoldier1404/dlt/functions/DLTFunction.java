package com.darksoldier1404.dlt.functions;

import com.darksoldier1404.dlt.Lotto;
import com.darksoldier1404.dppc.api.essentials.MoneyAPI;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.DataContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class DLTFunction {
    private static final Lotto plugin = Lotto.getInstance();
    private static final DataContainer data = plugin.getData();

    public static void switchLotto(Player p) {
        if (data.getConfig().getBoolean("Settings.Enabled")) {
            data.getConfig().set("Settings.Enabled", false);
            if (plugin.task != null) {
                plugin.task.cancel();
                plugin.task = null;
            }
            p.sendMessage(data.getPrefix() + "로또가 비활성화 되었습니다.");
        } else {
            data.getConfig().set("Settings.Enabled", true);
            String stime = data.getConfig().getString("Settings.LotteryTime") + ":00";
            if (data.getConfig().getString("Settings.Type").equalsIgnoreCase("F")) {
                plugin.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    Date time = new Date();
                    if (new SimpleDateFormat("HH:mm:ss").format(time).equals(stime)) {
                        startLotto();
                    }
                }, 0L, 20L);
            } else {
                Date time = new Date();
                SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
                f.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date d = null;
                try {
                    d = f.parse(stime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long secs = d.getTime() / 1000;
                plugin.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    startLotto();
                }, 20 * secs, secs * 20L);
            }
            p.sendMessage(data.getPrefix() + "로또가 활성화 되었습니다.");
            data.save();
        }
    }

    public static void initTask() {
        if (data.getConfig().getBoolean("Settings.Enabled")) {
            String stime = data.getConfig().getString("Settings.LotteryTime") + ":00";
            if (data.getConfig().getString("Settings.Type").equalsIgnoreCase("F")) {
                plugin.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    Date time = new Date();
                    if (new SimpleDateFormat("HH:mm:ss").format(time).equals(stime)) {
                        startLotto();
                    }
                }, 0L, 20L);
            } else {
                Date time = new Date();
                SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
                f.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date d = null;
                try {
                    d = f.parse(stime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long secs = d.getTime() / 1000;
                plugin.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    startLotto();
                }, 20 * secs, secs * 20L);
            }
        }
    }

    public static void startLotto() {
        if (data.getConfig().getBoolean("Settings.Enabled")) {
            if (data.getConfig().getConfigurationSection("Settings.Lotto") == null) {
                data.getConfig().set("Settings.Lotto.1.numbers", getLotto());
                Bukkit.broadcastMessage(data.getPrefix() + "로또" + 1 + "회차가 추첨되었습니다.");
            } else {
                int i = data.getConfig().getConfigurationSection("Settings.Lotto").getKeys(false).size() + 1;
                data.getConfig().set("Settings.Lotto." + i + ".numbers", getLotto().toString());
                Bukkit.broadcastMessage(data.getPrefix() + "로또" + i + "회차가 추첨되었습니다.");
            }
            saveConfig();
        }
    }

    public static String getLotto() {
        Random r = new Random();
        int[] lotto = new int[6];
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < lotto.length; i++) {
            lotto[i] = r.nextInt(45) + 1;
            if (list.contains(lotto[i])) {
                i--;
            } else {
                list.add(lotto[i]);
            }
        }
        // sort lotto
        for (int i = 0; i < lotto.length; i++) {
            for (int j = i + 1; j < lotto.length; j++) {
                if (lotto[i] > lotto[j]) {
                    int temp = lotto[i];
                    lotto[i] = lotto[j];
                    lotto[j] = temp;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lotto.length; i++) {
            sb.append(lotto[i]);
            if (i != lotto.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static void match(Player p, String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            p.sendMessage(data.getPrefix() + "옳바른 숫자를 입력해주세요.");
            return;
        }
        if (data.getConfig().getString("Settings.Lotto." + i + ".numbers") == null) {
            p.sendMessage(data.getPrefix() + "로또" + i + "회차가 존재하지 않습니다.");
            return;
        }
        List<String> uLotto = data.getUserData(p.getUniqueId()).getStringList("Lotto." + i + ".numbers");
        if (uLotto == null) {
            p.sendMessage(data.getPrefix() + "로또" + i + "회차를 구매한적이 없습니다.");
            return;
        }
        for (String s1 : uLotto) {
            String[] uNumbers = s1.split(", ");
            String lotto = data.getConfig().getString("Settings.Lotto." + i + ".numbers");
            List<String> numbers = new ArrayList<>(Arrays.stream(lotto.split(", ")).collect(Collectors.toList()));
            int count = 0;
            int loop = 0;
            String r = "";
            for (String number : uNumbers) {
                if (numbers.contains(number)) {
                    uNumbers[loop] = uNumbers[loop].replace(number, "§b" + number);
                    count++;
                } else {
                    uNumbers[loop] = uNumbers[loop].replace(number, "§c" + number);
                }
                r += uNumbers[loop] + ", ";
                loop++;
            }
            if (count == 6) {
                p.sendMessage(data.getPrefix() + "로또" + i + "회차에서 모든 번호가 맞았습니다.");
                p.sendMessage(data.getPrefix() + "축하합니다. 당신은 1등입니다! : " + r);
                if (!data.getUserData(p.getUniqueId()).getBoolean("Lotto." + i + ".accept")) {
                    p.sendMessage(data.getPrefix() + "당첨금 : " + data.getConfig().getInt("Settings.1th_price") + "원을 수령했습니다.");
                    MoneyAPI.addMoney(p, BigDecimal.valueOf(data.getConfig().getLong("Settings.1th_price")));
                }
            } else if (count == 5) {
                p.sendMessage(data.getPrefix() + "로또" + i + "회차에서 5개의 번호가 맞았습니다.");
                p.sendMessage(data.getPrefix() + "축하합니다. 당신은 2등입니다! : " + r);
                if (!data.getUserData(p.getUniqueId()).getBoolean("Lotto." + i + ".accept")) {
                    p.sendMessage(data.getPrefix() + "당첨금 : " + data.getConfig().getInt("Settings.2th_price") + "원을 수령했습니다.");
                    MoneyAPI.addMoney(p, BigDecimal.valueOf(data.getConfig().getLong("Settings.2th_price")));
                }
            } else if (count == 4) {
                p.sendMessage(data.getPrefix() + "로또" + i + "회차에서 4개의 번호가 맞았습니다.");
                p.sendMessage(data.getPrefix() + "축하합니다. 당신은 3등입니다! : " + r);
                if (!data.getUserData(p.getUniqueId()).getBoolean("Lotto." + i + ".accept")) {
                    p.sendMessage(data.getPrefix() + "당첨금 : " + data.getConfig().getInt("Settings.3th_price") + "원을 수령했습니다.");
                    MoneyAPI.addMoney(p, BigDecimal.valueOf(data.getConfig().getLong("Settings.3th_price")));
                }
            } else if (count == 3) {
                p.sendMessage(data.getPrefix() + "로또" + i + "회차에서 3개의 번호가 맞았습니다.");
                p.sendMessage(data.getPrefix() + "축하합니다. 당신은 4등입니다! : " + r);
                if (!data.getUserData(p.getUniqueId()).getBoolean("Lotto." + i + ".accept")) {
                    p.sendMessage(data.getPrefix() + "당첨금 : " + data.getConfig().getInt("Settings.4th_price") + "원을 수령했습니다.");
                    MoneyAPI.addMoney(p, BigDecimal.valueOf(data.getConfig().getLong("Settings.4th_price")));
                }
            } else if (count == 2) {
                p.sendMessage(data.getPrefix() + "로또" + i + "회차에서 2개의 번호가 맞았습니다.");
                p.sendMessage(data.getPrefix() + "축하합니다. 당신은 5등입니다! : " + r);
                if (!data.getUserData(p.getUniqueId()).getBoolean("Lotto." + i + ".accept")) {
                    p.sendMessage(data.getPrefix() + "당첨금 : " + data.getConfig().getInt("Settings.5th_price") + "원을 수령했습니다.");
                    MoneyAPI.addMoney(p, BigDecimal.valueOf(data.getConfig().getLong("Settings.5th_price")));
                }
            } else {
                p.sendMessage(data.getPrefix() + "아쉽지만 꽝입니다. : " + r);
            }
        }
        data.getUserData(p.getUniqueId()).set("Lotto." + i + ".accept", true);
    }

    public static void search(Player p, String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            p.sendMessage(data.getPrefix() + "옳바른 숫자를 입력해주세요.");
            return;
        }
        p.sendMessage(data.getPrefix() + "로또" + i + "회차의 번호는 " + data.getConfig().getString("Settings.Lotto." + i + ".numbers") + "입니다.");
        match(p, s);
    }

    public static void buyLotto(Player p) {
        if (MoneyAPI.hasEnoughMoney(p, data.getConfig().getDouble("Settings.buy_price"))) {
            if (data.getUserData(p.getUniqueId()).getConfigurationSection("Lotto") == null) {
                List<String> list = new ArrayList<>();
                if (list.size() >= data.getConfig().getInt("Settings.max_buyGame_per_round")) {
                    p.sendMessage(data.getPrefix() + "1회차당 최대 게임 구매 횟수를 초과합니다.");
                    p.sendMessage(data.getPrefix() + "구매할 수 없습니다.");
                    return;
                }
                list.add(getLotto());
                data.getUserData(p.getUniqueId()).set("Lotto.1.numbers", list);
            } else {
                int round = 0;
                if (data.getConfig().getConfigurationSection("Settings.Lotto") == null) {
                    round = 1;
                } else {
                    round = data.getConfig().getConfigurationSection("Settings.Lotto").getKeys(false).size() + 1;
                }
                int i = round;
                List<String> list = data.getUserData(p.getUniqueId()).getStringList("Lotto." + i + ".numbers");
                if (list.size() >= data.getConfig().getInt("Settings.max_buyGame_per_round")) {
                    p.sendMessage(data.getPrefix() + "1회차당 최대 게임 구매 횟수를 초과합니다.");
                    p.sendMessage(data.getPrefix() + "구매할 수 없습니다.");
                    return;
                }
                list.add(getLotto());
                data.getUserData(p.getUniqueId()).set("Lotto." + i + ".numbers", list);
            }
            MoneyAPI.takeMoney(p, data.getConfig().getDouble("Settings.buy_price"));
            p.sendMessage(data.getPrefix() + "로또를 구매하였습니다.");
            return;
        } else {
            p.sendMessage(data.getPrefix() + "구매 비용이 부족합니다.");
        }
    }

    public static void buyLotto(Player p, String sint) {
        try {
            int i = Integer.parseInt(sint);
            if(i <= 0) {
                p.sendMessage(data.getPrefix() + "옳바를 숫자를 입력해주세요.");
                return;
            }
            if (MoneyAPI.hasEnoughMoney(p, data.getConfig().getDouble("Settings.buy_price"))) {
                if (data.getUserData(p.getUniqueId()).getConfigurationSection("Lotto") == null) {
                    List<String> list = new ArrayList<>();
                    if (list.size() + i >= data.getConfig().getInt("Settings.max_buyGame_per_round")) {
                        p.sendMessage(data.getPrefix() + "1회차당 최대 게임 구매 횟수를 초과합니다.");
                        p.sendMessage(data.getPrefix() + "구매할 수 없습니다.");
                        return;
                    }
                    for (int ii = 0; ii < i; ii++) {
                        buyLotto(p);
                    }
                } else {
                    int count = data.getUserData(p.getUniqueId()).getConfigurationSection("Lotto").getKeys(false).size() + 1;
                    List<String> list = data.getUserData(p.getUniqueId()).getStringList("Lotto." + i + ".numbers");
                    if (list.size() + i >= data.getConfig().getInt("Settings.max_buyGame_per_round")) {
                        p.sendMessage(data.getPrefix() + "1회차당 최대 게임 구매 횟수를 초과합니다.");
                        p.sendMessage(data.getPrefix() + "구매할 수 없습니다.");
                        return;
                    }
                    for (int ii = 0; ii < i; ii++) {
                        buyLotto(p);
                    }
                }
            } else {
                p.sendMessage(data.getPrefix() + "구매 비용이 부족합니다.");
            }
        } catch (NumberFormatException e) {
            p.sendMessage(data.getPrefix() + "옳바른 숫자를 입력해주세요.");
        }
    }

    public static void buyLotto(Player p, String arg, String arg1, String arg2, String arg3, String arg4, String arg5) {
        List<String> args = new ArrayList<>();
        args.addAll(Arrays.asList(arg, arg1, arg2, arg3, arg4, arg5));
        for (String s : args) {
            try {
                if (!(Integer.parseInt(s) > 0) && !(Integer.parseInt(s) < 45)) {
                    p.sendMessage(data.getPrefix() + "옳바른 범위 내의 숫자를 입력해주세요.");
                    return;
                }
            } catch (NumberFormatException e) {
                p.sendMessage(data.getPrefix() + "옳바른 숫자를 입력해주세요.");
                return;
            }
        }
        if (MoneyAPI.hasEnoughMoney(p, data.getConfig().getDouble("Settings.buy_price"))) {
            if (data.getUserData(p.getUniqueId()).getConfigurationSection("Lotto") == null) {
                List<String> list = new ArrayList<>();
                if (list.size() >= data.getConfig().getInt("Settings.max_buyGame_per_round")) {
                    p.sendMessage(data.getPrefix() + "1회차당 최대 게임 구매 횟수를 초과합니다.");
                    p.sendMessage(data.getPrefix() + "구매할 수 없습니다.");
                    return;
                }
                list.add(args.stream().collect(Collectors.joining(", ")));
                data.getUserData(p.getUniqueId()).set("Lotto.1.numbers", list);
            } else {
                int round = 0;
                if (data.getConfig().getConfigurationSection("Settings.Lotto") == null) {
                    round = 1;
                } else {
                    round = data.getConfig().getConfigurationSection("Settings.Lotto").getKeys(false).size() + 1;
                }
                int i = round;
                List<String> list = data.getUserData(p.getUniqueId()).getStringList("Lotto." + i + ".numbers");
                if (list.size() >= data.getConfig().getInt("Settings.max_buyGame_per_round")) {
                    p.sendMessage(data.getPrefix() + "1회차당 최대 게임 구매 횟수를 초과합니다.");
                    p.sendMessage(data.getPrefix() + "구매할 수 없습니다.");
                    return;
                }
                list.add(args.stream().collect(Collectors.joining(", ")));
                data.getUserData(p.getUniqueId()).set("Lotto." + i + ".numbers", list);
            }
            MoneyAPI.takeMoney(p, data.getConfig().getDouble("Settings.buy_price"));
            p.sendMessage(data.getPrefix() + "로또를 구매하였습니다.");
            return;
        } else {
            p.sendMessage(data.getPrefix() + "구매 비용이 부족합니다.");
        }
    }

    private static void saveConfig() {
        ConfigUtils.savePluginConfig(plugin, data.getConfig());
    }
}
