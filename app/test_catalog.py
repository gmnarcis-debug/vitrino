#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
redesign_templates.py
Professional, individualized visual overhaul for all 85 templates in "ویترینو" (Vitrino).
Eliminates the single-layout cookie-cutter anti-pattern.
Each template receives a custom layout, palette, typography, hierarchy, and Persian copy
tailored to its category, format, and business purpose.
"""

import os
import json

TEMPLATES_DIR = "app/src/main/assets/templates"

def build_template_catalog():
    # We will define a comprehensive generator mapping for all 85 templates
    templates = []

    # =========================================================================
    # 1. HIGHLIGHT COVERS (کاورهای هایلایت - ۱۲ قالب)
    # Format: HIGHLIGHT (1:1). Must respect circular Instagram safe zone (~0.65-0.70 diameter center).
    # =========================================================================
    highlight_specs = [
        ("tpl_gen_077", "کاور هایلایت محصولات جدید", "معرفی محصول", False, "🛍️", "جدیدترین‌ها", "کالکشن جدید و ترند", "#0F172A", ["#1E293B", "#0F172A"], "#38BDF8", "#0284C7"),
        ("tpl_gen_078", "کاور هایلایت لیست قیمت", "قیمت‌نامه", False, "💰", "لیست قیمت", "تعرفه به‌روز خدمات", "#1E1B4B", ["#312E81", "#1E1B4B"], "#A855F7", "#7C3AED"),
        ("tpl_gen_079", "کاور هایلایت راهنمای خرید و سفارش", "سفارش‌گیری", False, "📦", "نحوه سفارش", "راهنمای خرید آسان", "#064E3B", ["#047857", "#064E3B"], "#34D399", "#059669"),
        ("tpl_gen_080", "کاور هایلایت نظرات و رضایت خریداران", "نظر مشتری", False, "⭐", "رضایت شما", "نظرات و پیام‌های مشتریان", "#831843", ["#9D174D", "#701A75"], "#F472B6", "#DB2777"),
        ("tpl_gen_081", "کاور هایلایت کد رهگیری پستی", "اطلاعیه", False, "🚚", "کد رهگیری", "ارسال‌های روزانه پیشتاز", "#18181B", ["#27272A", "#09090B"], "#FACC15", "#CA8A04"),
        ("tpl_gen_082", "کاور هایلایت تخفیف‌ها و حراجی‌ها", "تخفیف و حراج", True, "🔥", "تخفیف ویژه", "آفرهای فصلی و حراج", "#450A0A", ["#7F1D1D", "#450A0A"], "#FB7185", "#E11D48"),
        ("tpl_gen_083", "کاور هایلایت منوی کافه و نوشیدنی", "معرفی محصول", False, "☕", "منوی کافه", "نوشیدنی‌ها و دسرها", "#38220F", ["#583318", "#241407"], "#FBBF24", "#D97706"),
        ("tpl_gen_084", "کاور هایلایت درباره ما و آدرس", "اطلاعیه", False, "📍", "درباره ما", "شعبه‌ها و ساعات کاری", "#134E4A", ["#0F766E", "#134E4A"], "#2DD4BF", "#0D9488"),
        ("tpl_gen_085", "کاور هایلایت سوالات متداول خریداران", "معرفی محصول", False, "❓", "سوالات شما", "پاسخ به سوالات پرتکرار", "#312E81", ["#4338CA", "#1E1B4B"], "#818CF8", "#4F46E5"),
        ("tpl_highlight_about_19", "کاور هایلایت درباره ما و ساعات کاری", "اطلاعیه", True, "🏛️", "ساعات کاری", "پذیرایی و تماس", "#1C1917", ["#292524", "#0C0A09"], "#E7E5E4", "#78716C"),
        ("tpl_highlight_price_09", "کاور هایلایت تعرفه و لیست قیمت", "قیمت‌نامه", True, "🏷️", "تعرفه خدمات", "قیمت‌های مصوب", "#2E1065", ["#3B0764", "#1E1B4B"], "#C084FC", "#9333EA"),
        ("tpl_highlight_story_08", "کاور هایلایت رضایت و نظر مشتریان", "نظر مشتری", True, "💬", "پیام‌های شما", "فیدبک و تجربه خرید", "#14532D", ["#166534", "#052E16"], "#4ADE80", "#15803D"),
    ]

    for (tid, title, cat, is_vip, icon, main_txt, sub_txt, bg_col, grad, accent_col, badge_bg) in highlight_specs:
        elements = [
            # 1. Outer safe-zone framing ring (Circle)
            {
                "id": "el_hl_ring",
                "type": "SHAPE",
                "x": 0.14,
                "y": 0.14,
                "width": 0.72,
                "height": 0.72,
                "shapeType": "CIRCLE",
                "cornerRadius": 0.0,
                "backgroundColor": "#08FFFFFF",
                "borderColor": accent_col,
                "borderWidth": 2.5,
                "opacity": 0.95
            },
            # 2. Inner concentric disc
            {
                "id": "el_hl_inner_disc",
                "type": "SHAPE",
                "x": 0.22,
                "y": 0.22,
                "width": 0.56,
                "height": 0.56,
                "shapeType": "CIRCLE",
                "cornerRadius": 0.0,
                "backgroundColor": "#1AFFFFFF",
                "borderColor": "#33FFFFFF",
                "borderWidth": 1.5,
                "opacity": 0.85
            },
            # 3. Center Icon / Symbol Sticker
            {
                "id": "el_hl_icon",
                "type": "STICKER",
                "x": 0.35,
                "y": 0.30,
                "width": 0.30,
                "height": 0.18,
                "text": icon,
                "fontSize": 42.0,
                "textColor": "#FFFFFF",
                "textAlign": "CENTER"
            },
            # 4. Central Highlight Title (strictly inside safe circle)
            {
                "id": "el_hl_title",
                "type": "TEXT",
                "x": 0.18,
                "y": 0.52,
                "width": 0.64,
                "height": 0.10,
                "text": main_txt,
                "fontSize": 20.0,
                "fontWeight": "BOLD",
                "fontFamily": "sahel" if is_vip else "vazirmatn",
                "textColor": "#FFFFFF",
                "textAlign": "CENTER"
            },
            # 5. Subtle Sub-label Badge inside circle
            {
                "id": "el_hl_sub",
                "type": "BADGE",
                "x": 0.26,
                "y": 0.63,
                "width": 0.48,
                "height": 0.065,
                "text": sub_txt,
                "fontSize": 11.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#FFFFFF",
                "backgroundColor": badge_bg,
                "opacity": 0.92
            }
        ]

        templates.append({
            "id": tid,
            "title": title,
            "category": cat,
            "format": "HIGHLIGHT",
            "isVip": is_vip,
            "tags": ["هایلایت", "اینستاگرام", "کاور", cat],
            "background": {
                "type": "GRADIENT",
                "color": bg_col,
                "gradientColors": grad,
                "angle": 135.0
            },
            "elements": elements
        })

    # =========================================================================
    # 2. PRICE LIST & MENU (لیست قیمت و منو - ۵ قالب غیر هایلایت)
    # =========================================================================
    # 2.1 03_post_price: منو و لیست قیمت کافه تخصصی (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_post_price_03",
        "title": "منو و لیست قیمت کافه تخصصی",
        "category": "قیمت‌نامه",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["کافه", "منو", "قیمت", "قهوه", "لاته"],
        "background": {
            "type": "GRADIENT",
            "color": "#1C110A",
            "gradientColors": ["#2D1810", "#0E0704"],
            "angle": 150.0
        },
        "elements": [
            # Top Banner: Brand & Menu Headline
            {
                "id": "el_badge_menu",
                "type": "BADGE",
                "x": 0.30,
                "y": 0.04,
                "width": 0.40,
                "height": 0.045,
                "text": "☕ منوی پاییزه تخصصی",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FDE68A",
                "backgroundColor": "#78350F"
            },
            {
                "id": "el_title_menu",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.095,
                "width": 0.84,
                "height": 0.06,
                "text": "کافه رستوران آرتسان",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "shabnam",
                "textColor": "#FFFBEB",
                "textAlign": "CENTER"
            },
            # Menu Row 1 (Card)
            {
                "id": "el_card_1",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.17,
                "width": 0.88,
                "height": 0.13,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#291811",
                "borderColor": "#B45309",
                "borderWidth": 1.2
            },
            {
                "id": "el_item1_name",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.185,
                "width": 0.48,
                "height": 0.045,
                "text": "اسپرسو دبل آرتسان",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF3C7",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item1_desc",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.235,
                "width": 0.48,
                "height": 0.045,
                "text": "دانه ۱۰۰٪ عربیکا کلمبیا تک‌خاستگاه",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#D4D4D8",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item1_price",
                "type": "BADGE",
                "x": 0.10,
                "y": 0.20,
                "width": 0.28,
                "height": 0.065,
                "text": "۶۵,۰۰۰ تومان",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#B45309"
            },
            # Menu Row 2 (Card)
            {
                "id": "el_card_2",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.32,
                "width": 0.88,
                "height": 0.13,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#291811",
                "borderColor": "#B45309",
                "borderWidth": 1.2
            },
            {
                "id": "el_item2_name",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.335,
                "width": 0.48,
                "height": 0.045,
                "text": "کاپوچینو و کارامل لاته",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF3C7",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item2_desc",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.385,
                "width": 0.48,
                "height": 0.045,
                "text": "با سس کارامل دست‌ساز و شیر بخارداده",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#D4D4D8",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item2_price",
                "type": "BADGE",
                "x": 0.10,
                "y": 0.35,
                "width": 0.28,
                "height": 0.065,
                "text": "۸۸,۰۰۰ تومان",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#B45309"
            },
            # Menu Row 3 (Card)
            {
                "id": "el_card_3",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.47,
                "width": 0.88,
                "height": 0.13,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#291811",
                "borderColor": "#B45309",
                "borderWidth": 1.2
            },
            {
                "id": "el_item3_name",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.485,
                "width": 0.48,
                "height": 0.045,
                "text": "چیزکیک لوتوس و سن‌سباستین",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF3C7",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item3_desc",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.535,
                "width": 0.48,
                "height": 0.045,
                "text": "پخت روزانه با پنیر ماسکارپونه ارگانیک",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#D4D4D8",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item3_price",
                "type": "BADGE",
                "x": 0.10,
                "y": 0.50,
                "width": 0.28,
                "height": 0.065,
                "text": "۱۱۰,۰۰۰ تومان",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D97706"
            },
            # Menu Row 4 (Card)
            {
                "id": "el_card_4",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.62,
                "width": 0.88,
                "height": 0.13,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#291811",
                "borderColor": "#B45309",
                "borderWidth": 1.2
            },
            {
                "id": "el_item4_name",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.635,
                "width": 0.48,
                "height": 0.045,
                "text": "کروسان بیکن و تخم‌مرغ",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF3C7",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item4_desc",
                "type": "TEXT",
                "x": 0.42,
                "y": 0.685,
                "width": 0.48,
                "height": 0.045,
                "text": "صبحانه گرم با پنیر گودا و چیپس خانگی",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#D4D4D8",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_item4_price",
                "type": "BADGE",
                "x": 0.10,
                "y": 0.65,
                "width": 0.28,
                "height": 0.065,
                "text": "۱۳۵,۰۰۰ تومان",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D97706"
            },
            # Footer Note & Order
            {
                "id": "el_note",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.78,
                "width": 0.84,
                "height": 0.045,
                "text": "تمامی قیمت‌ها خالص بوده و حق سرویس دریافت نمی‌گردد.",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#A1A1AA",
                "textAlign": "CENTER"
            },
            {
                "id": "el_contact",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.86,
                "width": 0.76,
                "height": 0.07,
                "text": "سفارش بیرون‌بر و رزرو: ۰۲۱-۸۸۹۹۲۲۱۱",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#B45309",
                "textAlign": "CENTER"
            }
        ]
    })

    # 2.2 tpl_gen_053: لیست قیمت خدمات و منوی کافه (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_gen_053",
        "title": "لیست قیمت خدمات و منوی کافه",
        "category": "قیمت‌نامه",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["منو", "خدمات", "قیمت", "کافه", "صبحانه"],
        "background": {
            "type": "GRADIENT",
            "color": "#0F172A",
            "gradientColors": ["#1E293B", "#0B0F19"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.32,
                "y": 0.04,
                "width": 0.36,
                "height": 0.045,
                "text": "📋 تعرفه و پکیج‌ها",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#3B82F6"
            },
            {
                "id": "el_header",
                "type": "TEXT",
                "x": 0.06,
                "y": 0.095,
                "width": 0.88,
                "height": 0.06,
                "text": "پکیج‌های اشتراک صبحانه ماهانه",
                "fontSize": 18.0,
                "fontWeight": "BOLD",
                "fontFamily": "sahel",
                "textColor": "#F8FAFC",
                "textAlign": "CENTER"
            },
            # 3 Columnar Price Blocks
            {
                "id": "el_price_block1",
                "type": "PRICE_BLOCK",
                "x": 0.06,
                "y": 0.18,
                "width": 0.88,
                "height": 0.20,
                "fontSize": 16.0,
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "backgroundColor": "#1E293B",
                "borderColor": "#3B82F6",
                "borderWidth": 1.5,
                "oldPrice": 1400000,
                "newPrice": 1120000,
                "currencyUnit": "تومان"
            },
            {
                "id": "el_desc_plan1",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.39,
                "width": 0.80,
                "height": 0.04,
                "text": "پکیج اداری طلایی: ۲۰ وعده صبحانه گرم + قهوه دمی تازه",
                "fontSize": 11.0,
                "fontWeight": "MEDIUM",
                "textColor": "#94A3B8",
                "textAlign": "CENTER"
            },
            {
                "id": "el_price_block2",
                "type": "PRICE_BLOCK",
                "x": 0.06,
                "y": 0.46,
                "width": 0.88,
                "height": 0.20,
                "fontSize": 16.0,
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "backgroundColor": "#1E293B",
                "borderColor": "#10B981",
                "borderWidth": 1.5,
                "oldPrice": 850000,
                "newPrice": 690000,
                "currencyUnit": "تومان"
            },
            {
                "id": "el_desc_plan2",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.67,
                "width": 0.80,
                "height": 0.04,
                "text": "پکیج دانشجویی: ۱۰ وعده کروسان فرانسوی + آبمیوه طبیعی",
                "fontSize": 11.0,
                "fontWeight": "MEDIUM",
                "textColor": "#94A3B8",
                "textAlign": "CENTER"
            },
            {
                "id": "el_footer",
                "type": "BADGE",
                "x": 0.15,
                "y": 0.80,
                "width": 0.70,
                "height": 0.08,
                "text": "ثبت سفارش سازمانی و شرکتی در دایرکت",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#2563EB"
            }
        ]
    })

    # 2.3 tpl_gen_054: تعرفه طلا و سکه روز (STORY 9:16)
    templates.append({
        "id": "tpl_gen_054",
        "title": "تعرفه طلا و سکه روز",
        "category": "قیمت‌نامه",
        "format": "STORY",
        "isVip": True,
        "tags": ["طلا", "سکه", "تعرفه", "قیمت_روز", "طلافروشی"],
        "background": {
            "type": "GRADIENT",
            "color": "#1C1917",
            "gradientColors": ["#292524", "#0C0A09"],
            "angle": 135.0
        },
        "elements": [
            # Header
            {
                "id": "el_logo",
                "type": "BRAND_LOGO",
                "x": 0.42,
                "y": 0.04,
                "width": 0.16,
                "height": 0.09
            },
            {
                "id": "el_header",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.14,
                "width": 0.84,
                "height": 0.05,
                "text": "تابلوی نرخ رسمی طلا و مسکوکات",
                "fontSize": 19.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#FBBF24",
                "textAlign": "CENTER"
            },
            {
                "id": "el_date",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.195,
                "width": 0.44,
                "height": 0.035,
                "text": "به‌روزرسانی رسمی ساعت ۱۲:۰۰",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#1C1917",
                "backgroundColor": "#FDE68A"
            },
            # Row 1: طلای ۱۸ عیار
            {
                "id": "el_row1_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.25,
                "width": 0.84,
                "height": 0.09,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#292524",
                "borderColor": "#CA8A04",
                "borderWidth": 1.2
            },
            {
                "id": "el_row1_title",
                "type": "TEXT",
                "x": 0.50,
                "y": 0.275,
                "width": 0.38,
                "height": 0.04,
                "text": "طلای ۱۸ عیار (هر گرم)",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_row1_val",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.275,
                "width": 0.36,
                "height": 0.04,
                "text": "۴,۳۸۰,۰۰۰ تومان",
                "fontSize": 14.0,
                "fontWeight": "BLACK",
                "textColor": "#FFFFFF",
                "textAlign": "LEFT"
            },
            # Row 2: سکه امامی
            {
                "id": "el_row2_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.36,
                "width": 0.84,
                "height": 0.09,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#292524",
                "borderColor": "#CA8A04",
                "borderWidth": 1.2
            },
            {
                "id": "el_row2_title",
                "type": "TEXT",
                "x": 0.50,
                "y": 0.385,
                "width": 0.38,
                "height": 0.04,
                "text": "سکه تمام بهار آزادی",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_row2_val",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.385,
                "width": 0.36,
                "height": 0.04,
                "text": "۵۱,۲۰۰,۰۰۰ تومان",
                "fontSize": 14.0,
                "fontWeight": "BLACK",
                "textColor": "#FFFFFF",
                "textAlign": "LEFT"
            },
            # Row 3: نیم سکه
            {
                "id": "el_row3_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.47,
                "width": 0.84,
                "height": 0.09,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#292524",
                "borderColor": "#CA8A04",
                "borderWidth": 1.2
            },
            {
                "id": "el_row3_title",
                "type": "TEXT",
                "x": 0.50,
                "y": 0.495,
                "width": 0.38,
                "height": 0.04,
                "text": "نیم سکه بهار آزادی",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_row3_val",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.495,
                "width": 0.36,
                "height": 0.04,
                "text": "۲۷,۵۰۰,۰۰۰ تومان",
                "fontSize": 14.0,
                "fontWeight": "BLACK",
                "textColor": "#FFFFFF",
                "textAlign": "LEFT"
            },
            # Row 4: ربع سکه
            {
                "id": "el_row4_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.58,
                "width": 0.84,
                "height": 0.09,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#292524",
                "borderColor": "#CA8A04",
                "borderWidth": 1.2
            },
            {
                "id": "el_row4_title",
                "type": "TEXT",
                "x": 0.50,
                "y": 0.605,
                "width": 0.38,
                "height": 0.04,
                "text": "ربع سکه بهار آزادی",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_row4_val",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.605,
                "width": 0.36,
                "height": 0.04,
                "text": "۱۷,۴۰۰,۰۰۰ تومان",
                "fontSize": 14.0,
                "fontWeight": "BLACK",
                "textColor": "#FFFFFF",
                "textAlign": "LEFT"
            },
            # Jewelry Image Thumbnail
            {
                "id": "el_jewel_thumb",
                "type": "IMAGE",
                "x": 0.30,
                "y": 0.69,
                "width": 0.40,
                "height": 0.14,
                "imageAsset": "jewelry_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 18.0,
                "borderColor": "#FBBF24",
                "borderWidth": 1.5
            },
            # Footer
            {
                "id": "el_trust",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.845,
                "width": 0.80,
                "height": 0.04,
                "text": "دارای فاکتور رسمی معتبر و پروانه اتحادیه طلا و جواهر",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#A8A29E",
                "textAlign": "CENTER"
            },
            {
                "id": "el_contact",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.895,
                "width": 0.70,
                "height": 0.05,
                "text": "مشاوره و خرید غیرحضوری در دایرکت",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#1C1917",
                "backgroundColor": "#FBBF24"
            }
        ]
    })

    # 2.4 tpl_gen_055: تعرفه بسته‌های هدیه گل و باکس (POST_PORTRAIT 4:5)
    templates.append({
        "id": "tpl_gen_055",
        "title": "تعرفه بسته‌های هدیه گل و باکس",
        "category": "قیمت‌نامه",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["گل", "باکس", "قیمت", "هدیه", "رز"],
        "background": {
            "type": "GRADIENT",
            "color": "#3B0764",
            "gradientColors": ["#581C87", "#2E1065"],
            "angle": 135.0
        },
        "elements": [
            # Top Banner: Split Image + Title
            {
                "id": "el_flower_img",
                "type": "IMAGE",
                "x": 0.08,
                "y": 0.05,
                "width": 0.36,
                "height": 0.22,
                "imageAsset": "flower_gift_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 20.0,
                "borderColor": "#E879F9",
                "borderWidth": 1.5
            },
            {
                "id": "el_header_badge",
                "type": "BADGE",
                "x": 0.48,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "🌸 گالری گل و سورپرایز",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#C026D3"
            },
            {
                "id": "el_title_flower",
                "type": "TEXT",
                "x": 0.48,
                "y": 0.10,
                "width": 0.44,
                "height": 0.09,
                "text": "تعرفه پکیج‌های اختصاصی گل رز",
                "fontSize": 16.0,
                "fontWeight": "BOLD",
                "fontFamily": "samim",
                "textColor": "#FDF4FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_sub_flower",
                "type": "TEXT",
                "x": 0.48,
                "y": 0.20,
                "width": 0.44,
                "height": 0.06,
                "text": "گل‌های طبیعی تازه با ضمانت طراوت ۵ روزه",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#E9D5FF",
                "textAlign": "RIGHT"
            },
            # Package 1
            {
                "id": "el_pkg1_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.30,
                "width": 0.84,
                "height": 0.16,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#4C1D95",
                "borderColor": "#C084FC",
                "borderWidth": 1.2
            },
            {
                "id": "el_pkg1_title",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.32,
                "width": 0.48,
                "height": 0.045,
                "text": "باکس مینیاتوری عشق (۱۰ شاخه)",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FDF4FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_pkg1_desc",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.38,
                "width": 0.48,
                "height": 0.06,
                "text": "گل رز هلندی درجه یک + کارت‌پستال دست‌نویس",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#E9D5FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_pkg1_price",
                "type": "BADGE",
                "x": 0.12,
                "y": 0.34,
                "width": 0.26,
                "height": 0.07,
                "text": "۴۸۰,۰۰۰ ت",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#A855F7"
            },
            # Package 2
            {
                "id": "el_pkg2_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.49,
                "width": 0.84,
                "height": 0.16,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#4C1D95",
                "borderColor": "#C084FC",
                "borderWidth": 1.2
            },
            {
                "id": "el_pkg2_title",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.51,
                "width": 0.48,
                "height": 0.045,
                "text": "باکس استوانه‌ای لاکچری (۲۵ شاخه)",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FDF4FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_pkg2_desc",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.57,
                "width": 0.48,
                "height": 0.06,
                "text": "رز هلندی قرمز و سفید + تزئین روبان ابریشمی",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#E9D5FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_pkg2_price",
                "type": "BADGE",
                "x": 0.12,
                "y": 0.53,
                "width": 0.26,
                "height": 0.07,
                "text": "۹۵۰,۰۰۰ ت",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#A855F7"
            },
            # Package 3: VIP
            {
                "id": "el_pkg3_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.68,
                "width": 0.84,
                "height": 0.16,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#581C87",
                "borderColor": "#E879F9",
                "borderWidth": 1.8
            },
            {
                "id": "el_pkg3_title",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.70,
                "width": 0.48,
                "height": 0.045,
                "text": "جام گل لوکس سوپرایز VIP (۵۰ شاخه)",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_pkg3_desc",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.76,
                "width": 0.48,
                "height": 0.06,
                "text": "جام فلزی پایه‌دار با ارکیده و رز ممتاز",
                "fontSize": 10.0,
                "fontWeight": "NORMAL",
                "textColor": "#E9D5FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_pkg3_price",
                "type": "BADGE",
                "x": 0.12,
                "y": 0.72,
                "width": 0.26,
                "height": 0.07,
                "text": "۱,۸۵۰,۰۰۰ ت",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#C026D3"
            },
            # Footer CTA
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.88,
                "width": 0.76,
                "height": 0.07,
                "text": "سفارش و تحویل فوری ۲ ساعته در تهران",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D946EF"
            }
        ]
    })

    # 2.5 tpl_gen_056: قیمت خدمات زیبایی و مراقبت پوست (STORY 9:16)
    templates.append({
        "id": "tpl_gen_056",
        "title": "قیمت خدمات زیبایی و مراقبت پوست",
        "category": "قیمت‌نامه",
        "format": "STORY",
        "isVip": True,
        "tags": ["پوست", "زیبایی", "فیشیال", "تعرفه", "کلینیک"],
        "background": {
            "type": "GRADIENT",
            "color": "#1E1B4B",
            "gradientColors": ["#2E1065", "#0F0D24"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.04,
                "width": 0.44,
                "height": 0.035,
                "text": "✨ کلینیک زیبایی و اسکین‌کر",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#8B5CF6"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.09,
                "width": 0.84,
                "height": 0.055,
                "text": "تعرفه خدمات فیشیال و جوانسازی",
                "fontSize": 19.0,
                "fontWeight": "BOLD",
                "fontFamily": "sahel",
                "textColor": "#F3E8FF",
                "textAlign": "CENTER"
            },
            # Service 1
            {
                "id": "el_serv1",
                "type": "PRICE_BLOCK",
                "x": 0.08,
                "y": 0.16,
                "width": 0.84,
                "height": 0.15,
                "fontSize": 15.0,
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "backgroundColor": "#231B42",
                "borderColor": "#A855F7",
                "borderWidth": 1.2,
                "oldPrice": 850000,
                "newPrice": 590000,
                "currencyUnit": "تومان"
            },
            {
                "id": "el_serv1_desc",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.32,
                "width": 0.76,
                "height": 0.035,
                "text": "فیشیال تخصصی VIP: پاکسازی عمقی + آبرسانی هیالورونیک + ماسک طلا",
                "fontSize": 10.0,
                "fontWeight": "MEDIUM",
                "textColor": "#C4B5FD",
                "textAlign": "CENTER"
            },
            # Service 2
            {
                "id": "el_serv2",
                "type": "PRICE_BLOCK",
                "x": 0.08,
                "y": 0.37,
                "width": 0.84,
                "height": 0.15,
                "fontSize": 15.0,
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "backgroundColor": "#231B42",
                "borderColor": "#EC4899",
                "borderWidth": 1.2,
                "oldPrice": 1200000,
                "newPrice": 890000,
                "currencyUnit": "تومان"
            },
            {
                "id": "el_serv2_desc",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.53,
                "width": 0.76,
                "height": 0.035,
                "text": "میکرودرم ابریژن و لایه‌برداری: رفع لک و منافذ باز پوست",
                "fontSize": 10.0,
                "fontWeight": "MEDIUM",
                "textColor": "#FBCFE8",
                "textAlign": "CENTER"
            },
            # Service 3
            {
                "id": "el_serv3",
                "type": "PRICE_BLOCK",
                "x": 0.08,
                "y": 0.58,
                "width": 0.84,
                "height": 0.15,
                "fontSize": 15.0,
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "backgroundColor": "#231B42",
                "borderColor": "#38BDF8",
                "borderWidth": 1.2,
                "oldPrice": 1600000,
                "newPrice": 1250000,
                "currencyUnit": "تومان"
            },
            {
                "id": "el_serv3_desc",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.74,
                "width": 0.76,
                "height": 0.035,
                "text": "مزوتراپی جوانسازی: تحریک کلاژن‌سازی و لیفت طبیعی صورت",
                "fontSize": 10.0,
                "fontWeight": "MEDIUM",
                "textColor": "#BAE6FD",
                "textAlign": "CENTER"
            },
            # Beauty Thumbnail
            {
                "id": "el_thumb",
                "type": "IMAGE",
                "x": 0.35,
                "y": 0.79,
                "width": 0.30,
                "height": 0.09,
                "imageAsset": "beauty_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 16.0
            },
            # Footer CTA
            {
                "id": "el_footer_cta",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.90,
                "width": 0.70,
                "height": 0.055,
                "text": "رزرو وقت و مشاوره رایگان با پزشک",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#7C3AED"
            }
        ]
    })

    # =========================================================================
    # 3. CUSTOMER REVIEWS (نظرات و رضایت مشتری - ۵ قالب غیر هایلایت)
    # =========================================================================
    # 3.1 tpl_story_review_05: رضایت و بازخورد مشتریان وفادار (STORY 9:16)
    templates.append({
        "id": "tpl_story_review_05",
        "title": "رضایت و بازخورد مشتریان وفادار",
        "category": "نظر مشتری",
        "format": "STORY",
        "isVip": False,
        "tags": ["رضایت", "نظر", "مشتری", "فیدبک", "دایرکت"],
        "background": {
            "type": "GRADIENT",
            "color": "#0F172A",
            "gradientColors": ["#1E293B", "#0F172A"],
            "angle": 135.0
        },
        "elements": [
            # Top Customer Header
            {
                "id": "el_rev_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "⭐ پیام‌های پرمهر شما",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#E11D48"
            },
            # Direct Message Chat Bubble Card
            {
                "id": "el_chat_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.12,
                "width": 0.84,
                "height": 0.38,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 22.0,
                "backgroundColor": "#1E293B",
                "borderColor": "#38BDF8",
                "borderWidth": 1.5
            },
            {
                "id": "el_buyer_handle",
                "type": "TEXT",
                "x": 0.48,
                "y": 0.145,
                "width": 0.38,
                "height": 0.035,
                "text": "@mahsa_lifestyle",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#38BDF8",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_buyer_badge",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.145,
                "width": 0.30,
                "height": 0.03,
                "text": "خریدار تایید شده ✓",
                "fontSize": 9.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#059669"
            },
            # Quotation Glyphs
            {
                "id": "el_quote_mark",
                "type": "TEXT",
                "x": 0.78,
                "y": 0.19,
                "width": 0.10,
                "height": 0.05,
                "text": "«",
                "fontSize": 32.0,
                "fontWeight": "BLACK",
                "textColor": "#F43F5E",
                "textAlign": "CENTER"
            },
            {
                "id": "el_chat_content",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.23,
                "width": 0.76,
                "height": 0.18,
                "text": "سلام عزیزم خسته نباشید، پالتو و کیفی که سفارش داده بودم امروز رسید. واقعاً از عکسش هم قشنگ‌تره! جنس چرم کیف بی‌نظیره و دوخت پالتو کاملاً تمیز و مزونیه. ممنون از بسته‌بندی محترمانه و ارسال به موقع‌تون ❤️",
                "fontSize": 13.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "shabnam",
                "textColor": "#F8FAFC",
                "textAlign": "RIGHT"
            },
            # Rating Stars inside bubble
            {
                "id": "el_stars",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.42,
                "width": 0.44,
                "height": 0.045,
                "text": "⭐⭐⭐⭐⭐ امتیاز ۵ از ۵",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "backgroundColor": "#0F172A"
            },
            # Real Product Customer Proof Photo
            {
                "id": "el_proof_img",
                "type": "IMAGE",
                "x": 0.12,
                "y": 0.53,
                "width": 0.76,
                "height": 0.30,
                "imageAsset": "bag_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 20.0,
                "borderColor": "#F43F5E",
                "borderWidth": 2.0
            },
            {
                "id": "el_caption",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.85,
                "width": 0.80,
                "height": 0.04,
                "text": "عکس ارسالی مشتری عزیزمون از سفارش کیف چرم دست‌دوز",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#CBD5E1",
                "textAlign": "CENTER"
            },
            # Footer Trust Badge
            {
                "id": "el_trust_footer",
                "type": "BRAND_NAME",
                "x": 0.15,
                "y": 0.90,
                "width": 0.70,
                "height": 0.05,
                "text": "بوتیک مد روز | لبخند شما هدف ماست",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#E11D48",
                "textAlign": "CENTER"
            }
        ]
    })

    # 3.2 tpl_story_thankyou_18: تشکر از خرید و اعتماد همراهان گرامی (STORY 9:16)
    templates.append({
        "id": "tpl_story_thankyou_18",
        "title": "تشکر از خرید و اعتماد همراهان گرامی",
        "category": "نظر مشتری",
        "format": "STORY",
        "isVip": True,
        "tags": ["تشکر", "قدردانی", "اعتماد", "مشتری", "همراهی"],
        "background": {
            "type": "GRADIENT",
            "color": "#18181B",
            "gradientColors": ["#27272A", "#09090B"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_logo",
                "type": "BRAND_LOGO",
                "x": 0.42,
                "y": 0.06,
                "width": 0.16,
                "height": 0.09
            },
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.30,
                "y": 0.17,
                "width": 0.40,
                "height": 0.035,
                "text": "💌 پیام قدردانی",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D97706"
            },
            {
                "id": "el_headline",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.22,
                "width": 0.84,
                "height": 0.07,
                "text": "یک دنیا سپاس از اعتماد ارزشمند شما",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#FEF3C7",
                "textAlign": "CENTER"
            },
            # Elegant Letter Card
            {
                "id": "el_letter_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.31,
                "width": 0.84,
                "height": 0.42,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 20.0,
                "backgroundColor": "#27272A",
                "borderColor": "#FBBF24",
                "borderWidth": 1.5
            },
            {
                "id": "el_letter_body",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.35,
                "width": 0.76,
                "height": 0.24,
                "text": "همراهان و مشتریان عزیز مجموعه؛\nهر بسته‌ای که از فروشگاه ما راهی خانه شما می‌شود، نتیجه تلاش، دقت و عشق به کیفیت است.\nاز اینکه ما را برای خریدهای خاص و ماندگارتان انتخاب می‌کنید صمیمانه سپاسگزاریم.\nکد تخفیف اختصاصی خرید بعدی تقدیم دایرکت شما شد.",
                "fontSize": 13.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "shabnam",
                "textColor": "#E4E4E7",
                "textAlign": "RIGHT"
            },
            # Gift Voucher Pill
            {
                "id": "el_voucher",
                "type": "BADGE",
                "x": 0.16,
                "y": 0.62,
                "width": 0.68,
                "height": 0.07,
                "text": "🎁 هدیه ما: ۱۵٪ تخفیف روی سفارش بعدی",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#18181B",
                "backgroundColor": "#FDE68A"
            },
            # Jewelry / Gift Preview Image
            {
                "id": "el_gift_img",
                "type": "IMAGE",
                "x": 0.32,
                "y": 0.75,
                "width": 0.36,
                "height": 0.13,
                "imageAsset": "jewelry_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 16.0
            },
            {
                "id": "el_brand_footer",
                "type": "BRAND_NAME",
                "x": 0.15,
                "y": 0.90,
                "width": 0.70,
                "height": 0.045,
                "text": "فروشگاه آنلاین من | همواره در کنار شما",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#A1A1AA",
                "textAlign": "CENTER"
            }
        ]
    })

    # 3.3 tpl_gen_060: رضایت مشتری از کیفیت لباس و دوخت (STORY 9:16)
    templates.append({
        "id": "tpl_gen_060",
        "title": "رضایت مشتری از کیفیت لباس و دوخت",
        "category": "نظر مشتری",
        "format": "STORY",
        "isVip": False,
        "tags": ["رضایت", "لباس", "کیفیت", "دوخت", "پوشاک"],
        "background": {
            "type": "GRADIENT",
            "color": "#3B0D14",
            "gradientColors": ["#4C0519", "#1C050B"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "👗 رضایت مشتریان مزون",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#BE123C"
            },
            {
                "id": "el_photo_buyer",
                "type": "IMAGE",
                "x": 0.10,
                "y": 0.11,
                "width": 0.80,
                "height": 0.42,
                "imageAsset": "fashion_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 22.0,
                "borderColor": "#FB7185",
                "borderWidth": 2.0
            },
            # Overlay Testimonial Card on bottom half
            {
                "id": "el_card_quote",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.56,
                "width": 0.84,
                "height": 0.30,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 20.0,
                "backgroundColor": "#1C050B",
                "borderColor": "#F43F5E",
                "borderWidth": 1.2
            },
            {
                "id": "el_customer_name",
                "type": "TEXT",
                "x": 0.45,
                "y": 0.585,
                "width": 0.42,
                "height": 0.035,
                "text": "سرکار خانم صادقی (اصفهان)",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FDA4AF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_rating_stars",
                "type": "BADGE",
                "x": 0.12,
                "y": 0.585,
                "width": 0.30,
                "height": 0.03,
                "text": "⭐⭐⭐⭐⭐",
                "fontSize": 10.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "backgroundColor": "#881337"
            },
            {
                "id": "el_review_text",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.635,
                "width": 0.76,
                "height": 0.14,
                "text": "«کت فوتر پاییزه دقیقاً طبق جدول سایزبندی سایزم شد. جنس پارچه‌ش بدون پرزدهی و آسترکشیش واقعاً حرفه‌ایه. دست مریزاد به خیاط‌های هنرمندتون.»",
                "fontSize": 12.5,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#FFF1F2",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.89,
                "width": 0.70,
                "height": 0.055,
                "text": "ثبت سفارش کالکشن پاییزه در دایرکت",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#E11D48"
            }
        ]
    })

    # 3.4 tpl_gen_061: عکس ارسالی مشتری از کیک تولد (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_gen_061",
        "title": "عکس ارسالی مشتری از کیک تولد",
        "category": "نظر مشتری",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["کیک", "تولد", "قنادی", "مشتری", "شیرینی"],
        "background": {
            "type": "GRADIENT",
            "color": "#2E1065",
            "gradientColors": ["#3B0764", "#1E1B4B"],
            "angle": 135.0
        },
        "elements": [
            # Side-by-side Asymmetric Layout
            {
                "id": "el_cake_img",
                "type": "IMAGE",
                "x": 0.06,
                "y": 0.10,
                "width": 0.44,
                "height": 0.80,
                "imageAsset": "cake_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 20.0,
                "borderColor": "#E879F9",
                "borderWidth": 1.5
            },
            # Right Review Card Panel
            {
                "id": "el_card_right",
                "type": "SHAPE",
                "x": 0.53,
                "y": 0.10,
                "width": 0.41,
                "height": 0.80,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 20.0,
                "backgroundColor": "#1E1B4B",
                "borderColor": "#C084FC",
                "borderWidth": 1.5
            },
            {
                "id": "el_tag",
                "type": "BADGE",
                "x": 0.56,
                "y": 0.14,
                "width": 0.35,
                "height": 0.05,
                "text": "🎂 جشن تولد ۲۵ سالگی",
                "fontSize": 10.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#A855F7"
            },
            {
                "id": "el_stars",
                "type": "TEXT",
                "x": 0.56,
                "y": 0.22,
                "width": 0.35,
                "height": 0.04,
                "text": "⭐⭐⭐⭐⭐",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "CENTER"
            },
            {
                "id": "el_customer_quote",
                "type": "TEXT",
                "x": 0.55,
                "y": 0.28,
                "width": 0.37,
                "height": 0.40,
                "text": "«طعم شکلات بلژیکی کیک فوق‌العاده لذیذ و سبک بود و مهمون‌ها کلی تعریف کردن. ممنون که تولدمون رو شیرین‌تر کردید!»\n\n— سمانه رضایی",
                "fontSize": 11.5,
                "fontWeight": "MEDIUM",
                "fontFamily": "samim",
                "textColor": "#F3E8FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_order_btn",
                "type": "BADGE",
                "x": 0.55,
                "y": 0.78,
                "width": 0.37,
                "height": 0.08,
                "text": "سفارش کیک دلخواه شما",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D946EF"
            }
        ]
    })

    # 3.5 tpl_gen_062: انباکسینگ و نظر خریدار عطر (STORY 9:16)
    templates.append({
        "id": "tpl_gen_062",
        "title": "انباکسینگ و نظر خریدار عطر",
        "category": "نظر مشتری",
        "format": "STORY",
        "isVip": False,
        "tags": ["عطر", "انباکسینگ", "رضایت", "ادکلن", "ماندگاری"],
        "background": {
            "type": "GRADIENT",
            "color": "#1C1917",
            "gradientColors": ["#292524", "#0C0A09"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "✨ انباکسینگ و بازخورد عطر",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#B45309"
            },
            {
                "id": "el_perfume_img",
                "type": "IMAGE",
                "x": 0.12,
                "y": 0.11,
                "width": 0.76,
                "height": 0.40,
                "imageAsset": "perfume_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 22.0,
                "borderColor": "#FBBF24",
                "borderWidth": 1.5
            },
            # Review Card
            {
                "id": "el_card_eval",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.54,
                "width": 0.84,
                "height": 0.32,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 20.0,
                "backgroundColor": "#292524",
                "borderColor": "#F59E0B",
                "borderWidth": 1.2
            },
            {
                "id": "el_eval_title",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.565,
                "width": 0.76,
                "height": 0.04,
                "text": "تجربه استفاده: ادکلن تام‌فورد عود وود",
                "fontSize": 14.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF3C7",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_ratings_list",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.615,
                "width": 0.76,
                "height": 0.15,
                "text": "• پخش بو: بسیار عالی و ماندگار (۸ ساعت)\n• کیفیت اسانس: ۱۰۰٪ اورجینال بدون بوی الکل\n• بسته‌بندی: پلمپ شرکتی با هولوگرام اصالت\n«خرید سومم از این پیجه و بازم راضی بودم.»",
                "fontSize": 12.0,
                "fontWeight": "NORMAL",
                "fontFamily": "shabnam",
                "textColor": "#E7E5E4",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_verified",
                "type": "BADGE",
                "x": 0.25,
                "y": 0.78,
                "width": 0.50,
                "height": 0.045,
                "text": "ضمانت اصالت و بازگشت وجه ✓",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#1C1917",
                "backgroundColor": "#FDE68A"
            },
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.89,
                "width": 0.70,
                "height": 0.055,
                "text": "مشاوره تخصصی انتخاب عطر در دایرکت",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D97706"
            }
        ]
    })

    # =========================================================================
    # 4. ANNOUNCEMENTS & HOURS (اطلاعیه و ساعت کاری - ۵ قالب غیر هایلایت)
    # =========================================================================
    # 4.1 tpl_story_announcement_04: اطلاعیه مهم ارسال سفارشات پایان سال (STORY 9:16)
    templates.append({
        "id": "tpl_story_announcement_04",
        "title": "اطلاعیه مهم ارسال سفارشات پایان سال",
        "category": "اطلاعیه",
        "format": "STORY",
        "isVip": False,
        "tags": ["اطلاعیه", "ارسال", "تعطیلات", "پست", "مهم"],
        "background": {
            "type": "GRADIENT",
            "color": "#1E1B4B",
            "gradientColors": ["#312E81", "#0F0D24"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_alert_badge",
                "type": "BADGE",
                "x": 0.25,
                "y": 0.06,
                "width": 0.50,
                "height": 0.045,
                "text": "📢 اطلاعیه فوری و مهم",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#EF4444"
            },
            {
                "id": "el_headline",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.125,
                "width": 0.84,
                "height": 0.07,
                "text": "آخرین مهلت ثبت سفارش سال ۱۴۰۴",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "textAlign": "CENTER"
            },
            # Notice Box Card with double border
            {
                "id": "el_notice_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.22,
                "width": 0.84,
                "height": 0.50,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 22.0,
                "backgroundColor": "#1E1B4B",
                "borderColor": "#818CF8",
                "borderWidth": 1.5
            },
            {
                "id": "el_calendar_badge",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.25,
                "width": 0.72,
                "height": 0.05,
                "text": "🗓️ مهلت ارسال پستی: تا ۲۵ اسفندماه",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#4338CA"
            },
            {
                "id": "el_points",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.33,
                "width": 0.76,
                "height": 0.28,
                "text": "به اطلاع همراهان گرامی می‌رساند:\n\n۱. سفارش‌های ثبت‌شده تا ۲۵ اسفند با پست پیشتاز ارسال و قطعاً تا قبل از سال نو تحویل داده می‌شوند.\n\n۲. ارسال‌های شهر تهران تا ۲۸ اسفند با پیک اختصاصی فعال است.\n\n۳. سفارش‌های ثبت‌شده در ایام تعطیلات، از ۵ فروردین ارسال خواهند شد.",
                "fontSize": 13.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#E0E7FF",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_warning_pill",
                "type": "BADGE",
                "x": 0.16,
                "y": 0.63,
                "width": 0.68,
                "height": 0.055,
                "text": "⚠️ لطفاً ثبت سفارش را به روزهای پایانی موکول نکنید",
                "fontSize": 10.5,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "backgroundColor": "#7F1D1D"
            },
            # Brand Footer
            {
                "id": "el_brand_name",
                "type": "BRAND_NAME",
                "x": 0.15,
                "y": 0.77,
                "width": 0.70,
                "height": 0.045,
                "text": "فروشگاه آنلاین من | پشتیبانی ۲۴ ساعته",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#C7D2FE",
                "textAlign": "CENTER"
            },
            {
                "id": "el_contact",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.85,
                "width": 0.76,
                "height": 0.065,
                "text": "پاسخگویی در دایرکت و واتساپ: ۰۹۱۲۳۴۵۶۷۸۹",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#4F46E5"
            }
        ]
    })

    # 4.2 tpl_post_faq_15: پاسخ به سوالات متداول مشتریان (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_post_faq_15",
        "title": "پاسخ به سوالات متداول مشتریان",
        "category": "اطلاعیه",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["سوالات", "پاسخ", "خرید", "ارسال", "گارانتی"],
        "background": {
            "type": "GRADIENT",
            "color": "#064E3B",
            "gradientColors": ["#047857", "#064E3B"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.045,
                "text": "💡 سوالات متداول شما",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#059669"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.11,
                "width": 0.84,
                "height": 0.06,
                "text": "پاسخ به ۳ سوال پرتکرار خریداران",
                "fontSize": 18.0,
                "fontWeight": "BLACK",
                "fontFamily": "shabnam",
                "textColor": "#ECFDF5",
                "textAlign": "CENTER"
            },
            # FAQ 1
            {
                "id": "el_faq1_card",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.19,
                "width": 0.88,
                "height": 0.21,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#064E3B",
                "borderColor": "#34D399",
                "borderWidth": 1.2
            },
            {
                "id": "el_q1",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.21,
                "width": 0.80,
                "height": 0.045,
                "text": "❓ آیا امکان تعویض سایز یا مدل وجود دارد؟",
                "fontSize": 12.5,
                "fontWeight": "BOLD",
                "textColor": "#A7F3D0",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_a1",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.265,
                "width": 0.80,
                "height": 0.11,
                "text": "بله! تا ۴۸ ساعت پس از دریافت بسته در صورت عدم استفاده و سالم بودن اتیکت، بدون هیچ هزینه‌ای تعویض انجام می‌شود.",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "fontFamily": "vazirmatn",
                "textColor": "#F0FDF4",
                "textAlign": "RIGHT"
            },
            # FAQ 2
            {
                "id": "el_faq2_card",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.43,
                "width": 0.88,
                "height": 0.21,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#064E3B",
                "borderColor": "#34D399",
                "borderWidth": 1.2
            },
            {
                "id": "el_q2",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.45,
                "width": 0.80,
                "height": 0.045,
                "text": "❓ سفارش‌ها چند روزه به دستمان می‌رسد؟",
                "fontSize": 12.5,
                "fontWeight": "BOLD",
                "textColor": "#A7F3D0",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_a2",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.505,
                "width": 0.80,
                "height": 0.11,
                "text": "تهران همان‌روز با اسنپ‌باکس ارسال می‌شود و برای سایر شهرها ۲ الی ۳ روز کاری با پست پیشتاز تحویل داده می‌شود.",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "fontFamily": "vazirmatn",
                "textColor": "#F0FDF4",
                "textAlign": "RIGHT"
            },
            # FAQ 3
            {
                "id": "el_faq3_card",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.67,
                "width": 0.88,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#064E3B",
                "borderColor": "#34D399",
                "borderWidth": 1.2
            },
            {
                "id": "el_q3",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.69,
                "width": 0.80,
                "height": 0.045,
                "text": "❓ نحوه پرداخت به چه صورت است؟",
                "fontSize": 12.5,
                "fontWeight": "BOLD",
                "textColor": "#A7F3D0",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_a3",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.745,
                "width": 0.80,
                "height": 0.09,
                "text": "پرداخت از طریق درگاه امن زرین‌پال یا کارت‌به‌کارت با ارسال فیش معتبر انجام می‌پذیرد.",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "fontFamily": "vazirmatn",
                "textColor": "#F0FDF4",
                "textAlign": "RIGHT"
            },
            # Footer CTA
            {
                "id": "el_cta",
                "type": "BADGE",
                "x": 0.15,
                "y": 0.88,
                "width": 0.70,
                "height": 0.07,
                "text": "سوال دیگری دارید؟ در کامنت‌ها بپرسید",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#064E3B",
                "backgroundColor": "#A7F3D0"
            }
        ]
    })

    # 4.3 tpl_gen_057: ساعات کاری و روزهای تعطیل (STORY 9:16)
    templates.append({
        "id": "tpl_gen_057",
        "title": "ساعات کاری و روزهای تعطیل",
        "category": "اطلاعیه",
        "format": "STORY",
        "isVip": True,
        "tags": ["ساعت_کاری", "پذیرایی", "تعطیل", "کافه", "فروشگاه"],
        "background": {
            "type": "GRADIENT",
            "color": "#18181B",
            "gradientColors": ["#27272A", "#09090B"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_logo",
                "type": "BRAND_LOGO",
                "x": 0.42,
                "y": 0.05,
                "width": 0.16,
                "height": 0.09
            },
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.16,
                "width": 0.44,
                "height": 0.04,
                "text": "⏰ برنامه هفتگی مجموعه",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#D97706"
            },
            {
                "id": "el_title",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.22,
                "width": 0.84,
                "height": 0.06,
                "text": "ساعات پذیرایی و فعالیت حضوری",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#FEF3C7",
                "textAlign": "CENTER"
            },
            # Schedule Card
            {
                "id": "el_card_schedule",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.30,
                "width": 0.84,
                "height": 0.44,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 22.0,
                "backgroundColor": "#27272A",
                "borderColor": "#FBBF24",
                "borderWidth": 1.5
            },
            # Row 1
            {
                "id": "el_row1_badge",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.33,
                "width": 0.72,
                "height": 0.06,
                "text": "شنبه تا چهارشنبه: ۱۰:۰۰ الی ۲۲:۰۰",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#18181B",
                "backgroundColor": "#FDE68A"
            },
            {
                "id": "el_row1_sub",
                "type": "TEXT",
                "x": 0.14,
                "y": 0.40,
                "width": 0.72,
                "height": 0.035,
                "text": "پذیرایی کامل سالن و ارسال فوری سفارشات",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#D4D4D8",
                "textAlign": "CENTER"
            },
            # Row 2
            {
                "id": "el_row2_badge",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.46,
                "width": 0.72,
                "height": 0.06,
                "text": "پنج‌شنبه‌ها: ۱۰:۰۰ الی ۲۳:۳۰",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#B45309"
            },
            {
                "id": "el_row2_sub",
                "type": "TEXT",
                "x": 0.14,
                "y": 0.53,
                "width": 0.72,
                "height": 0.035,
                "text": "همراه با موسیقی زنده و منوی ویژه آخر هفته",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#D4D4D8",
                "textAlign": "CENTER"
            },
            # Row 3
            {
                "id": "el_row3_badge",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.59,
                "width": 0.72,
                "height": 0.06,
                "text": "جمعه‌ها و ایام تعطیل: ۱۶:۰۰ الی ۲۳:۰۰",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#78350F"
            },
            {
                "id": "el_row3_sub",
                "type": "TEXT",
                "x": 0.14,
                "y": 0.66,
                "width": 0.72,
                "height": 0.035,
                "text": "سفارش آنلاین در وبسایت: ۲۴ ساعته فعال است",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#A1A1AA",
                "textAlign": "CENTER"
            },
            # Address and Parking
            {
                "id": "el_addr_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.76,
                "width": 0.84,
                "height": 0.10,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#18181B",
                "borderColor": "#52525B",
                "borderWidth": 1.0
            },
            {
                "id": "el_address",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.78,
                "width": 0.76,
                "height": 0.06,
                "text": "📍 آدرس: تهران، خیابان ولیعصر، بالاتر از پارک‌وی، پلاک ۴۲\nدارای پارکینگ اختصاصی برای مشتریان گرامی",
                "fontSize": 11.0,
                "fontWeight": "MEDIUM",
                "textColor": "#E4E4E7",
                "textAlign": "CENTER"
            },
            {
                "id": "el_contact",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.88,
                "width": 0.70,
                "height": 0.06,
                "text": "شماره تماس و هماهنگی: ۰۲۱-۲۲۳۳۴۴۵۵",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#18181B",
                "backgroundColor": "#FBBF24"
            }
        ]
    })

    # 4.4 tpl_gen_058: نحوه و شرایط ارسال سفارش‌ها (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_gen_058",
        "title": "نحوه و شرایط ارسال سفارش‌ها",
        "category": "اطلاعیه",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["ارسال", "پست", "تیپاکس", "پیک", "شرایط"],
        "background": {
            "type": "GRADIENT",
            "color": "#0F172A",
            "gradientColors": ["#1E293B", "#0F172A"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.04,
                "width": 0.44,
                "height": 0.045,
                "text": "📦 راهنمای جامع ارسال",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#0284C7"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.06,
                "y": 0.095,
                "width": 0.88,
                "height": 0.06,
                "text": "روش‌های ارسال سریع به سراسر کشور",
                "fontSize": 18.0,
                "fontWeight": "BLACK",
                "fontFamily": "shabnam",
                "textColor": "#F0F9FF",
                "textAlign": "CENTER"
            },
            # Method 1 Card: پیک موتوری تهران
            {
                "id": "el_card_m1",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.17,
                "width": 0.88,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#1E293B",
                "borderColor": "#38BDF8",
                "borderWidth": 1.2
            },
            {
                "id": "el_m1_title",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.19,
                "width": 0.50,
                "height": 0.04,
                "text": "🛵 پیک فوری (مخصوص شهر تهران)",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#38BDF8",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_m1_desc",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.235,
                "width": 0.80,
                "height": 0.09,
                "text": "تحویل در همان‌روز با هماهنگی تلفنی قبل از حرکت | هزینه طبق تعرفه اسنپ‌باکس یا الوپیک",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#E2E8F0",
                "textAlign": "RIGHT"
            },
            # Method 2 Card: پست پیشتاز
            {
                "id": "el_card_m2",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.38,
                "width": 0.88,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#1E293B",
                "borderColor": "#F59E0B",
                "borderWidth": 1.2
            },
            {
                "id": "el_m2_title",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.40,
                "width": 0.50,
                "height": 0.04,
                "text": "📮 پست پیشتاز (تمام شهرستان‌ها)",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FBBF24",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_m2_desc",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.445,
                "width": 0.80,
                "height": 0.09,
                "text": "تحویل ۲ الی ۴ روز کاری درب منزل | ارسال پیامک کد رهگیری ۲۴ رقمی اداره پست بلافاصله پس از تحویل",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#E2E8F0",
                "textAlign": "RIGHT"
            },
            # Method 3 Card: تیپاکس
            {
                "id": "el_card_m3",
                "type": "SHAPE",
                "x": 0.06,
                "y": 0.59,
                "width": 0.88,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#1E293B",
                "borderColor": "#10B981",
                "borderWidth": 1.2
            },
            {
                "id": "el_m3_title",
                "type": "TEXT",
                "x": 0.40,
                "y": 0.61,
                "width": 0.50,
                "height": 0.04,
                "text": "⚡ ارسال با تیپاکس (تحویل سریع)",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#34D399",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_m3_desc",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.655,
                "width": 0.80,
                "height": 0.09,
                "text": "تحویل ۲۴ الی ۴۸ ساعته با امکان پرداخت هزینه پس‌کرایه درب منزل در مراکز استان‌ها",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#E2E8F0",
                "textAlign": "RIGHT"
            },
            # Free Shipping threshold
            {
                "id": "el_free_ship",
                "type": "BADGE",
                "x": 0.10,
                "y": 0.80,
                "width": 0.80,
                "height": 0.07,
                "text": "🎁 ارسال کاملاً رایگان برای سفارش‌های بالای ۱ میلیون تومان",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#059669"
            },
            {
                "id": "el_footer",
                "type": "BRAND_NAME",
                "x": 0.15,
                "y": 0.90,
                "width": 0.70,
                "height": 0.05,
                "text": "بسته‌بندی ایمن و ضدضربه | تضمین سلامت کالا",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#94A3B8",
                "textAlign": "CENTER"
            }
        ]
    })

    # 4.5 tpl_gen_059: فراخوان جذب همکاری و استخدام (STORY 9:16)
    templates.append({
        "id": "tpl_gen_059",
        "title": "فراخوان جذب همکاری و استخدام",
        "category": "اطلاعیه",
        "format": "STORY",
        "isVip": True,
        "tags": ["استخدام", "همکاری", "شغل", "ادمین", "تولید_محتوا"],
        "background": {
            "type": "GRADIENT",
            "color": "#1E1B4B",
            "gradientColors": ["#312E81", "#0F0D24"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "🤝 دعوت به همکاری",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#7C3AED"
            },
            {
                "id": "el_title",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.11,
                "width": 0.84,
                "height": 0.08,
                "text": "استخدام ادمین حرفه‌ای اینستاگرام و سناریونویس",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "textAlign": "CENTER"
            },
            # Requirements Card
            {
                "id": "el_card_req",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.21,
                "width": 0.84,
                "height": 0.45,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 22.0,
                "backgroundColor": "#1E1B4B",
                "borderColor": "#A855F7",
                "borderWidth": 1.5
            },
            {
                "id": "el_subtitle_req",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.24,
                "width": 0.72,
                "height": 0.045,
                "text": "شرایط و مهارت‌های مورد نیاز",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#6D28D9"
            },
            {
                "id": "el_req_bullets",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.31,
                "width": 0.76,
                "height": 0.32,
                "text": "• تسلط کامل به الگوریتم اینستاگرام، ریلز و استوری\n• مهارت در تولید محتوای ویدیویی (CapCut و InShot)\n• فن بیان بالا و روحیه خلاق و ایده‌پرداز\n• پاسخگویی منظم و صبورانه به دایرکت و کامنت‌ها\n• امکان همکاری حضوری / نیمه‌حضوری در تهران",
                "fontSize": 13.0,
                "fontWeight": "NORMAL",
                "fontFamily": "shabnam",
                "textColor": "#EDE9FE",
                "textAlign": "RIGHT"
            },
            # Benefits Pill
            {
                "id": "el_benefits",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.69,
                "width": 0.72,
                "height": 0.065,
                "text": "💼 حقوق ثابت عالی + پاداش عملکرد + بیمه",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "backgroundColor": "#4338CA"
            },
            # How to Apply CTA
            {
                "id": "el_apply_cta",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.80,
                "width": 0.76,
                "height": 0.08,
                "text": "ارسال رزومه و نمونه‌کار در تلگرام یا دایرکت",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#7C3AED"
            },
            {
                "id": "el_brand_name",
                "type": "BRAND_NAME",
                "x": 0.15,
                "y": 0.91,
                "width": 0.70,
                "height": 0.04,
                "text": "مجموعه مد و استایل من | فرصت رشد باهم",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "textColor": "#C4B5FD",
                "textAlign": "CENTER"
            }
        ]
    })

    # =========================================================================
    # 5. ORDER GUIDE & SHIPPING (سفارش‌گیری و شرایط ارسال - ۶ قالب غیر هایلایت)
    # =========================================================================
    # 5.1 tpl_story_order_07: راهنمای سفارش‌گیری آسان در ۳ مرحله (STORY 9:16)
    templates.append({
        "id": "tpl_story_order_07",
        "title": "راهنمای سفارش‌گیری آسان در ۳ مرحله",
        "category": "سفارش‌گیری",
        "format": "STORY",
        "isVip": False,
        "tags": ["سفارش", "راهنما", "مراحل", "دایرکت", "خرید"],
        "background": {
            "type": "GRADIENT",
            "color": "#064E3B",
            "gradientColors": ["#065F46", "#022C22"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "🛍️ نحوه ثبت سفارش",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#059669"
            },
            {
                "id": "el_title",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.11,
                "width": 0.84,
                "height": 0.07,
                "text": "ثبت سفارش در ۳ مرحله بسیار ساده",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#ECFDF5",
                "textAlign": "CENTER"
            },
            # Step 1 Card
            {
                "id": "el_step1_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.20,
                "width": 0.84,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#065F46",
                "borderColor": "#34D399",
                "borderWidth": 1.5
            },
            {
                "id": "el_step1_badge",
                "type": "BADGE",
                "x": 0.62,
                "y": 0.22,
                "width": 0.26,
                "height": 0.045,
                "text": "مرحله اول",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#064E3B",
                "backgroundColor": "#A7F3D0"
            },
            {
                "id": "el_step1_title",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.28,
                "width": 0.76,
                "height": 0.08,
                "text": "عکس محصول یا کد کالای مورد نظرتان را به همراه سایز دلخواه در دایرکت برای ما ارسال کنید.",
                "fontSize": 12.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#F0FDF4",
                "textAlign": "RIGHT"
            },
            # Step 2 Card
            {
                "id": "el_step2_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.41,
                "width": 0.84,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#065F46",
                "borderColor": "#34D399",
                "borderWidth": 1.5
            },
            {
                "id": "el_step2_badge",
                "type": "BADGE",
                "x": 0.62,
                "y": 0.43,
                "width": 0.26,
                "height": 0.045,
                "text": "مرحله دوم",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#064E3B",
                "backgroundColor": "#A7F3D0"
            },
            {
                "id": "el_step2_title",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.49,
                "width": 0.76,
                "height": 0.08,
                "text": "پس از استعلام موجودی، مشخصات پستی (آدرس، کد پستی و تلفن) را ثبت کرده و پرداخت را انجام دهید.",
                "fontSize": 12.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#F0FDF4",
                "textAlign": "RIGHT"
            },
            # Step 3 Card
            {
                "id": "el_step3_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.62,
                "width": 0.84,
                "height": 0.18,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#065F46",
                "borderColor": "#34D399",
                "borderWidth": 1.5
            },
            {
                "id": "el_step3_badge",
                "type": "BADGE",
                "x": 0.62,
                "y": 0.64,
                "width": 0.26,
                "height": 0.045,
                "text": "مرحله سوم",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#064E3B",
                "backgroundColor": "#A7F3D0"
            },
            {
                "id": "el_step3_title",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.70,
                "width": 0.76,
                "height": 0.08,
                "text": "سفارش شما در همان روز بسته‌بندی شده و کد رهگیری مرسوله پستی به شماره شما پیامک می‌شود.",
                "fontSize": 12.0,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#F0FDF4",
                "textAlign": "RIGHT"
            },
            # Guarantee
            {
                "id": "el_guar",
                "type": "BADGE",
                "x": 0.18,
                "y": 0.82,
                "width": 0.64,
                "height": 0.045,
                "text": "✓ گارانتی تعویض و بازگشت تا ۴۸ ساعت",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#022C22",
                "backgroundColor": "#6EE7B7"
            },
            # Action Button
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.88,
                "width": 0.76,
                "height": 0.07,
                "text": "شروع ثبت سفارش در دایرکت",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#047857"
            }
        ]
    })

    # 5.2 tpl_post_new_arrival_20: شارژ مجدد محصولات پرفروش (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_post_new_arrival_20",
        "title": "شارژ مجدد محصولات پرفروش",
        "category": "سفارش‌گیری",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["شارژ_مجدد", "پرفروش", "سفارش", "کفش", "کتونی"],
        "background": {
            "type": "GRADIENT",
            "color": "#18181B",
            "gradientColors": ["#27272A", "#09090B"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_ribbon",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.04,
                "width": 0.44,
                "height": 0.045,
                "text": "🔥 بنا به درخواست شما شارژ شد",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#E11D48"
            },
            {
                "id": "el_title",
                "type": "TEXT",
                "x": 0.06,
                "y": 0.095,
                "width": 0.88,
                "height": 0.065,
                "text": "اسنیکرز اسپرت نایک مدل ایر ۲۷۰",
                "fontSize": 18.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#F8FAFC",
                "textAlign": "CENTER"
            },
            # Real Product Shoes Photo
            {
                "id": "el_img",
                "type": "IMAGE",
                "x": 0.08,
                "y": 0.17,
                "width": 0.84,
                "height": 0.48,
                "imageAsset": "shoes_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 22.0,
                "borderColor": "#F43F5E",
                "borderWidth": 2.0
            },
            # Sizes Available Bar
            {
                "id": "el_sizes_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.67,
                "width": 0.84,
                "height": 0.09,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 14.0,
                "backgroundColor": "#27272A",
                "borderColor": "#52525B",
                "borderWidth": 1.0
            },
            {
                "id": "el_sizes_txt",
                "type": "TEXT",
                "x": 0.10,
                "y": 0.695,
                "width": 0.80,
                "height": 0.045,
                "text": "سایزبندی موجود: ۳۷ | ۳۸ | ۳۹ | ۴۰ | ۴۱ | ۴۲ (تعداد کاملاً محدود)",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FEF08A",
                "textAlign": "CENTER"
            },
            # Price Block
            {
                "id": "el_price",
                "type": "PRICE_BLOCK",
                "x": 0.08,
                "y": 0.78,
                "width": 0.84,
                "height": 0.18,
                "fontSize": 16.0,
                "fontFamily": "sahel",
                "textColor": "#FFFFFF",
                "backgroundColor": "#18181B",
                "borderColor": "#FB7185",
                "borderWidth": 1.5,
                "oldPrice": 1850000,
                "newPrice": 1390000,
                "currencyUnit": "تومان"
            }
        ]
    })

    # 5.3 tpl_gen_063: مراحل ثبت سفارش آسان از دایرکت (STORY 9:16)
    templates.append({
        "id": "tpl_gen_063",
        "title": "مراحل ثبت سفارش آسان از دایرکت",
        "category": "سفارش‌گیری",
        "format": "STORY",
        "isVip": True,
        "tags": ["سفارش", "دایرکت", "خرید", "ارسال", "مراحل"],
        "background": {
            "type": "GRADIENT",
            "color": "#1E1B4B",
            "gradientColors": ["#2E1065", "#0F0D24"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "📱 خرید سریع از اینستاگرام",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#8B5CF6"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.11,
                "width": 0.84,
                "height": 0.07,
                "text": "چگونه در ۲ دقیقه سفارش دهیم؟",
                "fontSize": 20.0,
                "fontWeight": "BLACK",
                "fontFamily": "sahel",
                "textColor": "#F3E8FF",
                "textAlign": "CENTER"
            },
            # Box 1
            {
                "id": "el_box1",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.20,
                "width": 0.84,
                "height": 0.19,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 18.0,
                "backgroundColor": "#231B42",
                "borderColor": "#C084FC",
                "borderWidth": 1.5
            },
            {
                "id": "el_box1_num",
                "type": "BADGE",
                "x": 0.70,
                "y": 0.22,
                "width": 0.18,
                "height": 0.04,
                "text": "گام ۱",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#7C3AED"
            },
            {
                "id": "el_box1_txt",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.28,
                "width": 0.76,
                "height": 0.09,
                "text": "از استوری یا پست مورد نظرتان اسکرین‌شات بگیرید و به همراه سایز برای ما بفرستید.",
                "fontSize": 12.5,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#EDE9FE",
                "textAlign": "RIGHT"
            },
            # Box 2
            {
                "id": "el_box2",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.42,
                "width": 0.84,
                "height": 0.19,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 18.0,
                "backgroundColor": "#231B42",
                "borderColor": "#C084FC",
                "borderWidth": 1.5
            },
            {
                "id": "el_box2_num",
                "type": "BADGE",
                "x": 0.70,
                "y": 0.44,
                "width": 0.18,
                "height": 0.04,
                "text": "گام ۲",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#7C3AED"
            },
            {
                "id": "el_box2_txt",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.50,
                "width": 0.76,
                "height": 0.09,
                "text": "لینک درگاه پرداخت اختصاصی و فاکتور رسمی را دریافت کرده و با کلیه کارت‌ها پرداخت کنید.",
                "fontSize": 12.5,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#EDE9FE",
                "textAlign": "RIGHT"
            },
            # Box 3
            {
                "id": "el_box3",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.64,
                "width": 0.84,
                "height": 0.19,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 18.0,
                "backgroundColor": "#231B42",
                "borderColor": "#C084FC",
                "borderWidth": 1.5
            },
            {
                "id": "el_box3_num",
                "type": "BADGE",
                "x": 0.70,
                "y": 0.66,
                "width": 0.18,
                "height": 0.04,
                "text": "گام ۳",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#7C3AED"
            },
            {
                "id": "el_box3_txt",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.72,
                "width": 0.76,
                "height": 0.09,
                "text": "مرسوله با بسته‌بندی محرمانه و ایمن ارسال شده و پیامک رهگیری پستی برای شما صادر می‌شود.",
                "fontSize": 12.5,
                "fontWeight": "MEDIUM",
                "fontFamily": "vazirmatn",
                "textColor": "#EDE9FE",
                "textAlign": "RIGHT"
            },
            # CTA
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.88,
                "width": 0.76,
                "height": 0.07,
                "text": "همین الان دایرکت بدهید",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#9333EA"
            }
        ]
    })

    # 5.4 tpl_gen_064: رزرو آنلاین سفارش گل و سوپرایز (POST_SQUARE 1:1)
    templates.append({
        "id": "tpl_gen_064",
        "title": "رزرو آنلاین سفارش گل و سوپرایز",
        "category": "سفارش‌گیری",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["گل", "رزرو", "سورپرایز", "هدیه", "آنلاین"],
        "background": {
            "type": "GRADIENT",
            "color": "#4A044E",
            "gradientColors": ["#701A75", "#38033E"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.045,
                "text": "🎁 خدمات سورپرایز و مناسبت‌ها",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#A21CAF"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.06,
                "y": 0.11,
                "width": 0.88,
                "height": 0.06,
                "text": "رزرو آنلاین باکس گل و هدیه با متن دلخواه",
                "fontSize": 17.0,
                "fontWeight": "BOLD",
                "fontFamily": "samim",
                "textColor": "#FDF4FF",
                "textAlign": "CENTER"
            },
            {
                "id": "el_img",
                "type": "IMAGE",
                "x": 0.08,
                "y": 0.19,
                "width": 0.84,
                "height": 0.48,
                "imageAsset": "flower_gift_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 22.0,
                "borderColor": "#F472B6",
                "borderWidth": 2.0
            },
            {
                "id": "el_perks",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.69,
                "width": 0.84,
                "height": 0.08,
                "text": "• امکان چاپ کارت‌پستال صوتی اختصاصی با صدای شما\n• تحویل سر ساعت دقیق به آدرس گیرنده سورپرایز",
                "fontSize": 12.0,
                "fontWeight": "MEDIUM",
                "textColor": "#F5D0FE",
                "textAlign": "CENTER"
            },
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.83,
                "width": 0.70,
                "height": 0.09,
                "text": "رزرو در دایرکت | واتساپ ۰۹۱۲۳۴۵۶۷۸۹",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#C026D3"
            }
        ]
    })

    # 5.5 tpl_gen_065: سفارش ساخت طلا با طرح دلخواه (POST_PORTRAIT 4:5)
    templates.append({
        "id": "tpl_gen_065",
        "title": "سفارش ساخت طلا با طرح دلخواه",
        "category": "سفارش‌گیری",
        "format": "POST_PORTRAIT",
        "isVip": True,
        "tags": ["طلا", "سفارش_ساخت", "جواهر", "طراحی", "دست‌ساز"],
        "background": {
            "type": "GRADIENT",
            "color": "#1C1917",
            "gradientColors": ["#292524", "#0C0A09"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_logo",
                "type": "BRAND_LOGO",
                "x": 0.42,
                "y": 0.04,
                "width": 0.16,
                "height": 0.08
            },
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.13,
                "width": 0.44,
                "height": 0.04,
                "text": "✨ کارگاه ساخت طلا و جواهر",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#1C1917",
                "backgroundColor": "#FDE68A"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.06,
                "y": 0.18,
                "width": 0.88,
                "height": 0.06,
                "text": "ساخت انگشتر، دستبند و پلاک با طرح دلخواه شما",
                "fontSize": 16.0,
                "fontWeight": "BOLD",
                "fontFamily": "sahel",
                "textColor": "#FEF3C7",
                "textAlign": "CENTER"
            },
            {
                "id": "el_img",
                "type": "IMAGE",
                "x": 0.10,
                "y": 0.25,
                "width": 0.80,
                "height": 0.42,
                "imageAsset": "jewelry_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 22.0,
                "borderColor": "#FBBF24",
                "borderWidth": 2.0
            },
            {
                "id": "el_features_card",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.69,
                "width": 0.84,
                "height": 0.16,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 16.0,
                "backgroundColor": "#292524",
                "borderColor": "#CA8A04",
                "borderWidth": 1.2
            },
            {
                "id": "el_feat_txt",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.71,
                "width": 0.76,
                "height": 0.12,
                "text": "• ساخت در وزن و بودجه مد نظر شما (از ۱.۵ گرم به بالا)\n• طراحی سه‌بعدی و تایید شما قبل از ریخته‌گری\n• دارای فاکتور رسمی اتحادیه با عیار استاندارد ۷۵۰",
                "fontSize": 11.0,
                "fontWeight": "NORMAL",
                "fontFamily": "vazirmatn",
                "textColor": "#F5F5F4",
                "textAlign": "RIGHT"
            },
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.12,
                "y": 0.88,
                "width": 0.76,
                "height": 0.07,
                "text": "مشاوره و تخمین قیمت در دایرکت",
                "fontSize": 13.0,
                "fontWeight": "BOLD",
                "textColor": "#1C1917",
                "backgroundColor": "#FBBF24"
            }
        ]
    })

    # 5.6 tpl_gen_066: رزرو دسر و کیک مجالس و مهمانی‌ها (STORY 9:16)
    templates.append({
        "id": "tpl_gen_066",
        "title": "رزرو دسر و کیک مجالس و مهمانی‌ها",
        "category": "سفارش‌گیری",
        "format": "STORY",
        "isVip": False,
        "tags": ["کیک", "مجالس", "مهمانی", "رزرو", "دسر"],
        "background": {
            "type": "GRADIENT",
            "color": "#31102A",
            "gradientColors": ["#4C1340", "#1F0819"],
            "angle": 135.0
        },
        "elements": [
            {
                "id": "el_badge",
                "type": "BADGE",
                "x": 0.28,
                "y": 0.05,
                "width": 0.44,
                "height": 0.04,
                "text": "🍰 کیک و فینگرفود مجالس",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#EC4899"
            },
            {
                "id": "el_head",
                "type": "TEXT",
                "x": 0.08,
                "y": 0.11,
                "width": 0.84,
                "height": 0.065,
                "text": "رزرو کیک و دسرهای خاص برای مراسم شما",
                "fontSize": 18.0,
                "fontWeight": "BOLD",
                "fontFamily": "shabnam",
                "textColor": "#FDF2F8",
                "textAlign": "CENTER"
            },
            {
                "id": "el_img",
                "type": "IMAGE",
                "x": 0.10,
                "y": 0.19,
                "width": 0.80,
                "height": 0.44,
                "imageAsset": "cake_01.webp",
                "contentScale": "CROP",
                "cornerRadius": 22.0,
                "borderColor": "#F472B6",
                "borderWidth": 2.0
            },
            # Reservation Terms
            {
                "id": "el_card_terms",
                "type": "SHAPE",
                "x": 0.08,
                "y": 0.65,
                "width": 0.84,
                "height": 0.19,
                "shapeType": "ROUNDED_RECT",
                "cornerRadius": 18.0,
                "backgroundColor": "#4C1340",
                "borderColor": "#F472B6",
                "borderWidth": 1.2
            },
            {
                "id": "el_terms_title",
                "type": "BADGE",
                "x": 0.14,
                "y": 0.675,
                "width": 0.72,
                "height": 0.04,
                "text": "نکات مهم قبل از ثبت سفارش",
                "fontSize": 11.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#DB2777"
            },
            {
                "id": "el_terms_txt",
                "type": "TEXT",
                "x": 0.12,
                "y": 0.73,
                "width": 0.76,
                "height": 0.09,
                "text": "حداقل زمان ثبت سفارش کیک ۳ روز قبل از مراسم است.\nامکان تست طعم فیلینگ‌ها قبل از ثبت نهایی سفارش فراهم است.",
                "fontSize": 11.5,
                "fontWeight": "NORMAL",
                "fontFamily": "vazirmatn",
                "textColor": "#FCE7F3",
                "textAlign": "CENTER"
            },
            {
                "id": "el_cta",
                "type": "BRAND_CONTACT",
                "x": 0.15,
                "y": 0.88,
                "width": 0.70,
                "height": 0.065,
                "text": "مشاوره و رزرو تاریخ مراسم در دایرکت",
                "fontSize": 12.0,
                "fontWeight": "BOLD",
                "textColor": "#FFFFFF",
                "backgroundColor": "#EC4899"
            }
        ]
    })

    print(f"Generated Phase 1 (Highlight & Service/Guide/Reviews): {len(templates)} templates")
    return templates

if __name__ == "__main__":
    t = build_template_catalog()
    print("Catalog test ok.")
