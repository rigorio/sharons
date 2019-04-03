package inc.pabacus.TaskMetrics.api.hardware;

import oshi.SystemInfo;
import oshi.hardware.Display;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.UsbDevice;

import java.util.*;
import java.util.stream.Collectors;

public class WindowsHardwareHandler implements HardwareService {

  private HardwareAbstractionLayer hal;

  public WindowsHardwareHandler() {
  }

  @Override
  public List<String> getHardwareData(Device device) {

    hal = getHal();
    UsbDevice[] usbDevices = hal.getUsbDevices(false);
    return Arrays.stream(usbDevices)
        .filter(usbDevice -> usbDevice.toString().toLowerCase().contains(device.getDevice()))
        .map(Object::toString)
        .collect(Collectors.toList());
  }

  @Override
  public List<String> getHardwareData(String name) {

    hal = getHal();
    UsbDevice[] usbDevices = hal.getUsbDevices(false);
    return Arrays.stream(usbDevices)
        .filter(usbDevice -> usbDevice.toString().toLowerCase().contains(name))
        .map(Object::toString)
        .collect(Collectors.toList());
  }

  @Override
  public List<HardwareData> getDisplays() {
    Display[] displays = new SystemInfo().getHardware().getDisplays();
    Arrays.stream(displays).forEach(System.out::println);
    List<String> displayList = Arrays.stream(displays)
        .map(Display::toString)
        .collect(Collectors.toList());
    List<HardwareData> monitors = new ArrayList<>();
    displayList.stream()
        .map((data) -> data.replace("=", ":"))
        .map((data) -> data.replace(": ", ":"))
        .map((data) -> data.replace(" ", ""))
        .map((data) -> data.replace(",", "\n"))
        .collect(Collectors.toList())
        .forEach((data) -> {
          List<String> newData = Arrays.asList(data.split("\n"));
          Map<String, Object> monitor = new HashMap<>();
          newData.forEach(val -> {
            if (val.contains(":")) {
              String[] keyVal = val.split(":");
              monitor.put(keyVal[0], keyVal[1]);
            }
          });

          String item = "Monitor";
          String name = monitor.get("Manuf.ID") + " " + monitor.get("ProductID");
          Object manufacturer = monitor.get("Manuf.ID");
          Object serial = monitor.get("Serial");
          HardwareData hardwareData = new HardwareData();
          hardwareData.setName(name);
          hardwareData.setManufacturer(manufacturer.toString());
          hardwareData.setType(item);
          hardwareData.setSerialNumber(serial.toString());
          monitors.add(hardwareData);
        });
    return monitors;
  }


}
