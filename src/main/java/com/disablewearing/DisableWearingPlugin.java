package com.disablewearing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.config.Keybind;


@Slf4j
@PluginDescriptor(
    name = "Disable Wearing"
)
public class DisableWearingPlugin extends Plugin
{
    @Inject
    private DisableWearingConfig config;

    @Inject
    private DisableWearingHotkeyListener hotkeyListener;

    @Inject
    private KeyManager keyManager;

    @Inject
    private Client client;

    private String items;
    private String[] itemList;
    private List<String> itemsList;
    private boolean isEnabled = false;

    @Provides
	DisableWearingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DisableWearingConfig.class);
	}

    @Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("disablewearing"))
		{
			reset();
		}
	}

    @Override
	protected void startUp() throws Exception
	{
		log.info("Disable Wearing started!");
        keyManager.registerKeyListener(hotkeyListener);
        reset();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Disable Wearing stopped!");
        keyManager.unregisterKeyListener(hotkeyListener);
	}

    @Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			log.info("Disable Wearing logged in!");
		}
	}

    // Favoring this method over MenuEntryAdded due to risk of breaking Jagex's ToS.
    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        if (isEnabled)
        {
            // Get the clicked item's name, remove color tags, and convert to lower case
            String clickedItem = event.getMenuTarget().replaceAll("<[^>]*>", "").trim().toLowerCase();

            // Check if the option is "Wear" and the item is in the list of items to block
            if (event.getMenuOption().equalsIgnoreCase("Wear") && itemsList.contains(clickedItem))
            {
                // Cancel the event to prevent the item from being worn
                event.consume();
                
                // Optionally, send a message to the chat to inform the user
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Blocked wearing of " + clickedItem, null);
            }
        }
    }

    // @Subscribe
    // public void onMenuEntryAdded(MenuEntryAdded event)
    // {
    //     if (isEnabled)
    //     {
    //         String targetItem = event.getTarget().replaceAll("<[^>]*>", "").trim().toLowerCase();

    //         // Check if the option is "Wear"
    //         if (event.getOption().equalsIgnoreCase("Wear") && itemsList.contains(targetItem))
    //         {
    //             // Get the current menu entries
    //             MenuEntry[] menuEntries = client.getMenuEntries();

    //             // Create a new list to hold the modified menu entries
    //             List<MenuEntry> newEntries = new ArrayList<>();

    //             // Loop through the menu entries
    //             for (MenuEntry entry : menuEntries)
    //             {
    //                 // If the entry option is "Wear" and the target is the item, skip it
    //                 if (entry.getOption().equalsIgnoreCase("Wear"))
    //                 {
    //                     continue;
    //                 }

    //                 // Otherwise, add it to the new entries
    //                 newEntries.add(entry);
    //             }

    //             // Convert the list back to an array and set the menu entries
    //             client.setMenuEntries(newEntries.toArray(new MenuEntry[0]));
    //         }
    //     }
    // }

    private void reset()
    {        
        items = config.items();
        itemList = items.split(",");

        for (int i = 0; i < itemList.length; i++)
        {
            itemList[i] = itemList[i].trim().toLowerCase();
        }
        
        itemsList = Arrays.asList(itemList);
    }

    public void toggle()
    {
        isEnabled = !isEnabled;
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Disable Wearing: " + (isEnabled ? "Enabled" : "Disabled"), null);
    }
}