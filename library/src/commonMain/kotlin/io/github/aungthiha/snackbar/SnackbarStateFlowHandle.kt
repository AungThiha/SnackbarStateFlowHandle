package io.github.aungthiha.snackbar

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

class SnackbarStateFlowHandle(
    private val snackbarMessages: MutableStateFlow<List<SnackbarModel>> = MutableStateFlow(emptyList()),
) : SnackbarStateFlowOwner, SnackbarStateFlow {

    override val snackbarStateFlow: SnackbarStateFlow = this

    /**
     * @param message text to be shown in the Snackbar
     * @param actionLabel optional action label to show as button in the Snackbar
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
     *   recommended to be set to true for better accessibility when a Snackbar is set with a
     *   [SnackbarDuration.Indefinite]
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
     *   [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     * @param onActionPerform called when action is clicked
     * @param onDismiss called if snackbar is dismissed via timeout or by the user
     */
    fun showSnackBar(
        message: StringResource,
        actionLabel: StringResource? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
        onActionPerform: () -> Unit = {},
        onDismiss: () -> Unit = {},
    ) = snackbarMessages.update { snackbarModels ->
        snackbarModels + SnackbarModel(
            message = SnackbarString(message),
            actionLabel = actionLabel?.let(SnackbarText::Resource),
            withDismissAction = withDismissAction,
            duration = duration,
            onActionPerform = onActionPerform,
            onDismiss = onDismiss,
        )
    }

    /**
     * @param message text to be shown in the Snackbar
     * @param actionLabel optional action label to show as button in the Snackbar
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
     *   recommended to be set to true for better accessibility when a Snackbar is set with a
     *   [SnackbarDuration.Indefinite]
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
     *   [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     * @param onActionPerform called when action is clicked
     * @param onDismiss called if snackbar is dismissed via timeout or by the user
     */
    fun showSnackBar(
        message: StringResource,
        actionLabel: String,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onActionPerform: () -> Unit = {},
        onDismiss: () -> Unit = {},
    ) = snackbarMessages.update { snackbarModels ->
        snackbarModels + SnackbarModel(
            message = SnackbarText.Resource(message),
            actionLabel = SnackbarText.Literal(actionLabel),
            withDismissAction = withDismissAction,
            duration = duration,
            onActionPerform = onActionPerform,
            onDismiss = onDismiss,
        )
    }

    /**
     * @param message text to be shown in the Snackbar
     * @param actionLabel optional action label to show as button in the Snackbar
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
     *   recommended to be set to true for better accessibility when a Snackbar is set with a
     *   [SnackbarDuration.Indefinite]
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
     *   [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     * @param onActionPerform called when action is clicked
     * @param onDismiss called if snackbar is dismissed via timeout or by the user
     */
    fun showSnackBar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
        onActionPerform: () -> Unit = {},
        onDismiss: () -> Unit = {},
    )= snackbarMessages.update { snackbarModels ->
        snackbarModels + SnackbarModel(
            message = SnackbarText.Literal(message),
            actionLabel = actionLabel?.let(SnackbarText::Literal),
            withDismissAction = withDismissAction,
            duration = duration,
            onActionPerform = onActionPerform,
            onDismiss = onDismiss,
        )
    }

    /**
     * @param message text to be shown in the Snackbar
     * @param actionLabel optional action label to show as button in the Snackbar
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
     *   recommended to be set to true for better accessibility when a Snackbar is set with a
     *   [SnackbarDuration.Indefinite]
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
     *   [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     * @param onActionPerform called when action is clicked
     * @param onDismiss called if snackbar is dismissed via timeout or by the user
     */
    fun showSnackBar(
        message: String,
        actionLabel: StringResource,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onActionPerform: () -> Unit = {},
        onDismiss: () -> Unit = {},
    )= snackbarMessages.update { snackbarModels ->
        snackbarModels + SnackbarModel(
            message = SnackbarText.Literal(message),
            actionLabel = SnackbarText.Resource(actionLabel),
            withDismissAction = withDismissAction,
            duration = duration,
            onActionPerform = onActionPerform,
            onDismiss = onDismiss,
        )
    }

    override fun snackbarShown(snackbarModel: SnackbarModel) {
        snackbarMessages.update { snackbarModels ->
            snackbarModels.filter { it != snackbarModel }
        }
    }

    override suspend fun collect(collector: FlowCollector<List<SnackbarModel>>) {
        snackbarMessages.collect(collector)
    }
}
