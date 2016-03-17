package np.com.rts.lib.acr1252.exceptions;

import np.com.rts.lib.acr1252.utility.Helper;

public class ReaderException extends RuntimeException {
  public enum ACR89_ERROR_CODE {
    COMMAND_NOT_SUPPORTED((byte) 0x00),
    HARDWARE_ERROR((byte) 0x01),
    ACCESS_DENIED((byte) 0x02),
    ADDRESS_ERROR((byte) 0x03),
    FRAME_ERROR((byte) 0x04),
    CHECKSUM_ERROR((byte) 0x05);

    private final int _id;

    ACR89_ERROR_CODE(int id) {
      this._id = id;
    }

    public int getAcr89ErrorCode() {
      return _id;
    }
  }

  protected ACR89_ERROR_CODE _readerResponse;

  public ACR89_ERROR_CODE getReaderResponse() {
    return this._readerResponse;
  }

  protected String _message;

  public String getMessage() {
    return getErrorMessage();
  }

  public ReaderException(byte errorCode) {
    _readerResponse = ACR89_ERROR_CODE.values()[errorCode];
  }

  private String getErrorMessage() {
    switch (getReaderResponse()) {
      case ACCESS_DENIED:
        return "Access Denied";
      case ADDRESS_ERROR:
        return "Address Error";
      case CHECKSUM_ERROR:
        return "Checksum Error";
      case COMMAND_NOT_SUPPORTED:
        return "Command not supported";
      case FRAME_ERROR:
        return "Frame error";
      case HARDWARE_ERROR:
        return "Hardware Error";
      default:
        return "Unknown Error";
    }
  }

  public ReaderException() {
    super();
  }

  public ReaderException(String message) {
    super(message);
  }

  public ReaderException(String message, byte[] readerResponse) {
    super(message + "\nResponse : " + Helper.byteAsString(readerResponse, true));
  }
}
