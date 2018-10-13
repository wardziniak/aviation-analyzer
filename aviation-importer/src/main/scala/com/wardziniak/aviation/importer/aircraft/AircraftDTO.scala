package com.wardziniak.aviation.importer.aircraft

import com.wardziniak.aviation.api.model.{Aircraft, PlaneClass}
import com.wardziniak.aviation.importer.external.model.ExternalObject
import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.BodyReadable

import scala.language.implicitConversions

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
  planeClasses: Option[List[PlaneClassDTO]]
) extends ExternalObject

case class PlaneClassDTO(name: String, seats: String, pitch: String, bedType: String) extends ExternalObject

object AircraftDTO {
  implicit val PlaneClassFormat: OFormat[PlaneClassDTO] = Json.format[PlaneClassDTO]
  implicit val AircraftDTOFormat: OFormat[AircraftDTO] = Json.format[AircraftDTO]

  implicit val readableAsAircraftDTO: BodyReadable[List[AircraftDTO]] = BodyReadable { response =>
    Json.fromJson[List[AircraftDTO]](Json.parse(response.bodyAsBytes.toArray)).get
  }

  implicit def asPlaneClass(dto: PlaneClassDTO): PlaneClass = PlaneClass(
    classType = dto.name,
    seats = dto.seats.toInt,
    pitch = dto.pitch,
    bedType = dto.bedType
  )

  implicit def asAircraft(dto: AircraftDTO): Aircraft = Aircraft(
    id = dto.airplaneId,
    numberRegistration = dto.numberRegistration,
    productionLine = dto.productionLine,
    airplaneIataType = dto.airplaneIataType,
    planeModel = dto.planeModel,
    modelCode = dto.modelCode,
    firstFlight = dto.firstFlight,
    deliveryDate = dto.deliveryDate,
    registrationDate = dto.registrationDate,
    planeSeries = dto.planeSeries,
    codeIataAirline = dto.codeIataAirline,
    codeIcaoAirline = dto.codeIcaoAirline,
    enginesCount = dto.enginesCount.toInt,
    enginesType= dto.enginesType,
    planeClasses= dto.planeClasses.map(classes => classes.map(asPlaneClass))
  )
}