package chase.minecraft.architectury.simplemodconfig.handlers;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import chase.minecraft.architectury.simplemodconfig.annotation.SimpleConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * The ConfigHandler class is a generic class that handles loading, saving, setting, and getting configuration data from a JSON file using the Gson library.
 */
public class ConfigHandler<T>
{
	public final File CONFIG_FILE;
	private final String name;
	
	private Component displayName;
	private T config;
	private final T initialConfig;
	
	/**
	 * Creates a new ConfigHandler object
	 *
	 * @param name          The name of the json file not including the .json
	 * @param initialConfig The config's initial values.
	 */
	public ConfigHandler(String name, T initialConfig, Component displayName)
	{
		this.name = name;
		this.displayName = displayName;
		CONFIG_FILE = Path.of(Platform.getConfigFolder().toString(), "%s.json".formatted(name)).toFile();
		this.initialConfig = this.config = initialConfig;
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
	private void save(T config)
	{
		try (FileWriter writer = new FileWriter(CONFIG_FILE))
		{
			this.config = config;
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			String json = gson.toJson(getAll());
			writer.write(json);
			writer.flush();
		} catch (IOException e)
		{
			SimpleModConfig.log.error("Unable to save config file: {}", CONFIG_FILE.getName(), e);
		}
	}
	
	public void save()
	{
		save(config);
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
		return get(name, config);
	}
	
	public @Nullable Object getInitial(String name)
	{
		return get(name, initialConfig);
	}
	
	private @Nullable Object get(String name, T config)
	{
		try
		{
			Field field = config.getClass().getDeclaredField(name);
			if (hasAnnotation(name))
			{
				return field.get(config);
			}
		} catch (NoSuchFieldException | IllegalAccessException e)
		{
			SimpleModConfig.log.error("Unable to get value of field: {}, from {}", name, CONFIG_FILE.getName(), e);
		}
		return null;
	}
	
	private boolean hasAnnotation(String fieldName)
	{
		return getConfigOptions(fieldName) != null;
	}
	
	public @Nullable SimpleConfig getConfigOptions(String fieldName)
	{
		try
		{
			Field field = config.getClass().getField(fieldName);
			return field.getAnnotation(SimpleConfig.class);
		} catch (NoSuchFieldException e)
		{
			SimpleModConfig.log.error("Unable to get value of field: {}, from {}", fieldName, CONFIG_FILE.getName(), e);
		}
		return null;
	}
	
	
	/**
	 * Checks if the config exists or not
	 *
	 * @param name The config name
	 * @return true if exists false otherwise
	 */
	public boolean exists(String name)
	{
		return get(name) != null;
	}
	
	/**
	 * Returns a HashMap containing all the fields and their corresponding types of the class
	 * specified in the configuration object.
	 *
	 * @return HashMap<String, Type> containing all the fields and their corresponding types
	 */
	public HashMap<String, Object> getAll()
	{
		HashMap<String, Object> map = new HashMap<>();
		Field[] fields = config.getClass().getFields();
		for (Field field : fields)
		{
			try
			{
				if (hasAnnotation(field.getName()))
				{
					map.put(field.getName(), field.get(config));
				}
			} catch (IllegalAccessException ignore)
			{
			}
		}
		return map;
	}
	
	public LinkedHashMap<String, Object> getAllSorted()
	{
		LinkedHashMap<String, Object> sortedMap = new LinkedHashMap<>();
		getAll().entrySet().stream()
				.sorted((a, b) -> Integer.compare(getConfigOptions(a.getKey()).index(), getConfigOptions(b.getKey()).index()))
				.forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
		return sortedMap;
	}
	
	public Component getTooltip(String name)
	{
		Object value = Objects.requireNonNull(get(name));
		SimpleConfig options = getConfigOptions(name);
		assert options != null;
		MutableComponent tooltip = Component.literal("%s%s%s".formatted(ChatFormatting.AQUA, options.displayName(), ChatFormatting.RESET));
		if (!options.description().isEmpty())
		{
			tooltip.append("\n%s%s%s".formatted(ChatFormatting.GOLD, options.description(), ChatFormatting.RESET));
		}
		tooltip.append("\n%sdefault: %s%s%s".formatted(ChatFormatting.GOLD, ChatFormatting.GREEN, get(name, initialConfig), ChatFormatting.RESET));
		if (options.options().length > 0)
		{
			tooltip.append("\n%stype: %sOptions".formatted(ChatFormatting.GOLD, ChatFormatting.GREEN));
			for (String option : options.options())
			{
				tooltip.append("\n%s - %s".formatted(ChatFormatting.BLUE, option));
			}
		} else
		{
			tooltip.append("\n%stype: %s%s".formatted(ChatFormatting.GOLD, ChatFormatting.GREEN, value.getClass().getSimpleName()));
		}
		if (options.min() != Double.MIN_VALUE)
		{
			tooltip.append("\n%smin: %s%s".formatted(ChatFormatting.GOLD, ChatFormatting.GREEN, options.min()));
		}
		if (options.max() != Double.MAX_VALUE)
		{
			tooltip.append("\n%smax: %s%s".formatted(ChatFormatting.GOLD, ChatFormatting.GREEN, options.max()));
		}
		return tooltip;
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
	
	public void reset(String name)
	{
		
		Field field;
		try
		{
			field = initialConfig.getClass().getDeclaredField(name);
			@Nullable Object value = field.get(initialConfig);
			if (value != null)
			{
				set(name, value);
			}
		} catch (NoSuchFieldException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the configs name
	 *
	 * @return the config name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the display name
	 *
	 * @return the display name component
	 */
	public Component getDisplayName()
	{
		return displayName;
	}
	
	/**
	 * Sets the display name
	 *
	 * @param displayName the display name component
	 */
	public void setDisplayName(Component displayName)
	{
		this.displayName = displayName;
	}
	
	@Override
	public String toString()
	{
		return new Gson().toJson(config);
	}
}
