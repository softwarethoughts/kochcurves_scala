package ch.swth.kochcurves

import javafx.scene.paint.Color

/**
  * Created by roland on 02.04.17.
  */
object Application extends App {
  var image = new Image(10, 10, Color.ALICEBLUE)
  image = image.line(0, 0, 8, 0, Color.BLACK)

  println(s"screen size: ${image.screen.size}")
  println(image.screen)
}
