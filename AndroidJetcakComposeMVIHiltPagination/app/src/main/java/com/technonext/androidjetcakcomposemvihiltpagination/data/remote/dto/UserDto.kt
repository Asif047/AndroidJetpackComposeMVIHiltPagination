package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto

// 📄 data/remote/dto/UserDto.kt

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Address
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Geo
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Company

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressDto,
    val phone: String,
    val website: String,
    val company: CompanyDto
)

data class AddressDto(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GeoDto
)

data class GeoDto(
    val lat: String,
    val lng: String
)

data class CompanyDto(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

// Extension functions to convert DTOs to domain models
fun UserDto.toDomainModel(): User {
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        address = address.toDomainModel(),
        phone = phone,
        website = website,
        company = company.toDomainModel()
    )
}

fun AddressDto.toDomainModel(): Address {
    return Address(
        street = street,
        suite = suite,
        city = city,
        zipcode = zipcode,
        geo = geo.toDomainModel()
    )
}

fun GeoDto.toDomainModel(): Geo {
    return Geo(
        lat = lat,
        lng = lng
    )
}

fun CompanyDto.toDomainModel(): Company {
    return Company(
        name = name,
        catchPhrase = catchPhrase,
        bs = bs
    )
}