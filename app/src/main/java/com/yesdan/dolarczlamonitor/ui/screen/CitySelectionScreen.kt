package com.yesdan.dolarczlamonitor.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.yesdan.dolarczlamonitor.data.local.UserPreferencesRepository
import com.yesdan.dolarczlamonitor.utils.AnalyticsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val venezuelaStates = listOf(
    "Amazonas", "Anzoátegui", "Apure", "Aragua", "Barinas",
    "Bolívar", "Carabobo", "Cojedes", "Delta Amacuro", "Distrito Capital",
    "Falcón", "Guárico", "Lara", "Mérida", "Miranda",
    "Monagas", "Nueva Esparta", "Portuguesa", "Sucre", "Táchira",
    "Trujillo", "Vargas", "Yaracuy", "Zulia"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionScreen(
    preferencesRepository: UserPreferencesRepository,
    scope: CoroutineScope,
    onCitySelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF0F2F5),
            Color(0xFFE0E5EC)
        )
    )

    val cardColor = Color.White.copy(alpha = 0.55f)
    val textColor = Color(0xFF2D3436)
    val secondaryTextColor = Color(0xFF636E72)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), clip = false, ambientColor = Color.Gray.copy(alpha = 0.2f), spotColor = Color.Gray.copy(alpha = 0.2f))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)), RoundedCornerShape(24.dp)),
                color = cardColor,
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🇻🇪 Bienvenido",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Selecciona tu estado para personalizar tu experiencia",
                        fontSize = 16.sp,
                        color = secondaryTextColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), clip = false, ambientColor = Color.Gray.copy(alpha = 0.2f), spotColor = Color.Gray.copy(alpha = 0.2f))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)), RoundedCornerShape(24.dp)),
                color = cardColor,
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Estado",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedCity ?: "",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            placeholder = {
                                Text(
                                    "Selecciona tu estado",
                                    color = secondaryTextColor
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF6C5CE7),
                                unfocusedIndicatorColor = secondaryTextColor.copy(alpha = 0.5f),
                                focusedPlaceholderColor = secondaryTextColor,
                                unfocusedPlaceholderColor = secondaryTextColor
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            venezuelaStates.forEach { state ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = state,
                                            color = textColor
                                        )
                                    },
                                    onClick = {
                                        selectedCity = state
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    selectedCity?.let { city ->
                        scope.launch {
                            preferencesRepository.setUserCity(city)
                            AnalyticsHelper.setUserCity(context, city)
                            AnalyticsHelper.logCitySelected(context, city)
                            onCitySelected()
                        }
                    }
                },
                enabled = selectedCity != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7),
                    disabledContainerColor = Color(0xFFB2BEC3)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
