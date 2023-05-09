<link href="/Simple-Mod-Config/style.min.css" rel="stylesheet">
<link rel="shortcut icon" href="/Simple-Mod-Config/images/Simple Config Logo.svg" type="image/x-icon">

![Simple Config Banner.svg](images%2FSimple%20Config%20Banner.svg)

# What is Simple Mod Config

Simple Config is a minecraft configuration library.
This helps minecraft mod developers easily create a configs in minecraft.   
This mod is built with no modding platform in mind. Meaning it can be integrated in
**_[Forge](https://files.minecraftforge.net)_**,
**_[Fabric](https://fabricmc.net/develop)_**,
**_[Quilt](https://quiltmc.org)_**,
and any other minecraft modding platforms that popup throughout the years.

# Table of Contents

- [Installation](#installation)
- [Getting Started](https://dcmanproductions.github.io/Simple-Mod-Config/getting-started)
- [Mod Menu](https://dcmanproductions.github.io/Simple-Mod-Config/mod-menu)

# Installation

Add the [Modrith Maven](https://docs.modrinth.com/docs/tutorials/maven/) to your `build.gradle` file

```groovy
repositories {
    // Add the modrinth maven
    maven {
        url = "https://api.modrinth.com/maven"
    }
}
```

Then add **Simple Mod Config** to your dependencies. You can find the right version on
our [Modrinth](https://modrinth.com/mod/fluidui) page

## Common

Add the version to your `gradle.properties` files

```properties
#Simple Mod Config Version
simpleconfig=0.0.1 #put your version here
```

## Fabric, Quilt, Architectury

```groovy
dependencies {
    // Adding a Simple Mod Config dependency for fabric, quilt and architectury
    modImplementation include("maven.modrinth:simple-mod-config:${project.simpleconfig}")
}
```

## Forge

```groovy
dependencies {
    // Adding a Simple Mod Config dependency for forge
    implementation fg.deobf("maven.modrinth:simple-mod-config:${project.simpleconfig}")
}
```