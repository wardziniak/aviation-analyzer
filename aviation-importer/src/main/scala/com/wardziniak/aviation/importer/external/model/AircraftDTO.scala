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
  enginesType: String//,
  //planeClasses: List[PlaneClassDTO]
) extends ExternalObject

sealed trait ClassTypeDTO
object Economy extends ClassTypeDTO
object Business extends ClassTypeDTO
object First extends ClassTypeDTO

case class PlaneClassDTO(classType: ClassTypeDTO, seats: Int, pitch: String, bedType: String) extends ExternalObject

object AircraftDTO {
  //implicit val ClassTypeDTOFormat: OFormat[ClassTypeDTO] = Json.format[ClassTypeDTO]
  //implicit val PlaneClassFormat: OFormat[PlaneClassDTO] = Json.format[PlaneClassDTO]
  implicit val AircraftDTOFormat: OFormat[AircraftDTO] = Json.format[AircraftDTO]

  implicit val readableAsAircraftDTO: BodyReadable[List[AircraftDTO]] = BodyReadable { response =>
    Json.fromJson[List[AircraftDTO]](Json.parse(response.bodyAsBytes.toArray)).get
  }
}