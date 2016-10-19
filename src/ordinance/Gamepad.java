package ordinance;

import com.ivan.xinput.*;
import com.ivan.xinput.listener.XInputDeviceListener;

public class Gamepad {
	public XInputDevice device;
	public XInputComponents comps;
	public XInputComponentsDelta compsDelta;
	public XInputAxes axes;
	public XInputAxesDelta axesDelta;
	public XInputButtons buttons;
	public XInputButtonsDelta buttonsDelta;
	
	public Gamepad(XInputDevice device) {
		this.device = device;
		this.comps = device.getComponents();
		this.compsDelta = device.getDelta();
		this.axes = comps.getAxes();
		this.axesDelta = compsDelta.getAxes();
		this.buttons = comps.getButtons();
		this.buttonsDelta = compsDelta.getButtons();
	}
	
	public void addListener(XInputDeviceListener listener) {
		device.addListener(listener);
	}
	
	public XInputComponents getComponents() {
		return comps;
	}
	
	public XInputComponentsDelta getDelta() {
		return device.getDelta();
	}
	
	public XInputComponents getLastComponents() {
		return device.getLastComponents();
	}
	
	public int getPlayerNum() {
		return device.getPlayerNum();
	}
	
	public boolean isConnected() {
		return device.isConnected();
	}
	
	public boolean poll() {
		return device.poll();
	}
	
	public void removeListener(XInputDeviceListener listener) {
		device.removeListener(listener);
	}
	
	public boolean setVibration(short leftMotor, short rightMotor) {
		return device.setVibration(leftMotor, rightMotor);
	}
	
}
