package red.line.instayar.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import red.line.instayar.data.local.BusinessCategoryPrefs
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateFormat
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.components.TemplateThumbnailPreview
import red.line.instayar.ui.components.VipCrownBadge
import red.line.instayar.ui.components.auroraGlowBackground
import red.line.instayar.ui.components.shimmerBrush
import red.line.instayar.ui.theme.BrandCyan
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.BrandViolet
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.DarkSurface
import red.line.instayar.ui.theme.LightSurface
import red.line.instayar.ui.theme.PrimaryGradientBrush
import red.line.instayar.ui.theme.VipGold
import red.line.instayar.ui.theme.VipGoldDark
import red.line.instayar.ui.theme.VipGoldLight
import red.line.instayar.ui.theme.VipGradientBrush
import red.line.instayar.util.PersianDateUtil
import red.line.instayar.util.toPersianDigits

val CATEGORIES = listOf(
    "همه",
    "معرفی محصول",
    "تخفیف و حراج",
    "قیمت‌نامه",
    "اطلاعیه",
    "مناسبتی",
    "نظر مشتری",
    "تبریک",
    "سفارش‌گیری"
)

private val CATEGORY_EMOJIS = mapOf(
    "همه" to "🌟",
    "معرفی محصول" to "🛍️",
    "تخفیف و حراج" to "🔥",
    "قیمت‌نامه" to "🏷️",
    "اطلاعیه" to "📢",
    "مناسبتی" to "🎈",
    "نظر مشتری" to "💬",
    "تبریک" to "🎉",
    "سفارش‌گیری" to "🛒"
)

fun getOrderedCategoriesForBusiness(businessCategory: String): List<String> {
    val preferredFirst = when (businessCategory) {
        BusinessCategoryPrefs.CAT_CLOTHING -> listOf("معرفی محصول", "تخفیف و حراج", "قیمت‌نامه", "سفارش‌گیری")
        BusinessCategoryPrefs.CAT_CAFE -> listOf("معرفی محصول", "سفارش‌گیری", "قیمت‌نامه", "تخفیف و حراج")
        BusinessCategoryPrefs.CAT_BEAUTY -> listOf("معرفی محصول", "تخفیف و حراج", "نظر مشتری", "قیمت‌نامه")
        BusinessCategoryPrefs.CAT_DIGITAL -> listOf("معرفی محصول", "قیمت‌نامه", "تخفیف و حراج", "اطلاعیه")
        BusinessCategoryPrefs.CAT_FLOWERS -> listOf("تبریک", "مناسبتی", "معرفی محصول", "سفارش‌گیری")
        else -> emptyList()
    }
    val remaining = CATEGORIES.filter { it != "همه" && it !in preferredFirst }
    return listOf("همه") + preferredFirst + remaining
}

private fun getBusinessKeywords(businessCategory: String): List<String> {
    return when (businessCategory) {
        BusinessCategoryPrefs.CAT_CLOTHING -> listOf("پوشاک", "لباس", "کفش", "کیف", "fashion", "کتونی", "مد", "تیشرت", "بوتیک")
        BusinessCategoryPrefs.CAT_CAFE -> listOf("کافه", "قهوه", "شیرینی", "کیک", "غذا", "دسر", "رستوران", "cafe")
        BusinessCategoryPrefs.CAT_BEAUTY -> listOf("آرایش", "پوست", "عطر", "زیبایی", "ادکلن", "میکاپ", "مراقبت")
        BusinessCategoryPrefs.CAT_DIGITAL -> listOf("دیجیتال", "ساعت", "موبایل", "هدفون", "گجت", "کوارتز", "هوشمند", "سوییسی")
        BusinessCategoryPrefs.CAT_FLOWERS -> listOf("گل", "هدیه", "تبریک", "کادو", "تولد", "ولنتاین", "مادر", "پدر")
        else -> emptyList()
    }
}

/**
 * متد کمکی برای خروج المان‌های افقی از حاشیه داخلی والد و گسترش به عرض کامل صفحه
 */
