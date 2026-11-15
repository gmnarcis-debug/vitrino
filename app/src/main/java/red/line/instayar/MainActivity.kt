package red.line.instayar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import red.line.instayar.data.repository.UserPreferences
import red.line.instayar.ui.MainApp
import red.line.instayar.ui.theme.MyApplicationTheme
import red.line.instayar.util.StoragePermissionHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userPreferences = UserPreferences(this)
        setContent {
            val isDarkTheme by userPreferences.isDarkTheme.collectAsState()
            MyApplicationTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(userPreferences = userPreferences)
                    StoragePermissionGate()
                }
            }
        }
    }
}

@Composable
private fun StoragePermissionGate() {
    val context = LocalContext.current
    var showExplanation by remember { mutableStateOf(false) }
    var showDenied by remember { mutableStateOf(false) }
    var wasAsked by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> if (!granted) showDenied = true }

    // نمایش دیالوگ توضیح، دقیقاً موقع اجرای برنامه (فقط اگر دستگاه واقعاً نیاز داشته باشد)
    LaunchedEffect(Unit) {
        if (!wasAsked &&
            StoragePermissionHelper.requiresRuntimePermission() &&
            !StoragePermissionHelper.hasPermission(context)
        ) {
            showExplanation = true
        }
    }

    if (showExplanation) {
        AlertDialog(
            onDismissRequest = {
                showExplanation = false
                wasAsked = true
            },
            title = {
                Text(
                    "دسترسی به حافظه لازم است 📁",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "برای ذخیره کردن تصاویر خروجی ساخته‌شده در حافظه گوشی و نمایش آن‌ها در گالری، " +
                    "برنامه به دسترسی نوشتن در حافظه نیاز دارد.\n\n" +
                    "این دسترسی فقط برای ذخیره تصاویر شما استفاده می‌شود و هیچ اطلاعاتی از شما " +
                    "جمع‌آوری، ذخیره یا به بیرون ارسال نمی‌شود.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExplanation = false
                        wasAsked = true
                        StoragePermissionHelper.requiredPermission()?.let {
                            launcher.launch(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6)
                    )
                ) { Text("اجازه می‌دهم", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExplanation = false
                    wasAsked = true
                }) { Text("بعداً") }
            }
        )
    }

    if (showDenied) {
        AlertDialog(
            onDismissRequest = { showDenied = false },
            title = { Text("دسترسی داده نشد", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "بدون این دسترسی، امکان ذخیره تصاویر در گالری وجود ندارد. " +
                    "می‌توانید از مسیر Settings → Apps → Vitrino → Permissions آن را به‌صورت دستی فعال کنید.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(onClick = { showDenied = false }) { Text("متوجه شدم") }
            }
        )
    }
}