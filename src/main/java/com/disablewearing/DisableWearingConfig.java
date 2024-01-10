package com.disablewearing;

import java.awt.event.KeyEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("disablewearing")
public interface DisableWearingConfig extends Config
{
    @ConfigItem(
        keyName = "isEnabled",
        name = "Is Enabled",
        description = "Toggle to enable/disable wearing items:"
    )
    default boolean isEnabled()
    {
        return false;
    }
    
    @ConfigItem(
        keyName = "items",
        name = "Items",
        description = "Comma-separated list of items to disable wearing:"
    )
    default String items()
    {
        return "Topaz amulet,Diamond amulet";
    }

    @ConfigItem(
        keyName = "hotkey",
        name = "Hotkey",
        description = "Hotkey to toggle disabling of wearing items:"
    )
    default Keybind hotkey()
    {
        return new Keybind(KeyEvent.VK_UNDEFINED, 0);
    }
}
