package chase.minecraft.architectury.simplemodconfig.handlers;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import com.google.common.base.Stopwatch;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LoadedConfigs
{
	private static LoadedConfigs instance;
	private LinkedHashMap<String, ConfigHandler<?>> loaded_mods;
	
	protected LoadedConfigs()
	{
		loaded_mods = new LinkedHashMap<>();
	}
	
	public static LoadedConfigs getInstance()
	{
		if (instance == null)
			instance = new LoadedConfigs();
		return instance;
	}
	
	public void add(String name, ConfigHandler<?> handler)
	{
		loaded_mods.put(name, handler);
		sort();
	}
	
	public void remove(String name)
	{
		if (exists(name))
			loaded_mods.remove(name);
	}
	
	public boolean exists(String name)
	{
		return loaded_mods.containsKey(name);
	}
	
	public Set<Map.Entry<String, ConfigHandler<?>>> get()
	{
		return loaded_mods.entrySet();
	}
	
	public ConfigHandler<?> get(String name)
	{
		return loaded_mods.get(name);
	}
	
	public int size()
	{
		return loaded_mods.size();
	}
	
	private void sort()
	{
		SimpleModConfig.log.warn("Sorting loaded mods list, this might take a moment!");
		Stopwatch stopwatch = Stopwatch.createStarted();
		
		LinkedHashMap<String, ConfigHandler<?>> sortedMap = new LinkedHashMap<>();
		loaded_mods.entrySet().parallelStream()
				.sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getKey(), b.getKey()))
				.forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
		loaded_mods = sortedMap;
		
		SimpleModConfig.log.info("Done sorting loaded mods list: process took {}", stopwatch.stop().elapsed());
	}
}
