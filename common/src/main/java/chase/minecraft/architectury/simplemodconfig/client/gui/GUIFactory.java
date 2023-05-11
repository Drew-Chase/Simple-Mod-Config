package chase.minecraft.architectury.simplemodconfig.client.gui;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The GUIFactory class contains static methods for creating various GUI components such as buttons, text boxes, and cycle buttons with specified parameters and callbacks.
 */
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
	
	/**
	 * This function creates a text box for numbers with a specified font, position, size, label, and data type.
	 *
	 * @param font   The font to be used for the text box.
	 * @param x      The x-coordinate of the top-left corner of the text box.
	 * @param y      The y parameter is an integer value representing the vertical position of the EditBox on the screen or component.
	 * @param width  The width parameter specifies the width of the EditBox in pixels.
	 * @param height The height parameter specifies the height of the EditBox component that will be created.
	 * @param label  The label parameter is a Component object that represents the label associated with the text box. It could be a JLabel or any other type of component that can be used to display text.
	 * @return A static method named `createNumbersTextBox` is being defined which returns an `EditBox` object. The method takes in six parameters: `font` of type `Font`, `x` and `y` of type `int`, `width` and `height` of type `int`, and `label` of type `Component`. The method also calls another overloaded version of the same method
	 */
	public static EditBox createNumbersTextBox(Font font, int x, int y, int width, int height, Component label)
	{
		return createNumbersTextBox(font, x, y, width, height, label, Integer.class);
	}
	
	/**
	 * This function creates a cycle button with specified parameters and callbacks.
	 *
	 * @param label         The label component that will be displayed next to the cycle button.
	 * @param x             The x-coordinate of the CycleButton's position on the screen.
	 * @param y             The y-coordinate of the CycleButton's position on the screen.
	 * @param width         The width of the CycleButton component.
	 * @param height        The height parameter is the height of the CycleButton component that will be created.
	 * @param initialValue  The initial value that the CycleButton should display.
	 * @param values        An array of String values that the CycleButton will cycle through.
	 * @param tooltip       The tooltip is an optional component that provides additional information about the cycle button when the user hovers over it. It can be any type of component, such as a label or a panel.
	 * @param onValueChange The onValueChange parameter is a Consumer functional interface that takes a String parameter and does some action with it. In this case, it is called when the value of the CycleButton is changed and it accepts the new value as a parameter.
	 * @param onUpdateLabel A function that takes a String value and returns a Component. This function is called whenever the value of the CycleButton changes, and it is used to update the label of the button with the new value.
	 * @return This method returns a CycleButton object.
	 */
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
