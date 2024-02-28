package top.infsky.mcstats.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.*;

public class ICmdEvent {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("mcstats")
//                .executes(HelpCommand::execute)
                    .then(literal("help")
                        .executes(HelpCommand::execute))
                    .then(literal("connect")
                        .executes(ConnectCommand::execute))
                    .then(literal("report")
                        .executes(ReportCommand::execute))
                    .then(literal("reportall")
                        .executes(ReportAllCommand::execute))
                    .then(literal("reportqq")
                        .executes(ReportQQCommand::execute))
        );
    }
}
