package ru.astrainteractive.ftpplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.astrainteractive.ftpplugin.model.SftpConnectionExtension
import ru.astrainteractive.ftpplugin.model.configure

class FtpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("ftpExtension", SftpConnectionExtension::class.java)
        extension.configure(target)
    }
}
