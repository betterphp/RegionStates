package uk.co.jacekk.bukkit.regionstates;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionState {
	
	private RegionStates plugin;
	private World world;
	private ProtectedRegion region;
	private String name;
	private File file;
	
	public RegionState(RegionStates plugin, World world, ProtectedRegion region, String name){
		this.plugin = plugin;
		this.world = world;
		this.region = region;
		this.name = name;
		
		this.file = new File(plugin.getDataFolder(), world.getName() + "_" + region.getId() + "_" + name + ".schematic");
	}
	
	public RegionState(RegionStates plugin, File file){
		this.plugin = plugin;
		this.file = file;
		
		String fileName = this.file.getName();
		String[] parts = fileName.substring(0, fileName.length() - 10).split("_");
		
		this.world = plugin.server.getWorld(parts[0]);
		this.region = this.plugin.worldGuard.getRegionManager(this.world).getRegion(parts[1]);
		this.name = parts[2];
	}
	
	@Override
	public int hashCode(){
		int code = 7;
		
		code = code + 37 * this.world.hashCode();
		code = code + 37 * this.region.hashCode();
		code = code + 37 * this.name.hashCode();
		
		return code;
	}
	
	@Override
	public boolean equals(Object object){
		if (object == this){
			return true;
		}
		
		if (!(object instanceof RegionState)){
			return false;
		}
		
		RegionState state = (RegionState) object;
		
		return (state.world.equals(this.world) && state.region.equals(this.region) && state.name.equals(this.name));
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public ProtectedRegion getRegion(){
		return this.region;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void save() throws IOException, DataException {
		EditSession session = new EditSession(new BukkitWorld(this.world), Integer.MAX_VALUE);
		CuboidClipboard clipboard = new CuboidClipboard(this.region.getMaximumPoint().subtract(this.region.getMinimumPoint()).add(1, 1, 1), this.region.getMinimumPoint());
		clipboard.copy(session);
		
		SchematicFormat.MCEDIT.save(clipboard, this.file);
		
		this.plugin.states.remove(this);
		this.plugin.states.add(this);
	}
	
	public void remove(){
		this.plugin.states.remove(this);
		this.file.delete();
	}
	
	public void apply() throws DataException, IOException, MaxChangedBlocksException {
		EditSession session = new EditSession(new BukkitWorld(this.world), Integer.MAX_VALUE);
		CuboidClipboard schematic = SchematicFormat.getFormat(this.file).load(this.file);
		
		schematic.paste(session, this.region.getMinimumPoint(), false, true);
	}
	
}
