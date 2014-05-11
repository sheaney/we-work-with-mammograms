package content

import org.scalatest.FunSpec

import java.io.{InputStream, File}
import com.amazonaws.{AmazonClientException, AmazonServiceException}
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.regions.{ Region, Regions }

/**
 * Created by sheaney on 5/8/14.
 */
class S3UploaderTest extends FunSpec {
  // https://github.com/aws/aws-sdk-java/blob/master/src/samples/AmazonS3/credentials
  describe("""Credentials "aws_secret_key and "aws_access_key" are set up in ~/.aws/config file""""") {
    it("tries to obtain credentials") {
      try {
        new ProfileCredentialsProvider().getCredentials()
      } catch {
        case _: Exception => fail(
          "Cannot load the credentials from the credential profiles file. " +
          "Please make sure that your credentials file is at the correct " +
          "location (~/.aws/config), and is in valid format.")
      }
    }
  }

  describe("#testConnection") {
    it("returns without an error") {
      val s3Uploader = new S3Uploader
      try {
        s3Uploader.testConnection()
      } catch {
        case ase: AmazonServiceException =>
          fail(ase.getMessage)
        case ace: AmazonClientException =>
          fail(ace.getMessage)
      }

    }
  }

  describe("Uploading, reading and destroying a file from AWS S3") {
    val key = "images/mammograms/1"

    describe("#write") {
      it("uploads an empty file to AWS S3") {
        val file = new File("test/toma3.gif")
        assert(file.exists(), "Need to provide a test file to upload")

        val s3Uploader = new S3Uploader
        try {
          s3Uploader.write(key, file)
        } catch {
          case ase: AmazonServiceException =>
            fail(ase.getMessage)
          case ace: AmazonClientException =>
            fail(ace.getMessage)
        }
      }
    }

    describe("#read") {
      it ("obtains the file that was uploaded to AWS S3") {
        val s3Uploader = new S3Uploader

        val inputStream = s3Uploader.read(key)
        inputStream match {
          case _: InputStream =>
          case _ => fail("Did not return an InputStream")
        }
      }
    }

    describe("destroy") {
      it("effectively destroys the file that was just previously updated") {
        val credentials = new ProfileCredentialsProvider().getCredentials()
        val bucketName = "wwwm"

        val s3 = new AmazonS3Client(credentials)
        val usEast = Region.getRegion(Regions.US_EAST_1)
        s3.setRegion(usEast)

        s3.deleteObject(bucketName, key)
      }
    }
  }

}
