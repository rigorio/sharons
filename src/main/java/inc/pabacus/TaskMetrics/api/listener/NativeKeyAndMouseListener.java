package inc.pabacus.TaskMetrics.api.listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class NativeKeyAndMouseListener implements NativeKeyListener, NativeMouseInputListener {


  private long time = 0;

  public void listen() throws NativeHookException {
    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(this);
    GlobalScreen.addNativeMouseListener(this);
    GlobalScreen.addNativeMouseMotionListener(this);

  }

  public void unListen() {
    GlobalScreen.removeNativeKeyListener(this);
    GlobalScreen.removeNativeMouseListener(this);
    GlobalScreen.removeNativeMouseMotionListener(this);
  }

  public long getTime() {
    return time;
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    updateStatus();
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    updateStatus();
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    updateStatus();
  }

  @Override
  public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    updateStatus();
  }

  @Override
  public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
    updateStatus();
  }

  @Override
  public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
    updateStatus();
  }

  @Override
  public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
    updateStatus();
  }

  @Override
  public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
    updateStatus();
  }

  private void updateStatus() {
    time = System.currentTimeMillis();
  }
}
