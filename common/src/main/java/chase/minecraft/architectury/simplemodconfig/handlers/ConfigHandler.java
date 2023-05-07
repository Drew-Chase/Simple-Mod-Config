package chase.minecraft.architectury.simplemodconfig.handlers;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * The ConfigHandler class is a generic class that handles loading, saving, setting, and getting configuration data from a JSON file using the Gson library.
 */
public class ConfigHandler<T>
{
	public final File CONFIG_FILE;
	private T config;
	private final T initialConfig;
	private final HashMap<String, Type> fields;
	
	/**
	 * Creates a new ConfigHandler object
	 *
	 * @param name          The name of the json file not including the .json
	 * @param initialConfig The config's initial values.
	 */
	public ConfigHandler(String name, T initialConfig)
	{
		CONFIG_FILE = Path.of(Platform.getConfigFolder().toString(), "%s.json".formatted(name)).toFile();
		this.initialConfig = this.config = initialConfig;
		fields = getAll();
		load();
	}
	
	/**
	 * Loads a configuration file using Gson library and saves an initial configuration if the file does not exist or cannot be parsed.
	 */
	public void load()
	{
		try
		{
			if (!CONFIG_FILE.exists())
				save(initialConfig);
			try (FileReader reader = new FileReader(CONFIG_FILE))
			{
				Gson gson = new Gson();
				config = (T) gson.fromJson(reader, config.getClass());
			}
		} catch (ClassCastException e)
		{
			SimpleModConfig.log.error("Unable to parse config file: {}", CONFIG_FILE.getName(), e);
			save(initialConfig);
		} catch (IOException ignore)
		{
			save(initialConfig);
		}
	}
	
	/**
	 * Saves a configuration object as a JSON file using the Gson library.
	 *
	 * @param config The parameter "config" is an object of type T, which is a generic type. The specific type of object being passed as "config" will depend on the context in which this method is being used. The method is designed to save the configuration data of an object to a file using the Gson
	 */
	public void save(T config)
	{
		try (FileWriter writer = new FileWriter(CONFIG_FILE))
		{
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			String json = gson.toJson(config);
			writer.write(json);
			writer.flush();
		} catch (IOException e)
		{
			SimpleModConfig.log.error("Unable to save config file: {}", CONFIG_FILE.getName(), e);
		}
	}
	
	/**
	 * This function sets a value for a given field in a configuration object and saves the configuration.
	 *
	 * @param name  The name of the field in the configuration object that needs to be set to the given value.
	 * @param value The value that you want to set the specified field to.
	 */
	public void set(String name, Object value)
	{
		try
		{
			Field field = config.getClass().getDeclaredField(name);
			field.set(config, value);
			save(config);
		} catch (NoSuchFieldException | IllegalAccessException e)
		{
			SimpleModConfig.log.error("Unable to set config field: '{}' to '{}'", name, value, e);
		}
	}
	
	/**
	 * This function retrieves the value of a field with a given name from a configuration object.
	 *
	 * @param name The name of the field that is being retrieved from the config object.
	 * @return The method is returning an object of the type @Nullable, which means it can either return an object of the specified type or null if an exception occurs while trying to get the value of the field.
	 */
	public @Nullable Object get(String name)
	{
		try
		{
			Field field = config.getClass().getDeclaredField(name);
			return field.get(config);
		} catch (NoSuchFieldException | IllegalAccessException e)
		{
			SimpleModConfig.log.error("Unable to get value of field: {}, from {}", name, CONFIG_FILE.getName(), e);
			return null;
		}
	}
	
	public boolean exists(String name)
	{
		return get(name) != null;
	}
	
	public HashMap<String, Type> getAll()
	{
		if (fields != null)
			return fields;
		HashMap<String, Type> map = new HashMap<>();
		Field[] fields = config.getClass().getFields();
		for (Field field : fields)
		{
			map.put(field.getName(), field.getType());
		}
		return map;
	}
	
	/**
	 * returns configuration object.
	 *
	 * @return The method is returning an object of type T, which is the type of the variable "config". The specific type of object being returned depends on how the "config" variable was defined and initialized.
	 */
	public T getConfig()
	{
		return config;
	}
	
	/**
	 * Gets the initial config values
	 *
	 * @return the initial config object
	 */
	public T getInitialConfig()
	{
		return initialConfig;
	}
	
	/**
	 * resets the config to the initial config values and saves it.
	 */
	public void reset()
	{
		save(initialConfig);
	}
	
}
