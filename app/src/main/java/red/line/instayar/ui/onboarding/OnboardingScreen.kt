package red.line.instayar.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import red.line.instayar.R
import red.line.instayar.data.local.BusinessCategoryPrefs
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.components.auroraGlowBackground
import red.line.instayar.ui.theme.BrandCyan
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.DarkSurface
import red.line.instayar.ui.theme.PrimaryGradientBrush

data class OnboardingPageData(
    val badge: String,
    val title: String,
    val description: String,
    val imageResId: Int
)

data class BusinessOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPageData(
            badge = "✨ قالب‌های آماده فروشگاهی",
            title = "بیش از صدها طرح استوری و پست",
            description = "طراحی اختصاصی برای پیج‌ها و فروشگاه‌های اینستاگرامی ایرانی، با تنوع بی‌نظیر و سبک مدرن.",
            imageResId = R.drawable.onboarding_templates_1792825833428
        ),
        OnboardingPageData(
            badge = "⚡ سریع، آسان، بدون اینترنت",
            title = "طراحی حرفه‌ای در کمتر از ۳۰ ثانیه",
            description = "بدون نیاز به فتوشاپ یا مهارت طراحی؛ فقط متن و عکست رو اضافه کن و خروجی باکیفیت بگیر!",
            imageResId = R.drawable.onboarding_speed_1792825833720
        ),
        OnboardingPageData(
            badge = "🎨 پروفایل برند هوشمند",
            title = "اعمال خودکار هویت برند شما",
            description = "لوگو، آیدی پیج اینستاگرام، شماره تماس و رنگ‌های کسب‌وکارت خودکار روی همه طرح‌ها اعمال می‌شوند.",
            imageResId = R.drawable.onboarding_branding_1792825846016
        )
    )

    val businessOptions = listOf(
        BusinessOption(BusinessCategoryPrefs.CAT_CLOTHING, "پوشاک و مد", "لباس، کیف، کفش و استایل", "👗"),
        BusinessOption(BusinessCategoryPrefs.CAT_CAFE, "شیرینی و کافه", "کیک، قهوه، فست‌فود و دسر", "☕"),
        BusinessOption(BusinessCategoryPrefs.CAT_BEAUTY, "آرایشی و زیبایی", "مراقبت پوست، عطر و مو", "💄"),
        BusinessOption(BusinessCategoryPrefs.CAT_DIGITAL, "دیجیتال و گجت", "موبایل، ساعت، هدفون و اکسسوری", "📱"),
        BusinessOption(BusinessCategoryPrefs.CAT_FLOWERS, "گل و هدیه", "دسته‌گل، کادو و یادبودها", "🌸"),
        BusinessOption(BusinessCategoryPrefs.CAT_OTHER, "سایر مشاغل", "خدمات، آموزش و فروشگاه‌های دیگر", "✨")
    )

    var selectedBusiness by remember { mutableStateOf(BusinessCategoryPrefs.CAT_CLOTHING) }
    // Total steps: 3 presentation pages + 1 question page = 4 pages
    val totalSteps = pages.size + 1
    val pagerState = rememberPagerState(pageCount = { totalSteps })
    val isQuestionPage = pagerState.currentPage == totalSteps - 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .auroraGlowBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // نوار بالا
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(BrandCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ویترینو",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    )
                }

                if (!isQuestionPage) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(totalSteps - 1)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "رد کردن",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            // محتوای پیجر
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                if (pageIndex < pages.size) {
                    // صفحات گرافیکی آنبوردینگ
                    val page = pages[pageIndex]
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // تصویر با کادر شیشه‌ای
                        GlassSurface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.22f),
                            shape = RoundedCornerShape(28.dp),
                            backgroundColor = Color(0x18FFFFFF),
                            borderColor = Color(0x28FFFFFF)
                        ) {
                            Image(
                                painter = painterResource(id = page.imageResId),
                                contentDescription = page.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // برچسب کپسولی
                        GlassSurface(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = Color(0x287C5CFF),
                            borderColor = Color(0x447C5CFF)
                        ) {
                            Text(
                                text = page.badge,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFC084FC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = page.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = page.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                } else {
                    // صفحه سؤال: «کسب‌و‌کارت چیه؟»
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        GlassSurface(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = Color(0x28FF5CA8),
                            borderColor = Color(0x44FF5CA8)
                        ) {
                            Text(
                                text = "🎯 شخصی‌سازی هوشمند ویترینو",
                                style = MaterialTheme.typography.labelMedium,
                                color = BrandPink,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "کسب‌و‌کارت چیه؟",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "برای نمایش اختصاصی قالب‌ها و پیشنهادهای روزانه متناسب با سلیقه شما",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // کارت‌های انتخابی دسته‌ها
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            businessOptions.chunked(2).forEach { rowOptions ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowOptions.forEach { opt ->
                                        val isSelected = selectedBusiness == opt.id
                                        GlassSurface(
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(18.dp),
                                            backgroundColor = if (isSelected) Color(0x287C5CFF) else Color(0x12FFFFFF),
                                            borderColor = if (isSelected) BrandPurple else Color(0x22FFFFFF),
                                            borderWidth = if (isSelected) 2.dp else 1.dp,
                                            onClick = { selectedBusiness = opt.id }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = opt.emoji,
                                                    fontSize = 24.sp
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = opt.title,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontSize = 13.sp
                                                    )
                                                    Text(
                                                        text = opt.subtitle,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontSize = 12.sp,
                                                        maxLines = 1
                                                    )
                                                }
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.CheckCircle,
                                                        contentDescription = null,
                                                        tint = BrandCyan,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // دکمه‌ها و نشانگر صفحات
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // نشانگر صفحات مدرن و متحرک
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    repeat(totalSteps) { index ->
                        val isSelected = pagerState.currentPage == index
                        val widthAnim by animateDpAsState(
                            targetValue = if (isSelected) 28.dp else 8.dp,
                            animationSpec = spring(),
                            label = "dotWidth"
                        )
                        val dotColor = if (isSelected) BrandPurple else Color(0xFF334155)

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(widthAnim)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                    }
                }

                // دکمه اصلی عملیات
                Button(
                    onClick = {
                        if (isQuestionPage) {
                            BusinessCategoryPrefs.setSelectedCategory(context, selectedBusiness)
                            onFinished()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(PrimaryGradientBrush),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isQuestionPage) "شروع به کار در ویترینو 🚀" else "مرحله بعدی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                        if (!isQuestionPage) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
