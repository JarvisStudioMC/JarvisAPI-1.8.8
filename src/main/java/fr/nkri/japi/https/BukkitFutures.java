package fr.nkri.japi.https;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BukkitFutures {

    /**
     * Execute a CompletableFuture callback on the Bukkit main thread.
     *
     * Useful to safely interact with the Bukkit API after an async task
     * (like an HTTP request) without manually using the scheduler each time.
     *
     * @param plugin   the plugin instance used to schedule the sync task
     * @param future   the asynchronous future to listen to
     * @param consumer the code to execute on the main thread when the future completes
     * @param <T>      the type of the future result
     */
    public static <T> void thenAcceptSync(final Plugin plugin, final CompletableFuture<T> future, final Consumer<T> consumer) {
        future.thenAccept(result -> {
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }
}
