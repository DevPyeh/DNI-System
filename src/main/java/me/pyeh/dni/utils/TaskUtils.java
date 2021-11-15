package me.pyeh.dni.utils;

import me.pyeh.dni.DNI;

public class TaskUtils {

    public static void aSyncDelayed(Callable callable, long delay) {
        DNI.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(DNI.getInstance(), callable::call, delay);
    }

    public interface Callable {
        void call();
    }
}
