# Getting Started

Simple Mod Config can be run on either the client or the server

## Config File

The `SimpleConfig` annotation is used in Minecraft modding with the Architectury library to define a simple
configuration for a mod. It can be applied to fields of specific types to provide a user-friendly interface for users to
modify the mod's settings.

The `SimpleConfig` annotation also includes several optional parameters. The `index` parameter specifies the order in
which configuration fields are displayed in the user interface. The `displayName` parameter provides a friendly name for
the configuration field. The `description` parameter provides additional information about the configuration field.

The `min` and `max` parameters can be used to specify the minimum and maximum values for numerical configuration fields.
The `options` parameter can be used to define a set of allowed values for the configuration field.

### String Config
```java
@SimpleConfig()
public String Name = "Hello World";
```

### String Config with Options
```java
@SimpleConfig(options = {"Option 1", "Option 2", "Option 3", "Option 4", "Option 5", "Option 6"})
public String Options = "Option 1";
```
### Boolean Config
```java
@SimpleConfig()
public boolean Toggle = false;
```
### Numbers
```java
@SimpleConfig(displayName = "Small Number")
public int SmallNumber = 5;

@SimpleConfig( displayName = "Large Number")
public long LargeNumber = 5;

@SimpleConfig(displayName = "Small Decimal")
public float SmallDecimal = .5f;

@SimpleConfig(displayName = "Large Decimal")
public double LargeDecimal = .5d;
```

### Example
```java
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

```

## Common
```java
public static SimpleModConfigBuilder builder;
```
```java
public static void init()
{
    Component displayName = Component.literal("Test Config");
    ConfigHandler<TestConfig> configHandler = new ConfigHandler<>("test-config", new TestConfig(), displayName);
    builder = new SimpleModConfigBuilder(configHandler)
            .withCommand("test-config", displayName);
}
```

## Client

```java
public void initClient()
{
    builder.withKey(InputConstants.KEY_NUMPAD0, "mod_id.category");
}
```