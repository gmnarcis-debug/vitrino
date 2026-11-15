package red.line.instayar.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import red.line.instayar.billing.BillingResult
import red.line.instayar.billing.MyketBillingManager
import red.line.instayar.billing.SkuDetails
import red.line.instayar.billing.SkuPriceState
import red.line.instayar.data.repository.UserPreferences
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.components.shimmerBrush
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.LightBackground
import red.line.instayar.ui.theme.VipGold
import red.line.instayar.ui.theme.VipGoldDark
import red.line.instayar.ui.theme.VipGoldLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipModalBottomSheet(
    onDismiss: () -> Unit,
    onPurchasePlan: (sku: String, onResult: (BillingResult) -> Unit) -> Unit,
    onRestorePurchases: (onResult: (Boolean, String) -> Unit) -> Unit,
    billingManager: MyketBillingManager? = null,
    skuPriceState: SkuPriceState? = null,
    onRetryFetchPrices: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    val actualBillingManager = billingManager ?: remember(context) {
        MyketBillingManager(context, UserPreferences(context))
    }

    val priceState by (skuPriceState?.let { remember(it) { MutableStateFlow(it) } } ?: actualBillingManager.skuPriceState).collectAsState()
    val onRetry = onRetryFetchPrices ?: { actualBillingManager.retryFetchPrices() }

    var selectedSku by remember { mutableStateOf(MyketBillingManager.SKU_VIP_QUARTERLY) }
    var isProcessing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccessMessage by remember { mutableStateOf(false) }
    var marketInstallUrl by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) DarkBackground else LightBackground,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0x33FFFFFF) else Color(0x33000000))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // ۱. تصویر بالای صفحه با تاج طلایی و درخشش شفق لوکس
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x66F5C451),
                                Color(0x22F5C451),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(VipGoldLight, VipGold, VipGoldDark)
                            )
                        )
                        .border(2.dp, Color(0x88FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "تاج طلایی VIP",
                        tint = Color(0xFF1E1000),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // عنوان «ویترینو VIP»
            Text(
                text = "ویترینو VIP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = VipGold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "طراحی بدون محدودیت، بدون واترمارک و با کیفیت فوق‌العاده",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // پیام وضعیت و خطا
            AnimatedVisibility(
                visible = statusMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                statusMessage?.let { msg ->
                    GlassSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        backgroundColor = if (isSuccessMessage) Color(0x2210B981) else Color(0x22EF4444),
                        borderColor = if (isSuccessMessage) Color(0x6610B981) else Color(0x66EF4444),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSuccessMessage) Color(0xFF34D399) else Color(0xFFFCA5A5),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            marketInstallUrl?.let { url ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = VipGold),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "دانلود و نصب اپلیکیشن مایکت",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ۲. کارت «قبل و بعد» نمونه خروجی با واترمارک و بدون آن
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (isDark) Color(0x18FFFFFF) else Color(0xF2FFFFFF),
                borderColor = if (isDark) Color(0x28FFFFFF) else Color(0x18000000),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مقایسه خروجی استوری (قبل و بعد)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "پیش‌نمایش زنده",
                            style = MaterialTheme.typography.labelSmall,
                            color = VipGold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // سمت راست: خروجی رایگان (با واترمارک مورب)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isDark) Color(0xFF161622) else Color(0xFFF1F5F9))
                                .border(1.dp, Color(0x33EF4444), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            // کارت محتوای شبیه‌سازی‌شده
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x337C3AED))
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "طرح استوری",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // متن مورب واترمارک (روی‌هم‌گذاری بدون دستکاری ImageExportUtil)
                            Box(
                                modifier = Modifier
                                    .rotate(-28f)
                                    .background(Color(0x99000000), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ویترینو - نسخه رایگان",
                                    color = Color(0xDDFFFFFF),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // برچسب وضعیت قرمز پایین کارت
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color(0xCC7F1D1D))
                                    .padding(vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "نسخه رایگان (واترمارک)",
                                    color = Color(0xFFFCA5A5),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // سمت چپ: خروجی طلایی VIP (بدون واترمارک)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF23163E), Color(0xFF140D26))
                                    )
                                )
                                .border(1.5.dp, VipGold, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(listOf(VipGold, VipGoldDark))
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "طرح استوری لوکس",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // برچسب وضعیت طلایی پایین کارت
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(listOf(VipGold, VipGoldDark))
                                    )
                                    .padding(vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "خروجی VIP (بدون واترمارک)",
                                    color = Color(0xFF1E1000),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ۳. جدول مقایسه‌ای «رایگان | VIP»
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (isDark) Color(0x16FFFFFF) else Color(0xF2FFFFFF),
                borderColor = if (isDark) Color(0x24FFFFFF) else Color(0x18000000),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ویژگی‌های اصلی",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "رایگان",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "VIP 👑",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = VipGold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    VipComparisonRow(
                        title = "همه‌ی قالب‌ها",
                        freeValue = "محدود",
                        vipValue = "۵۸+ قالب کامل"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    VipComparisonRow(
                        title = "بدون واترمارک",
                        freeValue = "واترمارک دارد",
                        vipValue = "کاملاً تمیز و حذف‌شده"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    VipComparisonRow(
                        title = "فونت‌ها و ابزارهای ویژه",
                        freeValue = "فونت‌های پایه",
                        vipValue = "تمامی فونت‌ها و ابزارها"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    VipComparisonRow(
                        title = "خروجی کیفیت بالا",
                        freeValue = "استاندارد",
                        vipValue = "کیفیت عالی Ultra HD"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ۴. کارت‌های پلن (استعلام مستقیم و لحظه‌ای از مایکت)
            when (val state = priceState) {
                is SkuPriceState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = VipGold,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "در حال استعلام قیمت‌های لحظه‌ای از مایکت...",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(68.dp)
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(shimmerBrush())
                            )
                        }
                    }
                }
                is SkuPriceState.Error -> {
                    GlassSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        backgroundColor = if (isDark) Color(0x24EF4444) else Color(0x15EF4444),
                        borderColor = Color(0x66EF4444),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (state.isMarketMissing) Icons.Rounded.Download else Icons.Rounded.CloudOff,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.errorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) Color(0xFFFCA5A5) else Color(0xFFB91C1C),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                if (state.isMarketMissing) {
                                    Button(
                                        onClick = {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(MyketBillingManager.MYKET_INSTALL_URL))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("نصب مایکت 📥", color = Color.White, fontSize = 12.sp)
                                    }
                                }
                                Button(
                                    onClick = onRetry,
                                    colors = ButtonDefaults.buttonColors(containerColor = VipGold),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Refresh,
                                        contentDescription = null,
                                        tint = Color(0xFF1E1000),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("تلاش مجدد", color = Color(0xFF1E1000), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                is SkuPriceState.Success -> {
                    val plans = state.skus
                    plans.forEach { plan ->
                        val isSelected = selectedSku == plan.sku
                        GlassSurface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            backgroundColor = if (isSelected) Color(0x28F5C451) else (if (isDark) Color(0x14FFFFFF) else Color(0xF2FFFFFF)),
                            borderColor = if (isSelected) VipGold else (if (isDark) Color(0x22FFFFFF) else Color(0x1A7C5CFF)),
                            borderWidth = if (isSelected) 2.dp else 1.dp,
                            onClick = { selectedSku = plan.sku }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = plan.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) Color.White else Color(0xFF0F172A),
                                            fontSize = 14.sp
                                        )
                                        if (plan.isPopular) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(
                                                        Brush.linearGradient(listOf(VipGold, VipGoldDark))
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "محبوب‌ترین 🔥",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFF1E1000)
                                                )
                                            }
                                        } else if (plan.discountPercent != null) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0x33EC4899))
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = plan.discountPercent,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFFF80BF)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = plan.period,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B),
                                        fontSize = 12.sp
                                    )
                                }

                                Text(
                                    text = plan.price,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) VipGold else (if (isDark) Color.White else Color(0xFF0F172A)),
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
                is SkuPriceState.Idle -> {}
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ۵. دکمه بزرگ گرادیانتی طلایی «فعال‌سازی VIP»
            val canPurchase = priceState is SkuPriceState.Success && !isProcessing
            Button(
                onClick = {
                    isProcessing = true
                    statusMessage = null
                    marketInstallUrl = null
                    onPurchasePlan(selectedSku) { result ->
                        isProcessing = false
                        when (result) {
                            is BillingResult.Success -> {
                                isSuccessMessage = true
                                statusMessage = result.message
                            }
                            is BillingResult.Error -> {
                                isSuccessMessage = false
                                statusMessage = result.errorMessage
                            }
                            is BillingResult.Cancelled -> {
                                isSuccessMessage = false
                                statusMessage = "خرید لغو شد"
                            }
                            is BillingResult.MarketNotInstalled -> {
                                isSuccessMessage = false
                                statusMessage = "برای خرید، مایکت را نصب کنید."
                                marketInstallUrl = result.installUrl
                            }
                            else -> {}
                        }
                    }
                },
                enabled = canPurchase,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = if (canPurchase) {
                            Brush.linearGradient(listOf(VipGoldDark, VipGold, VipGoldLight))
                        } else {
                            Brush.linearGradient(listOf(Color(0xFF4A4A58), Color(0xFF3A3A46)))
                        }
                    )
                    .testTag("purchase_vip_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent),
                shape = RoundedCornerShape(20.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF1E1000),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (canPurchase) Color(0xFF1E1000) else Color(0xFF888894),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val btnTitle = when (priceState) {
                            is SkuPriceState.Loading -> "در حال دریافت قیمت مایکت..."
                            is SkuPriceState.Error -> "قیمت مایکت در دسترس نیست"
                            is SkuPriceState.Success -> {
                                val selectedPlan = (priceState as SkuPriceState.Success).skus.firstOrNull { it.sku == selectedSku }
                                if (selectedPlan != null && selectedPlan.price.isNotBlank()) {
                                    "خرید ${selectedPlan.title} • ${selectedPlan.price}"
                                } else {
                                    "فعال‌سازی VIP ✨"
                                }
                            }
                            is SkuPriceState.Idle -> "فعال‌سازی VIP ✨"
                        }
                        Text(
                            text = btnTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (canPurchase) Color(0xFF1E1000) else Color(0xFF888894),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ۶. دکمه «بازیابی خرید»
            OutlinedButton(
                onClick = {
                    isProcessing = true
                    statusMessage = null
                    onRestorePurchases { success, msg ->
                        isProcessing = false
                        isSuccessMessage = success
                        statusMessage = msg
                    }
                },
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("restore_vip_button"),
                shape = RoundedCornerShape(18.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.linearGradient(
                        listOf(Color(0x44FFFFFF), Color(0x22FFFFFF))
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "بازیابی خرید قبلی از مایکت",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // خط اعتماد «پرداخت امن از طریق مایکت»
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "پرداخت امن از طریق مایکت • فعال‌سازی آنی و معتبر",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun VipComparisonRow(
    title: String,
    freeValue: String,
    vipValue: String
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155),
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = freeValue,
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                fontSize = 12.sp
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x33F5C451))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = vipValue,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = VipGold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
