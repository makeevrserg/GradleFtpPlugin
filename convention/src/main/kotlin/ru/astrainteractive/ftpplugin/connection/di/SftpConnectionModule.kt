package ru.astrainteractive.ftpplugin.connection.di

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import ru.astrainteractive.ftpplugin.connection.service.SftpConnectionSession
import ru.astrainteractive.ftpplugin.model.KeyVerifierStrategy
import ru.astrainteractive.ftpplugin.model.LoadKnownHostsStrategy
import ru.astrainteractive.ftpplugin.model.SftpConnection

internal class SftpConnectionModule(sftpConnection: SftpConnection) {
    val session = provideSession(sftpConnection)

    companion object {
        fun provideSession(sftpConnection: SftpConnection): SftpConnectionSession {
            val sshClient = SSHClient().apply {
                when (sftpConnection.hostsStrategy) {
                    LoadKnownHostsStrategy.Default -> loadKnownHosts()
                    is LoadKnownHostsStrategy.Exact -> loadKnownHosts(sftpConnection.hostsStrategy.file)
                    LoadKnownHostsStrategy.None -> Unit
                }
                when (sftpConnection.keyVerifierStrategy) {
                    is KeyVerifierStrategy.Fingerprint -> {
                        addHostKeyVerifier(sftpConnection.keyVerifierStrategy.value)
                    }

                    KeyVerifierStrategy.Promicious -> {
                        addHostKeyVerifier(PromiscuousVerifier())
                    }
                }
            }
            return SftpConnectionSession(sshClient = sshClient, sftpConnection = sftpConnection)
        }
    }
}
