package ru.astrainteractive.ftpplugin.task

import net.schmizz.sshj.xfer.FileSystemFile
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import ru.astrainteractive.ftpplugin.connection.SftpConnectionManager
import ru.astrainteractive.ftpplugin.model.SftpConnectionExtension
import ru.astrainteractive.ftpplugin.model.configure
import ru.astrainteractive.ftpplugin.model.toConnection
import java.io.File

abstract class UploadFileTask : DefaultTask() {

    @get:Input
    val taskExtension: Property<SftpConnectionExtension> = project.objects
        .property(SftpConnectionExtension::class)
        .convention(SftpConnectionExtension().configure(project))

    @get:InputFiles
    abstract val inputFiles: ListProperty<File>

    @get:Input
    abstract val outputPath: Property<String>

    override fun getGroup(): String = "ftp"

    @TaskAction
    fun run() {
        val sftpConnection = taskExtension.get().toConnection()
        val connectionManager = SftpConnectionManager(sftpConnection)
        try {
            connectionManager.use {
                mkdirs(outputPath.get())
                inputFiles.get().forEach { inputFile ->
                    put(FileSystemFile(inputFile), outputPath.get())
                }
            }
        } finally {
            connectionManager.disconnect()
        }
    }
}
