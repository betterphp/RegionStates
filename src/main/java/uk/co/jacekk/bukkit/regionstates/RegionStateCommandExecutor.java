package uk.co.jacekk.bukkit.regionstates;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.baseplugin.command.SubCommandHandler;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionStateCommandExecutor extends BaseCommandExecutor<RegionStates> {
	
	public RegionStateCommandExecutor(RegionStates plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"regionstate"}, description = "Save and load the state of WorldGuard regions", usage = " <option> <world> <region> [state]")
	@CommandTabCompletion({"save|remove|list|load"})
	public void regionState(CommandSender sender, String label, String[] args){
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <option> <world> <region> [state]");
		
		if (Permission.SAVE_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + " save - Creates a new state");
		}
		
		if (Permission.REMOVE_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + " remove - Removes an existing state");
		}
		
		if (Permission.LOAD_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + " list - List all states");
			sender.sendMessage(ChatColor.RED + " load - Loads a state");
		}
	}
	
	@SubCommandHandler(name = "save", parent = "regionstate")
	public void regionStateSave(CommandSender sender, String label, String[] args){
		if (!Permission.SAVE_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 3){
			sender.sendMessage(ChatColor.RED + "Usage /" + label + " save <world> <region> <name>");
			return;
		}
		
		World world = this.plugin.server.getWorld(args[0]);
		
		if (world == null){
			sender.sendMessage(ChatColor.RED + "No world found with the name '" + args[0] + "'");
			return;
		}
		
		ProtectedRegion region = this.plugin.getWorldGuard().getRegionManager(world).getRegion(args[1]);
		
		if (region == null){
			sender.sendMessage(ChatColor.RED + "No region found with the name '" + args[1] + "'");
			return;
		}
		
		try{
			this.plugin.getStateManager().add(new RegionState(this.plugin, world, region, args[2]));
			sender.sendMessage(ChatColor.GREEN + "State saved");
		}catch (Exception e){
			sender.sendMessage(ChatColor.RED + "Failed to save state: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@SubCommandHandler(name = "remove", parent = "regionstate")
	public void regionStateRemove(CommandSender sender, String label, String[] args){
		if (!Permission.REMOVE_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 3){
			sender.sendMessage(ChatColor.RED + "Usage /" + label + " remove <world> <region> <name>");
			return;
		}
		
		World world = this.plugin.server.getWorld(args[0]);
		
		if (world == null){
			sender.sendMessage(ChatColor.RED + "No world found with the name '" + args[0] + "'");
			return;
		}
		
		ProtectedRegion region = this.plugin.getWorldGuard().getRegionManager(world).getRegion(args[1]);
		
		if (region == null){
			sender.sendMessage(ChatColor.RED + "No region found with the name '" + args[1] + "'");
			return;
		}
		
		RegionState state = this.plugin.getStateManager().getState(world, region, args[2]);
		
		if (state == null){
			sender.sendMessage(ChatColor.RED + "No state found with the name '" + args[2] + "'");
			return;
		}
		
		try{
			this.plugin.getStateManager().remove(state);
			sender.sendMessage(ChatColor.GREEN + "State removed");
		}catch (Exception e){
			sender.sendMessage(ChatColor.RED + "Failed to remove state: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@SubCommandHandler(name = "list", parent = "regionstate")
	public void regionStateList(CommandSender sender, String label, String[] args){
		if (!Permission.LOAD_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 2){
			sender.sendMessage(ChatColor.RED + "Usage /" + label + " list <world> <region>");
			return;
		}
		
		World world = this.plugin.server.getWorld(args[0]);
		
		if (world == null){
			sender.sendMessage(ChatColor.RED + "No world found with the name '" + args[0] + "'");
			return;
		}
		
		ProtectedRegion region = this.plugin.getWorldGuard().getRegionManager(world).getRegion(args[1]);
		
		if (region == null){
			sender.sendMessage(ChatColor.RED + "No region found with the name '" + args[1] + "'");
			return;
		}
		
		sender.sendMessage(ChatColor.GREEN + "States:");
		
		for (RegionState state : this.plugin.getStateManager().getStates(world, region)){
			sender.sendMessage(ChatColor.GREEN + "  - " + state.getName());
		}
	}
	
	@SubCommandHandler(name = "load", parent = "regionstate")
	public void regionStateLoad(CommandSender sender, String label, String[] args){
		if (!Permission.LOAD_STATE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 3){
			sender.sendMessage(ChatColor.RED + "Usage /" + label + " load <world> <region> <name>");
			return;
		}
		
		World world = this.plugin.server.getWorld(args[0]);
		
		if (world == null){
			sender.sendMessage(ChatColor.RED + "No world found with the name '" + args[0] + "'");
			return;
		}
		
		ProtectedRegion region = this.plugin.getWorldGuard().getRegionManager(world).getRegion(args[1]);
		
		if (region == null){
			sender.sendMessage(ChatColor.RED + "No region found with the name '" + args[1] + "'");
			return;
		}
		
		RegionState state = this.plugin.getStateManager().getState(world, region, args[2]);
		
		if (state == null){
			sender.sendMessage(ChatColor.RED + "No state found with the name '" + args[2] + "'");
			return;
		}
		
		try{
			state.apply();
			sender.sendMessage(ChatColor.GREEN + "State applied");
		}catch (Exception e){
			sender.sendMessage(ChatColor.RED + "Failed to apply state: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