private fun Modifier.bleedHorizontally(padding: Dp = 16.dp): Modifier = this.layout { measurable, constraints ->
    val paddingPx = padding.roundToPx()
    val newWidth = constraints.maxWidth + paddingPx * 2
    val placeable = measurable.measure(
        constraints.copy(minWidth = newWidth, maxWidth = newWidth)
    )
    layout(constraints.maxWidth, placeable.height) {
        placeable.placeRelative(-paddingPx, 0)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    brandProfile: BrandProfile = BrandProfile(),
    onNavigateToEditor: (templateId: String) -> Unit = {},
    onOpenAbout: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val activity = context as? Activity
    var lastBackPressTime by remember { mutableStateOf(0L) }

    // خروج با دو بار کلیک دکمه بازگشت (BackHandler)
    BackHandler {
        val now = System.currentTimeMillis()
        if (now - lastBackPressTime < 2000L) {
            activity?.finish()
        } else {
            lastBackPressTime = now
            Toast.makeText(context, "برای خروج، یک‌بار دیگر بازگشت را بزنید", Toast.LENGTH_SHORT).show()
        }
    }

    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    val userBusiness = remember { BusinessCategoryPrefs.getSelectedCategory(context) }
    val orderedCategories = remember(userBusiness) { getOrderedCategoriesForBusiness(userBusiness) }

    // بنر مناسبت: فقط در تب دسته‌بندی «همه»، فیلتر فرمت «همه» و جستجوی خالی
    val showOccasionBanner = state.selectedCategory == "همه" && state.searchQuery.isBlank() && state.selectedFormatFilter == FormatFilter.ALL

    // قالب‌های پیشنهادی ردیف «پیشنهاد امروز» (متناسب با حوزه کسب‌وکار و فرمت فعال)
    val dailyRecommendedList = remember(state.allTemplates, userBusiness, state.selectedFormatFilter, state.searchQuery) {
        if (state.searchQuery.isNotBlank()) emptyList()
        else {
            val formatCandidates = when (state.selectedFormatFilter) {
                FormatFilter.ALL -> state.allTemplates
                FormatFilter.STORY -> state.allTemplates.filter { it.format == TemplateFormat.STORY }
                FormatFilter.POST -> state.allTemplates.filter { it.format == TemplateFormat.POST_SQUARE || it.format == TemplateFormat.POST_PORTRAIT }
                FormatFilter.HIGHLIGHT -> state.allTemplates.filter { it.format == TemplateFormat.HIGHLIGHT }
            }

            if (formatCandidates.isEmpty()) {
                emptyList()
            } else {
                val keywords = getBusinessKeywords(userBusiness)
                val matching = if (keywords.isNotEmpty()) {
                    formatCandidates.filter { t ->
                        keywords.any { kw -> t.title.contains(kw) || t.tags.any { tag -> tag.contains(kw) } }
                    }
                } else {
                    formatCandidates.filter { it.category == "معرفی محصول" || it.category == "تخفیف و حراج" }
                }
                val nonMatching = formatCandidates.filter { it !in matching }
                (matching + nonMatching).take(6)
            }
        }
    }

    val recommendedIds = remember(dailyRecommendedList) {
        dailyRecommendedList.map { it.id }.toSet()
    }

    // قالب‌های اصلی گرید: جلوگیری از تکرار قالب‌های پیشنهاد امروز در گرید اصلی
    val gridTemplates = remember(state.filteredTemplates, recommendedIds, state.searchQuery) {
        if (state.searchQuery.isBlank() && recommendedIds.isNotEmpty()) {
            state.filteredTemplates.filter { it.id !in recommendedIds }
        } else {
            state.filteredTemplates
        }
    }

    // نمایش نوار فشرده بالای صفحه زمانی که کاربر از کنترل قطعه‌ای عبور کرد (با derivedStateOf)
    val showStickyBar by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex >= 4 ||
                (gridState.firstVisibleItemIndex == 3 && gridState.firstVisibleItemScrollOffset > 30)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .auroraGlowBackground()
    ) {
        // اسکرول عمودی واحد برای کل صفحه با LazyVerticalGrid
        val isHighlightFormat = state.selectedFormatFilter == FormatFilter.HIGHLIGHT
        LazyVerticalGrid(
            columns = if (isHighlightFormat) GridCells.Fixed(3) else GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 0.dp,
                bottom = 120.dp // فاصله ایمن برای نوار ناوبری شناور + ۱۶dp
            ),
            horizontalArrangement = Arrangement.spacedBy(if (isHighlightFormat) 10.dp else 14.dp),
            verticalArrangement = Arrangement.spacedBy(if (isHighlightFormat) 10.dp else 14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("templates_grid")
        ) {
            // ۱. هدر شفق: نام برنامه، سلام یک‌خطی، تاریخ و ساعت، دکمه شیشه‌ای VIP
            item(key = "header_item", span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.statusBarsPadding())
                    Spacer(modifier = Modifier.height(4.dp))
                    HomeHeader(
                        isVip = state.isVipUser,
                        onVipClick = { viewModel.openVipModal() },
                        onOpenAbout = onOpenAbout
                    )
                }
            }

            // ۲. نوار جستجوی شیشه‌ای کاملاً گرد (Pill)
            item(key = "search_item", span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.height(6.dp))
                    GlassSearchBar(
                        query = state.searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onClear = {
                            viewModel.onSearchQueryChanged("")
                            focusManager.clearFocus()
                        }
                    )
                }
            }

            // ۳. ردیف دایره‌ای دسته‌ها با قطر فشرده ۵۶dp و اولویت‌بندی کسب‌وکار
            item(key = "category_item", span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    CircularCategoryRow(
                        categories = orderedCategories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelect = { viewModel.onCategorySelected(it) },
                        modifier = Modifier.bleedHorizontally(16.dp)
                    )
                }
            }

            // ۴. کنترل قطعه‌ای شیشه‌ای (استوری | پست | هایلایت | همه)
            item(key = "format_control_item", span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    GlassSegmentedFormatControl(
                        selectedFilter = state.selectedFormatFilter,
                        onFilterSelect = { viewModel.onFormatFilterSelected(it) }
                    )
                }
            }

            // ۵. بنر مناسبت پیش‌رو (فقط در تب دسته‌بندی «همه»، فیلتر فرمت «همه» و جستجوی خالی - حداکثر ارتفاع ۶۴dp یک‌خطی)
            if (showOccasionBanner) {
                item(key = "banner_item", span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        UpcomingOccasionBanner(
                            onClick = {
                                viewModel.onCategorySelected("مناسبتی")
                            }
                        )
                    }
                }
            }

            // ۵.۱ ردیف «پیشنهاد امروز» با کارت‌های عرض ثابت ۱۲۰dp متناسب با فرمت انتخابی و بدون تکرار در گرید اصلی
            if (dailyRecommendedList.isNotEmpty() && state.searchQuery.isBlank()) {
                item(key = "daily_rec_item", span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        DailyRecommendationsRow(
                            templates = dailyRecommendedList,
                            businessCategory = userBusiness,
                            brandProfile = brandProfile,
                            onTemplateClick = { template ->
                                viewModel.onTemplateClicked(template)
                            },
                            modifier = Modifier.bleedHorizontally(16.dp)
                        )
                    }
                }
            }

            // ۶. عنوان بخش و برچسب کوچک تعداد قالب‌ها در یک ردیف
            item(key = "section_header_item", span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            state.selectedCategory != "همه" -> state.selectedCategory
                            state.selectedFormatFilter == FormatFilter.STORY -> "قالب‌های استوری"
                            state.selectedFormatFilter == FormatFilter.POST -> "قالب‌های پست"
                            state.selectedFormatFilter == FormatFilter.HIGHLIGHT -> "کاورهای هایلایت"
                            else -> "همه‌ی قالب‌ها"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "تعداد قالب‌ها: ${state.filteredTemplates.size.toPersianDigits()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("templates_count_label")
                    )
                }
            }

            // ۷. گرید کارت‌های قالب (۳ ستونه دایره‌ای برای هایلایت، ۲ ستونه مستطیلی برای سایر فرمت‌ها)
            if (state.isLoading) {
                val skeletonCount = if (isHighlightFormat) 9 else 6
                items(skeletonCount, span = { GridItemSpan(1) }) {
                    if (isHighlightFormat) {
                        ShimmerHighlightSkeleton()
                    } else {
                        ShimmerTemplateSkeleton()
                    }
                }
            } else if (gridTemplates.isEmpty()) {
                item(key = "empty_view_item", span = { GridItemSpan(maxLineSpan) }) {
                    EmptyTemplatesView(
                        onResetFilters = {
                            viewModel.onSearchQueryChanged("")
                            viewModel.onCategorySelected("همه")
                            viewModel.onFormatFilterSelected(FormatFilter.ALL)
                        }
                    )
                }
            } else {
                itemsIndexed(
                    items = gridTemplates,
                    key = { _, template -> template.id }
                ) { index, template ->
                    StaggeredEntranceCard(index = index) {
                        if (isHighlightFormat) {
                            HighlightCircleTemplateCard(
                                template = template,
                                brandProfile = brandProfile,
                                onClick = {
                                    viewModel.onTemplateClicked(template)
                                }
                            )
                        } else {
                            ModernTemplateCard(
                                template = template,
                                brandProfile = brandProfile,
                                selectedFilter = state.selectedFormatFilter,
                                onClick = {
                                    viewModel.onTemplateClicked(template)
                                }
                            )
                        }
                    }
                }
            }
        }

        // ۲. نوار فشرده‌ی ثابت شیشه‌ای بالای صفحه (ظاهر شدن پس از رد شدن از کنترل قطعه‌ای)
        AnimatedVisibility(
            visible = showStickyBar,
            enter = fadeIn(animationSpec = tween(220)) + slideInVertically(animationSpec = tween(220)) { -it },
            exit = fadeOut(animationSpec = tween(220)) + slideOutVertically(animationSpec = tween(220)) { -it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 6.dp)
                .zIndex(10f)
        ) {
            StickyCompactFormatBar(
                selectedFilter = state.selectedFormatFilter,
                onFilterSelect = { viewModel.onFormatFilterSelected(it) },
                onSearchClick = {
                    coroutineScope.launch {
                        gridState.animateScrollToItem(1) // هدایت مستقیم به نوار جستجو در بالای صفحه
                    }
                }
            )
        }
    }

    // باتم‌شیت جزئیات و باز کردن قالب
    state.selectedTemplateForDetail?.let { template ->
        TemplateDetailBottomSheet(
            template = template,
            brandProfile = brandProfile,
            isVipUser = state.isVipUser,
            onDismiss = { viewModel.closeTemplateDetail() },
            onSelectTemplate = {
                viewModel.closeTemplateDetail()
                if (template.isVip && !state.isVipUser) {
                    viewModel.openVipModal()
                } else {
                    onNavigateToEditor(template.id)
                }
            }
        )
    }
}

