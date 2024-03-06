package top.infsky.timerecorder.mixins;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.config.ModConfig;

import java.util.Objects;

@Mixin(Commands.class)
public class MixinCommand {
    @Inject(method = "performCommand", at = @At("HEAD"))
    public void performPrefixedCommand(@NotNull ParseResults<CommandSourceStack> parseResults, String string, CallbackInfoReturnable<Integer> cir) {
        if (!ModConfig.INSTANCE.getCommon().isAllowCommandStats()) return;  // 绷
        CommandSourceStack commandSourceStack = parseResults.getContext().getSource();
        if (!commandSourceStack.hasPermission(2)) return;

        final String cmd = string.split(" ")[0];
        if (!ModConfig.INSTANCE.getCommon().getCommandStatsList().contains(cmd)) return;
        try {
            Utils.getStatsData().getPlayerDataMap().get(Objects.requireNonNull(commandSourceStack.getPlayer()).getUUID()).OPCommandUsed.add(cmd);
        } catch (NullPointerException ignored) {}
    }
}
