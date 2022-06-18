package com.breckworld.model.userprofile

data class UserProfileModel(
    val button_badges: ButtonBadges,
    val links: List<Link>,
    val profile: Profile,
    val error: String,
    val error_description: String
)