/**
 * ۱. هدر شفق: نام برنامه، احوال‌پرسی زنده یک‌خطی، تاریخ و ساعت شمسی، دکمه شیشه‌ای VIP
 */
@Composable
private fun HomeHeader(
    isVip: Boolean,
    onVipClick: () -> Unit,
    onOpenAbout: () -> Unit
) {
    var liveDateText by remember { mutableStateOf(PersianDateUtil.getPersianDate().fullFormattedText) }
    var liveTimeText by remember { mutableStateOf(PersianDateUtil.getCurrentLiveTime()) }
    var greetingText by remember { mutableStateOf(PersianDateUtil.getTimeOfDayGreeting()) }

    LaunchedEffect(Unit) {
        while (true) {
            liveDateText = PersianDateUtil.getPersianDate().fullFormattedText
            liveTimeText = PersianDateUtil.getCurrentLiveTime()
            greetingText = PersianDateUtil.getTimeOfDayGreeting()
            delay(60_000L)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryGradientBrush),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "و",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // جمله سلام یک‌خطی، کوتاه، بدون سه‌نقطه
                Text(
                    text = "سلام، $greetingText",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // تاریخ و ساعت شمسی در یک خط با فونت ۱۲sp
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CalendarToday,
                    contentDescription = null,
                    tint = BrandPink,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = liveDateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Text(text = "•", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), fontSize = 12.sp)
                Icon(
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = null,
                    tint = BrandCyan,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = liveTimeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // دکمه‌های اکشن شیشه‌ای: دکمه درباره ما و دکمه شیشه‌ای VIP
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlassSurface(
                modifier = Modifier
                    .size(38.dp)
                    .clickable { onOpenAbout() }
                    .testTag("home_info_icon_button"),
                shape = CircleShape,
                backgroundColor = Color(0x1AFFFFFF),
                borderColor = Color(0x28FFFFFF)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "درباره ما",
                    tint = Color.White,
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.Center)
                )
            }

            // دکمه شیشه‌ای تاج VIP
            GlassSurface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onVipClick() }
                    .testTag("vip_header_button"),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isVip) Color(0x2610B981) else Color(0x28F5C451),
                borderColor = if (isVip) Color(0x6610B981) else Color(0x88F5C451),
                borderWidth = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "VIP",
                        tint = if (isVip) Color(0xFF34D399) else VipGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isVip) "VIP فعال" else "ویژه 👑",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isVip) Color(0xFF34D399) else VipGoldLight,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/**
 * ۲. جستجوی شیشه‌ای کاملاً گرد (Pill)
 */
