package com.wardziniak.aviation.api.model



case class Airport(
  id: String,
  name: String,
  codeIata: String,
  codeIcao: String,
  latitude: BigDecimal,
  longitude: BigDecimal,
  geonameId: String,
  timezone: String,
  gmt: String,
  nameCountry: String,
  codeIso2Country: String,
  codeIataCity: String)
