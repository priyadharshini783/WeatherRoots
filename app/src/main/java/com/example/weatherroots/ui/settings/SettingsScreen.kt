package com.example.weatherroots.ui.settings
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable 
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    /*
     * AppCompat stores the currently selected application locale.
     *
     * Examples:
     * en -> English
     * ta -> Tamil
     * hi -> Hindi
     */
    val selectedLanguageCode =
        AppCompatDelegate
            .getApplicationLocales()
            .get(0)
            ?.language
            ?.takeIf { language ->
                language in supportedLanguageCodes
            }
            ?: ENGLISH_LANGUAGE_CODE

    /*
     * Frontend-only login state.
     * rememberSaveable keeps the state during configuration changes,
     * including Activity recreation during language switching.
     *
     * Later this will come from Firebase Authentication or another
     * authentication service.
     */
    val currentUser = FirebaseAuth.getInstance().currentUser

    val isLoggedIn = currentUser != null

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(24.dp)
        ) {
            SettingsHeader()

            LanguageSettingsCard(
                selectedLanguageCode =
                    selectedLanguageCode,
                onLanguageSelected = { languageCode ->

                    if (
                        languageCode !=
                        selectedLanguageCode
                    ) {
                        val localeList =
                            LocaleListCompat
                                .forLanguageTags(
                                    languageCode
                                )

                        AppCompatDelegate
                            .setApplicationLocales(
                                localeList
                            )
                    }
                }
            )
            AccountSettingsCard(
                isLoggedIn = isLoggedIn,

                onLoginClick = {
                    // Navigate to Login screen later
                },

                onLogoutClick = {

                    FirebaseAuth
                        .getInstance()
                        .signOut()

                    onLogout()

                },

                onOpenProfile = {

                    onOpenProfile()

                }
            )




            AppInformationCard()

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}


@Composable
private fun SettingsHeader() {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Customize WeatherRoots",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color =
                MaterialTheme.colorScheme.onBackground
        )

        Text(
            text =
                "Choose your preferred language and manage your account.",
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun LanguageSettingsCard(
    selectedLanguageCode: String,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf(
        LanguageOption(
            code = ENGLISH_LANGUAGE_CODE,
            nativeName = "English",
            secondaryName = "English",
            symbol = "EN"
        ),
        LanguageOption(
            code = TAMIL_LANGUAGE_CODE,
            nativeName = "தமிழ்",
            secondaryName = "Tamil",
            symbol = "TA"
        ),
        LanguageOption(
            code = HINDI_LANGUAGE_CODE,
            nativeName = "हिन्दी",
            secondaryName = "Hindi",
            symbol = "HI"
        )
    )

    val selectedLanguageName =
        languages
            .firstOrNull { language ->
                language.code ==
                        selectedLanguageCode
            }
            ?.nativeName
            ?: "English"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🌐 Language",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    "Select the language you want to use in WeatherRoots.",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            languages.forEachIndexed {
                    index,
                    language ->

                LanguageOptionRow(
                    language = language,
                    selected =
                        selectedLanguageCode ==
                                language.code,
                    onClick = {
                        onLanguageSelected(
                            language.code
                        )
                    }
                )

                if (
                    index !=
                    languages.lastIndex
                ) {
                    HorizontalDivider(
                        color =
                            MaterialTheme.colorScheme
                                .outlineVariant
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color =
                    MaterialTheme.colorScheme
                        .primaryContainer
            ) {
                Text(
                    text =
                        "Selected language: $selectedLanguageName",
                    modifier =
                        Modifier.padding(14.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color =
                        MaterialTheme.colorScheme
                            .onPrimaryContainer
                )
            }
        }
    }
}


@Composable
private fun LanguageOptionRow(
    language: LanguageOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                vertical = 10.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color =
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                }
        ) {
            Text(
                text = language.symbol,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
                fontWeight = FontWeight.Bold,
                color =
                    if (selected) {
                        MaterialTheme.colorScheme
                            .onPrimary
                    } else {
                        MaterialTheme.colorScheme
                            .onSurface
                    }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 14.dp
                )
        ) {
            Text(
                text = language.nativeName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            if (
                language.nativeName !=
                language.secondaryName
            ) {
                Text(
                    text =
                        language.secondaryName,
                    fontSize = 12.sp,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }
        }

        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}


@Composable
private fun AccountSettingsCard(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme
                    .primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "👤 Account",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme
                        .onPrimaryContainer
            )

            if (isLoggedIn) {
                LoggedInAccountContent(
                    onLogoutClick = onLogoutClick,
                    onOpenProfile = onOpenProfile
                )
            } else {
                LoggedOutAccountContent(
                    onLoginClick =
                        onLoginClick
                )
            }
        }
    }
}


@Composable
private fun LoggedInAccountContent(
    onLogoutClick: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color =
            MaterialTheme.colorScheme
                .surface
                .copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = "👩‍🌾",
                fontSize = 34.sp
            )

            Column(
                modifier = Modifier.padding(
                    start = 14.dp
                )
            ) {
                Text(
                    text = "Welcome back!",
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Your WeatherRoots account is active.",
                    fontSize = 13.sp,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }
        }
    }
    Button(
        onClick = onOpenProfile,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {

        Text(
            text = "Edit Farmer Profile",
            fontWeight = FontWeight.Bold
        )

    }

    OutlinedButton(
        onClick = onLogoutClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Logout",
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun LoggedOutAccountContent(
    onLoginClick: () -> Unit
) {
    Text(
        text =
            "Login to save your farms, crop recommendations and personal preferences.",
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color =
            MaterialTheme.colorScheme
                .onPrimaryContainer
    )

    Button(
        onClick = onLoginClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Login to WeatherRoots",
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun AppInformationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "ℹ️ About",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            SettingsInformationRow(
                title = "Application",
                value = "WeatherRoots"
            )

            HorizontalDivider()

            SettingsInformationRow(
                title = "Version",
                value = "1.0"
            )

            HorizontalDivider()

            SettingsInformationRow(
                title = "Purpose",
                value =
                    "Smart farming assistance"
            )
        }
    }
}


@Composable
private fun SettingsInformationRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


private data class LanguageOption(
    val code: String,
    val nativeName: String,
    val secondaryName: String,
    val symbol: String
)


private const val ENGLISH_LANGUAGE_CODE = "en"
private const val TAMIL_LANGUAGE_CODE = "ta"
private const val HINDI_LANGUAGE_CODE = "hi"

private val supportedLanguageCodes =
    setOf(
        ENGLISH_LANGUAGE_CODE,
        TAMIL_LANGUAGE_CODE,
        HINDI_LANGUAGE_CODE
    )