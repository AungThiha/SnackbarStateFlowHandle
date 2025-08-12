package io.github.aungthiha.snackbar

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import kotlin.jvm.JvmInline

sealed interface SnackbarText {
    @JvmInline
    value class Literal(val value: String) : SnackbarText

    @JvmInline
    value class Resource(val value: StringResource) : SnackbarText
}

suspend fun SnackbarText.unpackString(): String = when (this) {
    is SnackbarText.Literal -> value
    is SnackbarText.Resource -> getString(value)
}

/**
 * Creates a [SnackbarText.Literal] from a raw [String].
 *
 * Allows developers to instantiate either a [Literal] or [Resource] using the same `SnackbarString(...)` function name,
 * improving discoverability and reducing cognitive overhead.
 */
fun SnackbarString(value: String) = SnackbarText.Literal(value)

/**
 * Creates a [SnackbarText.Resource] from a [StringResource].
 *
 * Allows developers to instantiate either a [Literal] or [Resource] using the same `SnackbarString(...)` function name,
 * improving discoverability and reducing cognitive overhead.
 */
fun SnackbarString(value: StringResource) = SnackbarText.Resource(value)
