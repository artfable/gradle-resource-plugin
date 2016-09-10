package com.github.artfable.gradle

import groovy.lang.Closure
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.nio.charset.Charset
import java.util.*

/**
 * @author sbt-veselov-as
 * 06.09.2016
 */
open class GradleResourcePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val config = project.extensions.create("resourceConfig", GradleResourcePluginExtension::class.java)
        val task = project.task("resolveResources", object : Closure<Any>(project) {
            fun doCall() {
                val task = project.tasks.findByPath("resolveResources")
                task.doFirst {
                    for (file in config.files) {
                        val source = File(file.source)
                        val output = File(file.output)
                        if (!source.isFile || output.isDirectory) {
                            throw IllegalArgumentException("Invalid paths")
                        }
                        output.delete()
                        output.parentFile.mkdirs()
                        output.createNewFile()
                        source.forEachLine(Charset.forName("UTF-8"), { line ->
                            var newLine = line
                            file.parameters.forEach { param ->
                                newLine = newLine.replace("%${param.key}%", param.value)
                            }
                            output.appendText(newLine + '\n', Charset.forName("UTF-8"))
                        })
                    }
                }
            }
        })
        task.dependsOn("processResources")
    }

}

open class GradleResourcePluginExtension {
    var files: MutableList<GradleResourcePluginExtensionFile> = ArrayList()

    fun files(closure: Closure<Any>) {
        val fileMeta = GradleResourcePluginExtensionFile()
        closure.delegate = fileMeta
        closure.call()
        files.add(fileMeta)
    }
}

open class GradleResourcePluginExtensionFile {
    var source: String? = null
    var output: String? = null
    val parameters: MutableMap<String, String> = HashMap()

    fun params(closure: Closure<Any>) {
        closure.delegate = parameters
        closure.call()
    }

    override fun toString(): String {
        return "source: $source; output: $output; params: $parameters"
    }
}