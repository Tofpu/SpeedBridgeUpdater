package io.tofpu.speedbridgeupdater.executor;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public final class BukkitExecutor implements Executor {
    public static final @NotNull BukkitExecutor INSTANCE = new BukkitExecutor();

    private final @NotNull ExecutorService executor;

    public BukkitExecutor() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(final @NotNull Runnable command) {
        executor.execute(command);
    }

    public CompletableFuture<?> submit(final @NotNull Runnable runnable) {
        return CompletableFuture.runAsync(runnable, this);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
