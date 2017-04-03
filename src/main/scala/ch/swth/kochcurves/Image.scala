package ch.swth.kochcurves

import javafx.scene.paint.Color

/**
  * The immutable class Image holds a two-dimensional matrix that contains the color information for a javafx Screen to be drawn
  * and methods to redraw an image with new geometrical objects.
  */
class Image(w: Int, h: Int, c: Color, s: List[List[Color]]) {

  // width of the image
  private val width = w

  // height of the image
  private val height = h

  // default background color of the image
  private val color = c

  // the screen represents the pixel matrix of the image
  val screen = s

  def this(width: Int, height: Int, color: Color) = this(width, height, color, List.fill(height)(List.fill(width)(color)))

  def this(color: Color) = this(0, 0, color, List(Nil))

  def this(screen: List[List[Color]], color: Color) = this(screen.head.size, screen.size, color, screen)

  /**
    * Returns a new instance of this image filled with the specified background color
    *
    * @param color  The color to fill the image with
    * @return       The new image instance
    */
  def fill(color: Color) = new Image(width, height, color)

  /**
    * This method gives gives back a new instance of the actual Image with a line in it, specified by the coordinates of the start- and end-pixel and the Color.
    *
    * @param x1       x-cooridinate of the start-pixel
    * @param y1       y-coordinate of the start-pixel
    * @param x2       x-coordinate of the start-pixel
    * @param y2       y-coordinate of the start-pixel
    * @param color    {code Color} of the line
    * @return         A new instance of Image with the specified line
    */
  def line(x1: Int, y1: Int, x2: Int, y2: Int, color: Color): Image = {

    /**
      * Calculates the y-coordinate for the given x-coordinate on the line specified by coordinates received as parameters from line(x1: Int, y1: Int, x2: Int, y2: Int, color: Color)
      * The equation in use is derived from the two-point-form of two-dimentional linear equations ([[https://en.wikipedia.org/wiki/Linear_equation Wikipedia]]).
      *
      * @param x  the x-coordinate of the line
      * @return   the y-coordinate
      */
    def y(x: Int): Int = (y2 * x - y2 * x1 - y1 * x + y1 * x2) / (x2 - x1)

    /**
      * Returns a new Image with its screen enhanced by another pixel of the specified color, thereby checking the length of the current row and starting a new row
      * if the current has reached the width of the screen.
      *
      * @param image  The image to be processed
      * @param color  The color of the pixel to be set
      * @return       A new Image with the pixel of the specified color at it's last position.
      */
    def append(image: Image, color: Color) =
      if (image.screen.last.size < width) // same row
        new Image(image.screen.init ::: List(image.screen.last ::: color :: Nil), this.color)
      else // new row
        new Image(image.screen ::: List(color :: Nil), this.color)

    /**
      * Builds a new screen including the pixels of the specified line.
      *
      * @param image  actual image
      * @return       new image with line
      */
    def buildScreen(image: Image): Image = {
      // Checks if the actual y-coordinate is located on the line.
      def isPixelOnLine(x: Int): Boolean = y(x) == actualY

      // The color of the actual pixel on the original screen.
      def colorOfPixelInOriginalImage = if (image.screen.last.size < width) screen(actualY)(actualX) else screen(image.screen.size)(actualX)

      // The x-coordinate of the actual pixel
      def actualX = if (image.screen.last.size < width) image.screen.last.size else 1

      // The y-coordinate of the actual pixel
      def actualY = image.screen.size - 1

      if (image.screen.size == screen.size && image.screen.last.size == screen.last.size)
        image
      else if (actualX >= x1 && actualX <= x2 && isPixelOnLine(actualX))
        buildScreen(append(image, color))
      else
        buildScreen(append(image, colorOfPixelInOriginalImage))
    }

    buildScreen(new Image(this.color))
  }
}
