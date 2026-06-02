package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextFieldShape
import java.util.Locale

private val TurkishLocale = Locale.forLanguageTag("tr")

private fun String.matchesDropdownSearch(query: String): Boolean {
    if (query.isBlank()) return true
    return lowercase(TurkishLocale).contains(query.trim().lowercase(TurkishLocale))
}

@Composable
fun KocKitDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    searchable: Boolean = false,
    searchPlaceholder: String = "Ara...",
    emptySearchResultText: String = "Sonuç bulunamadı"
) {
    val colors = KocKitTheme.extraColors
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val display = selectedOption ?: ""
    val showActiveBorder = isFocused || expanded

    val filteredOptions = remember(options, searchQuery, searchable) {
        if (!searchable || searchQuery.isBlank()) {
            options
        } else {
            options.filter { it.matchesDropdownSearch(searchQuery) }
        }
    }

    fun dismissMenu() {
        expanded = false
        searchQuery = ""
    }

    LaunchedEffect(expanded) {
        if (!expanded) searchQuery = ""
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = display,
            onValueChange = {},
            readOnly = true,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { expanded = true },
            placeholder = { KocKitText(text = label, color = colors.textSecondary) },
            trailingIcon = {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            shape = TextFieldShape,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = KocKitFontFamily),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.cardBackground,
                unfocusedContainerColor = colors.cardBackground,
                focusedBorderColor = colors.pastelGreen,
                unfocusedBorderColor = if (showActiveBorder) colors.pastelGreen else colors.borderLight,
                errorBorderColor = colors.coralAccent
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { dismissMenu() },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Transparent)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    if (searchable) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            placeholder = {
                                KocKitText(
                                    text = searchPlaceholder,
                                    color = colors.textSecondary,
                                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = colors.textSecondary
                                )
                            },
                            singleLine = true,
                            shape = TextFieldShape,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = KocKitFontFamily,
                                color = colors.textPrimary
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = colors.cardBackground,
                                unfocusedContainerColor = colors.cardBackground,
                                focusedBorderColor = colors.pastelGreen,
                                unfocusedBorderColor = colors.borderLight,
                                cursorColor = colors.pastelGreen
                            )
                        )
                    }

                    if (filteredOptions.isEmpty()) {
                        KocKitText(
                            text = emptySearchResultText,
                            color = colors.textSecondary,
                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    } else {
                        val listHeight = if (searchable) 280.dp else 320.dp
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(listHeight)
                                .verticalScroll(rememberScrollState())
                        ) {
                            filteredOptions.forEach { option ->
                                val isSelected = option == selectedOption
                                DropdownMenuItem(
                                    text = {
                                        KocKitText(
                                            text = option,
                                            color = colors.textPrimary,
                                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                                            maxLines = 2
                                        )
                                    },
                                    onClick = {
                                        onOptionSelected(option)
                                        dismissMenu()
                                    },
                                    leadingIcon = {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = colors.pastelGreen
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 6.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) {
                                                colors.pastelGreen.copy(alpha = 0.12f)
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
        error?.let {
            KocKitText(
                text = it,
                color = colors.coralAccent,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
