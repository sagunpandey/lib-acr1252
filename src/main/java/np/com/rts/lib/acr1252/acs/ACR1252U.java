package np.com.rts.lib.acr1252.acs;

import np.com.rts.lib.acr1252.pcsc.PcscReader;
import np.com.rts.lib.acr1252.utility.Helper;

import java.util.Arrays;

public class ACR1252U extends PcscReader {

  public static final String PLATFORM_LINUX = "Linux";
  public static final String PLATFORM_WINDOWS = "Windows";

  public static final int FILE_DEVICE_SMARTCARD = 0x310000; // Reader action IOCTLs
  public static final int IOCTL_SMARTCARD_ACR1251_ACR1252_ESCAPE_COMMAND = FILE_DEVICE_SMARTCARD + 3500 * 4;

  public static final int IOCTL_SMARTCARD_ACR1251_ACR1252_ESCAPE_COMMAND_LINUX = 0x42000000 + 3500; //0x42000DAC

  public ACR1252U(String platform) {
    super();

    int escapeCommand;
    if (platform.equals(PLATFORM_LINUX)) {
      escapeCommand = IOCTL_SMARTCARD_ACR1251_ACR1252_ESCAPE_COMMAND_LINUX;
    } else {
      escapeCommand = IOCTL_SMARTCARD_ACR1251_ACR1252_ESCAPE_COMMAND;
    }
    this.setControlCode(escapeCommand);
  }

  public ACR1252U() {
    new ACR1252U(PLATFORM_WINDOWS);
  }

