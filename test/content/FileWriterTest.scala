package content

import org.scalatest.FunSpec
import java.io._
import org.apache.commons.io.FileUtils
import content.Uploader.FileWriterException
import java.awt.image.BufferedImage

/**
 * Created by sheaney on 5/8/14.
 */
class FileWriterTest extends FunSpec {
  describe("#read") {
    it("throws FileWriterException if file does not exist in the filesystem") {
      val key = "foo/bar/baz.gif"
      val fileWriter = new FileWriter
      intercept[FileWriterException] {
        fileWriter.read(key)
      }
    }

    it("obtains an InputStream from an existing file") {
      val key = "test/toma3"
      val file = new File(key)
      assert(file.exists(), """"A test image file needs to exist named "test/toma3 for test to pass""")
      val fileWriter = new FileWriter
      fileWriter.read(key) match {
        case fis: BufferedImage => assert(fis != null, "Input stream should be != null")
        case _ => fail("Should return an InputStream")
      }
    }
  }

  describe("#write") {

    it("writes an image file to disk") {
      val filePath = "test/toma3"
      val key = "test/images/mammograms/1"
      val file = new File(filePath)

      val fileWriter = new FileWriter
      try {
        fileWriter.write(key, file)
      } catch {
        case fwe: FileWriterException => fail(fwe.getMessage)
      }

      var writtenFile = new File(key)
      assert(writtenFile.exists(), "Image file should be written to disk drive")

      // updates existing file
      try {
        fileWriter.write(key, file)
      } catch {
        case fwe: FileWriterException => fail(fwe.getMessage)
      }
      writtenFile = new File(key)
      assert(writtenFile.exists(), "Image file should be written to disk drive")

      // delete directory and file when done
      FileUtils.deleteDirectory(new File("test/images"))
    }

  }
}
