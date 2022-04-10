package com.darksoldier1404.dlt.commands;

import com.darksoldier1404.dlt.Lotto;
import com.darksoldier1404.dlt.functions.DLTFunction;
import com.darksoldier1404.dppc.utils.DataContainer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class DLTUCommand implements CommandExecutor, TabCompleter {
    private final DataContainer data = Lotto.getData();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(data.getPrefix() + "플레이어만 사용 가능한 명령어 입니다.");
            return false;
        }
        Player p = (Player) sender;
        if(args.length == 0){
            p.sendMessage(data.getPrefix() + "/로또 구매 (게임수) - 로또를 1게임 구매 또는 게임수 만큼 구매합니다.");
            p.sendMessage(data.getPrefix() + "/로또 수동구매 <1~45> <1~45> <1~45> <1~45> <1~45> <1~45> - 로또를 1게임 수동구매 합니다.");
            p.sendMessage(data.getPrefix() + "/로또 조회 <회차> - 회차를 입력하면 해당 회차의 로또를 조회하며, 당첨금 존재시 수령합니다.");
            p.sendMessage(data.getPrefix() + "/로또 당첨금 - 로또 당첨금을 확인하고 수령합니다.");
            return false;
        }
        if(args[0].equals("구매")){
            if(args.length == 1){
                DLTFunction.buyLotto(p);
                return false;
            }
            if(args.length == 2){
                DLTFunction.buyLotto(p, args[1]);
                return false;
            }
        }
        if(args[0].equals("수동구매")){
            if(args.length == 1){
                p.sendMessage(data.getPrefix() + "1번째 번호를 입력해주세요.");
                return false;
            }
            if(args.length == 2){
                p.sendMessage(data.getPrefix() + "2번째 번호를 입력해주세요.");
                return false;
            }
            if(args.length == 3){
                p.sendMessage(data.getPrefix() + "3번째 번호를 입력해주세요.");
                return false;
            }
            if(args.length == 4){
                p.sendMessage(data.getPrefix() + "4번째 번호를 입력해주세요.");
                return false;
            }
            if(args.length == 5){
                p.sendMessage(data.getPrefix() + "5번째 번호를 입력해주세요.");
                return false;
            }
            if(args.length == 6){
                p.sendMessage(data.getPrefix() + "6번째 번호를 입력해주세요.");
                return false;
            }
            if(args.length == 7){
                DLTFunction.buyLotto(p, args[1], args[2], args[3], args[4], args[5], args[6]);
                return false;
            }
        }
        if(args[0].equals("조회")){
            if(args.length == 1){
                p.sendMessage(data.getPrefix() + "회차를 입력해주세요.");
                return false;
            }
            if(args.length == 2){
                DLTFunction.search(p, args[1]);
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            return Arrays.asList("구매", "수동구매", "조회", "당첨금");
        }
        return null;
    }
}
