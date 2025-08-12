# SnackbarStateFlowHandle

A lifecycle-aware Snackbar library that eliminates boilerplate and prevents missed/duplicated snackbars in KMP/CMP.

> ⚠️ **Using Jetpack Compose for Android only?**    
> This library relies on `StringResource` and `getString` from `org.jetbrains.compose.resources`, which are **not supported in pure Android projects**. Please refer to the Android-specific version instead: [AndroidSnackbarStateFlowHandle](https://github.com/AungThiha/AndroidSnackbarStateFlowHandle).
> 
---

## Why use SnackbarStateFlowHandle?

- No more manual management of Snackbar queue
- No more missed or duplicated snackbars
- One-liner API for triggering snackbars from your `ViewModel`
- Lifecycle-aware: events are only collected when the UI is active
- No brittle base classes - favors composition over inheritance using Kotlin delegation
- Converts string resources automatically (e.g. R.string.key → "Actual String")
- Supports all original showSnackbar parameters (action labels, duration, callbacks, etc.)
- Fully unit-testable

---

## Installation

```kotlin
commonMain.dependencies {
    implementation("io.github.aungthiha:snackbar-stateflow-handle:1.0.0")
}
```

---

## Setup

### 1. Add `SnackbarStateFlowHandle` to your `ViewModel`
```kotlin
import androidx.lifecycle.ViewModel
import io.github.aungthiha.snackbar.SnackbarStateFlowHandle
import io.github.aungthiha.snackbar.SnackbarStateFlowOwner
// import {your package}.generated.resources.Res

class MyViewModel(
    private val snackbarStateFlowHandle: SnackbarStateFlowHandle = SnackbarStateFlowHandle()
) : ViewModel(), SnackbarStateFlowOwner by snackbarStateFlowHandle {

    fun showSimpleSnackbar() {
        showSnackBar(message = Res.string.hello_world)
    }
}
```

### 2. Observe snackbars in your Composable
```kotlin
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import io.github.aungthiha.snackbar.observeWithLifecycle
import io.github.aungthiha.snackbar.showSnackbar

@Composable
fun MyScreen(
    // yes, it's a bad practice to directly pass a ViewModel into a Screen 
    // but this is to make it easy to show how to use the SnackbarStateFlowHandle
    viewModel: MyViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.snackbarStateFlow.observeWithLifecycle {
        snackbarHostState.showSnackbar(it)
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        // ... UI content
    }
}
```

---

## API Overview

Use showSnackBar(...) from your ViewModel. You can pass string resources, string literals, or even mix both.
```kotlin
// All parameters
showSnackBar(
    message = Res.string.hello_world, // can be either string resource or String
    actionLabel = "ok",              // can be either string resource or String
    duration = SnackbarDuration.Indefinite,
    onActionPerform = { /* handle action */ },
    onDismiss = { /* handle dismiss */ }
)

// Using a string resource
showSnackBar(
    message = Res.string.hello_world,
    actionLabel = Res.string.ok
)

// Using a raw string (e.g., from backend or dynamic input)
showSnackBar(
    message = "Something went wrong!",
    actionLabel = "Retry"
)

// Mixing string types
showSnackBar(
    message = "မင်္ဂလာပါ",
    actionLabel = Res.string.ok
)

showSnackBar(
    message = Res.string.hello_world,
    actionLabel = "ok"
)
```
All parameters are optional except the message.   
For more example usages, see
`composeApp/src/commonMain/kotlin/io/github/aungthiha/snackbar/demo/AppViewModel.kt`.

---

## Unit Testing

Use `kotlin.test` with `runTest` and collect from `snackbarStateFlow`.

```kotlin
class MyViewModelTest {

    private val viewModel = MyViewModel()

    @Test
    fun snackbar_is_emitted() = runTest {
        viewModel.showSimpleSnackbar()

        val emitted = viewModel.snackbarStateFlow.first() // List<SnackbarModel>

        assertEquals(
            SnackbarString(Res.string.hello_world),
            emitted.first().message
        )
    }
}
```
---

## Compose Multiplatform Ready
Tested with:
- Android
- iOS

(Other targets are available but not tested yet)

---

## Contributing
PRs and feedback welcome!

---

## License
MIT
