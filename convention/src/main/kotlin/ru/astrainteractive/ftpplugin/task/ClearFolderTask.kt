package ru.astrainteractive.ftpplugin.task

import net.schmizz.sshj.sftp.RemoteResourceInfo
import net.schmizz.sshj.sftp.SFTPClient
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import ru.astrainteractive.ftpplugin.connection.SftpConnectionManager
import ru.astrainteractive.ftpplugin.model.SftpConnectionExtension
import ru.astrainteractive.ftpplugin.model.configure
import ru.astrainteractive.ftpplugin.model.toConnection

abstract class ClearFolderTask : DefaultTask() {

    @get:Input
    val taskExtension: Property<SftpConnectionExtension> = project.objects
        .property(SftpConnectionExtension::class)
        .convention(SftpConnectionExtension().configure(project))

    @get:Input
    abstract val targetDirPath: Property<String>

    val isRecursive: Property<Boolean> = project.objects
        .property(Boolean::class)
        .convention(false)

    override fun getGroup(): String = "ftp"

    private fun clearDirectory(client: SFTPClient, items: List<RemoteResourceInfo>) {
        items.forEach { item ->
            if (item.isDirectory) {
                if (isRecursive.get()) {
                    clearDirectory(client, client.ls(item.path))
                    client.rmdir(item.path)
                }
            } else {
                val filePath = item.path
                logger.info("Remove $filePath")
                client.rm(filePath)
            }
        }
    }

    private fun clearDirectoryRecursively(client: SFTPClient, path: String) {
        val items = client.ls(path)
        clearDirectory(client, items)

        if (client.ls(path).size > 0) {
            logger.error("Target directory is not empty")
        } else {
            client.rmdir(path)
            client.mkdir(path)
            logger.info("Target directory cleared & recreated")
        }
    }

    @TaskAction
    fun run() {
        val sftpConnection = taskExtension.get().toConnection()
        val connectionManager = SftpConnectionManager(sftpConnection)
        try {
            connectionManager.use {
                val path = targetDirPath.get()
                clearDirectoryRecursively(this, path)
            }
        } finally {
            connectionManager.disconnect()
        }
    }
}
