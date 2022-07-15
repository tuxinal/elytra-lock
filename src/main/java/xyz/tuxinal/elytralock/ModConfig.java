package xyz.tuxinal.elytralock;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "elytra-lock")
public class ModConfig implements ConfigData {
    public boolean enabled = true;
    public boolean unlockUponRiptideThrow = true;

    public boolean showIndicator = true;
    public WhenToShow whenToShow = WhenToShow.Always;
}

enum WhenToShow {
    Locked,
    Unlocked,
    Always
}
