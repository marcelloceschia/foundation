package net.ntworld.foundation.processor

import net.ntworld.foundation.generator.GeneratedFile
import net.ntworld.foundation.generator.GeneratorSettings
import net.ntworld.foundation.generator.SettingsSerializer
import java.nio.file.Paths
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

internal object ProcessorOutput {
    private var isTest: Boolean = false

    private val files: MutableMap<String, String> = mutableMapOf()

    val output: Map<String, String> = files

    fun setupTest() {
        isTest = true
        files.clear()
    }

    fun tearDownTest() {
        isTest = false
        files.clear()
    }

    fun readSettingsFileTest(): GeneratorSettings {
        val path = FrameworkProcessor.SETTINGS_PATH + "/" + FrameworkProcessor.SETTINGS_FILENAME
        if (!files.contains(path)) {
            return GeneratorSettings(
                provider = "",
                events = listOf(),
                aggregateFactories = listOf()
            )
        }
        return SettingsSerializer.parse(files[path]!!)
    }

    fun readSettingsFile(processingEnv: ProcessingEnvironment): GeneratorSettings {
        if (isTest) {
            return readSettingsFileTest()
        }

        val file = Paths.get(
            getKaptGeneratedDirectory(processingEnv),
            FrameworkProcessor.SETTINGS_PATH,
            FrameworkProcessor.SETTINGS_FILENAME
        ).toFile()
        if (!file.exists()) {
            return GeneratorSettings(
                provider = "",
                events = listOf(),
                aggregateFactories = listOf()
            )
        }
        val content = file.readText()
        return SettingsSerializer.parse(content)
    }

    fun updateSettingsFile(processingEnv: ProcessingEnvironment, settings: GeneratorSettings) {
        writeText(
            processingEnv,
            FrameworkProcessor.SETTINGS_PATH,
            FrameworkProcessor.SETTINGS_FILENAME,
            SettingsSerializer.serialize(settings)
        )
    }

    fun writeText(processingEnv: ProcessingEnvironment, directory: String, fileName: String, content: String) {
        if (isTest) {
            files["$directory/$fileName"] = content
            return
        }

        val base = getKaptGeneratedDirectory(processingEnv)
        val dir = Paths.get(base, directory).toFile()
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = Paths.get(base, directory, fileName).toFile()
        file.writeText(content)
    }

    fun writeGeneratedFile(processingEnv: ProcessingEnvironment, file: GeneratedFile) {
        writeText(
            processingEnv,
            file.directory,
            file.fileName,
            file.content
        )
    }

    private fun getKaptGeneratedDirectory(processingEnv: ProcessingEnvironment): String {
        return processingEnv.options[FrameworkProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Can't find the target directory for generated Kotlin files."
            )
            throw FoundationProcessorException("Can't find the target directory for generated Kotlin files.")
        }
    }
}