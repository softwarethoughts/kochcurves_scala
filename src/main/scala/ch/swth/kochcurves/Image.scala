package ch.swth.kochcurves

import javafx.scene.paint.Color

/**
  * Created by roland on 31.03.17.
  */
class Image(w: Int, h: Int, c: Color, s: List[List[Color]]) {

  private val width = w
  private val height = h
  private val color = c
  private val screen = s

  def this(width: Int, height: Int, color: Color) = this(width, height, color, List.fill(height)(List.fill(width)(color)))

  def this(color: Color) = this(0, 0, color, List(Nil))

  def this(screen: List[List[Color]], color: Color) = this(screen.head.size, screen.size, color, screen)

  def fill(color: Color) = new Image(width, height, color)

  def line(x1: Int, y1: Int, x2: Int, y2: Int, color: Color): Image = {
    def y(x: Int): Int = y2 * x - y2 * x1 - y1 * x + y1 * x2 / (x2 - x1)

    def append(image: Image, color: Color) =
      if (image.screen.last.size < width) // same row
        new Image(image.screen.init ::: List(image.screen.last ::: color :: Nil), this.color)
      else // new row
        new Image(image.screen ::: List(color :: Nil), this.color)

    def buildScreen(image: Image): Image = {
      def isPixelOnLine(x: Int): Boolean = y(x) == actualY

      def colorOfPixelInOriginalImage = if (image.screen.last.size < width) screen(actualY)(actualX) else screen(image.screen.size)(actualX)

      def actualX = if (image.screen.last.size < width) image.screen.last.size else 1

      def actualY = image.screen.size - 1

      if (image.screen.size == this.screen.size)
        image
      else if (actualX >= x1 && actualX <= x2 && isPixelOnLine(actualX))
        buildScreen(append(image, color))
      else
        buildScreen(append(image, colorOfPixelInOriginalImage))
    }

    buildScreen(new Image(this.color))
  }

}
