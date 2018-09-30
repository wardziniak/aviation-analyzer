package com.wardziniak.aviation.importer.config

object ConfigLoader extends App {

  def loadConfig = {
    pureconfig.loadConfig[AviationEdgeConfig]("aviation-edge").right.get
  }
}
