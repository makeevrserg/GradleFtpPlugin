package ru.astrainteractive.ftpplugin.property.extension

import ru.astrainteractive.ftpplugin.property.PropertyValue

object PrimitivePropertyValueExt {
    // String
    val PropertyValue.stringOrNull: String?
        get() = value.getOrNull()
    val PropertyValue.stringOrEmpty: String
        get() = stringOrNull.orEmpty()
    val PropertyValue.requireString: String
        get() = value.getOrThrow()

    // Integer
    val PropertyValue.int: Result<Int>
        get() = value.mapCatching { it.toInt() }
    val PropertyValue.intOrNull: Int?
        get() = int.getOrNull()
    val PropertyValue.requireInt: Int
        get() = int.getOrThrow()
}
