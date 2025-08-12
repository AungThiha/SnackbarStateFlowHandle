package io.github.aungthiha.snackbar.demo

import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.hello_from_SnackbarStateFlowHandle
import io.github.aungthiha.snackbar.SnackbarString
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppViewModelTest {

    val viewModel = AppViewModel()
    @Test
    fun snackbarEmitted() = runTest {
        viewModel.snackbarWithStringResource()

        val snackbarModel = viewModel.snackbarStateFlow.first()

        assertEquals(
            SnackbarString(Res.string.hello_from_SnackbarStateFlowHandle),
            snackbarModel.first().message
        )
    }
}