@Composable
private fun GlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = CircleShape,
        borderWidth = 1.dp
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "جستجوی طرح (ساعت، لباس، حراج، کافه...)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = BrandViolet,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "پاک کردن",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = BrandPink
            ),
            modifier = Modifier
                .fillMaxSize()
                .testTag("search_text_field")
        )
    }
}

/**
 * ۳. ردیف دایره‌ای دسته‌ها با قطر ۵۶dp فشرده:
 * هر دسته یک دایره ۵۶dp با ایموجی و برچسب ۱۲sp؛ دسته‌ی فعال دارای حلقه گرادیانتی برند است.
 */
@Composable
private fun CircularCategoryRow(
    categories: List<String> = CATEGORIES,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category
            val emoji = CATEGORY_EMOJIS[category] ?: "✨"

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onCategorySelect(category)
                    }
            ) {
                val isDark = MaterialTheme.colorScheme.background == DarkBackground

                // دایره ۵۶dp فشرده
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) {
                                Modifier
                                    .border(2.dp, PrimaryGradientBrush, CircleShape)
                                    .background(if (isDark) Color(0x337C5CFF) else Color(0x227C5CFF), CircleShape)
                            } else {
                                Modifier
                                    .border(1.dp, if (isDark) Color(0x1FFFFFFF) else Color(0x18000000), CircleShape)
                                    .background(if (isDark) Color(0x10FFFFFF) else Color(0xF2FFFFFF), CircleShape)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) {
                        if (isDark) Color.White else BrandViolet
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * ۴. کنترل قطعه‌ای شیشه‌ای (Glass Segmented Control):
 * گزینه‌های «همه | استوری | پست | هایلایت» درون کپسول شیشه‌ای یکپارچه.
 */
@Composable
private fun GlassSegmentedFormatControl(
    selectedFilter: FormatFilter,
    onFilterSelect: (FormatFilter) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = CircleShape,
        backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xF2FFFFFF),
        borderColor = if (isDark) Color(0x22FFFFFF) else Color(0x18000000),
        borderWidth = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormatFilter.values().forEach { filter ->
                val isSelected = selectedFilter == filter

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) {
                                Modifier.background(PrimaryGradientBrush, CircleShape)
                            } else {
                                Modifier.background(Color.Transparent)
                            }
                        )
                        .clickable { onFilterSelect(filter) }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter.titleFa,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/**
 * ۵. بنر مناسبت پیش‌رو: کارت شیشه‌ای فشرده یک‌خطی با حداکثر ارتفاع ۶۴dp
 */
