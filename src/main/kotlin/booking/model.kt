package booking

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val shipReference: String,
    val shipToken: String,
    val canIssueTicketChecking: Boolean,
    val expiryTime: String,
    val duration: Int,
    val segments: List<Segment>
)

@Serializable
data class Segment(
    val id: Int,
    val originAndDestinationPair: OriginAndDestinationPair
)

@Serializable
data class OriginAndDestinationPair(
    val destination: Port,
    val destinationCity: String,
    val origin: Port,
    val originCity: String
)

@Serializable
data class Port(
    val code: String,
    val displayName: String,
    val url: String
)