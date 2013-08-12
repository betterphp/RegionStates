package uk.co.jacekk.bukkit.regionstates;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission SAVE_STATE		= new PluginPermission("regionstates.save", PermissionDefault.OP, "Allows the player to save the state of a region.");
	public static final PluginPermission REMOVE_STATE	= new PluginPermission("regionstates.remove", PermissionDefault.OP, "Allows the player to remove a region state.");
	public static final PluginPermission LOAD_STATE		= new PluginPermission("regionstates.load", PermissionDefault.OP, "Allows the player to load the state of a region.");
	
}
