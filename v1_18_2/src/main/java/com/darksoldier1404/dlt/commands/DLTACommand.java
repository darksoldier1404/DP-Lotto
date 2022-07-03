package com.darksoldier1404.dlt.commands;

import com.darksoldier1404.dlt.Lotto;
import com.darksoldier1404.dlt.functions.DLTFunction;
import com.darksoldier1404.dppc.utils.DataContainer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class DLTACommand implements CommandExecutor, TabCompleter {
    private final DataContainer data = Lotto.getData();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(data.getPrefix() + "관리자 전용 명령어 입니다.");
            return false;
        }
        if(args.length == 0){
            sender.sendMessage(data.getPrefix() + "/dlta 추첨 - 로또 추첨을 강제로 시작합니다.");
            sender.sendMessage(data.getPrefix() + "/dlta reload - 로또 설정을 다시 불러옵니다.");
            return false;
        }
        if(args[0].equalsIgnoreCase("추첨")){
            DLTFunction.startLotto();
            sender.sendMessage(data.getPrefix() + "로또 추첨을 시작합니다.");
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            return Arrays.asList("추첨", "reload");
        }
        return null;
    }
}
