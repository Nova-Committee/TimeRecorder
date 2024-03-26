package top.infsky.timerecorder.anticheat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.checks.FlightA;
import top.infsky.timerecorder.data.PlayerData;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {
    public List<Check> checks = new ArrayList<>();
    public double violations = 0;
    public CheckManager(List<Check> checks) {
        this.checks.addAll(checks);
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(PlayerData playerData) {
        return new CheckManager(List.of(
                new FlightA(playerData)
        ));
    }

    public void update() {
        for (Check check : checks) {
            check.update();
        }
    }
}
