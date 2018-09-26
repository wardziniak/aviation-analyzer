package com.wardziniak.aviation.importer.external.model

case class AirportDTO(
  airportId: String,
  nameAirport: String,
  codeIataAirport: String,
  codeIcaoAirport: String,
  latitudeAirport: String,
  longitudeAirport: String,
  geonameId: String,
  timezone: String,
  GMT: String,
  phone: String,
  nameCountry: String,
  codeIso2Country: String,
  codeIataCity: String)