@Composable
private fun UpcomingOccasionBanner(
    onClick: () -> Unit
) {
    val persianDate = remember { PersianDateUtil.getPersianDate() }

    // محاسبه تقریبی روزهای مانده تا شب یلدا (۳۰ آذر)
    val daysUntilYalda = remember(persianDate) {
        val currentMonth = persianDate.month
        val currentDay = persianDate.day
        when {
            currentMonth < 9 -> (9 - currentMonth - 1) * 30 + (30 - currentDay) + 30
            currentMonth == 9 -> 30 - currentDay
            else -> 365 - ((currentMonth - 9) * 30 + currentDay)
        }
    }

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x1CFF5CA8),
        borderColor = Color(0x44FF5CA8),
        borderWidth = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FF5CA8)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🍉", fontSize = 18.sp)
                }

                Text(
                    text = "${daysUntilYalda.toPersianDigits()} روز تا شب یلدا و جشنواره زمستانی",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                tint = BrandPink,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * ۵.۱ ردیف «پیشنهاد امروز» با کارت‌های عرض ثابت ۱۲۰dp و بدون تکرار در گرید اصلی
 */
@Composable
private fun DailyRecommendationsRow(
    templates: List<Template>,
    businessCategory: String,
    brandProfile: BrandProfile,
    onTemplateClick: (Template) -> Unit,
    modifier: Modifier = Modifier
) {
    if (templates.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "✨ پیشنهاد امروز",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x33FF5CA8))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = businessCategory,
                        color = Color(0xFFFF80BF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(templates, key = { "rec_daily_${it.id}" }) { template ->
                DailyRecommendationCard(
                    template = template,
                    brandProfile = brandProfile,
                    onClick = { onTemplateClick(template) }
                )
            }
        }
    }
}

