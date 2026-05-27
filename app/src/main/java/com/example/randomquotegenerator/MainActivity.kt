package com.example.randomquotegenerator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showDeveloperScreen by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(Unit) {
                delay(5000)
                showDeveloperScreen = false
            }

            if (showDeveloperScreen) {
                DeveloperScreen()
            } else {
                RandomQuoteApp()
            }
        }
    }
}

data class Quote(
    val text: String,
    val author: String,
    val category: String
)

@Composable
fun DeveloperScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF172554),
                        Color(0xFF0F766E),
                        Color(0xFFFFFBEB)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Random Quote Generator",
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Fresh thoughts. One tap away.",
                fontSize = 16.sp,
                color = Color(0xFFE0F2FE),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Developed by",
                        fontSize = 15.sp,
                        color = Color(0xFF0F766E),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "KUSUMANJALI DARA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    SocialRow("Instagram", "@kusumanjalidara9")
                    SocialRow("LinkedIn", "linkedin.com/in/kusumanjalidara")
                    SocialRow("GitHub", "github.com/KUSUMANJALI-DARA")
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Opening your quote space...",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.88f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SocialRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F766E)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF374151)
        )
    }
}

@Composable
fun RandomQuoteApp() {
    val quotes = listOf(
        Quote("Believe you can and you're halfway there.", "Theodore Roosevelt", "Motivation"),
        Quote("Push yourself, because no one else is going to do it for you.", "Unknown", "Motivation"),
        Quote("Dream big and dare to fail.", "Norman Vaughan", "Motivation"),
        Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill", "Success"),
        Quote("The only way to do great work is to love what you do.", "Steve Jobs", "Success"),
        Quote("Action is the foundational key to all success.", "Pablo Picasso", "Success"),
        Quote("Your time is limited, so don't waste it living someone else's life.", "Steve Jobs", "Life"),
        Quote("Life is what happens when you're busy making other plans.", "John Lennon", "Life"),
        Quote("Do something today that your future self will thank you for.", "Sean Patrick Flanery", "Life"),
        Quote("Study while others are sleeping; work while others are loafing.", "William Arthur Ward", "Study"),
        Quote("The expert in anything was once a beginner.", "Helen Hayes", "Study"),
        Quote("Learning never exhausts the mind.", "Leonardo da Vinci", "Study")
    )

    val categories = listOf("Motivation", "Success", "Life", "Study")
    val context = LocalContext.current
    val favoriteQuotes = remember { mutableStateListOf<String>() }
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var isDarkMode by remember { mutableStateOf(false) }
    var currentQuoteIndex by remember { mutableStateOf(0) }
    val filteredQuotes = quotes.filter { it.category == selectedCategory }
    val currentQuote = filteredQuotes[currentQuoteIndex]

    LaunchedEffect(Unit) {
        favoriteQuotes.addAll(loadFavoriteQuotes(context))
    }

    val screenColors = if (isDarkMode) {
        AppColors(
            background = Color(0xFF101828),
            card = Color(0xFF1D2939),
            primaryText = Color(0xFFF9FAFB),
            secondaryText = Color(0xFFCBD5E1),
            accent = Color(0xFF2DD4BF)
        )
    } else {
        AppColors(
            background = Color(0xFFF8FAFC),
            card = Color.White,
            primaryText = Color(0xFF111827),
            secondaryText = Color(0xFF64748B),
            accent = Color(0xFF0F766E)
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(screenColors.background),
        color = screenColors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(22.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Random Quote Generator",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = screenColors.primaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Developed by KUSUMANJALI DARA",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = screenColors.accent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            CategorySelector(
                categories = categories,
                selectedCategory = selectedCategory,
                accentColor = screenColors.accent,
                textColor = screenColors.primaryText,
                onCategorySelected = { category ->
                    selectedCategory = category
                    currentQuoteIndex = 0
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = screenColors.card),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QUOTE OF THE MOMENT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = screenColors.accent,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Quote ${currentQuoteIndex + 1} of ${filteredQuotes.size}",
                        fontSize = 13.sp,
                        color = screenColors.secondaryText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "\"${currentQuote.text}\"",
                        fontSize = 21.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = screenColors.primaryText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "- ${currentQuote.author}",
                        fontSize = 16.sp,
                        color = screenColors.secondaryText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    var nextIndex = Random.nextInt(filteredQuotes.size)
                    while (nextIndex == currentQuoteIndex && filteredQuotes.size > 1) {
                        nextIndex = Random.nextInt(filteredQuotes.size)
                    }
                    currentQuoteIndex = nextIndex
                },
                colors = ButtonDefaults.buttonColors(containerColor = screenColors.accent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "New Quote",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        shareQuote(context, currentQuote)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Share")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        toggleFavorite(favoriteQuotes, currentQuote)
                        saveFavoriteQuotes(context, favoriteQuotes)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isFavorite(favoriteQuotes, currentQuote)) "Saved" else "Favorite")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    isDarkMode = !isDarkMode
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isDarkMode) "Light Mode" else "Dark Mode")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Favorites saved: ${favoriteQuotes.size}",
                fontSize = 13.sp,
                color = screenColors.secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class AppColors(
    val background: Color,
    val card: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val accent: Color
)

@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    accentColor: Color,
    textColor: Color,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose Category",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onCategorySelected(category)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (category == selectedCategory) {
                            accentColor
                        } else {
                            Color(0xFF94A3B8)
                        }
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun shareQuote(context: Context, quote: Quote) {
    val quoteMessage = "\"${quote.text}\" - ${quote.author}\n\nShared from Random Quote Generator by KUSUMANJALI DARA"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, quoteMessage)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share Quote"))
}

fun quoteKey(quote: Quote): String {
    return "${quote.text}|${quote.author}"
}

fun isFavorite(favoriteQuotes: SnapshotStateList<String>, quote: Quote): Boolean {
    return favoriteQuotes.contains(quoteKey(quote))
}

fun toggleFavorite(favoriteQuotes: SnapshotStateList<String>, quote: Quote) {
    val key = quoteKey(quote)
    if (favoriteQuotes.contains(key)) {
        favoriteQuotes.remove(key)
    } else {
        favoriteQuotes.add(key)
    }
}

fun saveFavoriteQuotes(context: Context, favoriteQuotes: List<String>) {
    context
        .getSharedPreferences("quote_app_storage", Context.MODE_PRIVATE)
        .edit()
        .putStringSet("favorite_quotes", favoriteQuotes.toSet())
        .apply()
}

fun loadFavoriteQuotes(context: Context): Set<String> {
    return context
        .getSharedPreferences("quote_app_storage", Context.MODE_PRIVATE)
        .getStringSet("favorite_quotes", emptySet())
        ?: emptySet()
}
