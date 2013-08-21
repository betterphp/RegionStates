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
		
		this.file = new File(plugin.getDataFolder(), world.getUID().toString() + File.separator + region.getId() + File.separator + name + ".schematic");
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
	
	public File getSchematicFile(){
		return this.file;
	}
	
	public void apply() throws DataException, IOException, MaxChangedBlocksException {
		EditSession session = new EditSession(new BukkitWorld(this.world), Integer.MAX_VALUE);
		CuboidClipboard schematic = SchematicFormat.getFormat(this.file).load(this.file);
		
		schematic.paste(session, this.region.getMinimumPoint(), false, true);
	}
	
}