  @Override
  public byte[] getFirmwareVersion() throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x18, 0x00};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Get Firmware Version Failed");
    }

    return Arrays.copyOfRange(uResponse, 5, uResponse.length);
  }

  public LedStatus setLedStatus(LedStatus ledStatus) throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x29, 0x01, ledStatus.getRawLedStatus()};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set LED Status Failed");
    }

    return new LedStatus(uResponse[5]);
  }

  public LedStatus getLedStatus() throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x29, 0x00};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Get LED Status Failed");
    }

    return new LedStatus(uResponse[5]);
  }

  public byte setBuzzerStatus(byte uDuration) throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x28, 0x01, uDuration};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set Buzzer Status Failed");
    }

    return uResponse[5];
  }

  public byte getBuzzerStatus() throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x28, 0x00};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Get Buzzer Status Failed");
    }

    return uResponse[5];
  }

  public LedBuzzerBehaviorPicc setLedBuzzerBehaviorPicc(LedBuzzerBehaviorPicc behaviorSettings) throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x21, 0x01, behaviorSettings.getRawLedBuzzerBehaviorPicc()};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set Buzzer Status Failed");
    }

    return new LedBuzzerBehaviorPicc(uResponse[5]);
  }

  public LedBuzzerBehaviorPicc readLedBuzzerBehaviorPicc() throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x21, 0x00};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Get Buzzer Status Failed");
    }

    return new LedBuzzerBehaviorPicc(uResponse[5]);
  }

  public PiccOperatingParameter setPiccOperatingParameter(PiccOperatingParameter operatingParameter) throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x20, 0x01, operatingParameter.getRawOperatingParameter()};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set Buzzer Status Failed");
    }

    return new PiccOperatingParameter(uResponse[5]);
  }

  public PiccOperatingParameter readPiccOperatingParameter() throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x20, 0x00};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Get Buzzer Status Failed");
    }

    return new PiccOperatingParameter(uResponse[5]);
  }

  public AutoPiccPollingSettings setAutomaticPiccPolling(AutoPiccPollingSettings pollingSetting) throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x23, 0x01, pollingSetting.getRawPollingSettings()};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set Buzzer Status Failed");
    }

    return new AutoPiccPollingSettings(uResponse[5]);
  }

  public AutoPiccPollingSettings readAutomaticPiccPolling() throws Exception {
    byte[] uResponse;

    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x23, 0x00};
    sendControlCommand(uCommand);

    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Get Buzzer Status Failed");
    }

    return new AutoPiccPollingSettings(uResponse[5]);
  }

  public enum LED_STATE {
    ON((byte) 0x01),
    OFF((byte) 0x00);

    private int _id;

    LED_STATE(int id) {
      this._id = id;
    }

    public int getLedState() {
      return _id;
    }
  }

  public enum PARAMETER_OPTION {
    SKIP((byte) 0x00),
    DETECT((byte) 0x01);

    private final int _id;

    PARAMETER_OPTION(int id) {
      this._id = id;
    }

    public int getParameterOption() {
      return _id;
    }
  }

  public enum LED_BUZZER_BEHAVIOR {
    ENABLED((byte) 0x01),
    DISABLED((byte) 0x00);

    private final int _id;

    LED_BUZZER_BEHAVIOR(int id) {
      this._id = id;
    }

    public int getBuzzerBehavior() {
      return _id;
    }
  }

  public enum POLLING_SETTING {
    ENABLED((byte) 0x01),
    DISABLED((byte) 0x00);

    private final int _id;

    POLLING_SETTING(int id) {
      this._id = id;
    }

    public int getPollingSetting() {
      return _id;
    }
  }

  public enum POLLING_INTERVAL {
    MS_250,
    MS_500,
    MS_1000,
    MS_2500
  }

  public static class LedStatus {
    private LED_STATE _red = LED_STATE.OFF;
    private LED_STATE _green = LED_STATE.OFF;

    public LedStatus() {

    }

    public LedStatus(byte ledStatus) {
      if ((ledStatus & 0x01) == 0x01)
        _red = LED_STATE.ON;

      if ((ledStatus & 0x02) == 0x02)
        _green = LED_STATE.ON;
    }

    public LED_STATE getRed() {
      return _red;
    }

    public void setRed(LED_STATE value) {
      this._red = value;
    }

    public LED_STATE getGreen() {
      return _green;
    }

    public void setGreen(LED_STATE value) {
      this._green = value;
    }

    public byte getRawLedStatus() {
      byte ledStatus = 0x00;

      if (getRed() == LED_STATE.ON)
        ledStatus |= 0x01;

      if (getGreen() == LED_STATE.ON)
        ledStatus |= 0x02;

      return ledStatus;
    }
  }

  public static class PiccOperatingParameter {
    private PARAMETER_OPTION _iso14443TypeA = PARAMETER_OPTION.SKIP;
    private PARAMETER_OPTION _iso14443TypeB = PARAMETER_OPTION.SKIP;
    private PARAMETER_OPTION _felica212 = PARAMETER_OPTION.SKIP;
    private PARAMETER_OPTION _felica424 = PARAMETER_OPTION.SKIP;
    private PARAMETER_OPTION _topaz = PARAMETER_OPTION.SKIP;

    public PiccOperatingParameter() {

    }

    public PiccOperatingParameter(byte rawOperatingParameter) {
      if ((rawOperatingParameter & 0x01) == 0x01)
        _iso14443TypeA = PARAMETER_OPTION.DETECT;

      if ((rawOperatingParameter & 0x02) == 0x02)
        _iso14443TypeB = PARAMETER_OPTION.DETECT;

      if ((rawOperatingParameter & 0x04) == 0x04)
        _felica212 = PARAMETER_OPTION.DETECT;

      if ((rawOperatingParameter & 0x08) == 0x08)
        _felica424 = PARAMETER_OPTION.DETECT;

      if ((rawOperatingParameter & 0x10) == 0x10)
        _topaz = PARAMETER_OPTION.DETECT;
    }

    public PARAMETER_OPTION getIso14443TypeA() {
      return _iso14443TypeA;
    }

    public void setIso14443TypeA(PARAMETER_OPTION value) {
      this._iso14443TypeA = value;
    }

    public PARAMETER_OPTION getIso14443TypeB() {
      return _iso14443TypeB;
    }

    public void setIso14443TypeB(PARAMETER_OPTION value) {
      this._iso14443TypeB = value;
    }

    public PARAMETER_OPTION getFelica212() {
      return _felica212;
    }

    public void setFelica212(PARAMETER_OPTION value) {
      this._felica212 = value;
    }

    public PARAMETER_OPTION getFelica424() {
      return _felica424;
    }

    public void setFelica424(PARAMETER_OPTION value) {
      this._felica424 = value;
    }

    public PARAMETER_OPTION getTopaz() {
      return _topaz;
    }

    public void setTopaz(PARAMETER_OPTION value) {
      this._topaz = value;
    }

    public byte getRawOperatingParameter() {
      byte operatingParameter = 0x00;

      if (getIso14443TypeA() == PARAMETER_OPTION.DETECT)
        operatingParameter |= 0x01;

      if (getIso14443TypeB() == PARAMETER_OPTION.DETECT)
        operatingParameter |= 0x02;

      if (getFelica212() == PARAMETER_OPTION.DETECT)
        operatingParameter |= 0x04;

      if (getFelica424() == PARAMETER_OPTION.DETECT)
        operatingParameter |= 0x08;

      if (getTopaz() == PARAMETER_OPTION.DETECT)
        operatingParameter |= 0x10;

      return operatingParameter;
    }
  }

  public static class LedBuzzerBehaviorPicc {
    private LED_BUZZER_BEHAVIOR _piccCardOperationBlinkingLed = LED_BUZZER_BEHAVIOR.DISABLED;
    private LED_BUZZER_BEHAVIOR _piccPollingLed = LED_BUZZER_BEHAVIOR.DISABLED;
    private LED_BUZZER_BEHAVIOR _piccActivationLed = LED_BUZZER_BEHAVIOR.DISABLED;
    private LED_BUZZER_BEHAVIOR _piccCardInsertionRemovalBuzzer = LED_BUZZER_BEHAVIOR.DISABLED;
    private LED_BUZZER_BEHAVIOR _piccPn512ResetIndicationBuzzer = LED_BUZZER_BEHAVIOR.DISABLED;
    private LED_BUZZER_BEHAVIOR _piccColorSelectRed = LED_BUZZER_BEHAVIOR.DISABLED;
    private LED_BUZZER_BEHAVIOR _piccColorSelectGreen = LED_BUZZER_BEHAVIOR.DISABLED;

    public LedBuzzerBehaviorPicc() {

    }

    public LedBuzzerBehaviorPicc(byte behaviorSettings) {
      if ((behaviorSettings & 0x01) == 0x01)
        _piccCardOperationBlinkingLed = LED_BUZZER_BEHAVIOR.ENABLED;

      if ((behaviorSettings & 0x02) == 0x02)
        _piccPollingLed = LED_BUZZER_BEHAVIOR.ENABLED;

      if ((behaviorSettings & 0x04) == 0x04)
        _piccActivationLed = LED_BUZZER_BEHAVIOR.ENABLED;

      if ((behaviorSettings & 0x08) == 0x08)
        _piccCardInsertionRemovalBuzzer = LED_BUZZER_BEHAVIOR.ENABLED;

      if ((behaviorSettings & 0x20) == 0x20)
        _piccPn512ResetIndicationBuzzer = LED_BUZZER_BEHAVIOR.ENABLED;

      if ((behaviorSettings & 0x80) == 0x80)
        _piccColorSelectRed = LED_BUZZER_BEHAVIOR.ENABLED;

      if ((behaviorSettings & 0x40) == 0x40)
        _piccColorSelectGreen = LED_BUZZER_BEHAVIOR.ENABLED;
    }

    public LED_BUZZER_BEHAVIOR getPiccCardOperationBlinkingLed() {
      return _piccCardOperationBlinkingLed;
    }

    public void setPiccCardOperationBlinkingLed(LED_BUZZER_BEHAVIOR value) {
      this._piccCardOperationBlinkingLed = value;
    }

    public LED_BUZZER_BEHAVIOR getPiccPollingLed() {
      return _piccPollingLed;
    }

    public void setPiccPollingLed(LED_BUZZER_BEHAVIOR value) {
      this._piccPollingLed = value;
    }

    public LED_BUZZER_BEHAVIOR getPiccActivationLed() {
      return _piccActivationLed;
    }

    public void setPiccActivationLed(LED_BUZZER_BEHAVIOR value) {
      this._piccActivationLed = value;
    }

    public LED_BUZZER_BEHAVIOR getPiccCardInsertionRemovalBuzzer() {
      return _piccCardInsertionRemovalBuzzer;
    }

    public void setPiccCardInsertionRemovalBuzzer(LED_BUZZER_BEHAVIOR value) {
      this._piccCardInsertionRemovalBuzzer = value;
    }

    public LED_BUZZER_BEHAVIOR getPiccPn512ResetIndicationBuzzer() {
      return _piccPn512ResetIndicationBuzzer;
    }

    public void setPiccPn512ResetIndicationBuzzer(LED_BUZZER_BEHAVIOR value) {
      this._piccPn512ResetIndicationBuzzer = value;
    }

    public LED_BUZZER_BEHAVIOR getPiccColorSelectRed() {
      return _piccColorSelectRed;
    }

    public void setPiccColorSelectRed(LED_BUZZER_BEHAVIOR value) {
      this._piccColorSelectRed = value;
    }

    public LED_BUZZER_BEHAVIOR getPiccColorSelectGreen() {
      return _piccColorSelectGreen;
    }

    public void setPiccColorSelectGreen(LED_BUZZER_BEHAVIOR value) {
      this._piccColorSelectGreen = value;
    }

    public byte getRawLedBuzzerBehaviorPicc() {
      byte ledBuzzerBehavior = 0x00;

      if (getPiccCardOperationBlinkingLed() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x01;

      if (getPiccPollingLed() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x02;

      if (getPiccActivationLed() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x04;

      if (getPiccCardInsertionRemovalBuzzer() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x08;

      if (getPiccPn512ResetIndicationBuzzer() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x20;

      if (getPiccColorSelectRed() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x80;

      if (getPiccColorSelectGreen() == LED_BUZZER_BEHAVIOR.ENABLED)
        ledBuzzerBehavior |= 0x40;

      return ledBuzzerBehavior;
    }
  }

  public static class AutoPiccPollingSettings {
    private POLLING_SETTING _autoPiccPolling = POLLING_SETTING.DISABLED;
    private POLLING_SETTING _turnOffAntennaIfNoPicc = POLLING_SETTING.DISABLED;
    private POLLING_SETTING _turnOffAntennaIfPiccInactive = POLLING_SETTING.DISABLED;
    private POLLING_SETTING _activatePiccWhenDetected = POLLING_SETTING.DISABLED;
    private POLLING_SETTING _enforceIso14443APart4 = POLLING_SETTING.DISABLED;
    private POLLING_INTERVAL _pollingInterval = POLLING_INTERVAL.MS_250;

    public AutoPiccPollingSettings() {

    }

    public AutoPiccPollingSettings(byte rawPiccPollingSetting) {
      setRawPollingSetting(rawPiccPollingSetting);
    }

    public POLLING_SETTING getAutoPiccPolling() {
      return _autoPiccPolling;
    }

    public void setAutoPiccPolling(POLLING_SETTING value) {
      this._autoPiccPolling = value;
    }

    public POLLING_SETTING getTurnOffAntennaIfNoPicc() {
      return _turnOffAntennaIfNoPicc;
    }

    public void setTurnOffAntennaIfNoPicc(POLLING_SETTING value) {
      this._turnOffAntennaIfNoPicc = value;
    }

    public POLLING_SETTING getTurnOffAntennaIfPiccInactive() {
      return _turnOffAntennaIfPiccInactive;
    }

    public void setTurnOffAntennaIfPiccInactive(POLLING_SETTING value) {
      this._turnOffAntennaIfPiccInactive = value;
    }

    public POLLING_SETTING getActivatePiccWhenDetected() {
      return _activatePiccWhenDetected;
    }

    public void setActivatePiccWhenDetected(POLLING_SETTING value) {
      this._activatePiccWhenDetected = value;
    }

    public POLLING_SETTING getEnforceIso14443APart4() {
      return _enforceIso14443APart4;
    }

    public void setEnforceIso14443APart4(POLLING_SETTING value) {
      this._enforceIso14443APart4 = value;
    }

    public POLLING_INTERVAL getPollingInterval() {
      return _pollingInterval;
    }

    public void setPollingInterval(POLLING_INTERVAL value) {
      this._pollingInterval = value;
    }

    public void setRawPollingSetting(byte rawPollingSetting) {
      if ((rawPollingSetting & 0x01) == 0x01)
        _autoPiccPolling = POLLING_SETTING.ENABLED;

      if ((rawPollingSetting & 0x02) == 0x02)
        _turnOffAntennaIfNoPicc = POLLING_SETTING.ENABLED;

      if ((rawPollingSetting & 0x04) == 0x04)
        _turnOffAntennaIfPiccInactive = POLLING_SETTING.ENABLED;

      if ((rawPollingSetting & 0x08) == 0x08)
        _activatePiccWhenDetected = POLLING_SETTING.ENABLED;

      if ((rawPollingSetting & 0x80) == 0x80)
        _enforceIso14443APart4 = POLLING_SETTING.ENABLED;

      switch ((rawPollingSetting >> 4) & 0x03) {
        case 0:
          _pollingInterval = POLLING_INTERVAL.MS_250;
          break;
        case 1:
          _pollingInterval = POLLING_INTERVAL.MS_500;
          break;
        case 2:
          _pollingInterval = POLLING_INTERVAL.MS_1000;
          break;
        case 3:
          _pollingInterval = POLLING_INTERVAL.MS_2500;
          break;
        default:
          _pollingInterval = POLLING_INTERVAL.MS_250;
          break;
      }
    }

    public byte getRawPollingSettings() {
      byte rawPollingSetting = 0x00;

      if (getAutoPiccPolling() == POLLING_SETTING.ENABLED)
        rawPollingSetting |= 0x01;

      if (getTurnOffAntennaIfNoPicc() == POLLING_SETTING.ENABLED)
        rawPollingSetting |= 0x02;

      if (getTurnOffAntennaIfPiccInactive() == POLLING_SETTING.ENABLED)
        rawPollingSetting |= 0x04;

      if (getActivatePiccWhenDetected() == POLLING_SETTING.ENABLED)
        rawPollingSetting |= 0x08;

      if (getEnforceIso14443APart4() == POLLING_SETTING.ENABLED)
        rawPollingSetting |= 0x80;

      switch (getPollingInterval()) {
        case MS_2500:
          rawPollingSetting |= 0x30;
          break;
        case MS_1000:
          rawPollingSetting |= 0x20;
          break;
        case MS_500:
          rawPollingSetting |= 0x10;
          break;
        default:
          rawPollingSetting |= 0x00;
          break;
      }

      return rawPollingSetting;
    }
  }

  public void setSerialNumber(byte[] serialNumber) throws Exception {
    byte[] uResponse;
    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, (byte) 0xDA, (byte) serialNumber.length};
    byte[] apdu = Helper.appendArrays(uCommand, serialNumber);

    sendControlCommand(apdu);
    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set Serial Number Failed");
    }
  }

  public byte[] readSerialNumber() throws Exception {
    byte[] uResponse;
    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x33, 0x00};

    sendControlCommand(uCommand);
    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Read Serial Number Failed");
    }

    return Arrays.copyOfRange(uResponse, 5, uResponse.length);
  }

  public void setAndLockSerialNumber(byte[] serialNumber) throws Exception {
    byte[] uResponse;
    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, (byte) 0xD9, (byte) serialNumber.length};
    byte[] apdu = Helper.appendArrays(uCommand, serialNumber);

    sendControlCommand(apdu);
    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Set and Lock Serial Number Failed");
    }
  }

  public void unlockSerialNumber() throws Exception {
    byte[] uResponse;
    byte[] uCommand = new byte[]{(byte) 0xE0, 0x00, 0x00, 0x3E, 0x00};

    sendControlCommand(uCommand);
    uResponse = getControlResponse();

    if (uResponse[0] != (byte) 0xE1) {
      throw new Exception("Unlock Serial Number Failed");
    }
  }
}
