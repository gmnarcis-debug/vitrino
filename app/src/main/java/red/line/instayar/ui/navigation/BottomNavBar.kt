package red.line.instayar.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FolderSpecial
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.DarkBorder
import red.line.instayar.ui.theme.GlassBorderColor
import red.line.instayar.ui.theme.GlassSurfaceBg
import red.line.instayar.ui.theme.PrimaryGradientBrush

enum class NavDestination(
    val titleFa: String,
    val icon: ImageVector,
    val testTag: String
) {
    HOME("قالب‌ها", Icons.Rounded.GridView, "nav_home"),
    PROJECTS("پروژه‌ها", Icons.Rounded.FolderSpecial, "nav_projects"),
    BRAND("برند من", Icons.Rounded.Person, "nav_brand"),
    SETTINGS("تنظیمات", Icons.Rounded.Settings, "nav_settings")
}

@Composable
fun FloatingBottomNavBar(
    currentDestination: NavDestination,
    onDestinationSelect: (NavDestination) -> Unit,
    modifier: Modifier = Modifier,
    onNewProjectClick: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val isDark = MaterialTheme.colorScheme.background == red.line.instayar.ui.theme.DarkBackground
        val navBg = if (isDark) Color(0xEE12121D) else Color(0xF5FFFFFF)
        val navBorder = if (isDark) Color(0x28FFFFFF) else Color(0x227C5CFF)

        // نوار شناور pill شیشه‌ای
        GlassSurface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = Color(0x667C5CFF),
                    ambientColor = Color(0x33000000)
                ),
            shape = RoundedCornerShape(32.dp),
            backgroundColor = navBg,
            borderColor = navBorder,
            borderWidth = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // دو تب سمت راست: HOME و PROJECTS
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavItem(
                        destination = NavDestination.HOME,
                        isSelected = currentDestination == NavDestination.HOME,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDestinationSelect(NavDestination.HOME)
                        }
                    )
                    NavItem(
                        destination = NavDestination.PROJECTS,
                        isSelected = currentDestination == NavDestination.PROJECTS,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDestinationSelect(NavDestination.PROJECTS)
                        }
                    )
                }

                // دکمه وسط گرد بزرگتر «+» با گرادیانت برند
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(50.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            spotColor = Color(0x88FF5CA8)
                        )
                        .clip(CircleShape)
                        .background(PrimaryGradientBrush)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNewProjectClick()
                        }
                        .testTag("nav_new_project_fab"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "ساخت طرح جدید",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // دو تب سمت چپ: BRAND و SETTINGS
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavItem(
                        destination = NavDestination.BRAND,
                        isSelected = currentDestination == NavDestination.BRAND,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDestinationSelect(NavDestination.BRAND)
                        }
                    )
                    NavItem(
                        destination = NavDestination.SETTINGS,
                        isSelected = currentDestination == NavDestination.SETTINGS,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDestinationSelect(NavDestination.SETTINGS)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    destination: NavDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == red.line.instayar.ui.theme.DarkBackground
    val inactiveColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
    val activeColor = if (isDark) Color.White else BrandPurple

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = spring(),
        label = "navIconColor"
    )

    val activeBg = if (isSelected) {
        Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(Color(0x337C5CFF), Color(0x33FF5CA8))
            ),
            shape = RoundedCornerShape(16.dp)
        ).border(
            width = 1.dp,
            color = Color(0x44FF5CA8),
            shape = RoundedCornerShape(16.dp)
        )
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .then(activeBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .testTag(destination.testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = destination.titleFa,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = destination.titleFa,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = iconColor,
                fontSize = 12.sp
            )
        }
    }
}
