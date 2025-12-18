package com.example.pepeselforderingapp.data.model

data class OutletMenusResponse(
    val success: Boolean,
    val data: List<MenuCategoryData>
)

data class MenuCategoryData(
    val category: String,
    val menus: List<MenuItemData>
)

data class MenuItemData(
    val id: Int,
    val m_id: Int,
    val o_id: Int,
    val price: Int,
    val stock: Int?,
    val is_selling: Boolean,
    val sku: String,
    val name: String,
    val desc: String,
    val category: String,
    val picture_url: String?,
    val picture_path: String?,
    val subitems: List<SubitemData>
)

data class SubitemData(
    val id: Int,
    val sku: String,
    val name: String,
    val desc: String,
    val category: String,
    val url: String?,
    val path: String?,
    val price: Int?,
    val stock: Int?,
    val is_selling: Boolean
)

// Search response models
data class SearchMenuResponse(
    val success: Boolean,
    val data: List<SearchMenuCategory>
)

data class SearchMenuCategory(
    val category: String,
    val menus: List<SearchMenuItem>
)

data class SearchMenuItem(
    val id: Int,
    val m_id: Int,
    val o_id: Int,
    val price: Int,
    val stock: Int?,
    val is_selling: Boolean,
    val created_at: String,
    val updated_at: String,
    val menu: SearchMenuDetail
)

data class SearchMenuDetail(
    val id: Int,
    val sku: String,
    val name: String,
    val desc: String,
    val category: String,
    val picture_url: String?,
    val picture_path: String?
)
