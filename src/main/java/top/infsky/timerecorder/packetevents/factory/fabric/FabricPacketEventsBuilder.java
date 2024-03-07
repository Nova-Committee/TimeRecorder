package top.infsky.timerecorder.packetevents.factory.fabric;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;
import io.netty.channel.Channel;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.SharedConstants;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.packetevents.PacketEventsMod;
import top.infsky.timerecorder.packetevents.handler.ServerPacketDecoder;
import top.infsky.timerecorder.packetevents.handler.ServerPacketEncoder;

import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class FabricPacketEventsBuilder {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-ORX]");
    private static PacketEventsAPI<MinecraftServer> INSTANCE;

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<MinecraftServer> build(String modId) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(modId);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<MinecraftServer> build(String modId, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(modId, settings);
        }
        return INSTANCE;
    }


    public static PacketEventsAPI<MinecraftServer> buildNoCache(String modId) {
        return buildNoCache(modId, new PacketEventsSettings());
    }

    public static PacketEventsAPI<MinecraftServer> buildNoCache(String modId, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<>() {
            private final PacketEventsSettings settings = inSettings;
            //TODO Implement platform version
            private final ProtocolManager protocolManager = new ProtocolManagerAbstract() {
                @Override
                public ProtocolVersion getPlatformVersion() {
                    return ProtocolVersion.UNKNOWN;
                }
            };
            private final ServerManager serverManager = new ServerManagerAbstract() {
                private static ServerVersion VERSION;

                @Override
                public ServerVersion getVersion() {
                    //TODO Not perfect, as this is on the client! Might be inaccurate by a few patch versions.
                    if (VERSION == null) {
                        int targetPV = SharedConstants.getProtocolVersion();
                        for (ServerVersion version : ServerVersion.reversedValues()) {
                            if (version.getProtocolVersion() == targetPV) {
                                VERSION = version;
                            }
                        }
                    }
                    return VERSION;
                }
            };

            private final PlayerManagerAbstract playerManager = new PlayerManagerAbstract() {
                @Override
                public int getPing(@NotNull Object player) {
                    UUID id = ((Player) player).getGameProfile().getId();
                    try {
                        var minecraft = getPlugin();
                        if(minecraft == null)
                            return ((LocalPlayer) player).connection.getPlayerInfo(id).getLatency();
                        else {
                            var playerManager = minecraft.getPlayerList();
                            var playerEntity = playerManager.getPlayer(id);
                            return playerEntity.latency;
                        }
                    } catch (Exception ex) {
                        PacketEvents.getAPI().getLogManager().debug("Failed to get ping for player " + id);
                        return -1;
                    }
                }

                @Override
                public Object getChannel(@NotNull Object player) {
                    Connection connection = ((LocalPlayer) player).connection.getConnection();
                    ReflectionObject reflectConnection = new ReflectionObject(connection);
                    return reflectConnection.readObject(0, Channel.class);
                }
            };

            private final ChannelInjector injector = new ChannelInjector() {
                @Override
                   public boolean isServerBound() {
                    return true;
                }

                @Override
                public void inject() {

                }

                @Override
                public void uninject() {

                }

                @Override
                public void updateUser(Object ch, User user) {
                    Channel channel = (Channel) ch;
                    ServerPacketDecoder decoder = (ServerPacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
                    decoder.user = user;
                    ServerPacketEncoder encoder = (ServerPacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
                    encoder.user = user;
                }

                @Override
                public void setPlayer(Object ch, Object player) {
                    Channel channel = (Channel) ch;
                    ServerPacketDecoder decoder = (ServerPacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
                    decoder.player = (Player) player;

                    ServerPacketEncoder encoder = (ServerPacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
                    encoder.player = (Player) player;
                }

                @Override
                public boolean isProxy() {
                    return false;
                }
            };
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final LogManager logManager = new LogManager() {
                @Override
                protected void log(Level level, @Nullable NamedTextColor color, String message) {
                    //First we must strip away the color codes that might be in this message
                    message = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
                    PacketEventsMod.LOGGER.info(message);
                }
            };
            private boolean loaded;
            private boolean initialized;

            @Override
            public void load() {
                if (!loaded) {
                    final String id = modId.toLowerCase();
                    //Resolve server version and cache
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;

                    injector.inject();

                    loaded = true;

                    //Register internal packet listener (should be the first listener)
                    //This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalPacketListener());
                }
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void init() {
                //Load if we haven't loaded already
                load();
                if (!initialized) {
                    if (settings.shouldCheckForUpdates()) {
                        getUpdateChecker().handleUpdateCheck();
                    }

                    if (settings.isbStatsEnabled()) {
                        //TODO Cross-platform metrics?
                    }

                    PacketType.Play.Client.load();
                    PacketType.Play.Server.load();
                    initialized = true;
                }
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }

            @Override
            public void terminate() {
                if (initialized) {
                    //Eject the injector if needed(depends on the injector implementation)
                    injector.uninject();
                    //Unregister all our listeners
                    getEventManager().unregisterAllListeners();
                    initialized = false;
                }
            }

            @Override
            public MinecraftServer getPlugin() {
                return ServerInstanceManager.getServerInstance();
            }

            @Override
            public ProtocolManager getProtocolManager() {
                return protocolManager;
            }

            @Override
            public ServerManager getServerManager() {
                return serverManager;
            }

            @Override
            public LogManager getLogManager() {
                return logManager;
            }

            @Override
            public PlayerManager getPlayerManager() {
                return playerManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return injector;
            }

            @Override
            public PacketEventsSettings getSettings() {
                return settings;
            }

            @Override
            public NettyManager getNettyManager() {
                return nettyManager;
            }
        };
    }
}
