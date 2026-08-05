package sk.mkdigital.androidshowcase.domain.useCase.calendar

import kotlinx.datetime.LocalDate
import sk.mkdigital.androidshowcase.domain.repository.DateRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class GetTodayDateUseCase @Inject constructor(
    private val dateRepository: DateRepository,
) : UseCase<None, LocalDate>() {
    override suspend fun run(params: None): LocalDate = dateRepository.today()
}
