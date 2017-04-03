package ch.swth.kochcurves

import javafx.scene.paint.Color

/**
  * Created by roland on 31.03.17.
  */
class Image(w: Int, h: Int, c: Color, s: List[List[Color]]) {

  val width = w
  val height = h
  val color = c
  val screen = s

  def this(width: Int, height: Int, color: Color) = this(width, height, color, List.fill(height)(List.fill(width)(color)))

  def this(color: Color) = this(0, 0, color, List(Nil))

  def this(screen: List[List[Color]], color: Color) = this(screen.head.size, screen.size, color, screen)

  def fill(color: Color) = new Image(width, height, color)

  def line(x1: Int, y1: Int, x2: Int, y2: Int, color: Color): Image = {
    def yValue: (Int, Int) => (Int) = y2 * _ - y2 * x1 - y1 * _ + y1 * x2 / (x2 - x1)

    def append(image: Image, color: Color) =
      if (image.screen.last.size < width)
        new Image(image.screen.init ::: List(image.screen.last ::: color :: Nil), this.color)
      else
        new Image(image.screen ::: List(color :: Nil), this.color)

    def buildScreen(image: Image): Image = {
      def isPointMemberOfLine(x: Int): Boolean = yValue(x, x) == image.screen.size - 1

      def colorOfPixelinOriginalImage = if (image.screen.last.size < width) screen(actualYPosition)(actualXPosition) else screen(image.screen.size)(actualXPosition)

      def actualXPosition = if (image.screen.last.size < width) image.screen.last.size else 1

      def actualYPosition = image.screen.size - 1

      if (image.screen.size == this.screen.size)
        image
      else {
        if (actualXPosition >= x1 && actualXPosition <= x2 && isPointMemberOfLine(actualXPosition))
          buildScreen(append(image, color))
        else
          buildScreen(append(image, colorOfPixelinOriginalImage))
      }
    }

    buildScreen(new Image(this.color))
  }
}
