package com.wardziniak.aviation.importer.airports

import com.wardziniak.aviation.api.model.Airport
import com.wardziniak.aviation.importer.external.model.DTO
import com.wardziniak.aviation.importer.flights.FlightSnapshotDTO
import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.BodyReadable

import scala.language.implicitConversions

case class AirportDTO(
  airportId: String,
  nameAirport: String,
  codeIataAirport: String,
  codeIcaoAirport: Option[String],
  nameTranslations: String,
  latitudeAirport: String,
  longitudeAirport: String,
  geonameId: String,
  timezone: String,
  GMT: String,
  phone: String,
  nameCountry: String,
  codeIso2Country: String,
  codeIataCity: String
) extends DTO

object AirportDTO {

  implicit val AirportFormat: OFormat[AirportDTO] = Json.format[AirportDTO]

  implicit val readableAsAirport: BodyReadable[List[AirportDTO]] = BodyReadable { response =>
    val d = Json.fromJson[List[AirportDTO]](Json.parse(response.bodyAsBytes.toArray))
    d.get
  }

  implicit def asAirport(dto: AirportDTO): Airport = Airport(
    id = dto.airportId,
    name = dto.nameAirport,
    codeIata = dto.codeIataAirport,
    codeIcao = dto.codeIcaoAirport.getOrElse(""),
    latitude = dto.latitudeAirport.toDouble,
    longitude = dto.longitudeAirport.toDouble,
    nameCountry = dto.nameCountry,
    codeIso2Country = dto.codeIso2Country,
    codeIataCity = dto.codeIataCity
  )
}


/**
  * {
  * "airportId": "3",
  * "nameAirport": "El Arish International Airport",
  * "codeIataAirport": "AAC",
  * "codeIcaoAirport": "HEAR",
  * "nameTranslations": "El Arish International Airport,?????",
  * "latitudeAirport": "31.133333",
  * "longitudeAirport": "33.75",
  * "geonameId": "6297289",
  * "timezone": "Africa/Cairo",
  * "GMT": "2",
  * "phone": "",
  * "nameCountry": "Egypt",
  * "codeIso2Country": "EG",
  * "codeIataCity": "AAC"
  * },
  */
