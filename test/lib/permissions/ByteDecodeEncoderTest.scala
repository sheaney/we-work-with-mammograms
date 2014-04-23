package lib.permissions

import org.scalatest.FunSpec
import java.lang.Math.pow

class ByteDecodeEncoderTest extends FunSpec {
  def removeSpacing(bytes: String): String = bytes.replaceAll("""\s""", "")

  def assertDecodingWorks(decoded: Seq[String], encoded: Seq[Int]) {
    for ((dec, enc) <- decoded.zip(encoded)) {
      assert(ByteDecodeEncoder.encode(dec) === enc)
    }
  }

  describe("#encode") {
    it("encodes bytes into int values") {
      val oneByte = removeSpacing("11111111")
      val twoBytes = removeSpacing("11111111  11111111")
      val threeBytes = removeSpacing("11111111  11111111  11111111")

      val oneByteValue = math.pow(2, oneByte.length()) - 1
      val twoBytesValue = math.pow(2, twoBytes.length()) - 1
      val threeBytesValue = math.pow(2, threeBytes.length()) - 1

      val decoded = Seq(oneByte, twoBytes, threeBytes)
      val encoded = Seq(oneByteValue, twoBytesValue, threeBytesValue).map(_.toInt)
      
      assertDecodingWorks(decoded, encoded)
    }

    it("encodes bits into values") {
      val fiveBits = "11111"
      val twoBits = "11"
      val oneBit = "1"

      val fiveBitsValue = math.pow(2, fiveBits.length()) - 1
      val twoBitsValue = math.pow(2, twoBits.length()) - 1
      val oneBitValue = math.pow(2, oneBit.length()) - 1

      val decoded = Seq(fiveBits, twoBits, oneBit)
      val encoded = Seq(fiveBitsValue, twoBitsValue, oneBitValue).map(_.toInt)
      
      assertDecodingWorks(decoded, encoded)
    }
    
    describe("throws IllegalArgumentException if trying to encode >= 32 bits") {
      val killer = removeSpacing("11111111  11111111  11111111  11111111")
      intercept[IllegalArgumentException] {
        ByteDecodeEncoder.encode(killer)
      }
      
    }
    
  }

}