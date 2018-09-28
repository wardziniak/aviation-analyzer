package com.wardziniak.aviation.importer

import com.wardziniak.aviation.api.model.Value
import com.wardziniak.aviation.importer.external.model.ExternalObject

trait DataImporter[ExternalFormat <: ExternalObject,V <: Value] { self: DataDownloader[ExternalFormat] =>

  implicit def asValue(dto: ExternalFormat): V

  def importData(url: String) = {
    download(url)
  }

}