/**
 * کارت فشرده پیشنهاد امروز با عرض ثابت ۱۲۰dp
 */
@Composable
private fun DailyRecommendationCard(
    template: Template,
    brandProfile: BrandProfile,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "recCardSpringScale"
    )

    GlassSurface(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("template_card_${template.id}"),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x12FFFFFF),
        borderColor = if (template.isVip) Color(0x66F5C451) else Color(0x22FFFFFF),
        borderWidth = if (template.isVip) 1.5.dp else 1.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            TemplateThumbnailPreview(
                template = template,
                brandProfile = brandProfile,
                modifier = Modifier.fillMaxSize(),
                cornerRadius = 18f
            )

            // نشان VIP در گوشه بالا راست
            if (template.isVip) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    VipCrownBadge(text = "VIP")
                }
            }

            // لایه تیره گرادیانتی و عنوان در پایین کارت
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xD90B0B10),
                                Color(0xF20B0B10)
                            )
                        )
                    )
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = template.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.75f),
                            offset = Offset(0f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * ۲. نوار فشرده‌ی ثابت شیشه‌ای بالای صفحه (شامل کنترل فرمت و دکمه جستجو)
 */
@Composable
private fun StickyCompactFormatBar(
    selectedFilter: FormatFilter,
    onFilterSelect: (FormatFilter) -> Unit,
    onSearchClick: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = CircleShape,
        backgroundColor = if (isDark) Color(0xF012121A) else Color(0xF2FFFFFF),
        borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x18000000),
        borderWidth = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // دکمه جستجو: هدایت به فیلد جستجو در بالای صفحه
            GlassSurface(
                modifier = Modifier
                    .size(34.dp)
                    .clickable { onSearchClick() },
                shape = CircleShape,
                backgroundColor = if (isDark) Color(0x22FFFFFF) else Color(0x0A000000),
                borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x15000000)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "جستجو",
                    tint = BrandCyan,
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // چیپ‌های فشرده کنترل فرمت (همه | استوری | پست | هایلایت)
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormatFilter.values().forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .background(PrimaryGradientBrush, CircleShape)
                                        .border(1.dp, Color(0x88FFFFFF), CircleShape)
                                } else {
                                    Modifier
                                        .background(if (isDark) Color(0x14FFFFFF) else Color(0x0A000000), CircleShape)
                                        .border(1.dp, if (isDark) Color(0x18FFFFFF) else Color(0x10000000), CircleShape)
                                }
                            )
                            .clickable { onFilterSelect(filter) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter.titleFa,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * انیمیشن ورود پله‌ای برای کارت‌های گرید
 */
@Composable
private fun StaggeredEntranceCard(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((index % 8) * 35L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(280)) + slideInVertically(
            initialOffsetY = { 40 },
            animationSpec = tween(280)
        )
    ) {
        content()
    }
}

/**
 * آیکون ریز فرمت (استوری، پست، هایلایت) برای نمایش در گوشه کارت در حالت فیلتر «همه»
 */
