package sk.mkdigital.androidshowcase.data.dto.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.data.dto.LanguageDTO
import sk.mkdigital.androidshowcase.data.network.NetworkModule

class UserResponseDTOTest {

    @Test
    fun `the signed in user carries the account language by its key`() {
        assertEquals(LanguageDTO.SK, userAnswering(language = "SK").language)
    }

    @Test
    fun `the signed in user with a language this build does not know reads as English`() {
        assertEquals(LanguageDTO.EN, userAnswering(language = "FR").language)
    }

    private fun userAnswering(language: String): UserResponseDTO =
        NetworkModule.json.decodeFromString(
            """{"id":1,"email":"dana@example.com","createdAt":0,"themeMode":"system","language":"$language"}""",
        )
}
