package sk.mkdigital.androidshowcase.data.dto.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.data.dto.LanguageDTO
import sk.mkdigital.androidshowcase.data.network.NetworkModule

class AuthResponseDTOTest {

    @Test
    fun `a sign in answer carries the account language by its key`() {
        assertEquals(LanguageDTO.SK, signInAnswering(language = "SK").user.language)
    }

    @Test
    fun `a sign in answer with a language this build does not know opens in English`() {
        assertEquals(LanguageDTO.EN, signInAnswering(language = "FR").user.language)
    }

    @Test
    fun `a sign in answer that names no language opens in English`() {
        assertEquals(LanguageDTO.EN, signInAnswering(language = null).user.language)
    }

    private fun signInAnswering(language: String?): AuthResponseDTO {
        val named = language?.let { ""","language":"$it"""" }.orEmpty()
        return NetworkModule.json.decodeFromString(
            """{"token":"t0k3n","user":{"id":1,"email":"dana@example.com","themeMode":"system"$named}}""",
        )
    }
}