@Composable
private fun FormatMiniIcon(
    format: TemplateFormat,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
            .border(0.5.dp, Color(0x33FFFFFF), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(12.dp)) {
            val strokeColor = Color.White.copy(alpha = 0.9f)
            val strokeWidth = 1.3.dp.toPx()
            when (format) {
                TemplateFormat.STORY -> {
                    // مستطیل عمودی ۹:۱۶
                    drawRoundRect(
                        color = strokeColor,
                        topLeft = Offset(size.width * 0.2f, 0f),
                        size = Size(size.width * 0.6f, size.height),
                        cornerRadius = CornerRadius(2.dp.toPx()),
                        style = Stroke(width = strokeWidth)
                    )
                }
                TemplateFormat.POST_PORTRAIT, TemplateFormat.POST_SQUARE -> {
                    // مربع پست
                    drawRoundRect(
                        color = strokeColor,
                        topLeft = Offset(size.width * 0.08f, size.height * 0.08f),
                        size = Size(size.width * 0.84f, size.height * 0.84f),
                        cornerRadius = CornerRadius(2.dp.toPx()),
                        style = Stroke(width = strokeWidth)
                    )
                }
                TemplateFormat.HIGHLIGHT -> {
                    // دایره کاور هایلایت
                    drawCircle(
                        color = strokeColor,
                        radius = size.width * 0.44f,
                        style = Stroke(width = strokeWidth)
                    )
                }
            }
        }
    }
}

/**
 * ۶. کارت مدرن ۲۴dp با پیش‌نمایش تمام‌کارت و عنوان یک‌خطی تمام‌عرض روی گرادیانت تیره
 */
@Composable
private fun ModernTemplateCard(
    template: Template,
    brandProfile: BrandProfile,
    selectedFilter: FormatFilter = FormatFilter.ALL,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardSpringScale"
    )

    val previewAspectRatio = when (template.format) {
        TemplateFormat.STORY -> 9f / 16f
        TemplateFormat.POST_PORTRAIT -> 4f / 5f
        TemplateFormat.POST_SQUARE, TemplateFormat.HIGHLIGHT -> 1f / 1f
    }

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("template_card_${template.id}"),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color(0x10FFFFFF),
        borderColor = if (template.isVip) Color(0x44F5C451) else Color(0x1EFFFFFF),
        borderWidth = if (template.isVip) 1.5.dp else 1.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(previewAspectRatio)
        ) {
            // پیش‌نمایش بوم قالب تمام‌کارت
            TemplateThumbnailPreview(
                template = template,
                brandProfile = brandProfile,
                modifier = Modifier.fillMaxSize(),
                cornerRadius = 24f
            )

            // لایه گرادیانت تیره (مشکی ۰ تا ۷۵ درصد از وسط به پایین کارت برای تضمین خوانایی روی هر پس‌زمینه‌ای)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            0.45f to Color(0x55000000),
                            1.0f to Color(0xBF000000) // 75% black
                        )
                    )
            )

            // ردیف بالای کارت: آیکون ریز فرمت در حالت «همه» و تاج VIP
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // در حالت «همه» آیکون ریز فرمت روبروی تاج VIP نمایش داده می‌شود
                if (selectedFilter == FormatFilter.ALL) {
                    FormatMiniIcon(format = template.format)
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // تاج طلایی VIP در گوشه بالا
                if (template.isVip) {
                    VipCrownBadge(text = "VIP")
                }
            }

            // عنوان یک‌خطی در تمام عرض کارت با سایه ملایم روی گرادیانت تیره پایین
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = template.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.75f),
                            offset = Offset(0f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * کارت دایره‌ای اختصاصی کاور هایلایت برای گرید ۳ ستونه
 * پیش‌نمایش در CircleShape، نشان VIP روی لبه دایره، عنوان یک‌خطی ۱۲sp در زیر دایره
 */
@Composable
private fun HighlightCircleTemplateCard(
    template: Template,
    brandProfile: BrandProfile,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "highlightCircleScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("template_card_${template.id}")
            .padding(vertical = 4.dp)
    ) {
        // بوم دایره‌ای کاور هایلایت
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .aspectRatio(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(
                        width = if (template.isVip) 1.5.dp else 1.dp,
                        color = if (template.isVip) Color(0x66F5C451) else Color(0x22FFFFFF),
                        shape = CircleShape
                    )
            ) {
                TemplateThumbnailPreview(
                    template = template,
                    brandProfile = brandProfile,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 200f
                )
            }

            // نشان تاج طلایی VIP در لبه بالای دایره
            if (template.isVip) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                ) {
                    VipCrownBadge(text = "VIP")
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // عنوان یک‌خطی ۱۲sp زیر دایره وسط‌چین
        Text(
            text = template.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        )
    }
}

/**
 * اسکلت لودینگ Shimmer بدون لگ برای گرید ۲ ستونه
 */
