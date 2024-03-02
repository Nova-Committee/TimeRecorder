package top.infsky.mcstats.mixins;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.config.ModConfig;

import java.util.Objects;

@Mixin(Commands.class)
public class MixinCommand {
    @Inject(method = "performPrefixedCommand", at = @At("HEAD"))
    public void performPrefixedCommand(CommandSourceStack commandSourceStack, String string, CallbackInfoReturnable<Integer> cir) {
        if (!commandSourceStack.hasPermission(2)) return;
        if (!ModConfig.INSTANCE.getCommon().getCommandStatsList().contains(string.substring(1))) return;

        try {
            if (McStats.getStatsData() != null) {
                McStats.getStatsData().getPlayerDataMap().get(Objects.requireNonNull(commandSourceStack.getPlayer()).getUUID()).OPCommandUsed.add(string.substring(1));
            }
        } catch (NullPointerException ignored) {}
    }
}
