package com.sagunpandey.lib.acr1252.card.acos3;

import com.sagunpandey.lib.acr1252.apdu.Apdu;
import com.sagunpandey.lib.acr1252.pcsc.PcscReader;
import com.sagunpandey.lib.acr1252.utility.Helper;

@SuppressWarnings("Duplicates")
public class Acos3 {
  private String authenticationCode_;
  private PcscReader _PcscConnection;

  public Acos3(PcscReader pcscConnection) {
    _PcscConnection = pcscConnection;
  }

  public PcscReader getPcscConnection() {
    return this._PcscConnection;
  }

  public void setPcscConnection(PcscReader pcscConnection) {
    this._PcscConnection = pcscConnection;
  }

  public String getAuthenticationCode() {
    return this.authenticationCode_;
  }

  public void setAuthenticationCode(String authenticationCode) {
    this.authenticationCode_ = authenticationCode;
  }

  public String submitCode(CODE_TYPE codeType, String code) throws Exception {
    Apdu apdu;

    try {
      apdu = new Apdu();
      apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x20, (byte) codeType._id,
              (byte) 0x00, (byte) 0x08});
      apdu.setSendData(code.getBytes("ASCII"));

      getPcscConnection().sendApduCommand(apdu);

      if (apdu.getSw()[0] == (byte) 0x63) {
        int triesLeft = apdu.getSw()[1] - (byte) 0xC0;

        if (triesLeft == 0)
          throw new Exception("PIN/Code is locked");
        else if (triesLeft == 1)
          throw new Exception("Invalid PIN/Code, you only have " + triesLeft + " try left");
        else
          throw new Exception("Invalid PIN/Code, you only have " + triesLeft + " tries left");
      } else if (apdu.getSw()[0] == (byte) 0x69 && apdu.getSw()[1] == (byte) 0x83)
        throw new Exception("PIN/Code is locked");
      else if (apdu.getSw()[0] == (byte) 0x69 && apdu.getSw()[1] == (byte) 0x85)
        throw new Exception("Authentication incomplete");
      else if (apdu.getSw()[0] == (byte) 0x90)
        return "Valid";
      else
        return "Unknown state";
    } catch (Exception ex) {
      throw new Exception("Submit Code Failed - " + ex.getMessage());
    }
  }

  public String submitCode(CODE_TYPE codeType, byte[] code) throws Exception {
    Apdu apdu;

    try {
      apdu = new Apdu();
      apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x20, (byte) codeType._id,
              (byte) 0x00, (byte) 0x08});
      apdu.setSendData(code);

      getPcscConnection().sendApduCommand(apdu);

      if (apdu.getSw()[0] == (byte) 0x63) {
        int triesLeft = apdu.getSw()[1] - (byte) 0xC0;

        if (triesLeft == 0)
          throw new Exception("PIN/Code is locked");
        else if (triesLeft == 1)
          throw new Exception("Invalid PIN/Code, you only have " + triesLeft + " try left");
        else
          throw new Exception("Invalid PIN/Code, you only have " + triesLeft + " tries left");
      } else if (apdu.getSw()[0] == (byte) 0x69 && apdu.getSw()[1] == (byte) 0x83)
        throw new Exception("PIN/Code is locked");
      else if (apdu.getSw()[0] == (byte) 0x69 && apdu.getSw()[1] == (byte) 0x85)
        throw new Exception("Authentication incomplete");
      else if (apdu.getSw()[0] == (byte) 0x90)
        return "Valid";
      else
        return "Unknown state";
    } catch (Exception ex) {
      throw new Exception("Submit Code Failed - " + ex.getMessage());
    }
  }

  public byte[] getCardInfo(CARD_INFO_TYPE cardInfoType) throws Exception {
    Apdu apdu;

    apdu = new Apdu();

    if (cardInfoType == CARD_INFO_TYPE.CARD_SERIAL) {
      apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x14, (byte) 0x00,
              (byte) 0x00, (byte) 0x08});
    } else if (cardInfoType == CARD_INFO_TYPE.VERSION_NUMBER) {
      apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x14, (byte) 0x06,
              (byte) 0x00, (byte) 0x08});
    } else if (cardInfoType == CARD_INFO_TYPE.EEPROM) {
      apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x14, (byte) 0x05,
              (byte) 0x00, (byte) 0x00});
    }

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Get Card Info [" + Helper.byteAsString(apdu.getSw(), true) + "]");

    return apdu.getReceiveData();
  }

  public void clearCard() throws Exception {
    Apdu apdu;

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00});

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Clear Card Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public void selectFile(INTERNAL_FILE internalFile) throws Exception {
    byte[] fileID;

    if (internalFile == INTERNAL_FILE.MCUID_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x00};
    else if (internalFile == INTERNAL_FILE.MANUFACTURER_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x01};
    else if (internalFile == INTERNAL_FILE.PERSONALIZATION_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x02};
    else if (internalFile == INTERNAL_FILE.SECURITY_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x03};
    else if (internalFile == INTERNAL_FILE.USER_FILE_MGMT_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x04};
    else if (internalFile == INTERNAL_FILE.ACCOUNT_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x05};
    else if (internalFile == INTERNAL_FILE.ACCOUNT_SECURITY_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x06};
    else if (internalFile == INTERNAL_FILE.ATR_FILE)
      fileID = new byte[]{(byte) 0xFF, (byte) 0x07};
    else
      throw new Exception("Invalid internal file");

    this.selectFile(fileID);
  }

  public void selectFile(byte[] fileID) throws Exception {
    Apdu apdu;

    try {
      apdu = new Apdu();
      if (fileID == null || fileID.length != 2)
        throw new Exception("File ID length should be 2 bytes");

      apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xA4, (byte) 0x00,
              (byte) 0x00, (byte) 0x02});
      apdu.setSendData(fileID);

      getPcscConnection().sendApduCommand(apdu);

      if (apdu.getSw()[0] != (byte) 0x90) {
        if (apdu.getSw()[0] != (byte) 0x91)
          throw new Exception("Select File Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
      }
    } catch (Exception ex) {
      throw new Exception("Select File Failed - " + ex.getMessage());
    }
  }

  public byte[] readRecord(byte recordNumber, byte offset, byte lengthToRead) throws Exception {
    Apdu apdu;

    apdu = new Apdu();
    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xB2, recordNumber, offset, lengthToRead});

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Read Record Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");

    return apdu.getReceiveData();
  }

  public byte[] readBinary(byte highOffset, byte lowOffset, byte lengthToRead) throws Exception {
    Apdu apdu;

    apdu = new Apdu();
    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xB0, highOffset, lowOffset, lengthToRead});

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Read Binary Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");

    return apdu.getReceiveData();
  }

  public void writeRecord(byte recordNumber, byte offset, byte[] dataToWrite) throws Exception {
    Apdu apdu;

    if (dataToWrite == null || dataToWrite.length < 1)
      throw new Exception("Data to write is not specified");

    if (dataToWrite.length > 255)
      throw new Exception("Data to write is too long");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xD2, recordNumber, offset, (byte) dataToWrite.length});
    apdu.setSendData(dataToWrite);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Write Record Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public void writeBinary(byte highOffset, byte lowOffset, byte[] dataToWrite) throws Exception {
    Apdu apdu;

    if (dataToWrite == null || dataToWrite.length < 1)
      throw new Exception("Data to write is not specified");

    if (dataToWrite.length > 4096)
      throw new Exception("Data to write is too long");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xD0, highOffset, lowOffset, (byte) dataToWrite.length});
    apdu.setSendData(dataToWrite);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Write Binary Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public byte[] startSession() throws Exception {
    byte[] randomNumber = new byte[8];
    Apdu apdu;

    apdu = new Apdu();
    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x84, (byte) 0x00,
            (byte) 0x00, (byte) 0x08});

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Start Session Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");

    System.arraycopy(apdu.getReceiveData(), 0, randomNumber, 0, 8);

    return randomNumber;
  }

  public byte[] authenticate(byte[] encryptedData, byte[] terminalRandomNumber) throws Exception {
    Apdu apdu;

    if (encryptedData == null || encryptedData.length != 8)
      throw new Exception("Encrypted data length should be 8 bytes");

    if (terminalRandomNumber == null || terminalRandomNumber.length != 8)
      throw new Exception("Terminal random number length should be 8 bytes");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x10});
    byte[] apduSendData = new byte[16];

    System.arraycopy(encryptedData, 0, apduSendData, 0, 8);
    System.arraycopy(terminalRandomNumber, 0, apduSendData, 8, 8);
    apdu.setSendData(apduSendData);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] == (byte) 0x61)
      return apdu.getSw();
    else if (apdu.getSw()[0] == (byte) 0x90)
      return apdu.getReceiveData();
    else
      throw new Exception("Authenticate Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public byte[] getResponse(byte lengthToReceive) throws Exception {
    Apdu apdu;

    apdu = new Apdu();
    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xC0, (byte) 0x00,
            (byte) 0x00, lengthToReceive});

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Get Response Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");

    return apdu.getReceiveData();
  }

  public byte[] debitWithDbc(byte[] mac, byte[] amount, byte[] ttref) throws Exception {
    Apdu apdu;

    if (mac == null || mac.length != 4)
      throw new Exception("MAC length should be 4 bytes");

    if (amount == null || amount.length != 3)
      throw new Exception("Amount length should be 3 bytes");

    if (ttref == null || ttref.length != 4)
      throw new Exception("Terminal transaction reference should be 4 bytes");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xE6, (byte) 0x01, (byte) 0x00, (byte) 0x0B});
    byte[] apduSendData = new byte[11];

    System.arraycopy(mac, 0, apduSendData, 0, 4);
    System.arraycopy(amount, 0, apduSendData, 4, 3);
    System.arraycopy(ttref, 0, apduSendData, 7, 4);
    apdu.setSendData(apduSendData);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] == (byte) 0x61)
      return this.getResponse(apdu.getSw()[1]);
    else if (apdu.getSw()[0] == (byte) 0x90)
      return apdu.getReceiveData();
    else
      throw new Exception("Debit Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public void debit(byte[] mac, byte[] amount, byte[] ttref) throws Exception {
    Apdu apdu;

    if (mac == null || mac.length != 4)
      throw new Exception("MAC length should be 4 bytes");

    if (amount == null || amount.length != 3)
      throw new Exception("Amount length should be 3 bytes");

    if (ttref == null || ttref.length != 4)
      throw new Exception("Terminal transaction reference should be 4 bytes");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xE6, (byte) 0x00, (byte) 0x00, (byte) 0x0B});
    byte[] apduSendData = new byte[11];

    System.arraycopy(mac, 0, apduSendData, 0, 4);
    System.arraycopy(amount, 0, apduSendData, 4, 3);
    System.arraycopy(ttref, 0, apduSendData, 7, 4);
    apdu.setSendData(apduSendData);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Debit Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public void credit(byte[] mac, byte[] amount, byte[] ttref) throws Exception {
    Apdu apdu;

    if (mac == null || mac.length != 4)
      throw new Exception("MAC length should be 4 bytes");

    if (amount == null || amount.length != 3)
      throw new Exception("Amount length should be 3 bytes");

    if (ttref == null || ttref.length != 4)
      throw new Exception("Terminal transaction reference should be 4 bytes");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xE2, (byte) 0x00, (byte) 0x00, (byte) 0x0B});
    byte[] apduSendData = new byte[11];

    System.arraycopy(mac, 0, apduSendData, 0, 4);
    System.arraycopy(amount, 0, apduSendData, 4, 3);
    System.arraycopy(ttref, 0, apduSendData, 7, 4);
    apdu.setSendData(apduSendData);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Credit Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public void revokeDebit(byte[] mac) throws Exception {
    Apdu apdu;

    if (mac == null || mac.length != 4)
      throw new Exception("MAC length should be 4 bytes");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xE8, (byte) 0x00, (byte) 0x00, (byte) 0x04});
    apdu.setSendData(mac);

    getPcscConnection().sendApduCommand(apdu);

    if (apdu.getSw()[0] != (byte) 0x90)
      throw new Exception("Revoke Debit Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
  }

  public InquireAccountResponse inquireAccount(byte keyNumber, byte[] randomNumber) throws Exception {
    Apdu apdu;

    if (randomNumber == null || randomNumber.length != 4)
      throw new Exception("Random data length should be 4 bytes");

    apdu = new Apdu();

    apdu.setCommand(new byte[]{(byte) 0x80, (byte) 0xE4, keyNumber, (byte) 0x00, (byte) 0x04});
    apdu.setSendData(randomNumber);

    getPcscConnection().sendApduCommand(apdu);

    byte[] responseFromCard = new byte[25];
    if (apdu.getSw()[0] == (byte) 0x90) {
      for (int i = 0; i < 25; i++)
        responseFromCard[i] = apdu.getReceiveData()[i];

      return new InquireAccountResponse(responseFromCard);
    } else if (apdu.getSw()[0] == (byte) 0x61) {
      responseFromCard = getResponse(apdu.getSw()[1]);
      return new InquireAccountResponse(responseFromCard);
    } else {
      throw new Exception("Inquire Failed [" + Helper.byteAsString(apdu.getSw(), true) + "]");
    }
  }

  public void createRecordFile(byte recordNumber, byte[] fileID, byte numberOfRecords, byte recordLength,
                               SecurityAttribute writeSecurityAttribute, SecurityAttribute readSecurityAttribute,
                               boolean readRequireSecureMessaging, boolean writeRequireSecureMessaging) throws Exception {
    byte[] buffer;

    this.selectFile(INTERNAL_FILE.USER_FILE_MGMT_FILE);

    buffer = new byte[7];
    buffer[0] = recordLength;
    buffer[1] = numberOfRecords;
    buffer[2] = readSecurityAttribute.getRawValue();
    buffer[3] = writeSecurityAttribute.getRawValue();
    buffer[4] = fileID[0];
    buffer[5] = fileID[1];


    if (readRequireSecureMessaging)
      buffer[6] |= 0x40;

    if (writeRequireSecureMessaging)
      buffer[6] |= 0x20;

    writeRecord(recordNumber, (byte) 0x00, buffer);
  }

  public void configurePersonalizationFile(OptionRegister optionRegister,
                                           SecurityOptionRegister securityRegister, byte NumberOfFiles) throws Exception {
    try {
      byte[] data;

      this.selectFile(INTERNAL_FILE.PERSONALIZATION_FILE);

      data = new byte[]{optionRegister.getRawValue(), securityRegister.getRawValue(), NumberOfFiles, 0x00};
      this.writeRecord((byte) 0x00, (byte) 0x00, data);

    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void createBinaryFile(byte[] fileID, int fileLength,
                               SecurityAttribute writeSecurityAttribute, SecurityAttribute readSecurityAttribute,
                               boolean readRequireSecureMessaging, boolean writeRequireSecureMessaging) throws Exception {
    byte[] buffer, fileLengthByte, tempFileLength;
    byte numberOfFiles = 0x00;
    byte recordNumber = 0x00;

    if (fileLength <= 0)
      throw new Exception("Invalid file length");

    fileLengthByte = new byte[2];
    tempFileLength = Helper.intToByte(fileLength);
    System.arraycopy(tempFileLength, 2, fileLengthByte, 0, 2);

    //Read number of files specified
    this.selectFile(INTERNAL_FILE.PERSONALIZATION_FILE);

    buffer = this.readRecord((byte) 0x00, (byte) 0x00, (byte) 0x04);
    numberOfFiles = buffer[2];

    if (numberOfFiles < 1)
      throw new Exception("Number of files specified in Personalization File is zero (0)");


    this.selectFile(INTERNAL_FILE.USER_FILE_MGMT_FILE);

    for (recordNumber = 0; recordNumber < numberOfFiles; recordNumber++) {
      buffer = this.readRecord(recordNumber, (byte) 0x00, (byte) 0x07);
      if (Helper.byteArrayIsEqual(buffer, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}))
        break;

      if ((recordNumber + 1) >= numberOfFiles)
        throw new Exception("Maximum number of files that can be created is already been reached");
    }

    buffer = new byte[7];
    buffer[0] = fileLengthByte[0];
    buffer[1] = fileLengthByte[1];
    buffer[2] = readSecurityAttribute.getRawValue();
    buffer[3] = writeSecurityAttribute.getRawValue();
    buffer[4] = fileID[0];
    buffer[5] = fileID[1];

    buffer[6] = (byte) 0x80;

    if (readRequireSecureMessaging)
      buffer[6] |= (byte) 0x40;

    if (writeRequireSecureMessaging)
      buffer[6] |= (byte) 0x20;

    this.writeRecord(recordNumber, (byte) 0x00, buffer);
  }

  public enum CODE_TYPE {
    AC1(0x01),
    AC2(0x02),
    AC3(0x03),
    AC4(0x04),
    AC5(0x05),
    PIN(0x06),
    IC(0x07);

    private final int _id;

    CODE_TYPE(int id) {
      this._id = id;
    }
  }

  public enum INTERNAL_FILE {
    MCUID_FILE(0),
    MANUFACTURER_FILE(1),
    PERSONALIZATION_FILE(2),
    SECURITY_FILE(3),
    USER_FILE_MGMT_FILE(4),
    ACCOUNT_FILE(5),
    ACCOUNT_SECURITY_FILE(6),
    ATR_FILE(7);

    private final int _id;

    INTERNAL_FILE(int id) {
      this._id = id;
    }
  }

  public enum CARD_INFO_TYPE {
    CARD_SERIAL,
    EEPROM,
    VERSION_NUMBER
  }
}
