package com.crisvillamil.platzirecipes.creation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.crisvillamil.platzirecipes.NavigationScreens
import com.crisvillamil.platzirecipes.fileprovider.ComposeFileProvider
import com.crisvillamil.platzirecipes.model.Difficulty


@Composable
fun CreateRecipeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    createRecipeUIState: CreateRecipeUIState,
    onCreateRecipeEvent: (CreateRecipeEvent) -> Unit,
) {
    var recipeNameText by rememberSaveable { mutableStateOf("") }
    var difficulty by remember { mutableStateOf<Difficulty?>(null) }
    var hour by rememberSaveable { mutableStateOf("") }
    var minute by rememberSaveable { mutableStateOf("") }
    var ingredients by rememberSaveable { mutableStateOf("") }
    var createButtonEnabled by rememberSaveable { mutableStateOf(false) }
    createButtonEnabled =
        recipeNameText.isNotEmpty() && difficulty != null && hour.isNotEmpty()
                && minute.isNotEmpty() && ingredients.isNotEmpty()
                && createRecipeUIState.cookingSteps.isNotEmpty()
    Surface(
        modifier = modifier
            .fillMaxSize(),
        content = {
            CreateRecipeContent(
                state = createRecipeUIState,
                recipeNameText = recipeNameText,
                onCreateRecipe = {
                    onCreateRecipeEvent(
                        CreateRecipeEvent.OnCreateRecipe(
                            name = recipeNameText,
                            ingredients = ingredients,
                            difficulty = difficulty,
                            cookingTime = "${hour}h ${minute}min"
                        )
                    )
                    navController.navigate(NavigationScreens.Home.route, navOptions {
                        launchSingleTop = true
                    })
                },
                onAddStep = { onCreateRecipeEvent((CreateRecipeEvent.OnAddStep(it))) },
                onRemoveStep = { onCreateRecipeEvent((CreateRecipeEvent.OnRemoveStep(it))) },
                onNameTextChange = {
                    recipeNameText = it
                },
                onDifficultySelected = {
                    difficulty = it
                },
                hour = hour,
                min = minute,
                onHourChange = {
                    hour = it.trim()
                },
                onMinChange = {
                    minute = it.trim()
                },
                ingredients = ingredients,
                onIngredientsChange = {
                    ingredients = it
                },
                createButtonEnabled = createButtonEnabled,
            )

        }
    )


}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    recipeNameText: String,
    onNameTextChange: (String) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit
) {
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        })
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            hasImage = it
        })
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "Crea tu receta")
        if (hasImage && imageUri != null) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = imageUri,
                contentDescription = null
            )
        } else {
            Button(onClick = {
                val uri = ComposeFileProvider.getImageUri(context)
                imageUri = uri
                cameraLauncher.launch(uri)
            }) {
                Text(text = "Tomar foto")
            }
            Button(onClick = { imagePicker.launch("image/") }) {
                Text(text = "Agregar imagen")
            }

        }
        Text(text = "Título de la receta")
        TextField(
            value = recipeNameText,
            onValueChange = {
                onNameTextChange(it)
            },
            label = {
                Text(text = "Dale un nombre a tu receta")
            })
        Text(text = "Dificultad")
        RadioButtonSample(onDifficultySelected)
    }
}

@Composable
fun CreateRecipeContent(
    modifier: Modifier = Modifier,
    state: CreateRecipeUIState,
    recipeNameText: String,
    ingredients: String,
    onIngredientsChange: (String) -> Unit,
    onCreateRecipe: () -> Unit,
    onAddStep: (String) -> Unit,
    onRemoveStep: (Int) -> Unit,
    onNameTextChange: (String) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
    hour: String,
    min: String,
    onHourChange: (String) -> Unit,
    onMinChange: (String) -> Unit,
    createButtonEnabled: Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            recipeNameText = recipeNameText,
            onNameTextChange = onNameTextChange,
            onDifficultySelected = onDifficultySelected,
        )
        IngredientSection(
            modifier = Modifier.padding(top = 16.dp),
            hour, min, ingredients, onIngredientsChange, onHourChange, onMinChange
        )
        CookingStepSection(
            modifier = Modifier.padding(top = 16.dp),
            onAddStep = {
                onAddStep(it)
            },
            onRemoveStep = onRemoveStep,
            state = state,
        )
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            enabled = createButtonEnabled,
            onClick = { onCreateRecipe() }) {
            Text(text = "Crear Receta")
        }
    }
}

@Composable
fun RadioButtonSample(onDifficultySelected: (Difficulty) -> Unit) {
    val radioOptions = Difficulty.entries.toTypedArray()
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
    Row {
        radioOptions.forEach { item ->
            Row(
                Modifier
                    .selectable(
                        selected = (item == selectedOption),
                        onClick = {
                            onOptionSelected(item)
                            onDifficultySelected(item)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (item == selectedOption),
                    onClick = {
                        onOptionSelected(item)
                        onDifficultySelected(item)
                    }
                )
                Text(
                    text = item.text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun IngredientSection(
    modifier: Modifier,
    hour: String,
    min: String,
    ingredients: String,
    onIngredientsChange: (String) -> Unit,
    onHourChange: (String) -> Unit,
    onMinChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Ingredientes")
        TextField(
            value = ingredients,
            placeholder = { Text(text = "Family favorite chicken soup") },
            onValueChange = { onIngredientsChange(it) })
    }
    Divider()
    TimeInput(hour, min, onHourChange, onMinChange)
}

@Composable
private fun TimeInput(
    hour: String,
    min: String,
    onHourChange: (String) -> Unit,
    onMinChange: (String) -> Unit
) {
    Row {
        TextField(
            modifier = Modifier.width(100.dp),
            value = hour,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "horas") },
            onValueChange = {
                if (it.isDigitsOnly() && it.length <= 2) {
                    onHourChange(it)
                }
            })
        Text(text = "H")
        TextField(
            modifier = Modifier.width(100.dp),
            value = min,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "minutos") },
            onValueChange = {
                if (it.isDigitsOnly() && it.length <= 2) {
                    onMinChange(it)
                }
            })
        Text(text = "min")
    }
}

@Composable
private fun CookingStepSection(
    modifier: Modifier,
    state: CreateRecipeUIState,
    onAddStep: (String) -> Unit,
    onRemoveStep: (Int) -> Unit,
) {
    var stepDescription by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    Column(modifier = modifier) {
        Text(text = "Pasos para cocinar")
        state.cookingSteps.forEachIndexed { index, step ->
            Column {
                Text(text = (index + 1).toString())
                Text(text = step)
                Button(onClick = {
                    onRemoveStep(index)
                }) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }
        }
        Column {
            Text(text = "Paso número ${state.cookingSteps.size + 1}")
            Row {
                TextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = stepDescription,
                    onValueChange = {
                        stepDescription = it
                    },
                    placeholder = {
                        Text(text = "Descripción del paso")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onAddStep(stepDescription)
                            stepDescription = ""
                            focusRequester.freeFocus()
                            focusRequester.captureFocus()
                        }
                    )
                )
                Button(onClick = {
                    onAddStep(stepDescription)
                    stepDescription = ""
                }) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "add item"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateRecipeScreen() {
    CreateRecipeScreen(
        navController = rememberNavController(),
        createRecipeUIState = CreateRecipeUIState(),
        onCreateRecipeEvent = {}
    )
}