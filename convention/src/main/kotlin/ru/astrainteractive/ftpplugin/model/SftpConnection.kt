package ru.astrainteractive.ftpplugin.model

import org.gradle.api.Project
import ru.astrainteractive.ftpplugin.property.PropertyValue.Companion.secretProperty
import ru.astrainteractive.ftpplugin.property.extension.PrimitivePropertyValueExt.intOrNull
import ru.astrainteractive.ftpplugin.property.extension.PrimitivePropertyValueExt.stringOrNull
import java.io.File

sealed interface Credential {
    data class Password(
        val username: String,
        val password: String
    ) : Credential

    data class PublicKey(
        val key: String,
    ) : Credential
}

sealed interface LoadKnownHostsStrategy {
    object None : LoadKnownHostsStrategy
    data class Exact(val file: File) : LoadKnownHostsStrategy
    object Default : LoadKnownHostsStrategy
}

sealed interface KeyVerifierStrategy {
    object Promicious : KeyVerifierStrategy
    data class Fingerprint(val value: String) : KeyVerifierStrategy
}

open class SftpConnectionExtension {
    var host: String? = null
    var port: Int? = null
    var credential: Credential? = null
    var hostsStrategy: LoadKnownHostsStrategy = LoadKnownHostsStrategy.None
    var keyVerifierStrategy: KeyVerifierStrategy = KeyVerifierStrategy.Promicious
}

fun SftpConnectionExtension.configure(target: Project): SftpConnectionExtension {
    this.host = target.secretProperty("ftp.host").stringOrNull
    this.port = target.secretProperty("ftp.port").intOrNull
    this.credential = target.secretProperty("ftp.username").stringOrNull?.let { username ->
        target.secretProperty("ftp.password").stringOrNull?.let { password ->
            Credential.Password(
                username = username,
                password = password
            )
        }
    }
    return this
}

fun SftpConnectionExtension.toConnection(): SftpConnection {
    return SftpConnection(
        host = host ?: error("Host is nulL!"),
        port = port ?: error("Port is null!"),
        credential = credential ?: error("Credential is null!"),
        hostsStrategy = hostsStrategy,
        keyVerifierStrategy = keyVerifierStrategy
    )
}

data class SftpConnection(
    val host: String,
    val port: Int,
    val credential: Credential,
    val hostsStrategy: LoadKnownHostsStrategy = LoadKnownHostsStrategy.None,
    val keyVerifierStrategy: KeyVerifierStrategy = KeyVerifierStrategy.Promicious
)
