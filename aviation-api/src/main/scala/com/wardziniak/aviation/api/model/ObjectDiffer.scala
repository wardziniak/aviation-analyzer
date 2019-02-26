package com.wardziniak.aviation.api.model

case class ObjectDiffer[T](source: T, destination: T) {

  /**
    *
    * @param n zero base
    * @param size how many elements is between source and destination
    * @return n element between @link source and target
    */
  def getNthFromM(n: Int, size: Int): Option[T] = {
    if (n >= size)
      Option.empty[T]
    ???
  }

}
