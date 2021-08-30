package com.artfable.gradle

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.nio.charset.Charset

/**
 * @author artfable
 * 06.09.2016
 */
open class GradleResourcePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val config = project.extensions.create("resourceConfig", GradleResourcePluginExtension::class.java)
        val task = project.task("resolveResources") {
            doFirst {
                for (file in config.files) {
                    val source = File(file.source ?: throw IllegalArgumentException("Source file should be provided"))
                    val output = File(file.output ?: throw IllegalArgumentException("Output file should be provided"))
                    if (!source.isFile || output.isDirectory) {
                        throw IllegalArgumentException("Invalid paths, $source and $output should be files")
                    }

                    output.delete()
                    output.parentFile.mkdirs()
                    output.createNewFile()

                    source.forEachLine(Charset.forName("UTF-8")) { line ->
                        var newLine = line
                        file.parameters.forEach { param ->
                            newLine = newLine.replace("%${param.key}%", param.value.toString())
                        }
                        output.appendText(newLine + '\n', Charset.forName("UTF-8"))
                    }
                }
            }
        }

        project.gradle.afterProject {
            tasks.findByPath("processResources")?.let { task.dependsOn(it) }
        }
    }

}

open class GradleResourcePluginExtension {
    var files: MutableList<GradleResourcePluginExtensionFile> = ArrayList()

    fun file(action: Action<GradleResourcePluginExtensionFile>) {
        files.add(GradleResourcePluginExtensionFile().apply(action::execute))
    }
}

open class GradleResourcePluginExtensionFile {
    var source: String? = null
    var output: String? = null
    val parameters: MutableMap<String, Any> = HashMap()

    fun params(action: Action<MutableMap<String, Any>>) {
        action.execute(parameters)
    }

    override fun toString(): String {
        return "GradleResourcePluginExtensionFile(source=$source, output=$output, params=$parameters)"
    }


}