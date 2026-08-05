package sk.mkdigital.androidshowcase.data.base

interface TransformToDomainModel<out DomainModel> {
    fun transform(): DomainModel
}
