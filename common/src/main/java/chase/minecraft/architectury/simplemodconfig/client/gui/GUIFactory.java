package chase.minecraft.architectury.simplemodconfig.client.gui;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Function;

public class GUIFactory
{
	/**
	 * This function creates a button with specified dimensions, label, and press action.
	 *
	 * @param x           The x-coordinate of the button's top-left corner.
	 * @param y           The y parameter represents the vertical position of the button on the screen or component. It is usually measured in pixels from the top of the screen or component.
	 * @param width       The width parameter is an integer value that represents the desired width of the button in pixels.
	 * @param height      The height parameter is an integer value that represents the height of the button in pixels.
	 * @param label       The label parameter is a Component object that represents the text or image that will be displayed on the button. It can be a simple text string or a more complex graphical element.
	 * @param pressAction `pressAction` is a functional interface that defines the action to be performed when the button is pressed. It typically takes no arguments and returns no value. The implementation of this interface is passed as a lambda expression or method reference when creating the button.
	 * @return A Button object is being returned.
	 */
	public static Button createButton(int x, int y, int width, int height, Component label, Button.OnPress pressAction)
	{
		return Button.builder(label, pressAction).bounds(x, y, width, height).build();
	}
	
	/**
	 * This function creates an EditBox object with specified font, position, size, and label.
	 *
	 * @param font   The font to be used for the text in the EditBox.
	 * @param x      The x-coordinate of the top-left corner of the text box.
	 * @param y      The y-coordinate of the top-left corner of the text box on the screen.
	 * @param width  The width parameter specifies the width of the EditBox component in pixels.
	 * @param height The height parameter specifies the height of the EditBox component in pixels.
	 * @param label  The label parameter is a Component object that represents the label or caption for the EditBox. It could be a text label or an icon that provides context or instructions for the user.
	 * @return An instance of the EditBox class is being returned.
	 */
	public static EditBox createTextBox(Font font, int x, int y, int width, int height, Component label)
	{
		return new EditBox(font, x, y, width, height, label);
	}
	
	/**
	 * This function creates an EditBox for entering numbers with a filter to only allow numeric input.
	 *
	 * @param font   The font to be used for the text in the EditBox.
	 * @param x      The x-coordinate of the top-left corner of the text box.
	 * @param y      The parameter "y" represents the vertical position of the EditBox on the screen or component. It is usually measured in pixels from the top of the screen or component.
	 * @param width  The width parameter specifies the width of the EditBox in pixels.
	 * @param height The height parameter is the height of the EditBox component being created.
	 * @param label  The label parameter is a Component object that represents the label associated with the EditBox. It could be a JLabel or any other type of Component that can be added to a container.
	 * @return The method is returning an instance of the EditBox class with a filter that only allows input of numbers (as a string).
	 */
	public static EditBox createNumbersTextBox(Font font, int x, int y, int width, int height, Component label, Class<? extends Number> number)
	{
		EditBox box = new EditBox(font, x, y, width, height, label);
		box.setFilter(f ->
		{
			if (f.isEmpty())
				return true;
			try
			{
				boolean test = number == Integer.class;
				if (number == Integer.class)
				{
					Integer.parseInt(f);
				} else if (number == Float.class)
				{
					Float.parseFloat(f);
				} else if (number == Double.class)
				{
					Double.parseDouble(f);
				} else if (number == Long.class)
				{
					Long.parseLong(f);
				}
				
				return true;
			} catch (NumberFormatException e)
			{
				return false;
			}
		});
		return box;
	}
	
	public static EditBox createNumbersTextBox(Font font, int x, int y, int width, int height, Component label)
	{
		return createNumbersTextBox(font, x, y, width, height, label, Integer.class);
	}
	
	public static CycleButton<Object> createCycleButton(Component label, int x, int y, int width, int height, String initialValue, String[] values, Component tooltip, Consumer<String> onValueChange, Function<String, Component> onUpdateLabel)
	{
		CycleButton.Builder<Object> builder = CycleButton.builder(mode -> onUpdateLabel.apply((String) mode));
		builder.withValues(values);
		builder.displayOnlyValue();
		builder.withInitialValue(initialValue);
		CycleButton<Object> button = builder.create(x, y, width, height, label, (cycleButton, mode) ->
		{
			onValueChange.accept((String) mode);
		});
		
		button.setTooltip(Tooltip.create(tooltip));
		button.setTooltipDelay(0);
		return button;
	}
	
}
