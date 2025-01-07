package ru.astrainteractive.ftpplugin.connection

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import ru.astrainteractive.ftpplugin.model.Credential
import ru.astrainteractive.ftpplugin.model.KeyVerifierStrategy
import ru.astrainteractive.ftpplugin.model.LoadKnownHostsStrategy
import ru.astrainteractive.ftpplugin.model.SftpConnection

class SftpConnectionManager(private val sftpConnection: SftpConnection) {
    private val sshClient = SSHClient().apply {
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
        connect(sftpConnection.host, sftpConnection.port)
        when (sftpConnection.credential) {
            is Credential.Password -> {
                authPassword(sftpConnection.credential.username, sftpConnection.credential.password)
            }

            is Credential.PublicKey -> {
                authPublickey(sftpConnection.credential.key)
            }
        }
    }
    private val sftpClient = sshClient.newSFTPClient()

    fun use(block: SFTPClient.() -> Unit) {
        if (!sshClient.isConnected) {
            error("Ssh client is not connected!")
        }
        sftpClient.use {
            it.block()
        }
    }

    fun disconnect() {
        if (!sshClient.isConnected) return
        sshClient.disconnect()
    }
}
