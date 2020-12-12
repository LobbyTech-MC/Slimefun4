package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

public enum MachineTier {

    BASIC("&e基础"),
    AVERAGE("&6普通"),
    MEDIUM("&a中级"),
    GOOD("&2高级"),
    ADVANCED("&6超级"),
    END_GAME("&4终极");

    private final String prefix;

    MachineTier(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return prefix;
    }

}
