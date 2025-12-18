package com.example.pepeselforderingapp.data.model

// Request models
data class CreateOrderRequest(
    val outlet_id: Int,
    val table_no: String,
    val user_id: Int,
    val order_item: List<OrderItemRequest>
)

data class OrderItemRequest(
    val menu_id: Int,
    val quantity: Int,
    val subitems: List<SubitemRequest>
)

data class SubitemRequest(
    val menu_id: Int,
    val quantity: Int = 1
)

// Response models
data class CreateOrderResponse(
    val success: Boolean,
    val message: String,
    val data: OrderData
)

data class OrderData(
    val id: Int,
    val uid: String,
    val o_id: Int,
    val table_no: String,
    val u_id: Int,
    val tax: Double,
    val sc: Double,
    val subtotal: Int,
    val grand_total: Int,
    val order_item: String,  // JSON string
    val created_at: String? = null
)

data class TrackOrderResponse(
    val success: Boolean,
    val data: TrackOrderData
)

data class TrackOrderData(
    val id: Int,
    val uid: String,
    val o_id: Int,
    val table_no: String,
    val u_id: Int,
    val tax: Double,
    val sc: Double,
    val subtotal: Int,
    val grand_total: Int,
    val or_order_item: OrderItemDetails,  // Changed from order_item to or_order_item
    val created_at: String,
    val outlet: OutletInfo,
    val user: UserInfo
)

data class OrderItemDetails(
    val items: List<OrderItemDetail>,
    val summary: OrderSummary
)

data class OrderItemDetail(
    val menu_id: Int,  // Changed from id to menu_id
    val quantity: Int,
    val price: Int,
    val total: Int,
    val subitems: List<SubitemDetail>,  // Changed to proper type
    val name: String  // Added name field
)

data class SubitemDetail(
    val menu_id: Int,
    val name: String
)

data class OrderSummary(
    val subtotal: String,
    val service_charge: String,  // Changed from charge to service_charge
    val tax: String,
    val grand_total: String  // Changed from total to grand_total
)

data class OutletInfo(
    val id: Int,
    val name: String
)

data class UserInfo(
    val id: Int,
    val email: String
)
