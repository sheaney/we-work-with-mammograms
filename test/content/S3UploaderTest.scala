package content

import org.scalatest.FunSpec
import com.amazonaws.{AmazonClientException, AmazonServiceException}

/**
 * Created by sheaney on 5/8/14.
 */
class S3UploaderTest extends FunSpec {
  describe("""Environment variables "AWS_SECRET_KEY and "AWS_ACCESS_KEY" are set""""") {
    it("environment variables need to be set up.") {
      val accessKey = System.getenv("AWS_ACCESS_KEY")
      val secretKey = System.getenv("AWS_SECRET_KEY")
      assert(accessKey !== null)
      assert(secretKey !== null)
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

}
