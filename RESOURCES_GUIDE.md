# Pepe Self Ordering App - Resources Guide

## Color Palette ✅ COMPLETED

The following colors have been set up in `Color.kt`:

- **#FFF4DF** - `CreamBackground` - Main background color
- **#EF7810** - `OrangePrimary` - Primary buttons and accents
- **#D26E00** - `OrangeDark` - Darker orange for pressed states
- **#544941** - `BrownDark` - Dark text and borders
- **#95A397** - `GreenMuted` - Secondary elements
- **#B6D9C3** - `GreenLight` - Light accents
- **#EADCC1** - `BeigeLight` - Card backgrounds

## Where to Put Icons

### 1. Vector Icons (Recommended - XML format)
**Location:** `app/src/main/res/drawable/`

Place your icon files here with lowercase names using underscores:
- `ic_login.xml`
- `ic_register.xml`
- `ic_qr_scan.xml`
- `ic_menu.xml`
- `ic_cart.xml`
- `ic_search.xml`
- `ic_add.xml`
- `ic_remove.xml`
- `ic_delete.xml`
- `ic_profile.xml`
- `ic_logout.xml`
- `ic_receipt.xml`
- `ic_back.xml`
- `ic_close.xml`
- `ic_check.xml`

**Example:** Convert your SVG icons to Android Vector Drawable XML format.

### 2. PNG Icons (if needed)
If you have PNG icons, place them in density-specific folders:
- `app/src/main/res/drawable-mdpi/` (48x48 dp → ~48px)
- `app/src/main/res/drawable-hdpi/` (48x48 dp → ~72px)
- `app/src/main/res/drawable-xhdpi/` (48x48 dp → ~96px)
- `app/src/main/res/drawable-xxhdpi/` (48x48 dp → ~144px)
- `app/src/main/res/drawable-xxxhdpi/` (48x48 dp → ~192px)

### 3. Food/Product Images
**Location:** `app/src/main/res/drawable/`

Name examples:
- `food_burger.png`
- `food_pizza.png`
- `food_pasta.png`
- `category_main_course.png`
- `category_drinks.png`

## Where to Put Fonts ✅ FOLDER CREATED

**Location:** `app/src/main/res/font/`

### Font File Naming Convention
Use lowercase with underscores. For example:
- `poppins_regular.ttf`
- `poppins_medium.ttf`
- `poppins_semibold.ttf`
- `poppins_bold.ttf`
- `roboto_regular.ttf`
- `inter_regular.ttf`

### Supported Font Formats
- `.ttf` (TrueType Font)
- `.otf` (OpenType Font)

### Recommended Fonts for Food Ordering App
- **Poppins** - Modern, clean, great for UI
- **Inter** - Excellent readability
- **Roboto** - Android standard, professional
- **Montserrat** - Great for headings

### How to Download Fonts
1. Visit [Google Fonts](https://fonts.google.com/)
2. Search for your preferred font (e.g., "Poppins")
3. Download the font family
4. Extract the TTF files
5. Rename to lowercase with underscores
6. Copy to `app/src/main/res/font/`

## Recommended Icons for the App

### Authentication Pages
- Email icon
- Password/Lock icon
- Eye icon (show/hide password)
- User/Profile icon

### QR Scanner Page
- QR code icon
- Camera icon
- Flashlight icon

### Menu/Main Page
- Search icon
- Category icons (burger, pizza, drinks, dessert, etc.)
- Plus/Add icon
- Shopping cart icon

### Cart Page
- Trash/Delete icon
- Minus icon
- Plus icon
- Checkout icon

### Receipt Page
- Check/Success icon
- Receipt/Document icon
- Home icon

### Navigation
- Back arrow
- Menu hamburger
- Close/X icon
- Settings icon

## Icon Resources

### Free Icon Sources
1. **Material Icons** - https://fonts.google.com/icons
2. **Iconify** - https://icon-sets.iconify.design/
3. **Flaticon** - https://www.flaticon.com/
4. **Feather Icons** - https://feathericons.com/

### Converting SVG to Android Vector Drawable
1. In Android Studio: Right-click `drawable` folder
2. Select **New → Vector Asset**
3. Choose **Local file** and select your SVG
4. Android Studio will convert it automatically

## Next Steps

After placing icons and fonts:
1. Icons will be accessible via `R.drawable.icon_name`
2. Fonts will be configured in `Type.kt`
3. Components will be created using these resources
4. Pages will be built from components

## Current Status
- ✅ Colors configured
- ✅ Strings resources added
- ✅ Font folder created
- ⏳ Awaiting icons placement
- ⏳ Awaiting fonts placement
- ⏳ Typography configuration pending
- ⏳ Components to be created
- ⏳ Pages to be created

