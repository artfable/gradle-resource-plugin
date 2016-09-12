# Gradle Resource Plugin
(version: 0.0.1)

## Overview
Simple plugin that was written on [kotlin](https://kotlinlang.org). It give you opportunity to add some parameters into your resources. 
For example, you can add a version or a build time to the property file. 

## Install
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.github.artfable.gradle:gradle-resource-plugin:0.0.1"
    }
}

apply plugin: 'artfable.resource'
```

It'll add a task `resolveResources`

## Usage
First, you need to create your template file, and set parameters there. For instance: `%SOME_PARAM%` 

Then set the configuration of the plugin:
+ source - path to template file
+ output - path to output file
+ params - you parameters

```groovy
resourceConfig {
    files {
        source = "src/main/resources/test.json"
        output = "${buildDir}/tmp/weabapp/test.json"
        params {
            SOME_PARAM = project.version
        }
    }
    files {
        // another file
    }
}
```

You can add so many files as you want by adding more `files` blocks.