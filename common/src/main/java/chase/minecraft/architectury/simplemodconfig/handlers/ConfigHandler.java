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
	public ConfigHandler(String name, T initialConfig)
	{
		this.name = name;
		CONFIG_FILE = Path.of(Platform.getConfigFolder().toString(), "%s.json".formatted(name)).toFile();
		this.initialConfig = initialConfig;
		this.config = initialConfig;
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
	
	/**
	 * This Java function returns an object from the initial configuration based on a given name, and it may return null.
	 *
	 * @param name A string representing the name of the object to retrieve from the initial configuration.
	 * @return The method `getInitial` returns an `Object` that may be nullable. The specific object being returned depends on the implementation of the `get` method and the value of the `initialConfig` parameter passed to it.
	 */
	public @Nullable Object getInitial(String name)
	{
		return get(name, initialConfig);
	}
	
	/**
	 * This Java function attempts to retrieve a field value from a given object based on its name and returns null if it fails.
	 *
	 * @param name   The name of the field that we want to retrieve from the config object.
	 * @param config The `config` parameter is an object of a generic type `T`, which represents a configuration object. The method is designed to retrieve a field value from this configuration object.
	 * @return The method is returning a nullable object. If the field with the given name exists and has the required annotation, then the value of that field from the given config object is returned. Otherwise, null is returned.
	 */
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
	
	/**
	 * This function checks if a given field name has any associated configuration options.
	 *
	 * @param fieldName fieldName is a String parameter that represents the name of a field in a class. The method checks if this field has any annotations associated with it by calling the getConfigOptions() method. If the getConfigOptions() method returns a non-null value, it means that the field has annotations and the method returns true
	 * @return The method `hasAnnotation` is returning a boolean value. It returns `true` if the `getConfigOptions` method returns a non-null value for the given `fieldName`, indicating that the field has an annotation. Otherwise, it returns `false`.
	 */
	private boolean hasAnnotation(String fieldName)
	{
		return getConfigOptions(fieldName) != null;
	}
	
	/**
	 * This function retrieves the SimpleConfig annotation for a given field name in a configuration file.
	 *
	 * @param fieldName fieldName is a String parameter that represents the name of the field for which we want to retrieve the SimpleConfig annotation.
	 * @return The method is returning an object of type `SimpleConfig` which is annotated on the field with the given `fieldName`. If the field is not found, it returns `null`.
	 */
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
	 * This function retrieves all fields with a specific annotation from a given object and returns them in a HashMap.
	 *
	 * @return A HashMap containing the names and values of all fields in the "config" object that have an annotation.
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
	
	/**
	 * This function returns a LinkedHashMap with all entries sorted based on their index value in the configuration options.
	 *
	 * @return The method is returning a LinkedHashMap<String, Object> object.
	 */
	public LinkedHashMap<String, Object> getAllSorted()
	{
		LinkedHashMap<String, Object> sortedMap = new LinkedHashMap<>();
		getAll().entrySet().stream()
				.sorted((a, b) -> Integer.compare(getConfigOptions(a.getKey()).index(), getConfigOptions(b.getKey()).index()))
				.forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
		return sortedMap;
	}
	
	/**
	 * This function returns a tooltip with information about a configuration option.
	 *
	 * @param name The name of the configuration option for which the tooltip is being generated.
	 * @return A Component object is being returned.
	 */
	public Component getTooltip(String name)
	{
		Object value = Objects.requireNonNull(get(name));
		SimpleConfig options = getConfigOptions(name);
		assert options != null;
		String displayName = options.displayName();
		if (displayName.isEmpty())
		{
			displayName = name;
		}
		MutableComponent tooltip = Component.literal("%s%s%s".formatted(ChatFormatting.AQUA, displayName, ChatFormatting.RESET));
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
	
	/**
	 * The function resets a field in an object to its initial value based on the field name.
	 *
	 * @param name The name of the field that needs to be reset.
	 */
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
	
	/**
	 * Returns the json representation of the configuration file
	 *
	 * @return json
	 */
	@Override
	public String toString()
	{
		return new Gson().toJson(config);
	}
}
