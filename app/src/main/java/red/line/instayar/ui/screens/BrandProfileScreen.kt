package red.line.instayar.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.components.auroraGlowBackground
import red.line.instayar.ui.editor.LUXURY_PALETTE
import red.line.instayar.ui.theme.BrandCyan
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.BrandViolet
import red.line.instayar.ui.theme.PrimaryGradientBrush
import java.io.File
import java.io.FileOutputStream

@Composable
fun BrandProfileScreen(
    brandProfile: BrandProfile,
    onSaveProfile: (BrandProfile) -> Unit,
    onNavigateHome: () -> Unit
) {
    BackHandler { onNavigateHome() }
    val context = LocalContext.current

    var brandName by remember(brandProfile) { mutableStateOf(brandProfile.brandName) }
    var handle by remember(brandProfile) { mutableStateOf(brandProfile.instagramHandle) }
    var phone by remember(brandProfile) { mutableStateOf(brandProfile.phoneNumber) }
    var address by remember(brandProfile) { mutableStateOf(brandProfile.websiteOrAddress) }
    var primaryColor by remember(brandProfile) { mutableStateOf(brandProfile.primaryColorHex) }
    var secondaryColor by remember(brandProfile) { mutableStateOf(brandProfile.secondaryColorHex) }
    var logoUri by remember(brandProfile) { mutableStateOf(brandProfile.logoUri) }

    var showSuccessToast by remember { mutableStateOf(false) }

    // انتخاب تصویر لوگو از گالری
    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val copiedPath = copyUriToPrivateStorage(context, uri)
            if (copiedPath != null) {
                logoUri = copiedPath
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .auroraGlowBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // سربرگ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "کیت هویت برند 🎨",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "تنظیم خودکار لوگو، نام و رنگ‌ها روی تمام ۵۸ قالب",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ۱. پیش‌نمایش زنده کارت برند (Live Mockup Card Preview)
            Text(
                text = "پیش‌نمایش زنده هویت برند شما روی استوری و پست:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0x18FFFFFF),
                borderColor = Color(0x33FFFFFF)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    parseColor(primaryColor).copy(alpha = 0.85f),
                                    parseColor(secondaryColor).copy(alpha = 0.5f),
                                    Color(0xFF0F0B1E)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // لوگوی گرد یا حرف اول اسم
                                val logoBitmap = remember(logoUri) {
                                    if (!logoUri.isNullOrBlank()) {
                                        try {
                                            BitmapFactory.decodeFile(logoUri)
                                        } catch (e: Exception) {
                                            null
                                        }
                                    } else null
                                }

                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(2.5.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (logoBitmap != null) {
                                        Image(
                                            bitmap = logoBitmap.asImageBitmap(),
                                            contentDescription = "لوگوی برند",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Text(
                                            text = brandName.take(1).ifBlank { "ب" },
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = parseColor(primaryColor)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = brandName.ifBlank { "فروشگاه آنلاین من" },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = handle.ifBlank { "@my_shop" },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BrandCyan,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x44000000))
                                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "پیش‌نمایش زنده",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x33000000))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📞 ${phone.ifBlank { "۰۹۱۲۳۴۵۶۷۸۹" }}",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "📍 ${address.ifBlank { "تهران، بازار بزرگ" }}",
                                    fontSize = 12.sp,
                                    color = Color(0xFFE2E8F0)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val isDark = MaterialTheme.colorScheme.background == red.line.instayar.ui.theme.DarkBackground

            // ۲. انتخاب فایل لوگوی اختصاصی برند
            Text(
                text = "لوگوی رسمی فروشگاه:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xF2FFFFFF),
                borderColor = if (isDark) Color(0x22FFFFFF) else Color(0x18000000)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val logoBitmap = remember(logoUri) {
                            if (!logoUri.isNullOrBlank()) {
                                try {
                                    BitmapFactory.decodeFile(logoUri)
                                } catch (e: Exception) {
                                    null
                                }
                            } else null
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isDark) Color(0x22FFFFFF) else Color(0x12000000))
                                .border(1.5.dp, BrandPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (logoBitmap != null) {
                                Image(
                                    bitmap = logoBitmap.asImageBitmap(),
                                    contentDescription = "لوگو",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = if (isDark) Color(0xFFCBD5E1) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (logoUri.isNullOrBlank()) "هنوز لوگویی انتخاب نشده" else "لوگوی اختصاصی فعال است",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (logoUri.isNullOrBlank()) "برای انتخاب تصویر لوگو کلیک کنید" else "ذخیره در حافظه پایدار برنامه",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!logoUri.isNullOrBlank()) {
                            OutlinedButton(
                                onClick = { logoUri = null },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = Brush.linearGradient(listOf(Color(0x44EF4444), Color(0x22EF4444)))
                                )
                            ) {
                                Icon(Icons.Rounded.Delete, contentDescription = "حذف", modifier = Modifier.size(16.dp))
                            }
                        }

                        Button(
                            onClick = {
                                logoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                        ) {
                            Text(
                                text = if (logoUri.isNullOrBlank()) "انتخاب لوگو" else "تغییر",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ۳. فیلدهای ورودی فرم با استایل شیشه‌ای
            GlassInputField(
                label = "نام پیج یا فروشگاه تجاری",
                value = brandName,
                onValueChange = { brandName = it },
                placeholder = "مثال: بوتیک گل سرخ"
            )

            Spacer(modifier = Modifier.height(14.dp))

            GlassInputField(
                label = "آیدی اینستاگرام (یا لینک پیج)",
                value = handle,
                onValueChange = { handle = it },
                placeholder = "مثال: @niloufar_boutique"
            )

            Spacer(modifier = Modifier.height(14.dp))

            GlassInputField(
                label = "شماره تماس یا واتساپ سفارشات",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "مثال: ۰۹۱۲۳۴۵۶۷۸۹"
            )

            Spacer(modifier = Modifier.height(14.dp))

            GlassInputField(
                label = "آدرس فروشگاه یا نشانی وب‌سایت",
                value = address,
                onValueChange = { address = it },
                placeholder = "مثال: تهران، بازار بزرگ، طبقه همکف"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ۴. انتخاب رنگ اصلی و مکمل برند با دایره‌ها
            Text(
                text = "رنگ سازمانی و اصلی برند:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LUXURY_PALETTE.forEach { colorHex ->
                    val isSelected = primaryColor.equals(colorHex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(colorHex)))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) BrandCyan else Color(0x44FFFFFF),
                                shape = CircleShape
                            )
                            .clickable { primaryColor = colorHex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "رنگ دوم و مکمل برند:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LUXURY_PALETTE.forEach { colorHex ->
                    val isSelected = secondaryColor.equals(colorHex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(colorHex)))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) BrandPink else Color(0x44FFFFFF),
                                shape = CircleShape
                            )
                            .clickable { secondaryColor = colorHex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ۵. دکمه ذخیره گرادیانتی
            Button(
                onClick = {
                    onSaveProfile(
                        BrandProfile(
                            brandName = brandName,
                            instagramHandle = handle,
                            phoneNumber = phone,
                            websiteOrAddress = address,
                            primaryColorHex = primaryColor,
                            secondaryColorHex = secondaryColor,
                            logoUri = logoUri
                        )
                    )
                    showSuccessToast = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryGradientBrush)
                    .testTag("save_brand_profile_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ذخیره و اعمال روی همه قالب‌ها ✨",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            // پیام موفقیت
            AnimatedVisibility(
                visible = showSuccessToast,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GlassSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0x2810B981),
                    borderColor = Color(0x6610B981)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "هویت برند شما با موفقیت ذخیره شد و روی قالب‌ها اعمال می‌شود! ✅",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6EE7B7),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

private fun copyUriToPrivateStorage(context: Context, uri: Uri): String? {
    return try {
        val destFile = File(context.filesDir, "brand_logo_${System.currentTimeMillis()}.png")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        }
        destFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
private fun GlassInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val isDark = MaterialTheme.colorScheme.background == red.line.instayar.ui.theme.DarkBackground
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 13.sp) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (isDark) Color(0x14FFFFFF) else Color(0xF2FFFFFF),
                unfocusedContainerColor = if (isDark) Color(0x0EFFFFFF) else Color(0xEAFFFFFF),
                focusedBorderColor = BrandViolet,
                unfocusedBorderColor = if (isDark) Color(0x22FFFFFF) else Color(0x1A7C5CFF),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

private fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        BrandPurple
    }
}
