package chase.minecraft.architectury.simplemodconfig.test;

import chase.minecraft.architectury.simplemodconfig.annotation.SimpleConfig;

public class TestConfig
{
	@SimpleConfig(index = 1, description = "This is a String template")
	public String Name = "Hello World";
	@SimpleConfig(index = 0, description = "This is a option selection template", options = {"Option 1", "Option 2", "Option 3", "Option 4", "Option 5", "Option 6"})
	public String Options = "Option 1";
	
	@SimpleConfig(index = 2)
	public boolean Toggle = false;
	
	@SimpleConfig(index = 3, displayName = "Small Number", description = "This is an example of a 32bit whole number")
	public int SmallNumber = 5;
	@SimpleConfig(index = 4, displayName = "Ranged Number", description = "This is an example of a 32bit whole number with a min and max", min = 0, max = 32)
	public int RangeNumber = 5;
	
	@SimpleConfig(index = 5, displayName = "Large Number")
	public long LargeNumber = 5;
	
	@SimpleConfig(index = 6, displayName = "Small Decimal")
	public float SmallDecimal = .5f;
	
	@SimpleConfig(index = 7, displayName = "Large Decimal")
	public double LargeDecimal = .5d;
	
	public String unused = "";
}
