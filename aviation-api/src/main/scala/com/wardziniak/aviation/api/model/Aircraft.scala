package com.wardziniak.aviation.api.model

case class Aircraft(
  id: String,
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
  enginesCount: Int,
  enginesType: String,
  planeClasses: List[PlaneClass]
)

sealed trait ClassType
object Economy extends ClassType
object Business extends ClassType
object First extends ClassType

case class PlaneClass(classType: ClassType, seats: Int, pitch: String, bedType: String)

