# Gradle Resource Plugin
[ ![artifactory](https://img.shields.io/badge/Artifactory-v0.1.0-green) ](https://artfable.jfrog.io/ui/packages/gav:%2F%2Fcom.artfable.gradle:gradle-resource-plugin)

## Overview
Simple plugin that was written on [kotlin](https://kotlinlang.org). It gives you opportunity to add some parameters into your resources. 
For example, you can add a version, or a build time to the property file. 

## Install
```kotlin
buildscript {
    repositories {
        maven(url = "https://artfable.jfrog.io/artifactory/default-maven-local")
    }
    dependencies {
        classpath("com.artfable.gradle:gradle-resource-plugin:0.1.0")
    }
}

apply(plugin = "artfable.resource")
```

It'll add a task `resolveResources`

For use in `plugins {}` see [Gradle resolution strategy](https://docs.gradle.org/current/userguide/custom_plugins.html#note_for_plugins_published_without_java_gradle_plugin)

## Usage
First, you need to create your template file, and set parameters there. For instance: `%SOME_PARAM%` 

Then set the configuration of the plugin:
+ source - path to template file
+ output - path to output file
+ params - you parameters

```groovy
resourceConfig {
    file {
        source = "$projectDir/src/main/resources/test.json"
        output = "${buildDir}/tmp/weabapp/test.json"
        params {
            it["SOME_PARAM"] = project.version
        }
    }
    file {
        // another file
    }
}
```
or
```kotlin
resourceConfig {
    file {
        source = "$projectDir/src/main/resources/test.json"
        output = "${buildDir}/tmp/weabapp/test.json"
        params {
            put("SOME_PARAM", project.version)
            this["SOME_PARAM2"] = project.version
        }
    }
    file {
        // another file
    }
}
```

You can add so many files as you want by adding more `file` blocks.