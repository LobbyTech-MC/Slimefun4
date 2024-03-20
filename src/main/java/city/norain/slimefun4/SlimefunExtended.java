package city.norain.slimefun4;

import city.norain.slimefun4.listener.SlimefunMigrateListener;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import java.util.logging.Level;
import javax.annotation.Nonnull;

public final class SlimefunExtended {
    private static SlimefunMigrateListener migrateListener = new SlimefunMigrateListener();

    public static boolean checkEnvironment(@Nonnull Slimefun sf) {
        if (EnvironmentChecker.checkHybridServer()) {
            sf.getLogger().log(Level.WARNING, "#######################################################");
            sf.getLogger().log(Level.WARNING, "");
            sf.getLogger().log(Level.WARNING, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            sf.getLogger().log(Level.WARNING, "检测到正在使用混合端, Slimefun 将会被禁用!");
            sf.getLogger().log(Level.WARNING, "混合端已被多个用户报告有使用问题,");
            sf.getLogger().log(Level.WARNING, "强制绕过检测将不受任何反馈支持.");
            sf.getLogger().log(Level.WARNING, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            sf.getLogger().log(Level.WARNING, "");
            sf.getLogger().log(Level.WARNING, "#######################################################");
            return false;
        }

        return !EnvironmentChecker.checkIncompatiblePlugins(sf.getLogger());
    }

    public static void register(@Nonnull Slimefun sf) {
        EnvironmentChecker.scheduleSlimeGlueCheck(sf);

        VaultIntegration.register(sf);

        migrateListener.register(sf);
    }

    public static void shutdown() {
        migrateListener = null;

        VaultIntegration.cleanup();
    }
}
