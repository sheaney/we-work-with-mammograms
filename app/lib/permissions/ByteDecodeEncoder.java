package lib.permissions;

public class ByteDecodeEncoder {
	private final static int BYTE_SIZE = 8;
//	private final static int MAX_ENCODING = (int) Math.pow(2, 31) - 1;
	
	public static int encode(String decoded) {
		int decodedSize = decoded.length();
		
		if (decodedSize >= 4 * BYTE_SIZE)
			throw new IllegalArgumentException("decoded parameter must be under 32 bits");
		
		int encoded = 0;
		for (int i = 0, bit = decodedSize - 1; i < decodedSize; i++, bit--) {
			if (decoded.charAt(i) == '1') {
				encoded += Math.pow(2, bit);
			}
		}
		return encoded;
	}

}
