package top.infsky.mcstats.mixins;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.config.ModConfig;
import top.infsky.mcstats.log.LogUtils;

import java.util.Objects;

@Mixin(Commands.class)
public class MixinCommand {
    @Inject(method = "performPrefixedCommand", at = @At("HEAD"))
    public void performPrefixedCommand(@NotNull CommandSourceStack commandSourceStack, String string, CallbackInfoReturnable<Integer> cir) {
        LogUtils.LOGGER.info(String.format("cur command: %s", string));
        if (!commandSourceStack.hasPermission(2)) return;

        final String cmd = string.substring(1).split(" ")[0];
        if (!ModConfig.INSTANCE.getCommon().getCommandStatsList().contains(cmd)) return;
        try {
            McStats.getStatsData().getPlayerDataMap().get(Objects.requireNonNull(commandSourceStack.getPlayer()).getUUID()).OPCommandUsed.add(cmd);
        } catch (NullPointerException ignored) {}
    }
}
