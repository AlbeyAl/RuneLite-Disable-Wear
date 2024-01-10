package com.disablewearing;

import com.disablewearing.DisableWearingPlugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DisableWearingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DisableWearingPlugin.class);
		RuneLite.main(args);
	}
}