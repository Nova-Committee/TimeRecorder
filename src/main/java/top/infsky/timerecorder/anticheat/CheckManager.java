package top.infsky.timerecorder.anticheat;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.anticheat.checks.FlightA;
import top.infsky.timerecorder.anticheat.events.PacketPlayerAbilities;
import top.infsky.timerecorder.anticheat.events.PlayerLoggedIn;

import java.util.Objects;

public class CheckManager extends PacketListenerAbstract {
    ClassToInstanceMap<Check> packetChecks;

    public CheckManager() {}

    public CheckManager(TRPlayer player) {
        packetChecks = new ImmutableClassToInstanceMap.Builder<Check>()
                .put(PacketPlayerAbilities.class, new PacketPlayerAbilities(player))
//                .put(FlightA.class, new FlightA(player)) 现在知道为什么GrimAC没有启用这个检测了
                .build();
    }

    @Override
    public void onPacketReceive(@NotNull PacketReceiveEvent event) {
        if (event.getConnectionState() != ConnectionState.PLAY) return;

        TRPlayer player = Utils.getTRPlayer(event.getUser());
        if (player == null) PlayerLoggedIn.onUserLogin(event);

        // Call the packet checks last as they can modify the contents of the packet
        // Such as the NoFall check setting the player to not be on the ground
        for (Check check : Objects.requireNonNull(Objects.requireNonNull(Utils.getPlayer(event.getUser().getUUID())).getCheckManager()).packetChecks.values()) {
            check.onPacketReceive(event);
        }
    }
}