@Composable
private fun ShimmerTemplateSkeleton() {
    val shimmer = shimmerBrush()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(24.dp))
            .background(shimmer)
            .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(24.dp))
    )
}

/**
 * اسکلت لودینگ دایره‌ای برای حالت هایلایت (گرید ۳ ستونه)
 */
@Composable
private fun ShimmerHighlightSkeleton() {
    val shimmer = shimmerBrush()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(shimmer)
                .border(1.dp, Color(0x18FFFFFF), CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmer)
        )
    }
}

/**
 * حالت خالی جستجو با پیام دوستانه
 */
@Composable
private fun EmptyTemplatesView(
    onResetFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color(0x227C5CFF))
                .border(1.dp, Color(0x447C5CFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = BrandViolet,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "قالبی با این عنوان پیدا نشد!",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "می‌توانید کلمه دیگری را جستجو کنید یا فیلتر دسته‌بندی را تغییر دهید.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryGradientBrush)
                .clickable { onResetFilters() }
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "مشاهده تمام قالب‌ها",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * باتم‌شیت شیشه‌ای نمایش جزئیات و انتخاب قالب
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateDetailBottomSheet(
    template: Template,
    brandProfile: BrandProfile,
    isVipUser: Boolean,
    onDismiss: () -> Unit,
    onSelectTemplate: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) DarkSurface else LightSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0x33FFFFFF) else Color(0x33000000))
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // پیش‌نمایش بزرگ در مرکز با درخشش ملایم و برچسب طلایی
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = if (template.isVip && !isVipUser) VipGold else Color(0x667C5CFF),
                        ambientColor = if (template.isVip && !isVipUser) VipGold else Color(0x447C5CFF)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.5.dp,
                        if (template.isVip && !isVipUser) VipGold else (if (isDark) Color(0x44FFFFFF) else Color(0x337C5CFF)),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                TemplateThumbnailPreview(
                    template = template,
                    brandProfile = brandProfile,
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24f
                )

                // نشان طلایی روی پیش‌نمایش برای قالب‌های VIP قفل
                if (template.isVip && !isVipUser) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xE62E1F00))
                            .border(1.dp, VipGold, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("👑", fontSize = 12.sp)
                            Text("طلایی VIP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VipGold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // عنوان و نشان
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = template.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                if (template.isVip) {
                    Spacer(modifier = Modifier.width(8.dp))
                    VipCrownBadge()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // چیپ‌های شیشه‌ای دسته‌بندی و فرمت
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassSurface(
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = Color(0x228B5CF6),
                    borderColor = Color(0x338B5CF6)
                ) {
                    Text(
                        text = template.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandCyan,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                GlassSurface(
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = Color(0x22EC4899),
                    borderColor = Color(0x33EC4899)
                ) {
                    Text(
                        text = "قالب ${template.format.titleFa}",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color(0xFFFDE68A) else BrandPink,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // کارت شیشه‌ای مشخصات قالب
            GlassSurface(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isDark) Color(0x18FFFFFF) else Color(0xF2FFFFFF),
                borderColor = if (isDark) Color(0x22FFFFFF) else Color(0x15000000),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("المان‌های قابل ویرایش", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            red.line.instayar.util.PersianDateUtil.toPersianDigits("${template.elements.size} المان"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(if (isDark) Color(0x22FFFFFF) else Color(0x18000000)))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("نوع دسترسی", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            if (template.isVip) "طلایی VIP 👑" else "رایگان ✨",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (template.isVip) VipGold else Color(0xFF10B981)
                        )
                    }
                }
            }

            if (template.isVip && !isVipUser) {
                Spacer(modifier = Modifier.height(12.dp))
                GlassSurface(
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = Color(0x22FBBF24),
                    borderColor = VipGold
                ) {
                    Text(
                        text = "🔒 این قالب مخصوص اعضای طلایی VIP است",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = VipGold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // دکمه شروع ویرایش
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (template.isVip && !isVipUser) {
                            VipGradientBrush
                        } else {
                            PrimaryGradientBrush
                        }
                    )
                    .clickable { onSelectTemplate() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (template.isVip && !isVipUser) {
                        "باز کردن با نسخه طلایی VIP 👑"
                    } else {
                        "شروع ویرایش این قالب ✨"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (template.isVip && !isVipUser) Color(0xFF1E1000) else Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
