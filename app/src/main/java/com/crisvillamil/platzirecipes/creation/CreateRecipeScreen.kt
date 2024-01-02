package com.crisvillamil.platzirecipes.creation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.crisvillamil.platzirecipes.R
import com.crisvillamil.platzirecipes.fileprovider.ComposeFileProvider
import com.crisvillamil.platzirecipes.fromHex
import com.crisvillamil.platzirecipes.model.Difficulty
import com.crisvillamil.platzirecipes.ui.theme.light_gray_color


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
    var description by rememberSaveable { mutableStateOf("") }
    var createButtonEnabled by rememberSaveable { mutableStateOf(false) }
    createButtonEnabled =
        recipeNameText.isNotEmpty() && difficulty != null && hour.isNotEmpty()
                && minute.isNotEmpty() && ingredients.isNotEmpty()
                && createRecipeUIState.cookingSteps.isNotEmpty()
                && description.isNotEmpty()
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        content = {
            CreateRecipeContent(
                state = createRecipeUIState,
                navController = navController,
                recipeNameText = recipeNameText,
                descriptionText = description,
                onCreateRecipe = {
                    onCreateRecipeEvent(
                        CreateRecipeEvent.OnCreateRecipe(
                            name = recipeNameText,
                            description = description,
                            ingredients = ingredients,
                            difficulty = difficulty,
                            cookingTime = "${hour}h ${minute}min",
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
                onDescriptionTextChange = {
                    description = it
                }
            )

        }
    )


}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    recipeNameText: String,
    descriptionText: String,
    onNameTextChange: (String) -> Unit,
    onDescriptionTextChange: (String) -> Unit,
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
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = "Crea tu receta"
        )
        if (hasImage && imageUri != null) {
            AsyncImage(
                modifier = Modifier
                    .height(330.dp)
                    .fillMaxWidth(),
                model = imageUri,
                contentDescription = null
            )
        } else {
            Box(modifier = Modifier
                .height(330.dp)
                .fillMaxWidth()
                .clickable {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }
                .background(Color.fromHex("F3F4F6"))) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp),
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = null
                )
            }
            Button(
                onClick = { imagePicker.launch("image/") },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Agregar desde galería")
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "Título de la receta"
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = recipeNameText,
            onValueChange = {
                onNameTextChange(it)
            },
            label = {
                Text(text = "Dale un nombre a tu receta")
            })
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "Descripción"
        )
        OutlinedTextField(
            value = descriptionText,
            onValueChange = {
                onDescriptionTextChange(it)
            },
            label = {
                Text(text = "De que trata la receta")
            })

        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "Dificultad"
        )
        RadioButtonSample(onDifficultySelected)
    }
}

@Composable
fun CreateRecipeContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: CreateRecipeUIState,
    recipeNameText: String,
    descriptionText: String,
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
    onDescriptionTextChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Icon(
            modifier = Modifier.clickable {
                navController.popBackStack()
            },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            recipeNameText = recipeNameText,
            descriptionText = descriptionText,
            onNameTextChange = onNameTextChange,
            onDifficultySelected = onDifficultySelected,
            onDescriptionTextChange = onDescriptionTextChange,
        )
        Spacer(modifier = Modifier.height(8.dp))
        IngredientSection(
            modifier = Modifier.padding(top = 16.dp),
            ingredients = ingredients,
            onIngredientsChange = onIngredientsChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimeInput(
            hour,
            min,
            onHourChange,
            onMinChange
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
            shape = RoundedCornerShape(8.dp),
            onClick = { onCreateRecipe() }) {
            Text(text = "Crear Receta")
        }
    }
}

//TODO: Check how to select here
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
    ingredients: String,
    onIngredientsChange: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "Ingredientes"
        )
        OutlinedTextField(
            value = ingredients,
            placeholder = { Text(text = "Family favorite chicken soup") },
            onValueChange = { onIngredientsChange(it) })
    }

}

@Composable
private fun TimeInput(
    hour: String,
    min: String,
    onHourChange: (String) -> Unit,
    onMinChange: (String) -> Unit
) {
    Column {
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "Tiempo de preparación"
        )
        Row(verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(
                modifier = Modifier.width(100.dp),
                value = hour,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = "horas") },
                onValueChange = {
                    if (it.isDigitsOnly() && it.length <= 2) {
                        onHourChange(it)
                    }
                })
            OutlinedTextField(
                modifier = Modifier.width(100.dp),
                value = min,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = "minutos") },
                onValueChange = {
                    if (it.isDigitsOnly() && it.length <= 2) {
                        onMinChange(it)
                    }
                })
        }
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
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "Pasos para cocinar"
        )
        state.cookingSteps.forEachIndexed { index, step ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.fromHex("F3F4F6"))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.fromHex("9CA3AF"),
                    text = "${index + 1}"
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.fromHex("F3F4F6"))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.fromHex("9CA3AF"),
                    text = step
                )
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
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = "Paso número ${state.cookingSteps.size + 1}"
            )
            Row {
                OutlinedTextField(
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
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
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