package chase.minecraft.architectury.simplemodconfig.handlers;

import chase.minecraft.architectury.simplemodconfig.annotation.SimpleConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.LongArgumentType.longArg;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;


/**
 * The CommandHandler class registers and handles commands with sub-commands and configuration options
 */
public class CommandHandler
{
	private final ConfigHandler<?> configHandler;
	private final Component displayName;
	private final String cmdName;
	
	public CommandHandler(String cmdName, Component displayName, ConfigHandler<?> configHandler)
	{
		this.cmdName = cmdName;
		
		this.displayName = displayName;
		this.configHandler = configHandler;
	}
	
	/**
	 * This function registers a command with a given name and permission level, which can execute sub-commands and reload a configuration file.
	 *
	 * @param dispatcher      The CommandDispatcher object that is used to register the command with the server.
	 * @param permissionLevel The permission level required to execute the command. In this case, it is set to 4, which is typically reserved for administrators or operators.
	 */
	public void register(CommandDispatcher<CommandSourceStack> dispatcher, int permissionLevel)
	{
		LiteralArgumentBuilder<CommandSourceStack> cmd = literal(cmdName)
				.requires((commandSourceStack) -> commandSourceStack.hasPermission(4))
				.executes(context ->
				{
					context.getSource().sendSystemMessage(getAll());
					return 1;
				})
				.then(literal("reload")
						.executes(context ->
						{
							configHandler.load();
							MutableComponent message = Component.literal("[");
							message.append(displayName.copy());
							message.append("] reloaded config");
							context.getSource().sendSystemMessage(message);
							return 1;
						}));
		
		for (Map.Entry<String, Object> entry : configHandler.getAll().entrySet())
		{
			cmd.then(getSubCommand(entry.getKey(), entry.getValue()));
		}
		
		
		dispatcher.register(cmd);
	}
	
	/**
	 * This function builds a subcommand for a command with different argument types based on the type of the input value.
	 *
	 * @param name  The name of the subcommand being created.
	 * @param value The value to be set for the configuration option. It can be a String, Boolean, Integer, Float, Double, or Long depending on the type of the configuration option.
	 * @param sub   A LiteralArgumentBuilder object representing a subcommand that will be added to the main command.
	 * @return The method is returning a LiteralArgumentBuilder object with sub-commands added based on the type of the value parameter.
	 */
	private LiteralArgumentBuilder<CommandSourceStack> getSubCommand(String name, Object value, LiteralArgumentBuilder<CommandSourceStack> sub)
	{
		SimpleConfig options = configHandler.getConfigOptions(name);
		assert options != null;
		if (value instanceof String)
		{
			if (options.options().length > 0)
			{
				sub = sub
						.then(argument("value", greedyString())
								.suggests(((context, builder) -> SharedSuggestionProvider.suggest(options.options(), builder)))
								.executes(ctx ->
								{
									configHandler.set(name, getString(ctx, "value"));
									return 1;
								}));
			} else
			{
				sub = sub
						.then(argument("value", greedyString())
								.executes(ctx ->
								{
									configHandler.set(name, getString(ctx, "value"));
									return 1;
								}));
			}
		} else if (value instanceof Boolean)
		{
			sub = sub.then(argument("value", bool()).executes(ctx ->
			{
				configHandler.set(name, getBool(ctx, "value"));
				return 1;
			}));
		} else if (value instanceof Integer)
		{
			sub = sub.then(argument("value", integer((int) options.min(), (int) options.max())).executes(ctx ->
			{
				configHandler.set(name, getInteger(ctx, "value"));
				return 1;
			}));
		} else if (value instanceof Float)
		{
			sub = sub.then(argument("value", floatArg((float) options.min(), (float) options.max())).executes(ctx ->
			{
				configHandler.set(name, getFloat(ctx, "value"));
				return 1;
			}));
		} else if (value instanceof Double)
		{
			sub = sub.then(argument("value", doubleArg(options.min(), options.max())).executes(ctx ->
			{
				configHandler.set(name, getDouble(ctx, "value"));
				return 1;
			}));
		} else if (value instanceof Long)
		{
			sub = sub.then(argument("value", longArg((long) options.min(), (long) options.max())).executes(ctx ->
			{
				configHandler.set(name, getLong(ctx, "value"));
				return 1;
			}));
		}
		return sub;
	}
	
	private LiteralArgumentBuilder<CommandSourceStack> getSubCommand(String name, Object value)
	{
		return getSubCommand(name, value, literal(name));
	}
	
	/**
	 * This Java function returns a component containing a formatted display name and a list of configuration entries with associated hover and click events.
	 *
	 * @return The method is returning a Component object.
	 */
	public Component getAll()
	{
		MutableComponent component = displayName.copy();
		component.append(" %sConfiguration:%s".formatted(ChatFormatting.AQUA, ChatFormatting.RESET));
		for (Map.Entry<String, Object> entry : configHandler.getAllSorted().entrySet())
		{
			String name = entry.getKey();
			component.append("\n");
			HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, configHandler.getTooltip(entry.getKey()));
			ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/%s %s ".formatted(cmdName, name));
			MutableComponent line = get(entry.getKey()).copy();
			line.withStyle(style -> style.withHoverEvent(hoverEvent).withClickEvent(clickEvent));
			component.append(line);
		}
		return component;
	}
	
	/**
	 * This function returns a component with a formatted string based on the value retrieved from a config handler.
	 *
	 * @param name The name of the component to retrieve from the configHandler.
	 * @return The method is returning a Component object.
	 */
	public Component get(String name)
	{
		MutableComponent component = Component.literal("%s%s%s: ".formatted(ChatFormatting.GOLD, name, ChatFormatting.RESET));
		@Nullable Object value = configHandler.get(name);
		if (value != null)
		{
			if (value instanceof Boolean bool)
			{
				if (bool)
				{
					component.append(ChatFormatting.GREEN + "TRUE" + ChatFormatting.RESET);
				} else
				{
					component.append(ChatFormatting.RED + "FALSE" + ChatFormatting.RESET);
				}
			} else
			{
				component.append(ChatFormatting.GREEN + value.toString() + ChatFormatting.RESET);
			}
		} else
		{
			component.append(ChatFormatting.RED + "NULL" + ChatFormatting.RESET);
		}
		return component;
		
	}
}
