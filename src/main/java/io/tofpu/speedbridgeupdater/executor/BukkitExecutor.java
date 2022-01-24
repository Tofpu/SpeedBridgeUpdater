package io.tofpu.speedbridgeupdater.executor;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class BukkitExecutor implements Executor {
    public static final @NotNull BukkitExecutor INSTANCE = new BukkitExecutor();

    private final @NotNull Executor executor;

    public BukkitExecutor() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NotNull final Runnable command) {
        executor.execute(command);
    }
}
