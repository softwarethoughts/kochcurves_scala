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

    def appendRow(image: Image, color: Color) = new Image(image.screen.init ::: List(image.screen.last ::: color :: Nil), this.color)

    def newRow(image: Image, color: Color) = new Image(image.screen ::: List(color :: Nil), this.color)

    def buildScreen(image: Image, position: Int): Image = {
      def isPointMemberOfLine(x: Int) : Boolean = yValue(x, x) == image.screen.size - 1

      def colorOfPixelinOriginalImage = screen(image.screen.size - 1)(position)

      if (image.screen.size < this.screen.size) {
        if (position >= x1 && position <= x2 && isPointMemberOfLine(position))
          if (position < width && position != 0)
            buildScreen(appendRow(image, color), position + 1)
          else
            buildScreen(appendRow(image, color), 1)
        else if (position < width && position != 0)
          buildScreen(appendRow(image, colorOfPixelinOriginalImage), position + 1)
        else
          buildScreen(newRow(image, screen(image.screen.size)(0)), 1)
      } else if (yValue(position, position) == image.screen.size - 1)
        buildScreen(newRow(image, color), 1)
      else if (position < width && position >= x1 && position <= x2 && isPointMemberOfLine(position))
          buildScreen(appendRow(image, color), position + 1)
      else if (position < width)
        buildScreen(appendRow(image, colorOfPixelinOriginalImage), position + 1)
      else image
    }

    buildScreen(new Image(this.color), 0)
  }
}
