package com.wardziniak.aviation.importer.external.model


import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.BodyReadable

case class AircraftDTO(
  airplaneId: String,
  numberRegistration: String,
  productionLine: String,
  airplaneIataType: String,
  planeModel: String,
  modelCode: String,
  firstFlight: String,
  deliveryDate: String,
  registrationDate: String,
  planeSeries: String,
  codeIataAirline: String,
  codeIcaoAirline: String,
  enginesCount: String,
  enginesType: String,
  planeClass: Option[List[PlaneClassDTO]]
) extends ExternalObject

case class PlaneClassDTO(name: String, seats: String, pitch: String, bedType: String) extends ExternalObject

object AircraftDTO {
  implicit val PlaneClassFormat: OFormat[PlaneClassDTO] = Json.format[PlaneClassDTO]
  implicit val AircraftDTOFormat: OFormat[AircraftDTO] = Json.format[AircraftDTO]

  implicit val readableAsAircraftDTO: BodyReadable[List[AircraftDTO]] = BodyReadable { response =>
    Json.fromJson[List[AircraftDTO]](Json.parse(response.bodyAsBytes.toArray)).get
  }
}