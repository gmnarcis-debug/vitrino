import json
import os

out_dir = "/app/app/src/main/assets/templates"
os.makedirs(out_dir, exist_ok=True)

# 60 templates: 24 STORY, 18 POST_SQUARE, 9 POST_PORTRAIT, 9 HIGHLIGHT
# Total: 60 (18 VIP, 42 FREE)
templates = [
    # =========================================================================
    # 1. مناسبتی (17 templates)
    # =========================================================================
    {
        "id": "tpl_yalda_story_26",
        "title": "جشنواره اناری شب یلدا",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["یلدا", "انار", "تخفیف_یلدا", "جشن"],
        "background": {"type": "IMAGE", "color": "#4C0519", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#4C0519", "#1E020D"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.04, "text": "🍉 بلندترین شب سال مبارک", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#E11D48"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "آفرهای ویژه یلدایی تا ۵۰٪ تخفیف", "fontSize": 20, "fontWeight": "BOLD", "textColor": "#FEE2E2", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#290D15", "borderColor": "#F43F5E", "borderWidth": 2, "placeholderLabel": "تصویر پک کادویی یلدا", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "همراه با فال حافظ و ارسال رایگان شب چله", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FECDD3", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#BE123C"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "خرید بسته یلدا با کد: YALDA", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "مهلت ثبت سفارش فقط تا ۳۰ آذر ⏰", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FDA4AF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_yalda_post_27",
        "title": "پست تبریک و تخفیف یلدا",
        "category": "مناسبتی",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["شب_یلدا", "تخفیف", "پست_یلدا"],
        "background": {"type": "GRADIENT", "color": "#4C0519", "gradientColors": ["#4C0519", "#0F0106"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "❄️ یلدای شگفت‌انگیز", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#E11D48"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "دورهمی گرم با شیرینی‌های یلدایی", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FFE4E6", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#230A12", "borderColor": "#FB7185", "borderWidth": 2, "placeholderLabel": "عکس پک یلدا", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "تخفیف ۲۰ درصدی روی کلیه محصولات فروشگاه", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FECDD3", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_CONTACT", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.06, "text": "سفارش آنلاین: دایرکت و سایت 🛍️", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_nowruz_story_28",
        "title": "عیدانه و کالکشن بهاره نوروز",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["نوروز", "عید", "بهار", "کالکشن_عید"],
        "background": {"type": "IMAGE", "color": "#14532D", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#14532D", "#052E16"], "overlayAlpha": 0.7, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🌱 سال نو مبارک ۱۴۰۵", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#16A34A"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "رونمایی کالکشن نوبرانه بهار و عید", "fontSize": 20, "fontWeight": "BOLD", "textColor": "#DCFCE7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#064E3B", "borderColor": "#4ADE80", "borderWidth": 2, "placeholderLabel": "تصویر کالکشن عید", "imageAsset": "fashion_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "با هر خرید عیدانه، یک کارت هدیه عیدی بگیرید", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#BBF7D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#15803D"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "مشاهده کامل ست‌های نوروزی", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "ارسال سریع تا قبل از سال تحویل 🚚", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#86EFAC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_nowruz_post_29",
        "title": "پست تبریک سال نو و هفت سین",
        "category": "مناسبتی",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["نوروز", "سال_نو", "تبریک"],
        "background": {"type": "GRADIENT", "color": "#064E3B", "gradientColors": ["#064E3B", "#022C22"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🌿 بوی بهار و شکوفه", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#10B981"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "آغاز بهار و نوروز باستانی فرخنده باد", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 2, "placeholderLabel": "تصویر تبریک نوروز", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "سالی پر از برکت و سلامتی برای شما آرزومندیم", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#A7F3D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "فروشگاه آنلاین همراه شما در سال جدید", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_mother_story_30",
        "title": "پک کادویی روز مادر و زن",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["روز_مادر", "روز_زن", "کادو_مادر", "عطر"],
        "background": {"type": "IMAGE", "color": "#831843", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#831843", "#500724"], "overlayAlpha": 0.65, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "❤️ روز فرشته‌های زمینی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#BE185D"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "پک‌های لوکس هدیه مخصوص روز مادر", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FCE7F3", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#4C0519", "borderColor": "#F472B6", "borderWidth": 2, "placeholderLabel": "هدیه روز مادر", "imageAsset": "perfume_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "شامل عطر دلخواه، گل‌آرایی و بسته‌بندی روبانی", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FBCFE8", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#BE185D"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "سفارش پک با ۲۰٪ تخفیف", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "ارسال مستقیم به آدرس مادر عزیزتان 🎁", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#F9A8D4", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_mother_post_31",
        "title": "پست تبریک روز مادر و همسر",
        "category": "مناسبتی",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["روز_مادر", "مادر", "تبریک"],
        "background": {"type": "GRADIENT", "color": "#4A044E", "gradientColors": ["#4A044E", "#2E0233"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🌹 روز مادر مبارک", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#C026D3"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "بهترین هدیه برای باارزش‌ترین فرد زندگی", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FAE8FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#2E0233", "borderColor": "#E879F9", "borderWidth": 2, "placeholderLabel": "تصویر هدیه زنانه", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "کیف چرم شیک همراه با ارسال کادویی رایگان", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#F5D0FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_CONTACT", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.06, "text": "مشاوره خرید: ۰۹۱۲۳۴۵۶۷۸۹ 📞", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_father_story_32",
        "title": "ست هدیه شیک روز پدر و مرد",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["روز_پدر", "روز_مرد", "ساعت", "هدیه_مردانه"],
        "background": {"type": "IMAGE", "color": "#1E293B", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#1E293B", "#0F172A"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "👔 تبریک روز مرد و پدر", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#0284C7"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "ست ساعت و اکسسوری کلاسیک مردانه", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#E0F2FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#0F172A", "borderColor": "#38BDF8", "borderWidth": 2, "placeholderLabel": "تصویر ست روز پدر", "imageAsset": "jewelry_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "ساعت لوکس همراه با جعبه چوبی و ضمانت اصالت", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#BAE6FD", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#0369A1"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "سفارش ست با تخفیف ویژه", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "ارسال رایگان در سراسر کشور 🎁", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#7DD3FC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_father_post_33",
        "title": "پست کتونی و استایل روز پدر",
        "category": "مناسبتی",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["روز_پدر", "کتونی", "هدیه"],
        "background": {"type": "GRADIENT", "color": "#1E293B", "gradientColors": ["#1E293B", "#020617"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.04, "text": "⚡ هدیه‌ای راحت برای پدران", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#09090B", "backgroundColor": "#38BDF8"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#0F172A", "borderColor": "#0284C7", "borderWidth": 2, "placeholderLabel": "کتونی طبی مردانه", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "کتونی پیاده‌روی فوق سبک طبی اورجینال", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "کفی طبی با فوم ضد ضربه | سایزبندی ۴۰ تا ۴۶", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#94A3B8", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.82, "width": 0.6, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#0284C7"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.835, "width": 0.56, "height": 0.05, "text": "قیمت آفر روز مرد: ۱,۳۹۰,۰۰۰ تومان", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_ramadan_story_34",
        "title": "پکیج افطاری لذیذ ماه رمضان",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["رمضان", "افطار", "سحر", "کافه", "رستوران"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#1C1917", "#0C0A09"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🌙 ماه مبارک رمضان", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#1C1917", "backgroundColor": "#F59E0B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "سینی مخصوص افطار و شام کافه", "fontSize": 20, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#29180E", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "سینی افطاری", "imageAsset": "food_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "شامل حلیم، نان گرم، زولبیا بامیه و چای باریک‌کمر", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#D97706"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "رزرو میز افطار: دایرکت دهید", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "سرویس‌دهی از افطار تا پاسی از شب 🌙", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_ramadan_post_35",
        "title": "پست حلول ماه مبارک رمضان",
        "category": "مناسبتی",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["رمضان", "تبریک_رمضان", "طاعت"],
        "background": {"type": "GRADIENT", "color": "#1E1B4B", "gradientColors": ["#1E1B4B", "#0F0D24"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "✨ ماه رحمت و برکت", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#7C3AED"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "طاعات و عبادات شما قبول درگاه حق", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#EDE9FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#0F0D24", "borderColor": "#A78BFA", "borderWidth": 2, "placeholderLabel": "شیرینی ماه رمضان", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "ساعات کاری ویژه در ماه مبارک رمضان", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#DDD6FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "التماس دعا در لحظات ملکوتی افطار و سحر", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#C4B5FD", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_moharram_story_36",
        "title": "استوری تسلیت ایام محرم و عاشورا",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": False,
        "tags": ["محرم", "عاشورا", "تاسوعا", "تسلیت"],
        "background": {"type": "GRADIENT", "color": "#18181B", "gradientColors": ["#18181B", "#09090B"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🖤 ایام سوگواری حسینی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#DC2626"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "فرا رسیدن ایام سوگواری اباعبدالله تسلیت باد", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#F3F4F6", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.2, "width": 0.8, "height": 0.45, "shapeType": "ROUNDED_RECT", "cornerRadius": 24, "backgroundColor": "#27272A", "borderColor": "#DC2626", "borderWidth": 2},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.32, "width": 0.7, "height": 0.08, "text": "یا حسین مظلوم (ع)", "fontSize": 24, "fontWeight": "BOLD", "textColor": "#EF4444", "textAlign": "CENTER"},
            {"id": "el_5", "type": "TEXT", "x": 0.12, "y": 0.44, "width": 0.76, "height": 0.12, "text": "باز این چه شورش است که در خلق عالم است\nباز این چه نوحه و چه عزا و چه ماتم است", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#D1D5DB", "textAlign": "CENTER"},
            {"id": "el_6", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "مجموعه در روزهای تاسوعا و عاشورا تعطیل می‌باشد", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#9CA3AF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "التماس دعا در مراسم عزاداری 🖤", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#6B7280", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_moharram_post_37",
        "title": "پست تسلیت و اطلاعیه تعطیلی محرم",
        "category": "مناسبتی",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["محرم", "تعطیلی", "اطلاعیه"],
        "background": {"type": "GRADIENT", "color": "#18181B", "gradientColors": ["#18181B", "#050505"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "⚫ ماه ماتم و عزا", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#991B1B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.13, "width": 0.84, "height": 0.07, "text": "تسلیت ایام عزاداری حضرت اباعبدالله الحسین", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.23, "width": 0.8, "height": 0.45, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#1C1917", "borderColor": "#78350F", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.35, "width": 0.7, "height": 0.1, "text": "به احترام این ایام، سفارشات پس از عاشورا ارسال خواهد شد", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#E5E7EB", "textAlign": "CENTER"},
            {"id": "el_5", "type": "TEXT", "x": 0.1, "y": 0.74, "width": 0.8, "height": 0.05, "text": "شروع مجدد فعالیت کاری از شنبه آینده", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#9CA3AF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_fitr_story_38",
        "title": "جشنواره عید سعید فطر",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["عید_فطر", "عید", "جشنواره", "عیدی"],
        "background": {"type": "IMAGE", "color": "#064E3B", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#064E3B", "#022C22"], "overlayAlpha": 0.7, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🌙 عید سعید فطر مبارک", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#059669"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "عیدی ویژه فروشگاه به مناسبت عید فطر", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 2, "placeholderLabel": "تصویر عیدی عید فطر", "imageAsset": "perfume_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "کد تخفیف ۲۵٪ عیدانه: FITR1405", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#A7F3D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#047857"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "خرید آنلاین با ارسال رایگان", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "اعتبار کد فقط تا پایان تعطیلات عید فطر ✨", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_blackfriday_story_39",
        "title": "حراج بلک فرایدی و جمعه سیاه",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": True,
        "tags": ["بلک_فرایدی", "جمعه_سیاه", "حراج_بزرگ", "تخفیف_ویژه"],
        "background": {"type": "GRADIENT", "color": "#18181B", "gradientColors": ["#18181B", "#09090B"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.045, "text": "🖤 BLACK FRIDAY SALE", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#FACC15"},
            {"id": "el_2", "type": "TEXT", "x": 0.05, "y": 0.11, "width": 0.9, "height": 0.07, "text": "بزرگترین حراج سال تا ۷۰٪ تخفیف", "fontSize": 22, "fontWeight": "BOLD", "textColor": "#FACC15", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#EAB308", "borderWidth": 2.5, "placeholderLabel": "تصویر محصول حراجی", "imageAsset": "digital_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "موجودی بسیار محدود | بدون تمدید مجدد", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FEF08A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.18, "y": 0.77, "width": 0.64, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 22, "backgroundColor": "#EAB308"},
            {"id": "el_6", "type": "TEXT", "x": 0.2, "y": 0.785, "width": 0.6, "height": 0.05, "text": "شروع خرید از ساعت ۰۰:۰۰ بامداد ⏳", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#000000", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "سبد خریدتان را از قبل آماده کنید!", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#CA8A04", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_blackfriday_post_40",
        "title": "پست بلک فرایدی جمعه سیاه",
        "category": "مناسبتی",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["بلک_فرایدی", "آفر_ویژه", "حراج"],
        "background": {"type": "GRADIENT", "color": "#0F0F10", "gradientColors": ["#0F0F10", "#000000"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.06, "width": 0.44, "height": 0.045, "text": "🔥 تخفیف انفجاری ۷۰٪", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#F59E0B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "جمعه سیاه را در فروشگاه ما تجربه کنید", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#000000", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "محصول حراج بلک فرایدی", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "ارسال رایگان به سراسر ایران برای تمام فاکتورها", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "لینک خرید آنلاین در بیو پیج 🛒", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#F59E0B", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_school_story_41",
        "title": "جشنواره بوی ماه مهر و مدارس",
        "category": "مناسبتی",
        "format": "STORY",
        "isVip": False,
        "tags": ["مدارس", "بوی_ماه_مهر", "لوازم_تحریر", "کوله_پشتی"],
        "background": {"type": "GRADIENT", "color": "#1E3A8A", "gradientColors": ["#1E3A8A", "#0F172A"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🎒 آماده برای مدرسه", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#2563EB"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "حراج لوازم تحریر، کیف و کفش مدرسه", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#DBEAFE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#0F172A", "borderColor": "#60A5FA", "borderWidth": 2, "placeholderLabel": "لوازم مدرسه و کوله", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "پک‌های اقتصادی دانش‌آموزی با تخفیف ۳۰٪", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#BFDBFE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#1D4ED8"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "مشاهده ملزومات تحصیلی", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "ارسال سریع تا قبل از شروع ماه مهر 📚", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#93C5FD", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_school_post_42",
        "title": "پست کتونی و کفش مدرسه",
        "category": "مناسبتی",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["مدارس", "کفش_مدرسه", "کتونی_اسپرت"],
        "background": {"type": "GRADIENT", "color": "#172554", "gradientColors": ["#172554", "#090D16"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.04, "text": "👟 کفش مقاوم و استاندارد", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#3B82F6"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#090D16", "borderColor": "#60A5FA", "borderWidth": 2, "placeholderLabel": "کتونی مدرسه", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "کتونی بادوام و راحت مناسب ساعات طولانی مدرسه", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#EFF6FF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "سایز ۳۵ تا ۴۲ | رویه تنفس‌پذیر ضد تعریق", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#93C5FD", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.82, "width": 0.6, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#2563EB"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.835, "width": 0.56, "height": 0.05, "text": "قیمت ویژه دانش‌آموزی: ۸۹۰,۰۰۰ تومان", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 2. معرفی محصول (Product Showcase - 10 templates)
    # =========================================================================
    {
        "id": "tpl_prod_headphone_story_43",
        "title": "هدفون بی‌سیم نویز کنسلینگ",
        "category": "معرفی محصول",
        "format": "STORY",
        "isVip": True,
        "tags": ["هدفون", "دیجیتال", "موزیک", "گجت"],
        "background": {"type": "IMAGE", "color": "#0F172A", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#0F172A", "#020617"], "overlayAlpha": 0.8, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🎧 صدای شفاف استودیویی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#06B6D4"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "هدفون بلوتوثی پرو با ۴۰ ساعت شارژدهی", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#E0F2FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#020617", "borderColor": "#38BDF8", "borderWidth": 2, "placeholderLabel": "تصویر هدفون دیجیتال", "imageAsset": "digital_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "مجهز به ANC فعال و میکروفون هوش مصنوعی", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#BAE6FD", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#0891B2"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "قیمت ویژه: ۱,۸۵۰,۰۰۰ تومان", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "گارانتی تعویض ۱۸ ماهه معتبر شرکتی 🛡️", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#67E8F9", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_coat_portrait_44",
        "title": "پالتو فوتر کتی زنانه مزونی",
        "category": "معرفی محصول",
        "format": "POST_PORTRAIT",
        "isVip": True,
        "tags": ["پوشاک", "پالتو", "زنانه", "مزون"],
        "background": {"type": "GRADIENT", "color": "#2C1252", "gradientColors": ["#2C1252", "#0F0B1E"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "👗 دوخت سفارشی مزون", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#8B5CF6"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#0F0B1E", "borderColor": "#A855F7", "borderWidth": 2, "placeholderLabel": "تصویر پالتو زنانه", "imageAsset": "fashion_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "کت فوتر کوبیده استردار با تنخور ژورنالی", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#F3E8FF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "قد ۸۵ سانتی‌متر | مناسب استایل روزمره و اداری", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#D8B4FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.82, "width": 0.6, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#7C3AED"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.835, "width": 0.56, "height": 0.05, "text": "قیمت مزون: ۱,۹۵۰,۰۰۰ تومان", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_skincare_square_45",
        "title": "سرم آبرسان و جوانساز پوست",
        "category": "معرفی محصول",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["پوست", "زیبایی", "آرایشی", "سرم_صورت"],
        "background": {"type": "GRADIENT", "color": "#500724", "gradientColors": ["#500724", "#1E020D"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "💧 هیدراتاسیون عمیق", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#EC4899"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "سرم هیالورونیک اسید با جذب سریع", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FCE7F3", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#1E020D", "borderColor": "#F472B6", "borderWidth": 2, "placeholderLabel": "تصویر سرم صورت", "imageAsset": "beauty_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "فاقد چربی و پارابن | مناسب انواع تیپ‌های پوستی", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FBCFE8", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "تضمین اصالت برند و خرید مطمئن در سایت", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#F472B6", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_watch_story_46",
        "title": "ساعت مچی کرونوگراف سوئیسی",
        "category": "معرفی محصول",
        "format": "STORY",
        "isVip": True,
        "tags": ["ساعت", "اکسسوری", "لاکچری"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#1C1917", "#09090B"], "overlayAlpha": 0.8, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "⌚ طراحی اصیل سوئیسی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#F59E0B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "موتور اتوماتیک با بند استیل ضد زنگ", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "ساعت مچی لوکس", "imageAsset": "jewelry_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "شیشه یاقوت ضد خش | ضد آب تا عمق ۱۰۰ متر", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#D97706"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "مشاهده قیمت و ثبت سفارش", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "دارای ۲ سال ضمانت بی‌قید و شرط 🏆", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_bakery_square_47",
        "title": "نان تست هفت‌غله و نان‌های فانتزی",
        "category": "معرفی محصول",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["نان", "صبحانه", "شیرینی", "ارگانیک"],
        "background": {"type": "GRADIENT", "color": "#451A03", "gradientColors": ["#451A03", "#1C0A00"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🥖 نان تازه داغ تنوری", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#D97706"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "پخت صبحگاهی نان و شیرینی فرانسوی", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#1C0A00", "borderColor": "#F59E0B", "borderWidth": 2, "placeholderLabel": "نان و شیرینی داغ", "imageAsset": "coffee_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "تخمیر طبیعی بدون جوش شیرین | فیبر بالا", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "ارسال داغ و سریع با پیک اختصاصی نانوایی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#F59E0B", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_burger_story_48",
        "title": "ساندویچ کریسپی مرغ سوخاری",
        "category": "معرفی محصول",
        "format": "STORY",
        "isVip": False,
        "tags": ["فست_فود", "برگر", "ساندویچ", "رستوران"],
        "background": {"type": "GRADIENT", "color": "#7C2D12", "gradientColors": ["#7C2D12", "#1C0800"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🍗 طعم کرانچی واقعی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#EA580C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "فیله مرغ کریسپی دوبل با دیپ پنیر چدار", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FED7AA", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#1C0800", "borderColor": "#FB923C", "borderWidth": 2, "placeholderLabel": "عکس برگر سوخاری", "imageAsset": "food_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "همراه با سیب‌زمینی سرخ‌کرده ادویه‌دار و نوشابه", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FFEDD5", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#C2410C"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "قیمت ساندویچ: ۲۱۰,۰۰۰ تومان", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "سفارش مستقیم از اسنپ فود و تماس تلفنی 🛵", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FDBA74", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_boots_portrait_49",
        "title": "نیم‌بوت چرم زمستانه ضد آب",
        "category": "معرفی محصول",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["کفش", "بوت", "زمستانه", "چرم"],
        "background": {"type": "GRADIENT", "color": "#27272A", "gradientColors": ["#27272A", "#09090B"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.04, "text": "❄️ ضد سرما و ضد نفوذ آب", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#09090B", "backgroundColor": "#A1A1AA"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#E4E4E7", "borderWidth": 2, "placeholderLabel": "تصویر نیم بوت", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "نیم‌بوت سبک وزن کوهنوردی و شهری", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "زیره عاج‌دار ترمو ضد لغزش در برف و یخ", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#A1A1AA", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.82, "width": 0.6, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#52525B"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.835, "width": 0.56, "height": 0.05, "text": "قیمت پاییزی: ۱,۶۵۰,۰۰۰ تومان", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_perfume_square_50",
        "title": "عطر مینیاتوری جیبی ماندگار",
        "category": "معرفی محصول",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["عطر", "مینیاتوری", "هدیه"],
        "background": {"type": "GRADIENT", "color": "#2E1065", "gradientColors": ["#2E1065", "#0F0728"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🌸 سایز جیبی و همراه", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#8B5CF6"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "پک ۴ عددی عطرهای پرفروش فصل", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#EDE9FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#0F0728", "borderColor": "#A78BFA", "borderWidth": 2, "placeholderLabel": "شیشه مینیاتوری عطر", "imageAsset": "perfume_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "ماندگاری تضمینی بالای ۲۴ ساعت و پخش بوی عالی", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#DDD6FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "قیمت پک کامل: ۷۹۰,۰۰۰ تومان 🎁", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#C4B5FD", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_tote_story_51",
        "title": "کیف دوشی بزرگ چرم زنانه",
        "category": "معرفی محصول",
        "format": "STORY",
        "isVip": False,
        "tags": ["کیف", "توت_بگ", "چرم", "زنانه"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#292524", "#0C0A09"], "overlayAlpha": 0.7, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "💼 جادار و بسیار شیک", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#B45309"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "کیف توت بگ مناسب لپ‌تاپ و دانشگاه", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#0C0A09", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "عکس کیف دوشی", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "جنس چرم صنعتی ضد آب با بند دوشی محکم", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#D97706"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "قیمت استثنایی: ۵۴۰,۰۰۰ تومان", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "جهت مشاهده رنگ‌بندی ورق بزنید 👉", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_prod_cupcake_square_52",
        "title": "کاپ‌کیک‌های فرانسوی فانتزی",
        "category": "معرفی محصول",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["کاپ_کیک", "شیرینی", "تولد", "عصرانه"],
        "background": {"type": "GRADIENT", "color": "#701A75", "gradientColors": ["#701A75", "#2E0332"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🧁 طعم‌های شکلاتی و وانیلی", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#D946EF"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "باکس ۶ عددی کاپ‌کیک خامه‌ای", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FDF4FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#2E0332", "borderColor": "#E879F9", "borderWidth": 2, "placeholderLabel": "کاپ کیک شیرینی", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "تزیین دستی با گل‌های خوراکی و تم دلخواه", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#F5D0FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "قیمت باکس ۶ تایی: ۱۹۰,۰۰۰ تومان 🎀", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#E879F9", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 3. تخفیف و حراج (Discounts & Sales - 8 templates)
    # =========================================================================
    {
        "id": "tpl_sale_flash_story_53",
        "title": "حراج فلش ۳ ساعته امشب",
        "category": "تخفیف و حراج",
        "format": "STORY",
        "isVip": True,
        "tags": ["تخفیف", "حراج", "فلش_سیل", "فروش_فوق_العاده"],
        "background": {"type": "GRADIENT", "color": "#7F1D1D", "gradientColors": ["#7F1D1D", "#1C0202"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.04, "text": "⚡ FLASH SALE امشب", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#DC2626"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "فقط تا ساعت ۲۴ امشب ۵۰٪ تخفیف", "fontSize": 20, "fontWeight": "BOLD", "textColor": "#FEE2E2", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#1C0202", "borderColor": "#EF4444", "borderWidth": 2, "placeholderLabel": "محصول فلش سیل", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "تخفیف به صورت اتوماتیک در سبد اعمال می‌شود", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FECDD3", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#B91C1C"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "خرید با تخفیف شگفت‌انگیز", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "شمارش معکوس آغاز شد ⏳", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FCA5A5", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_weekend_square_54",
        "title": "تخفیف پایان هفته کالکشن",
        "category": "تخفیف و حراج",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["آخر_هفته", "تخفیف", "حراجی"],
        "background": {"type": "GRADIENT", "color": "#312E81", "gradientColors": ["#312E81", "#0F0D2E"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🎉 حراج پنجشنبه و جمعه", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#4F46E5"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "یکی بخر، دومی رو با نیم‌بها ببر!", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#EEF2FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#0F0D2E", "borderColor": "#818CF8", "borderWidth": 2, "placeholderLabel": "محصول تخفیفی", "imageAsset": "fashion_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "شامل تمامی اقلام بوتیک و مانتوهای بهاره", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#C7D2FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "ارسال به سراسر کشور | ثبت در دایرکت", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#A5B4FC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_clearance_story_55",
        "title": "حراج تک‌سایزها و انبارگردانی",
        "category": "تخفیف و حراج",
        "format": "STORY",
        "isVip": True,
        "tags": ["تک_سایز", "انبارگردانی", "حراج_ویژه"],
        "background": {"type": "IMAGE", "color": "#18181B", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#18181B", "#09090B"], "overlayAlpha": 0.8, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🏷️ تک‌سایزهای آخر فصل", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#FBBF24"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "قیمت‌های باورنکردنی زیر قیمت فاکتور", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#F59E0B", "borderWidth": 2, "placeholderLabel": "اجناس تک سایز", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "هر آیتم فقط ۱ یا ۲ عدد موجود است", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#D97706"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "رزرو فوری در دایرکت 💬", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "اولویت با اولین واریزی است!", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_gadget_square_56",
        "title": "تخفیف شگفت‌انگیز لوازم جانبی",
        "category": "تخفیف و حراج",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["دیجیتال", "تخفیف", "لوازم_جانبی"],
        "background": {"type": "GRADIENT", "color": "#064E3B", "gradientColors": ["#064E3B", "#022C22"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "💥 تا ۴۰٪ تخفیف گجت", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#10B981"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "ساعت هوشمند، ایرپاد و پاوربانک", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#ECFDF5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 2, "placeholderLabel": "محصولات دیجیتال تخفیفی", "imageAsset": "digital_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "همراه با کابل شارژ فست رایگان به عنوان هدیه", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#A7F3D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "ضمانت ۷ روز مهلت تست فنی محصولات", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_beauty_portrait_57",
        "title": "حراج پک مراقبت از مو و پوست",
        "category": "تخفیف و حراج",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["بیوتی", "تخفیف", "پک_بهداشتی"],
        "background": {"type": "GRADIENT", "color": "#701A75", "gradientColors": ["#701A75", "#2B032D"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.25, "y": 0.05, "width": 0.5, "height": 0.04, "text": "🌸 تخفیف رویایی فصل", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#C026D3"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#2B032D", "borderColor": "#E879F9", "borderWidth": 2, "placeholderLabel": "پک محصولات مراقبت مو", "imageAsset": "beauty_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "پک کراتین و روغن آرگان اصل مراکش", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FAE8FF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "ترمیم عمیق ساقه مو و جلوگیری از موخوره", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#E9D5FF", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.82, "width": 0.6, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#A21CAF"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.835, "width": 0.56, "height": 0.05, "text": "قیمت پک با ۳۰٪ تخفیف: ۶۹۰,۰۰۰ تومان", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_cafe_story_58",
        "title": "آفر دونفره کافه و نوشیدنی",
        "category": "تخفیف و حراج",
        "format": "STORY",
        "isVip": False,
        "tags": ["کافه", "تخفیف", "دونفره", "قهوه"],
        "background": {"type": "GRADIENT", "color": "#431407", "gradientColors": ["#431407", "#120502"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "☕ کمبوی دونفره کافه", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#EA580C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "۲ فنجان نوشیدنی گرم + ۲ برش کیک روز", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FFEDD5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#120502", "borderColor": "#FB923C", "borderWidth": 2, "placeholderLabel": "کمبو کافه و کیک", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "فقط ۲۴۰ هزار تومان به جای ۳۴۰ هزار تومان", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FED7AA", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#C2410C"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "رزرو میز یا سفارش بیرون‌بر", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "هر روز از ساعت ۱۶ الی ۱۹ منتظرتان هستیم 💛", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FDBA74", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_gold_square_59",
        "title": "حراج طلا بدون اجرت ساخت",
        "category": "تخفیف و حراج",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["طلا", "بدون_اجرت", "سرمایه_گذاری"],
        "background": {"type": "GRADIENT", "color": "#1C1405", "gradientColors": ["#2D2005", "#0F0B02"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "✨ فرصت استثنایی طلا", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#F59E0B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "فروش طلاهای کارکرده بدون اجرت", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#0F0B02", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "طلای کم اجرت", "imageAsset": "jewelry_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "بهترین گزینه برای سرمایه‌گذاری و پس‌انداز", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "همراه با فاکتور رسمی اتحادیه طلافروشان", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_sale_flower_story_60",
        "title": "تخفیف پایان هفته گل‌های آپارتمانی",
        "category": "تخفیف و حراج",
        "format": "STORY",
        "isVip": False,
        "tags": ["گل", "گلدان", "تخفیف_گل", "گیاه"],
        "background": {"type": "IMAGE", "color": "#064E3B", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#064E3B", "#022C22"], "overlayAlpha": 0.7, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🌿 حراج سبز آخر هفته", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#059669"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "خرید ۲ گلدان = ۱ گلدان بونسای هدیه", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 2, "placeholderLabel": "گلدان گیاه آپارتمانی", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "گیاهان تصفیه‌کننده هوا و مقاوم به کم‌نوری", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#A7F3D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#047857"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "مشاهده کاتالوگ گیاهان آپارتمانی", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "ارسال با ماشین مخصوص بدون آسیب به برگ‌ها 🪴", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 4. قیمت‌نامه (Price List & Menus - 5 templates)
    # =========================================================================
    {
        "id": "tpl_price_beauty_story_61",
        "title": "تعرفه خدمات رنگ و لایت بیوتی",
        "category": "قیمت‌نامه",
        "format": "STORY",
        "isVip": True,
        "tags": ["قیمت", "تعرفه", "سالن_زیبایی", "رنگ_مو"],
        "background": {"type": "IMAGE", "color": "#500724", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#500724", "#1F020E"], "overlayAlpha": 0.8, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "💇‍♀️ منوی خدمات اسفند", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#BE185D"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "لیست قیمت خدمات تخصصی مو", "fontSize": 20, "fontWeight": "BOLD", "textColor": "#FCE7F3", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.08, "y": 0.19, "width": 0.84, "height": 0.1, "shapeType": "ROUNDED_RECT", "cornerRadius": 16, "backgroundColor": "#831843"},
            {"id": "el_4", "type": "TEXT", "x": 0.45, "y": 0.21, "width": 0.43, "height": 0.06, "text": "بالیاژ و آمبره روسی", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_5", "type": "TEXT", "x": 0.12, "y": 0.21, "width": 0.35, "height": 0.06, "text": "از ۱,۸۰۰,۰۰۰ ت", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#F472B6", "textAlign": "LEFT"},
            {"id": "el_6", "type": "SHAPE", "x": 0.08, "y": 0.31, "width": 0.84, "height": 0.1, "shapeType": "ROUNDED_RECT", "cornerRadius": 16, "backgroundColor": "#831843"},
            {"id": "el_7", "type": "TEXT", "x": 0.45, "y": 0.33, "width": 0.43, "height": 0.06, "text": "پروتئین‌تراپی و احیا", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_8", "type": "TEXT", "x": 0.12, "y": 0.33, "width": 0.35, "height": 0.06, "text": "از ۱,۲۰۰,۰۰۰ ت", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#F472B6", "textAlign": "LEFT"},
            {"id": "el_9", "type": "SHAPE", "x": 0.08, "y": 0.43, "width": 0.84, "height": 0.1, "shapeType": "ROUNDED_RECT", "cornerRadius": 16, "backgroundColor": "#831843"},
            {"id": "el_10", "type": "TEXT", "x": 0.45, "y": 0.45, "width": 0.43, "height": 0.06, "text": "کوپ ژورنالی + براشینگ", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_11", "type": "TEXT", "x": 0.12, "y": 0.45, "width": 0.35, "height": 0.06, "text": "۳۵۰,۰۰۰ ت", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#F472B6", "textAlign": "LEFT"},
            {"id": "el_12", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.56, "width": 0.8, "height": 0.22, "cornerRadius": 18, "backgroundColor": "#500724", "borderColor": "#F472B6", "borderWidth": 1.5, "placeholderLabel": "نمونه کار سالن", "imageAsset": "beauty_01.webp", "contentScale": "CROP"},
            {"id": "el_13", "type": "BRAND_CONTACT", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "رزرو نوبت: ۰۹۱۲۳۴۵۶۷۸۹ 📞", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_price_cafe_square_62",
        "title": "منوی نوشیدنی‌های گرم و بار گرم کافه",
        "category": "قیمت‌نامه",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["منو_کافه", "قیمت_قهوه", "لاته"],
        "background": {"type": "GRADIENT", "color": "#27170D", "gradientColors": ["#27170D", "#0F0703"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.04, "text": "☕ منوی بار گرم کافه", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#C2410C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.06, "text": "اسپرسو بار و نوشیدنی‌های تخصصی", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#FED7AA", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.08, "y": 0.2, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#431407"},
            {"id": "el_4", "type": "TEXT", "x": 0.5, "y": 0.215, "width": 0.38, "height": 0.05, "text": "اسپرسو سینگل / دبل", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_5", "type": "TEXT", "x": 0.12, "y": 0.215, "width": 0.35, "height": 0.05, "text": "۵۵ / ۷۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FB923C", "textAlign": "LEFT"},
            {"id": "el_6", "type": "SHAPE", "x": 0.08, "y": 0.31, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#431407"},
            {"id": "el_7", "type": "TEXT", "x": 0.5, "y": 0.325, "width": 0.38, "height": 0.05, "text": "کاپوچینو و لاته آرت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_8", "type": "TEXT", "x": 0.12, "y": 0.325, "width": 0.35, "height": 0.05, "text": "۹۵ / ۱۰۵ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FB923C", "textAlign": "LEFT"},
            {"id": "el_9", "type": "SHAPE", "x": 0.08, "y": 0.42, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#431407"},
            {"id": "el_10", "type": "TEXT", "x": 0.5, "y": 0.435, "width": 0.38, "height": 0.05, "text": "هات‌چاکلت غلیظ بلژیکی", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_11", "type": "TEXT", "x": 0.12, "y": 0.435, "width": 0.35, "height": 0.05, "text": "۱۱۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FB923C", "textAlign": "LEFT"},
            {"id": "el_12", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.54, "width": 0.76, "height": 0.28, "cornerRadius": 16, "backgroundColor": "#120502", "borderColor": "#EA580C", "borderWidth": 1.5, "placeholderLabel": "لاته کافه", "imageAsset": "coffee_01.webp", "contentScale": "CROP"},
            {"id": "el_13", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.05, "text": "امکان سفارش حضوری و بیرون‌بر 🛵", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FDBA74", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_price_confectionery_story_63",
        "title": "تعرفه کیک و دسر مجالس",
        "category": "قیمت‌نامه",
        "format": "STORY",
        "isVip": False,
        "tags": ["شیرینی", "قیمت_کیک", "سفارش_شیرینی"],
        "background": {"type": "IMAGE", "color": "#1C1014", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#38131E", "#13070B"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🎂 قیمت کیک‌های خاص", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#BE123C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "نرخ‌نامه انواع کیک تولد و عروسی", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FFE4E6", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.32, "cornerRadius": 20, "backgroundColor": "#13070B", "borderColor": "#FB7185", "borderWidth": 2, "placeholderLabel": "کیک تولد", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "SHAPE", "x": 0.08, "y": 0.54, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#290D15"},
            {"id": "el_5", "type": "TEXT", "x": 0.45, "y": 0.555, "width": 0.43, "height": 0.05, "text": "کیک فوندانت اختصاصی", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_6", "type": "TEXT", "x": 0.12, "y": 0.555, "width": 0.35, "height": 0.05, "text": "کیلویی ۵۵۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FDA4AF", "textAlign": "LEFT"},
            {"id": "el_7", "type": "SHAPE", "x": 0.08, "y": 0.65, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#290D15"},
            {"id": "el_8", "type": "TEXT", "x": 0.45, "y": 0.665, "width": 0.43, "height": 0.05, "text": "کیک خامه مدرن مینیمال", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_9", "type": "TEXT", "x": 0.12, "y": 0.665, "width": 0.35, "height": 0.05, "text": "کیلویی ۳۹۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FDA4AF", "textAlign": "LEFT"},
            {"id": "el_10", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.05, "text": "تحویل در یخچال مخصوص قنادی 🚚", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FB7185", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_price_shoes_portrait_64",
        "title": "جدول قیمت و سایزبندی کتونی",
        "category": "قیمت‌نامه",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["کتونی", "قیمت_کفش", "حراج"],
        "background": {"type": "GRADIENT", "color": "#18181B", "gradientColors": ["#27272A", "#09090B"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "👟 لیست قیمت کالکشن پاییز", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#22C55E"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.42, "cornerRadius": 22, "backgroundColor": "#09090B", "borderColor": "#4ADE80", "borderWidth": 2, "placeholderLabel": "کتونی اسپرت", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.55, "width": 0.84, "height": 0.05, "text": "کالکشن نایک و آدیداس اصل ویتنام", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "SHAPE", "x": 0.08, "y": 0.62, "width": 0.84, "height": 0.08, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#27272A"},
            {"id": "el_5", "type": "TEXT", "x": 0.5, "y": 0.635, "width": 0.38, "height": 0.05, "text": "مدل ایر زوم رانینگ", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_6", "type": "TEXT", "x": 0.12, "y": 0.635, "width": 0.35, "height": 0.05, "text": "۱,۴۵۰,۰۰۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#4ADE80", "textAlign": "LEFT"},
            {"id": "el_7", "type": "SHAPE", "x": 0.08, "y": 0.72, "width": 0.84, "height": 0.08, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#27272A"},
            {"id": "el_8", "type": "TEXT", "x": 0.5, "y": 0.735, "width": 0.38, "height": 0.05, "text": "مدل استریت ساقدار", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_9", "type": "TEXT", "x": 0.12, "y": 0.735, "width": 0.35, "height": 0.05, "text": "۱,۶۹۰,۰۰۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#4ADE80", "textAlign": "LEFT"},
            {"id": "el_10", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.05, "text": "تعویض سایز رایگان تا ۴۸ ساعت 📦", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#86EFAC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_price_bags_square_65",
        "title": "تعرفه کیف‌های چرم دست‌دوز",
        "category": "قیمت‌نامه",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["کیف", "قیمت_چرم", "دستدوز"],
        "background": {"type": "GRADIENT", "color": "#1C1917", "gradientColors": ["#292524", "#0C0A09"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "👜 نرخ تولیدی کارگاه", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#D97706"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.06, "text": "لیست قیمت انواع کیف زنانه چرم", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.08, "y": 0.2, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#292524"},
            {"id": "el_4", "type": "TEXT", "x": 0.5, "y": 0.215, "width": 0.38, "height": 0.05, "text": "کیف دوشی مجلسی", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_5", "type": "TEXT", "x": 0.12, "y": 0.215, "width": 0.35, "height": 0.05, "text": "۱,۳۵۰,۰۰۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FBBF24", "textAlign": "LEFT"},
            {"id": "el_6", "type": "SHAPE", "x": 0.08, "y": 0.31, "width": 0.84, "height": 0.09, "shapeType": "ROUNDED_RECT", "cornerRadius": 14, "backgroundColor": "#292524"},
            {"id": "el_7", "type": "TEXT", "x": 0.5, "y": 0.325, "width": 0.38, "height": 0.05, "text": "کیف پاسپورتی و جیبی", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "RIGHT"},
            {"id": "el_8", "type": "TEXT", "x": 0.12, "y": 0.325, "width": 0.35, "height": 0.05, "text": "۶۵۰,۰۰۰ ت", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FBBF24", "textAlign": "LEFT"},
            {"id": "el_9", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.43, "width": 0.76, "height": 0.38, "cornerRadius": 18, "backgroundColor": "#0C0A09", "borderColor": "#D97706", "borderWidth": 1.5, "placeholderLabel": "کیف چرم شیک", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_10", "type": "BRAND_NAME", "x": 0.1, "y": 0.86, "width": 0.8, "height": 0.05, "text": "دوخت تمیز با نخ ترک و یراق طلایی درجه یک", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 5. اطلاعیه (Announcements & Store Hours - 5 templates)
    # =========================================================================
    {
        "id": "tpl_notice_hours_story_66",
        "title": "ساعات کاری و روزهای فعالیت",
        "category": "اطلاعیه",
        "format": "STORY",
        "isVip": False,
        "tags": ["ساعات_کاری", "اطلاعیه", "فروشگاه"],
        "background": {"type": "IMAGE", "color": "#0F172A", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#0F172A", "#020617"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "⏰ ساعات کاری مجموعه", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#0284C7"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "پاسخگویی و ارسال سفارشات فروشگاه", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#E0F2FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.2, "width": 0.8, "height": 0.38, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#1E293B", "borderColor": "#38BDF8", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.24, "width": 0.7, "height": 0.06, "text": "شنبه تا چهارشنبه: ۱۰ الی ۲۱", "fontSize": 15, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_5", "type": "TEXT", "x": 0.15, "y": 0.32, "width": 0.7, "height": 0.06, "text": "پنجشنبه‌ها: ۱۰ الی ۱۸", "fontSize": 15, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_6", "type": "TEXT", "x": 0.15, "y": 0.4, "width": 0.7, "height": 0.06, "text": "جمعه‌ها و ایام تعطیل رسمی: تعطیل", "fontSize": 14, "fontWeight": "NORMAL", "textColor": "#EF4444", "textAlign": "CENTER"},
            {"id": "el_7", "type": "IMAGE_PLACEHOLDER", "x": 0.15, "y": 0.62, "width": 0.7, "height": 0.2, "cornerRadius": 16, "backgroundColor": "#020617", "borderColor": "#0284C7", "borderWidth": 1.5, "placeholderLabel": "فضای فروشگاه", "imageAsset": "coffee_01.webp", "contentScale": "CROP"},
            {"id": "el_8", "type": "BRAND_NAME", "x": 0.1, "y": 0.86, "width": 0.8, "height": 0.04, "text": "ثبت سفارش آنلاین در سایت به صورت ۲۴ ساعته 🌐", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#7DD3FC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_notice_holiday_square_67",
        "title": "اطلاعیه تعطیلی موقت فروشگاه",
        "category": "اطلاعیه",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["تعطیلی", "اطلاعیه", "انبار"],
        "background": {"type": "GRADIENT", "color": "#1C1917", "gradientColors": ["#292524", "#0C0A09"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "📢 اطلاعیه مهم", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#EA580C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.13, "width": 0.84, "height": 0.07, "text": "تعطیلی موقت جهت انبارگردانی سالانه", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FFEDD5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.23, "width": 0.8, "height": 0.44, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#292524", "borderColor": "#FB923C", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.28, "width": 0.7, "height": 0.15, "text": "مشتریان گرامی، ارسال سفارشات از ۲۵ تا ۲۸ اسفند موقتاً متوقف خواهد بود.\nسفارشات ثبت‌شده از ۲۹ اسفند با اولویت ارسال می‌شوند.", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FED7AA", "textAlign": "CENTER"},
            {"id": "el_5", "type": "TEXT", "x": 0.15, "y": 0.48, "width": 0.7, "height": 0.06, "text": "از صبوری و همراهی شما سپاسگزاریم 💐", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_6", "type": "BRAND_NAME", "x": 0.1, "y": 0.78, "width": 0.8, "height": 0.05, "text": "پشتیبانی دایرکت همچنان پاسخگوی شماست", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FDBA74", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_notice_post_story_68",
        "title": "اطلاعیه ارسال بسته‌های پستی",
        "category": "اطلاعیه",
        "format": "STORY",
        "isVip": False,
        "tags": ["کد_رهگیری", "پست", "ارسال"],
        "background": {"type": "IMAGE", "color": "#064E3B", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#064E3B", "#022C22"], "overlayAlpha": 0.8, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "📦 کد رهگیری بسته‌ها", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#059669"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "تمام سفارشات هفته تحویل اداره پست شد", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.2, "width": 0.8, "height": 0.38, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.26, "width": 0.7, "height": 0.12, "text": "کدهای رهگیری ۲۴ رقمی پست در کانال تلگرام و هایلایت پیج قرار گرفت.", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#A7F3D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "TEXT", "x": 0.15, "y": 0.4, "width": 0.7, "height": 0.08, "text": "جهت پیگیری آنلاین به سایت tracking.post.ir مراجعه کنید.", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_6", "type": "IMAGE_PLACEHOLDER", "x": 0.15, "y": 0.62, "width": 0.7, "height": 0.2, "cornerRadius": 16, "backgroundColor": "#022C22", "borderColor": "#10B981", "borderWidth": 1.5, "placeholderLabel": "بسته‌های پستی", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.86, "width": 0.8, "height": 0.04, "text": "از اعتماد شما بی‌نهایت متشکریم 🌸", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_notice_terms_portrait_69",
        "title": "قوانین ثبت سفارش و مرجوعی کالا",
        "category": "اطلاعیه",
        "format": "POST_PORTRAIT",
        "isVip": True,
        "tags": ["قوانین", "مرجوعی", "ضمانت"],
        "background": {"type": "GRADIENT", "color": "#1E1B4B", "gradientColors": ["#1E1B4B", "#0F0D24"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "📋 شرایط و قوانین خرید", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#7C3AED"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "راهنمای تعویض سایز و بازگشت کالا", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#EDE9FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.08, "y": 0.19, "width": 0.84, "height": 0.52, "shapeType": "ROUNDED_RECT", "cornerRadius": 22, "backgroundColor": "#0F0D24", "borderColor": "#A78BFA", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.12, "y": 0.23, "width": 0.76, "height": 0.12, "text": "۱. تا ۴۸ ساعت پس از دریافت بسته امکان تعویض سایز وجود دارد.\n۲. کالا نباید بوی عطر، شستشو یا لکه به خود گرفته باشد.", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#DDD6FE", "textAlign": "RIGHT"},
            {"id": "el_5", "type": "TEXT", "x": 0.12, "y": 0.38, "width": 0.76, "height": 0.12, "text": "۳. قبل از ارسال مرجوعی حتما با پشتیبانی هماهنگ نمایید.\n۴. هزینه ارسال مجدد در صورت تغییر سایز با مشتری محترم است.", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#DDD6FE", "textAlign": "RIGHT"},
            {"id": "el_6", "type": "IMAGE_PLACEHOLDER", "x": 0.15, "y": 0.54, "width": 0.7, "height": 0.14, "cornerRadius": 14, "backgroundColor": "#1E1B4B", "borderColor": "#8B5CF6", "borderWidth": 1, "placeholderLabel": "کالکشن پوشاک", "imageAsset": "fashion_01.webp", "contentScale": "CROP"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "رضایت و آرامش خیال شما اولویت ماست 🤝", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#C4B5FD", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_notice_job_square_70",
        "title": "دعوت به همکاری و استخدام پرسنل",
        "category": "اطلاعیه",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["استخدام", "کاریابی", "همکاری"],
        "background": {"type": "GRADIENT", "color": "#0F172A", "gradientColors": ["#1E293B", "#020617"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "💼 به تیم ما بپیوندید", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#3B82F6"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "فرصت شغلی: ادمین اینستاگرام و فروش", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#EFF6FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.22, "width": 0.8, "height": 0.44, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#020617", "borderColor": "#60A5FA", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.27, "width": 0.7, "height": 0.18, "text": "شرایط:\n• روابط عمومی قوی و پاسخگویی سریع به دایرکت\n• مسلط به الگوریتم و سناریونویسی استوری\n• حقوق ثابت + پورسانت فروش ماهانه", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#BFDBFE", "textAlign": "RIGHT"},
            {"id": "el_5", "type": "TEXT", "x": 0.15, "y": 0.48, "width": 0.7, "height": 0.06, "text": "ارسال رزومه به تلگرام یا واتساپ 📱", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_6", "type": "BRAND_CONTACT", "x": 0.1, "y": 0.8, "width": 0.8, "height": 0.05, "text": "شماره ارتباطی: ۰۹۱۲۳۴۵۶۷۸۹", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#93C5FD", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 6. نظر مشتری (Customer Reviews & Testimonials - 5 templates)
    # =========================================================================
    {
        "id": "tpl_review_unboxing_story_71",
        "title": "رضایت و آنباکسینگ بسته مشتری",
        "category": "نظر مشتری",
        "format": "STORY",
        "isVip": True,
        "tags": ["رضایت_مشتری", "آنباکسینگ", "نظرات"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#1C1917", "#09090B"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "⭐ رضایت شما افتخار ماست", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#F59E0B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "عکس ارسالی مشتری عزیزمون از تهران", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "عکس ارسالی مشتری", "imageAsset": "perfume_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.06, "text": "«کیفیت عطر فوق‌العاده بود و بسته‌بندی خیلی تمیز به دستم رسید. حتما بازم سفارش میدم!»", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.04, "text": "مبارکتون باشه و به شادی استفاده کنید 🌸", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_review_chat_square_72",
        "title": "اسکرین‌شات پیام رضایت در واتساپ",
        "category": "نظر مشتری",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["چت_مشتری", "رضایت", "اعتماد"],
        "background": {"type": "GRADIENT", "color": "#064E3B", "gradientColors": ["#064E3B", "#022C22"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "💬 پیام محبت‌آمیز شما", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#10B981"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.06, "text": "انرژی مثبت شما خستگی رو از تنمون درمیاره", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "SHAPE", "x": 0.1, "y": 0.21, "width": 0.8, "height": 0.48, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 1.5},
            {"id": "el_4", "type": "TEXT", "x": 0.15, "y": 0.28, "width": 0.7, "height": 0.15, "text": "«سلام عزیزم، بسته‌ام امروز رسید. پالتو خیلی خوش‌دوخته و دقیقاً همون چیزی بود که می‌خواستم. ممنون از ارسالتون!»", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_5", "type": "IMAGE_PLACEHOLDER", "x": 0.2, "y": 0.46, "width": 0.6, "height": 0.18, "cornerRadius": 14, "backgroundColor": "#064E3B", "borderColor": "#10B981", "borderWidth": 1, "placeholderLabel": "پالتوی ارسالی", "imageAsset": "fashion_01.webp", "contentScale": "CROP"},
            {"id": "el_6", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "اعتماد شما، بزرگترین سرمایه ماست 💚", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_review_stars_story_73",
        "title": "امتیاز و رضایت مشتریان وفادار",
        "category": "نظر مشتری",
        "format": "STORY",
        "isVip": False,
        "tags": ["امتیاز", "نظرات", "کیفیت"],
        "background": {"type": "IMAGE", "color": "#1C1014", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#290D15", "#13070B"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "⭐⭐⭐⭐⭐ پنج ستاره", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#BE123C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "طعم لذیذ و کیفیت بدون رقیب", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FFE4E6", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#13070B", "borderColor": "#FB7185", "borderWidth": 2, "placeholderLabel": "عکس کیک تولد مشتری", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.06, "text": "«کیک تولد پسرم بی‌نهایت تازه و کم‌شیرین بود. همه مهمون‌ها تعریف کردن!»", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FECDD3", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.04, "text": "خوشحالیم که سهمی در شادی‌های شما داریم 🎉", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FDA4AF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_review_shoe_portrait_74",
        "title": "رضایت از راحتی و کیفیت کفش",
        "category": "نظر مشتری",
        "format": "POST_PORTRAIT",
        "isVip": True,
        "tags": ["کفش", "رضایت_کتونی", "کیفیت"],
        "background": {"type": "GRADIENT", "color": "#18181B", "gradientColors": ["#27272A", "#09090B"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "👟 پیاده‌روی بدون خستگی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#22C55E"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#4ADE80", "borderWidth": 2, "placeholderLabel": "کتونی مشتری", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "«یک ماهه برای پیاده‌روی روزانه می‌پوشم، عالیه»", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "کفی نرم و تهویه هوای مناسب پا بدون بو گرفتگی", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#A1A1AA", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.83, "width": 0.8, "height": 0.05, "text": "ثبت رضایت بیش از ۹۸٪ مشتریان عزیز 💯", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#86EFAC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_review_flower_square_75",
        "title": "رضایت از سورپرایز و بسته‌بندی گل",
        "category": "نظر مشتری",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["گل", "سورپرایز", "کادو", "رضایت"],
        "background": {"type": "GRADIENT", "color": "#2E1065", "gradientColors": ["#2E1065", "#0F0728"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🌸 ثبت لبخند عزیزانتان", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#A855F7"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "«دقیقاً سر ساعت مقرر با کارت‌پستال تحویل شد»", "fontSize": 17, "fontWeight": "BOLD", "textColor": "#F3E8FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#0F0728", "borderColor": "#C084FC", "borderWidth": 2, "placeholderLabel": "باکس گل ارسالی", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "گل‌های بسیار باطراوت با عطر دلنشین و ماندگار", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#E9D5FF", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "خدمات گل‌آرایی و ارسال VIP گل‌فروشی 🚗", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#C084FC", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 7. سفارش‌گیری (Order Taking & Restocks - 5 templates)
    # =========================================================================
    {
        "id": "tpl_order_preorder_story_76",
        "title": "پیش‌ثبت‌نام و پیش‌فروش ویژه",
        "category": "سفارش‌گیری",
        "format": "STORY",
        "isVip": True,
        "tags": ["پیش_فروش", "سفارش", "کالکشن_جدید"],
        "background": {"type": "IMAGE", "color": "#1E1B4B", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#1E1B4B", "#0F0D24"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "💎 پیش‌فروش کالکشن جدید", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#7C3AED"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "رزرو اختصاصی با تخفیف ۱۵٪ پیش‌فروش", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#EDE9FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#0F0D24", "borderColor": "#A78BFA", "borderWidth": 2, "placeholderLabel": "کالکشن طلا یا کیف", "imageAsset": "jewelry_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "تحویل تضمینی ظرف ۷ روز کاری پس از ثبت نهایی", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#DDD6FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#6D28D9"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "ثبت پیش‌سفارش در دایرکت 💬", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "ظرفیت سفارش محدود است ✨", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#C4B5FD", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_order_food_story_77",
        "title": "رزرو آنلاین میز و سفارش غذا",
        "category": "سفارش‌گیری",
        "format": "STORY",
        "isVip": False,
        "tags": ["رزرو", "رستوران", "غذا", "کافه"],
        "background": {"type": "GRADIENT", "color": "#7C2D12", "gradientColors": ["#7C2D12", "#1C0800"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🍽️ رزرو آنلاین بدون معطلی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#EA580C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "رزرو میز برای دورهمی و مراسم تولد", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#FED7AA", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#1C0800", "borderColor": "#FB923C", "borderWidth": 2, "placeholderLabel": "میز رستوران و غذا", "imageAsset": "food_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "همراه با موزیک زنده و منوی غذاهای فرنگی و سنتی", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FFEDD5", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#C2410C"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "رزرو میز: تماس یا دایرکت", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_CONTACT", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "تلفن رزرو: ۰۹۱۲۳۴۵۶۷۸۹ 📞", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FDBA74", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_order_bag_square_78",
        "title": "سفارش رنگ اختصاصی کیف چرم",
        "category": "سفارش‌گیری",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["کیف", "سفارشی", "چرم"],
        "background": {"type": "GRADIENT", "color": "#1C1917", "gradientColors": ["#292524", "#0C0A09"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🎨 انتخاب رنگ سفارشی", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#D97706"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "دوخت اختصاصی کیف با چرم و رنگ دلخواه شما", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#0C0A09", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "کیف زنانه سفارشی", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "امکان حک پلاک اسم یا لوگو اختصاصی روی چرم", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "مدت زمان آماده‌سازی: فقط ۵ روز کاری ✂️", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#F59E0B", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_order_shoes_portrait_79",
        "title": "رزرو شارژ مجدد کتونی وارداتی",
        "category": "سفارش‌گیری",
        "format": "POST_PORTRAIT",
        "isVip": False,
        "tags": ["شارژ_مجدد", "رزرو_کفش", "کتونی"],
        "background": {"type": "GRADIENT", "color": "#18181B", "gradientColors": ["#27272A", "#09090B"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🔥 ثبت نام پارت جدید", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#22C55E"},
            {"id": "el_2", "type": "IMAGE_PLACEHOLDER", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.54, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#4ADE80", "borderWidth": 2, "placeholderLabel": "کتونی شارژ شده", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_3", "type": "TEXT", "x": 0.08, "y": 0.68, "width": 0.84, "height": 0.06, "text": "پارت جدید فردا می‌رسه، سایزتو رزرو کن!", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.75, "width": 0.8, "height": 0.05, "text": "تعداد این سری محدود است و سریع تموم میشه", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#A1A1AA", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.82, "width": 0.6, "height": 0.07, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#16A34A"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.835, "width": 0.56, "height": 0.05, "text": "سفارش در دایرکت با ارسال رایگان", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_order_cake_square_80",
        "title": "رزرو کیک آخر هفته و مناسبت‌ها",
        "category": "سفارش‌گیری",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["سفارش_کیک", "تولد", "قنادی"],
        "background": {"type": "GRADIENT", "color": "#701A75", "gradientColors": ["#701A75", "#2E0332"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "🎂 رزرو کیک پایان هفته", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#C026D3"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "سفارش آنلاین با طرح و طعم سفارشی", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FDF4FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#2E0332", "borderColor": "#E879F9", "borderWidth": 2, "placeholderLabel": "کیک تولد", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "فیلینگ نوتلا، موز و گردو با خامه ارگانیک", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#F5D0FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "ارسال با بسته‌بندی محکم و بدون آسیب 🚗", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#E879F9", "textAlign": "CENTER"}
        ]
    },

    # =========================================================================
    # 8. تبریک (Celebrations & Milestones - 5 templates)
    # =========================================================================
    {
        "id": "tpl_congrats_birthday_story_81",
        "title": "استوری تبریک تولد شیک و لوکس",
        "category": "تبریک",
        "format": "STORY",
        "isVip": True,
        "tags": ["تبریک_تولد", "تولد", "جشن"],
        "background": {"type": "IMAGE", "color": "#1C1014", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#38131E", "#13070B"], "overlayAlpha": 0.7, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🎉 HAPPY BIRTHDAY", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#BE123C"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "تولدت مبارک بهترین دوست و همراه من", "fontSize": 20, "fontWeight": "BOLD", "textColor": "#FFE4E6", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#13070B", "borderColor": "#FB7185", "borderWidth": 2, "placeholderLabel": "کیک یا هدیه تولد", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "برات آرزوی شادی، سلامتی و رسیدن به همه رویاهات دارم 🎂", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FECDD3", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.04, "text": "جشن تولدت پر از لبخند و خاطره‌های قشنگ ✨", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FDA4AF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_congrats_anniversary_square_82",
        "title": "پست سالگرد ازدواج و آشنایی",
        "category": "تبریک",
        "format": "POST_SQUARE",
        "isVip": True,
        "tags": ["سالگرد", "ازدواج", "عشق", "تبریک"],
        "background": {"type": "GRADIENT", "color": "#4A044E", "gradientColors": ["#4A044E", "#1F0121"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "💍 سالگرد عشقمون مبارک", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#C026D3"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "یک سال دیگر در کنار تو با عشق سپری شد", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FAE8FF", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#1F0121", "borderColor": "#E879F9", "borderWidth": 2, "placeholderLabel": "حلقه یا هدیه سالگرد", "imageAsset": "jewelry_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "بهترین اتفاق زندگی‌ام، همسفر همیشگی من", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#F5D0FE", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "تا همیشه با عشق در کنار هم ❤️", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#E879F9", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_congrats_graduation_story_83",
        "title": "استوری فارغ‌التحصیلی و موفقیت",
        "category": "تبریک",
        "format": "STORY",
        "isVip": False,
        "tags": ["فارغ_التحصیلی", "موفقیت", "دانشگاه"],
        "background": {"type": "IMAGE", "color": "#0F172A", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#0F172A", "#020617"], "overlayAlpha": 0.75, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "🎓 تبریک فارغ‌التحصیلی", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#0284C7"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "موفقیت درخشان و فارغ‌التحصیلی‌ات مبارک", "fontSize": 19, "fontWeight": "BOLD", "textColor": "#E0F2FE", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#020617", "borderColor": "#38BDF8", "borderWidth": 2, "placeholderLabel": "دسته گل فارغ‌التحصیلی", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "آغاز فصلی نوین در مسیر پیشرفت و موفقیت‌های بزرگ", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#BAE6FD", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.85, "width": 0.8, "height": 0.04, "text": "همیشه در اوج بدرخشی عزیز دلم 🌟", "fontSize": 13, "fontWeight": "BOLD", "textColor": "#7DD3FC", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_congrats_newborn_square_84",
        "title": "پست قدم نو رسیده و تبریک نوزاد",
        "category": "تبریک",
        "format": "POST_SQUARE",
        "isVip": False,
        "tags": ["قدم_نو_رسیده", "تولد_نوزاد", "تبریک"],
        "background": {"type": "GRADIENT", "color": "#064E3B", "gradientColors": ["#064E3B", "#022C22"], "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.3, "y": 0.06, "width": 0.4, "height": 0.045, "text": "👶 قدم نو رسیده مبارک", "fontSize": 11, "fontWeight": "BOLD", "textColor": "#FFFFFF", "backgroundColor": "#10B981"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.12, "width": 0.84, "height": 0.07, "text": "شکفتن گل زندگی‌تان فرخنده باد", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.12, "y": 0.21, "width": 0.76, "height": 0.48, "cornerRadius": 22, "backgroundColor": "#022C22", "borderColor": "#34D399", "borderWidth": 2, "placeholderLabel": "گل و هدیه نوزاد", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.72, "width": 0.8, "height": 0.05, "text": "با آرزوی سلامت، شادی و عاقبت‌بخیری برای فرشته کوچولو", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#A7F3D0", "textAlign": "CENTER"},
            {"id": "el_5", "type": "BRAND_NAME", "x": 0.1, "y": 0.82, "width": 0.8, "height": 0.05, "text": "تبریک صمیمانه از طرف فروشگاه ما 🍼", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#6EE7B7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_congrats_opening_story_85",
        "title": "استوری افتتاحیه شعبه جدید فروشگاه",
        "category": "تبریک",
        "format": "STORY",
        "isVip": True,
        "tags": ["افتتاحیه", "شعبه_جدید", "جشن"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#1C1917", "#09090B"], "overlayAlpha": 0.7, "angle": 135},
        "elements": [
            {"id": "el_1", "type": "BADGE", "x": 0.28, "y": 0.05, "width": 0.44, "height": 0.04, "text": "✂️ روبان افتتاحیه", "fontSize": 12, "fontWeight": "BOLD", "textColor": "#000000", "backgroundColor": "#F59E0B"},
            {"id": "el_2", "type": "TEXT", "x": 0.08, "y": 0.11, "width": 0.84, "height": 0.06, "text": "افتتاح رسمی شعبه ۲ در مجتمع تجاری کوروش", "fontSize": 18, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "IMAGE_PLACEHOLDER", "x": 0.1, "y": 0.19, "width": 0.8, "height": 0.48, "cornerRadius": 24, "backgroundColor": "#09090B", "borderColor": "#FBBF24", "borderWidth": 2, "placeholderLabel": "شیرینی افتتاحیه", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_4", "type": "TEXT", "x": 0.1, "y": 0.7, "width": 0.8, "height": 0.05, "text": "۳۰٪ تخفیف ویژه به مدت ۳ روز برای تمام مراجعین", "fontSize": 13, "fontWeight": "NORMAL", "textColor": "#FDE68A", "textAlign": "CENTER"},
            {"id": "el_5", "type": "SHAPE", "x": 0.2, "y": 0.78, "width": 0.6, "height": 0.065, "shapeType": "ROUNDED_RECT", "cornerRadius": 20, "backgroundColor": "#D97706"},
            {"id": "el_6", "type": "TEXT", "x": 0.22, "y": 0.795, "width": 0.56, "height": 0.05, "text": "دیدار ما: پنجشنبه ساعت ۱۷ 🎈", "fontSize": 14, "fontWeight": "BOLD", "textColor": "#FFFFFF", "textAlign": "CENTER"},
            {"id": "el_7", "type": "BRAND_NAME", "x": 0.1, "y": 0.87, "width": 0.8, "height": 0.04, "text": "پذیرایی و هدایای ویژه در مراسم افتتاحیه", "fontSize": 12, "fontWeight": "NORMAL", "textColor": "#FCD34D", "textAlign": "CENTER"}
        ]
    }
]

# Add remaining to reach 60 templates total (9 HIGHLIGHT covers, more stories & square/portrait)
# We already have:
# Story: yalda(26), nowruz(28), mother(30), father(32), ramadan(34), moharram(36), fitr(38), blackfriday(39), school(41), prod_headphone(43), prod_watch(46), prod_burger(48), prod_tote(51), sale_flash(53), sale_clearance(55), sale_cafe(58), sale_flower(60), price_beauty(61), price_confectionery(63), notice_hours(66), notice_post(68), review_unboxing(71), review_stars(73), order_preorder(76), order_food(77), congrats_birthday(81), congrats_graduation(83), congrats_opening(85) -> Total 28 stories! (46%)
# Square: yalda(27), nowruz(29), mother(31), ramadan(35), moharram(37), blackfriday(40), prod_skincare(45), prod_bakery(47), prod_perfume(50), prod_cupcake(52), sale_weekend(54), sale_gadget(56), sale_gold(59), price_cafe(62), price_bags(65), notice_holiday(67), notice_job(70), review_chat(72), review_flower(75), order_bag(78), order_cake(80), congrats_anniversary(82), congrats_newborn(84) -> 23 square!
# Portrait: father(33), school(42), prod_coat(44), prod_boots(49), sale_beauty(57), price_shoes(64), notice_terms(69), review_shoe(74), order_shoes(79) -> 9 portrait! (15%)

# 9 HIGHLIGHT templates:
highlights = [
    {
        "id": "tpl_hl_product_86",
        "title": "کاور هایلایت محصولات فروشگاه",
        "category": "معرفی محصول",
        "format": "HIGHLIGHT",
        "isVip": False,
        "tags": ["هایلایت", "محصولات", "کاور"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#1C1917", "#09090B"], "overlayAlpha": 0.8, "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#09090B", "borderColor": "#F59E0B", "borderWidth": 4, "placeholderLabel": "محصولات", "imageAsset": "bag_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "محصولات", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_sale_87",
        "title": "کاور هایلایت حراج و تخفیف",
        "category": "تخفیف و حراج",
        "format": "HIGHLIGHT",
        "isVip": True,
        "tags": ["هایلایت", "حراج", "تخفیف"],
        "background": {"type": "IMAGE", "color": "#4C0519", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#4C0519", "#1E020D"], "overlayAlpha": 0.8, "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#1E020D", "borderColor": "#F43F5E", "borderWidth": 4, "placeholderLabel": "حراجی", "imageAsset": "shoes_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "حراج ویژه", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#FFE4E6", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_price_88",
        "title": "کاور هایلایت تعرفه و قیمت",
        "category": "قیمت‌نامه",
        "format": "HIGHLIGHT",
        "isVip": False,
        "tags": ["هایلایت", "قیمت", "تعرفه"],
        "background": {"type": "GRADIENT", "color": "#064E3B", "gradientColors": ["#064E3B", "#022C22"], "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#022C22", "borderColor": "#10B981", "borderWidth": 4, "placeholderLabel": "قیمت‌ها", "imageAsset": "jewelry_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "قیمت‌نامه", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#D1FAE5", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_review_89",
        "title": "کاور هایلایت رضایت مشتریان",
        "category": "نظر مشتری",
        "format": "HIGHLIGHT",
        "isVip": True,
        "tags": ["هایلایت", "رضایت", "نظرات"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_marble_01.webp", "overlayColors": ["#1C1917", "#0C0A09"], "overlayAlpha": 0.75, "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#0C0A09", "borderColor": "#FBBF24", "borderWidth": 4, "placeholderLabel": "رضایت", "imageAsset": "perfume_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "رضایت شما", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_order_90",
        "title": "کاور هایلایت نحوه ثبت سفارش",
        "category": "سفارش‌گیری",
        "format": "HIGHLIGHT",
        "isVip": False,
        "tags": ["هایلایت", "سفارش", "آموزش_خرید"],
        "background": {"type": "GRADIENT", "color": "#1E1B4B", "gradientColors": ["#1E1B4B", "#0F0D24"], "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#0F0D24", "borderColor": "#8B5CF6", "borderWidth": 4, "placeholderLabel": "نحوه سفارش", "imageAsset": "cake_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "نحوه سفارش", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#EDE9FE", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_cafe_91",
        "title": "کاور هایلایت کافه و نوشیدنی",
        "category": "معرفی محصول",
        "format": "HIGHLIGHT",
        "isVip": False,
        "tags": ["هایلایت", "کافه", "قهوه"],
        "background": {"type": "IMAGE", "color": "#1C140E", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#382013", "#120A05"], "overlayAlpha": 0.75, "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#120A05", "borderColor": "#FB923C", "borderWidth": 4, "placeholderLabel": "منو کافه", "imageAsset": "coffee_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "کافه منو", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#FED7AA", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_flower_92",
        "title": "کاور هایلایت گل‌آرایی و هدیه",
        "category": "معرفی محصول",
        "format": "HIGHLIGHT",
        "isVip": True,
        "tags": ["هایلایت", "گل", "کادو"],
        "background": {"type": "GRADIENT", "color": "#2E1065", "gradientColors": ["#2E1065", "#0F0728"], "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#0F0728", "borderColor": "#C084FC", "borderWidth": 4, "placeholderLabel": "باکس گل", "imageAsset": "flower_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "باکس هدیه", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#F3E8FF", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_digital_93",
        "title": "کاور هایلایت آنباکس لوازم دیجیتال",
        "category": "معرفی محصول",
        "format": "HIGHLIGHT",
        "isVip": False,
        "tags": ["هایلایت", "دیجیتال", "آنباکس"],
        "background": {"type": "GRADIENT", "color": "#0F172A", "gradientColors": ["#1E293B", "#020617"], "angle": 45},
        "elements": [
            {"id": "el_1", "type": "IMAGE_PLACEHOLDER", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "cornerRadius": 100, "backgroundColor": "#020617", "borderColor": "#38BDF8", "borderWidth": 4, "placeholderLabel": "گجت‌ها", "imageAsset": "digital_01.webp", "contentScale": "CROP"},
            {"id": "el_2", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "گجت و تکنولوژی", "fontSize": 15, "fontWeight": "BOLD", "textColor": "#E0F2FE", "textAlign": "CENTER"}
        ]
    },
    {
        "id": "tpl_hl_contact_94",
        "title": "کاور هایلایت آدرس و تماس با ما",
        "category": "اطلاعیه",
        "format": "HIGHLIGHT",
        "isVip": False,
        "tags": ["هایلایت", "تماس", "آدرس"],
        "background": {"type": "IMAGE", "color": "#1C1917", "imageAsset": "bg_story_wood_01.webp", "overlayColors": ["#1C1917", "#09090B"], "overlayAlpha": 0.75, "angle": 45},
        "elements": [
            {"id": "el_1", "type": "SHAPE", "x": 0.22, "y": 0.22, "width": 0.56, "height": 0.56, "shapeType": "CIRCLE", "backgroundColor": "#09090B", "borderColor": "#EAB308", "borderWidth": 4},
            {"id": "el_2", "type": "TEXT", "x": 0.25, "y": 0.42, "width": 0.5, "height": 0.15, "text": "📍\nتماس با ما", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#FEF3C7", "textAlign": "CENTER"},
            {"id": "el_3", "type": "TEXT", "x": 0.15, "y": 0.8, "width": 0.7, "height": 0.08, "text": "شعب و آدرس", "fontSize": 16, "fontWeight": "BOLD", "textColor": "#FDE68A", "textAlign": "CENTER"}
        ]
    }
]

templates.extend(highlights)

# Write each template to a JSON file
count = 0
for tpl in templates:
    file_name = f"{tpl['id']}.json"
    file_path = os.path.join(out_dir, file_name)
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(tpl, f, ensure_ascii=False, indent=2)
    count += 1

print(f"Successfully generated {count} new templates in {out_dir}")
