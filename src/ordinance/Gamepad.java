package ordinance;

import com.ivan.xinput.*;
import com.ivan.xinput.exceptions.XInputNotLoadedException;
import com.ivan.xinput.listener.XInputDeviceListener;

public class Gamepad {
	public XInputDevice device;
	public XInputDevice14 device14;
	public XInputComponents comps;
	public XInputComponentsDelta compsDelta;
	public XInputAxes axes;
	public XInputAxesDelta axesDelta;
	public XInputButtons buttons;
	public XInputButtonsDelta buttonsDelta;
	
	public Gamepad(XInputDevice device) {
		this.device = device;
		this.device14 = null;
		this.comps = device.getComponents();
		this.compsDelta = device.getDelta();
		this.axes = comps.getAxes();
		this.axesDelta = compsDelta.getAxes();
		this.buttons = comps.getButtons();
		this.buttonsDelta = compsDelta.getButtons();
	}
	
	public Gamepad(XInputDevice14 device14) {
		this.device = null;
		this.device14 = device14;
		this.comps = device14.getComponents();
		this.compsDelta = device14.getDelta();
		this.axes = comps.getAxes();
		this.axesDelta = compsDelta.getAxes();
		this.buttons = comps.getButtons();
		this.buttonsDelta = compsDelta.getButtons();
	}
	
	public void addListener(XInputDeviceListener listener) {
		if (device14 != null)
			device14.addListener(listener);
		else if (device != null)
			device.addListener(listener);
	}
	
	public XInputComponents getComponents() {
		return comps;
	}
	
	public XInputComponentsDelta getDelta() {
		if (device14 != null)
			return device14.getDelta();
		else if (device != null)
			return device.getDelta();
		else return null;
	}
	
	public XInputComponents getLastComponents() {
		if (device14 != null)
			return device14.getLastComponents();
		else if (device != null)
			return device.getLastComponents();
		else return null;
	}
	
	public int getPlayerNum() {
		if (device14 != null)
			return device14.getPlayerNum();
		else if (device != null)
			return device.getPlayerNum();
		else return -1;
	}
	
	public boolean isConnected() {
		if (device14 != null)
			return device14.isConnected();
		else if (device != null)
			return device.isConnected();
		else return false;
	}
	
	public boolean poll() {
		if (device14 != null)
			return device14.poll();
		else if (device != null)
			return device.poll();
		else return false;
	}
	
	public void removeListener(XInputDeviceListener listener) {
		if (device14 != null)
			device14.removeListener(listener);
		else if (device != null)
			device.removeListener(listener);
	}
	
	public boolean setVibration(short leftMotor, short rightMotor) {
		if (device14 != null)
			return device14.setVibration(leftMotor, rightMotor);
		else if (device != null)
			return device.setVibration(leftMotor, rightMotor);
		else return false;
	}
	
	
	public static XInputDevice[] getAllDevices() {
		try {
			return XInputDevice.getAllDevices();
		} catch (XInputNotLoadedException e) {
			return null;
		}
	}
	
	public static XInputDevice14[] getAllDevices14() {
		try {
			return XInputDevice14.getAllDevices();
		} catch (XInputNotLoadedException e) {
			return null;
		}
	}
	
	public static XInputDevice getDeviceFor(int playerNum) {
		try {
			return XInputDevice.getDeviceFor(playerNum);
		} catch (XInputNotLoadedException e) {
			return null;
		}
	}
	
	public static XInputDevice14 getDeviceFor14(int playerNum) {
		try {
			return XInputDevice14.getDeviceFor(playerNum);
		} catch (XInputNotLoadedException e) {
			return null;
		}
	}
	
}
