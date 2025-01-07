package ru.astrainteractive.ftpplugin.property

import org.gradle.api.Project
import ru.astrainteractive.ftpplugin.property.internal.SecretPropertyValue

/**
 * This interface will load [String] value from property file
 */
interface PropertyValue {
    val key: String
    val value: Result<String>
    val isExists: Boolean

    companion object {
        fun Project.secretProperty(path: String): SecretPropertyValue {
            return SecretPropertyValue(this, path)
        }
    }
}
