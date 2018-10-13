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
  planeClasses: Option[List[PlaneClass]]
) extends Value

sealed trait ClassType
object Economy extends ClassType
object Business extends ClassType
object First extends ClassType

case class PlaneClass(classType: String, seats: Int, pitch: String, bedType: String)

