package io.github.aungthiha.snackbar.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.aungthiha.snackbar.observeWithLifecycle
import io.github.aungthiha.snackbar.showSnackbar
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            AppScreen()
        }
    }
}

@Composable
fun AppScreen(
    // yes, it's a bad practice to directly pass a ViewModel into a Screen
    // but this is to make it easy to show how to use the SnackbarStateFlowHandle
    viewModel: AppViewModel = viewModel { AppViewModel() }
) {

    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.snackbarStateFlow.observeWithLifecycle {
        snackbarHostState.showSnackbar(it)
    }

    val onActionPerformCalled by viewModel.onActionPerformCalled.collectAsStateWithLifecycle()
    val onDismissCalled by viewModel.onDismissedCalled.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            val greeting = remember { Greeting().greet() }
            Text("SnackbarChannel: $greeting")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Number of times onActionPerform called: $onActionPerformCalled")
            Text("Number of times onDismiss called: $onDismissCalled")

            Button(
                onClick = { viewModel.snackbarWithStringResource() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Snackbar with StringResource")
            }

            Button(
                onClick = { viewModel.snackbarWithStringLiteral() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Snackbar with String Literal")
            }

            Button(
                onClick = { viewModel.snackbarWithMixedStringTypes() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Snackbar with mixed String types")
            }

            Button(
                onClick = { viewModel.snackbarWithAction() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Show Snackbar with action")
            }

            Button(
                onClick = { viewModel.snackbarWithDismissAction() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Show Snackbar with dismiss action")
            }

            Button(
                onClick = { viewModel.snackbarWithOnActionPerformCallback() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Show Snackbar with onActionPerform callback")
            }

            Button(
                onClick = { viewModel.snackbarWithOnDismissCallback() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Show Snackbar with onDismiss callback")
            }

            Button(
                onClick = { viewModel.indefiniteSnackbar() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Indefinite Snackbar")
            }

            Spacer(modifier = Modifier.weight(1f)) // Push buttons up but keep them below content
        }
    }
}