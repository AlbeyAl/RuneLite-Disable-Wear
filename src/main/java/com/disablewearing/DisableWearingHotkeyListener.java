package com.disablewearing;

import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.HotkeyListener;

public class DisableWearingHotkeyListener extends HotkeyListener {
    
    private final DisableWearingPlugin plugin;
    private final DisableWearingConfig config;

    private Instant lastPressed;
    
    @Inject
    private ClientThread clientThread;

    @Inject
	private DisableWearingHotkeyListener(DisableWearingPlugin plugin, DisableWearingConfig config)
	{
		super(config::hotkey);

		this.plugin = plugin;
		this.config = config;
	}

    @Override
    public void hotkeyPressed() {
        if (lastPressed != null && Duration.between(lastPressed, Instant.now()).toMillis() < 500) {
            return;
        }

        lastPressed = Instant.now();

        clientThread.invoke(() -> {
            plugin.toggle();
        });
    }
}
