package city.norain.slimefun4.utils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class SlimefunRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        var name = executor instanceof SlimefunPoolExecutor
                ? ((SlimefunPoolExecutor) executor).getName()
                : executor.getClass().getSimpleName();

        Slimefun.logger().log(Level.WARNING, "A task was rejected from " + name + ", use fallback thread.");
        Slimefun.getThreadService().newThread(Slimefun.instance(), "Fallback-Service", r);
    }
}
