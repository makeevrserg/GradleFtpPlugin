package ru.astrainteractive.ftpplugin.connection.service

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.SFTPClient
import ru.astrainteractive.ftpplugin.model.Credential
import ru.astrainteractive.ftpplugin.model.SftpConnection

internal class SftpConnectionSession(private val sshClient: SSHClient, private val sftpConnection: SftpConnection) {
    fun connect() {
        sshClient.connect(sftpConnection.host, sftpConnection.port)
        when (sftpConnection.credential) {
            is Credential.Password -> {
                sshClient.authPassword(sftpConnection.credential.username, sftpConnection.credential.password)
            }

            is Credential.PublicKey -> {
                sshClient.authPublickey(sftpConnection.credential.key)
            }
        }
    }

    fun use(block: SFTPClient.() -> Unit) {
        if (!sshClient.isConnected) {
            error("Ssh client is not connected!")
        }
        sshClient.newSFTPClient().use(block)
    }

    fun disconnect() {
        if (!sshClient.isConnected) return
        sshClient.disconnect()
    }
}
