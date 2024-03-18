package top.infsky.timerecorder.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.*;

public class ICmdEvent {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("tr")  // 没参数默认显示帮助信息
                .executes(HelpCommand::execute)
                        .then(literal("help")  // 显示帮助信息
                                .executes(HelpCommand::execute))
                        .then(literal("reload")  // 热重载
                                .requires(source -> source.hasPermission(2))
                                .executes(ReloadCommand::execute))
                        .then(literal("dump")
                                .requires(source -> source.hasPermission(2))
                                .then(literal("save")  // 保存迄今的统计数据
                                        .executes(DumpCommand.Save::execute))
                                .then(literal("load")  // 从文件还原统计数据
                                        .executes(DumpCommand.Load::execute)))
                        .then(literal("report")  // 对自己显示当日截止目前的统计信息
                                .executes(ReportCommand::execute))
                        .then(literal("reportall")  // 对自己显示当日截止目前的所有玩家的统计信息
                                .requires(source -> source.hasPermission(2))
                                .executes(ReportAllCommand::execute))
                        .then(literal("reportqq")  // 对所有人显示和发送当日截止目前的统计信息到QQ
                                .requires(source -> source.hasPermission(2))
                                .executes(ReportQQCommand::execute))
                        .then(literal("recall")
                                .executes(RecallCommand::execute)  // 撤回上一条消息
                )
        );
    }
}
