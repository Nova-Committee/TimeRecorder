package top.infsky.mcstats.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.*;

public class ICmdEvent {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("mcstats")  // 没参数默认显示帮助信息
                .executes(HelpCommand::execute)
                    .then(literal("help")  // 显示帮助信息
                        .executes(HelpCommand::execute))
                    .then(literal("connect")  // 连接QQ机器人
                        .requires(source -> source.hasPermission(2))
                            .executes(ConnectCommand::execute)
                    .then(literal("report")  // 对自己显示当日截止目前的统计信息
                        .executes(ReportCommand::execute))
                    .then(literal("reportall")  // 对自己显示当日截止目前的所有玩家的统计信息
                        .requires(source -> source.hasPermission(2))
                            .executes(ReportAllCommand::execute)
                    .then(literal("reportqq")  // 对所有人显示和发送当日截止目前的统计信息到QQ
                        .requires(source -> source.hasPermission(2))
                            .executes(ReportQQCommand::execute)
        ))));
    }
}
