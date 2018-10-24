package com.wardziniak.aviation.importer.api

sealed trait Action

trait DownloadAction extends Action {
  def endpoint: String
  def queryParameters: String = ""
}

object FlightDownloadAction extends DownloadAction {
  override def endpoint: String = "flights"
}

