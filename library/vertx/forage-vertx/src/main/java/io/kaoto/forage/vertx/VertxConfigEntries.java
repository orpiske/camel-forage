package io.kaoto.forage.vertx;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;

public final class VertxConfigEntries extends ConfigEntries {
    public static final ConfigModule WORKER_POOL_SIZE =
            ConfigModule.of(VertxConfig.class, "forage.vertx.worker.pool.size");
    public static final ConfigModule EVENT_LOOP_POOL_SIZE =
            ConfigModule.of(VertxConfig.class, "forage.vertx.event.loop.pool.size");
    public static final ConfigModule INTERNAL_BLOCKING_POOL_SIZE =
            ConfigModule.of(VertxConfig.class, "forage.vertx.internal.blocking.pool.size");
    public static final ConfigModule BLOCKED_THREAD_CHECK_INTERVAL =
            ConfigModule.of(VertxConfig.class, "forage.vertx.blocked.thread.check.interval");
    public static final ConfigModule MAX_EVENT_LOOP_EXECUTE_TIME =
            ConfigModule.of(VertxConfig.class, "forage.vertx.max.event.loop.execute.time");
    public static final ConfigModule MAX_WORKER_EXECUTE_TIME =
            ConfigModule.of(VertxConfig.class, "forage.vertx.max.worker.execute.time");
    public static final ConfigModule HA_ENABLED = ConfigModule.of(VertxConfig.class, "forage.vertx.ha.enabled");
    public static final ConfigModule QUORUM_SIZE = ConfigModule.of(VertxConfig.class, "forage.vertx.quorum.size");
    public static final ConfigModule HA_GROUP = ConfigModule.of(VertxConfig.class, "forage.vertx.ha.group");
    public static final ConfigModule WARNING_EXCEPTION_TIME =
            ConfigModule.of(VertxConfig.class, "forage.vertx.warning.exception.time");
    public static final ConfigModule WARNING_EXCEPTION_TIME_UNIT =
            ConfigModule.of(VertxConfig.class, "forage.vertx.warning.exception.time.unit");
    public static final ConfigModule PREFER_NATIVE_TRANSPORT =
            ConfigModule.of(VertxConfig.class, "forage.vertx.prefer.native.transport");
    public static final ConfigModule MAX_EVENT_LOOP_EXECUTE_TIME_UNIT =
            ConfigModule.of(VertxConfig.class, "forage.vertx.max.event.loop.execute.time.unit");
    public static final ConfigModule MAX_WORKER_EXECUTE_TIME_UNIT =
            ConfigModule.of(VertxConfig.class, "forage.vertx.max.worker.execute.time.unit");
    public static final ConfigModule BLOCKED_THREAD_CHECK_INTERVAL_UNIT =
            ConfigModule.of(VertxConfig.class, "forage.vertx.blocked.thread.check.interval.unit");
    public static final ConfigModule DISABLE_TCCL = ConfigModule.of(VertxConfig.class, "forage.vertx.disable.tccl");
    public static final ConfigModule USE_DAEMON_THREAD =
            ConfigModule.of(VertxConfig.class, "forage.vertx.use.daemon.thread");
    public static final ConfigModule CLUSTERED = ConfigModule.of(VertxConfig.class, "forage.vertx.clustered");

    static {
        initModules(
                VertxConfigEntries.class,
                WORKER_POOL_SIZE,
                EVENT_LOOP_POOL_SIZE,
                INTERNAL_BLOCKING_POOL_SIZE,
                BLOCKED_THREAD_CHECK_INTERVAL,
                MAX_EVENT_LOOP_EXECUTE_TIME,
                MAX_WORKER_EXECUTE_TIME,
                HA_ENABLED,
                QUORUM_SIZE,
                HA_GROUP,
                WARNING_EXCEPTION_TIME,
                WARNING_EXCEPTION_TIME_UNIT,
                PREFER_NATIVE_TRANSPORT,
                MAX_EVENT_LOOP_EXECUTE_TIME_UNIT,
                MAX_WORKER_EXECUTE_TIME_UNIT,
                BLOCKED_THREAD_CHECK_INTERVAL_UNIT,
                DISABLE_TCCL,
                USE_DAEMON_THREAD,
                CLUSTERED);
    }
}
