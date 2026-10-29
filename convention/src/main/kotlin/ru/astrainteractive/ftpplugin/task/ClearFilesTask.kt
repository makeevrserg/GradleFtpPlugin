package ru.astrainteractive.ftpplugin.task

import net.schmizz.sshj.sftp.Response
import net.schmizz.sshj.sftp.SFTPException
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.kotlin.dsl.property
import ru.astrainteractive.ftpplugin.connection.di.SftpConnectionModule
import ru.astrainteractive.ftpplugin.model.SftpConnectionExtension
import ru.astrainteractive.ftpplugin.model.configure
import ru.astrainteractive.ftpplugin.model.toConnection

@UntrackedTask(because = "SFTP operations have remote side effects")
abstract class ClearFilesTask : DefaultTask() {

    @get:Input
    val taskExtension: Property<SftpConnectionExtension> = project.objects
        .property(SftpConnectionExtension::class)
        .convention(SftpConnectionExtension().configure(project))

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val targetFilesPaths: ListProperty<String>

    override fun getGroup(): String = "ftp"

    @TaskAction
    fun run() {
        val sftpConnection = taskExtension.get().toConnection()
        val connectionSession = SftpConnectionModule.provideSession(sftpConnection)
        try {
            connectionSession.connect()
            connectionSession.use {
                targetFilesPaths.get().forEach(::rm)
            }
        } catch (e: SFTPException) {
            when (e.statusCode) {
                Response.StatusCode.NO_SUCH_FILE -> Unit
                else -> throw e
            }
        } finally {
            connectionSession.disconnect()
        }
    }
}
