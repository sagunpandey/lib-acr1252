package np.com.rts.lib.acr1252.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
  public static String byteAsString(byte[] data, boolean spaceInBetween) {
    String tmpStr = "";

    if (data == null)
      return "";

    for (byte aData : data) tmpStr += String.format((spaceInBetween ? "%02X " : "%02X"), aData);

    return tmpStr;
  }

  public static byte[] getBytes(String stringBytes, String delimiter) {
    String[] arrayStr = stringBytes.split(delimiter);
    byte[] bytesResult = new byte[arrayStr.length];

    for (int i = 0; i < arrayStr.length; i++)
      bytesResult[i] = Byte.parseByte(arrayStr[i]);

    return bytesResult;
  }

  public static byte[] getBytes(String stringBytes) {
    String formattedString = "";
    int counter = 0;

    if (stringBytes.trim().equals(""))
      return null;

    for (int i = 0; i < stringBytes.length(); i++) {
      if (stringBytes.charAt(i) == ' ')
        continue;

      if (counter > 0 && counter % 2 == 0)
        formattedString += " ";

      formattedString += stringBytes.charAt(i);

      counter++;
    }

    return getBytes(formattedString, " ");
  }

  public static byte[] stringToByteArray(String str) {
    byte[] buffer = new byte[str.length() / 2];
    String temp;

    for (int i = 0; i < buffer.length; i++) {
      temp = str.substring(i * 2, i * 2 + 2);
      buffer[i] = ((Integer) Integer.parseInt(temp, 16)).byteValue();
    }

    return buffer;
  }

  public static String byteArrayToString(byte[] data) {
    String str = "";

    for (byte aData : data) {
      str += (char) aData;
    }

    return str;
  }

  public static int byteToInt(byte[] data, boolean isLittleEndian) {
    byte[] holder = new byte[4];
    byte[] reverseArray = new byte[4];

    if (isLittleEndian) {
      // Make sure that the array size is 4
      System.arraycopy(data, 0, holder, 0, data.length);

      for (int i = 0; i < 4; i++)
        reverseArray[i] = holder[3 - i];

      return byteToInt(reverseArray);
    } else {
      return byteToInt(data);
    }
  }

  public static int byteToInt(byte[] data) {
    byte[] holder = new byte[4];

    if (data == null)
      return -1;

    // Make sure that the array size is 4
    System.arraycopy(data, 0, holder, 4 - data.length, data.length);

    return (((holder[0] & 0xFF) << 24) + ((holder[1] & 0xFF) << 16) + ((holder[2] & 0xFF) << 8) + (holder[3] & 0xFF));
  }

  public static byte[] intToByte(int number) {
    byte[] data = new byte[4];

    data[0] = (byte) ((number >> 24) & 0xFF);
    data[1] = (byte) ((number >> 16) & 0xFF);
    data[2] = (byte) ((number >> 8) & 0xFF);
    data[3] = (byte) (number & 0xFF);

    return data;
  }

  public static String removeWhiteSpaces(String str) {
    return str.replaceAll("\\s", "");
  }

  // Convert Hex String to Byte Array
  public byte[] hex2Byte(String str) {
    byte[] bytes = new byte[str.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
  }

  public static String byteAsString(byte[] b, int startIndex, int len, boolean SpaceInBetween) {
    int i;
    byte[] tmpStorageArray;
    if (b.length < startIndex + len) {
      tmpStorageArray = new byte[len];
      // Resize the array
      for (i = 0; i < len; i++)
        tmpStorageArray[i] = b[i];

      b = new byte[len];

      for (i = 0; i < len; i++)
        b[i] = tmpStorageArray[i];
    }

    byte[] newByte = new byte[len];
    for (i = 0; i < len; i++)
      newByte[i] = b[startIndex + i];

    return byteAsString(newByte, SpaceInBetween);
  }

  public static boolean byteArrayIsEqual(byte[] array1, byte[] array2, int length) {
    if (array1.length < length)
      return false;

    if (array2.length < length)
      return false;

    for (int i = 0; i < length; i++) {
      if (array1[i] != array2[i])
        return false;
    }

    return true;
  }

  public static boolean byteArrayIsEqual(byte[] array1, byte[] array2) {
    return byteArrayIsEqual(array1, array2, array2.length);
  }

  public static byte[] appendArrays(byte[] arr1, byte[] arr2) {
    byte[] c = new byte[arr1.length + arr2.length];

    System.arraycopy(arr1, 0, c, 0, arr1.length);

    System.arraycopy(arr2, 0, c, arr1.length, arr2.length);

    return c;
  }

  public static byte[] appendArrays(byte[] arr1, byte arr2) {
    byte[] c = new byte[1 + arr1.length];

    System.arraycopy(arr1, 0, c, 0, arr1.length);

    c[arr1.length] = arr2;
    return c;
  }

  public static byte[] shortToByte(short number) {
    byte[] data = new byte[2];

    data[0] = (byte) ((number >> 8) & 0xFF);
    data[1] = (byte) (number & 0xFF);

    return data;
  }

  public static byte[] dateTimeToByteArray(Date value) {
    String dateString;
    DateFormat dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmm");

    dateString = dateTimeFormatter.format(value) + "00";

    return stringToByteArray(dateString);
  }

  public static Date byteArrayToDateTime(byte[] value) throws Exception {
    DateFormat dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String dateString = byteAsString(value, false);

    return dateTimeFormatter.parse(dateString);
  }
}
