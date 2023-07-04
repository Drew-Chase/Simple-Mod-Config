<link href="/Simple-Mod-Config/style.min.css" rel="stylesheet">
<link rel="shortcut icon" href="/Simple-Mod-Config/images/Simple Config Logo.svg" type="image/x-icon">
# Modmenu Support

I'm going to write better modmenu docs them modmenu themselves

## Dependency

add the following to your `build.gradle`

### Maven

```groovy
repositories {
    // Modmenu maven
    maven {
        url = "https://maven.terraformersmc.com/releases/"
    }
}
```

### Dependency

```groovy
dependencies {
    // Modmenu dependency
    modImplementation include("com.terraformersmc:modmenu:${project.modmenu}")
}
```

then add the following to your `gradle.properties`   
Replace `0.0.0` with your version of modmenu

```properties
# Modmenu
modmenu=0.0.0
```

## Fabric Configuration

Place this entrypoint in you're `fabric.mod.json`

```json
"entrypoints": {
"modmenu": ["project.namespace.ModMenuEntry"]
},
```

## Mod Menu Entry

Create a class, for this we will name it `ModMenuEntry` and this class with `implement` `ModMenuApi`, and we will add an
override method named `getModConfigScreenFactory()`

### Example:

```java
@Override
public ConfigScreenFactory<?> getModConfigScreenFactory()
{
    return (ConfigScreenFactory<ConfigScreen>) screen -> new ConfigScreen(SimpleModConfig.builder.configHandler, screen);
}
```

# Overview:

```java
package project.namespace;

import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuEntry implements ModMenuApi
{
	public static SimpleModConfigBuilder builder;
	@Override
	public ConfigScreenFactory<ConfigScreen> getModConfigScreenFactory()
	{
		return new ConfigScreenFactory<ConfigScreen>()
		{
			@Override
			public ConfigScreen create(Screen screen)
			{
				return new ConfigScreen(builder.configHandler, screen);
			}
		};
	}
}

```