package content

import org.scalatest.FunSpec
import java.io._
import org.apache.commons.io.FileUtils
import content.Uploader.FileWriterException

/**
 * Created by sheaney on 5/8/14.
 */
class FileWriterTest extends FunSpec {
  describe("#read") {
    it("returns FileNotFoundException exception if file does not exist in the filesystem") {
      val key = "foo/bar/baz"
      val fileWriter = new FileWriter
      intercept[FileWriterException] {
        fileWriter.read(key)
      }
    }

    it("obtains an InputStream from an existing file") {
      val key = "test/toma3.gif"
      val file = new File(key)
      assert(file.exists(), "Image file needs to exist for test to pass")
      // Validate InputStream from fileName
      val fileWriter = new FileWriter
      fileWriter.read(key) match {
        case fis: FileInputStream => assert(fis != null, "Input stream should be != null")
        case _ => fail("Should return an InputStream")
      }
    }
  }

  describe("#write") {
    def getFileExtension(file: File): String = {
      val fileName = file.getName()
      fileName.substring(fileName.lastIndexOf("."), fileName.length)
    }

    it("writes an image file to disk") {
      val filePath = "test/toma3.gif"
      val key = "test/images/mammograms/1"
      val file = new File(filePath)
      val fileExtension = getFileExtension(file)
      val writtenFilePath = s"${key}${fileExtension}"

      val fileWriter = new FileWriter
      fileWriter.write(key, file)
      var writtenFile = new File(writtenFilePath)
      assert(writtenFile.exists(), "Image file should be written to disk drive")

      // updates existing file
      fileWriter.write(key, file)
      writtenFile = new File(writtenFilePath)
      assert(writtenFile.exists(), "Image file should be written to disk drive")

      // delete directory and file when done
      FileUtils.deleteDirectory(new File("test/images"))
    }

  }
}
