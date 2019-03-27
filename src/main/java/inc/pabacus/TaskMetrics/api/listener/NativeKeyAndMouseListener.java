package inc.pabacus.TaskMetrics.api.listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class NativeKeyAndMouseListener implements NativeKeyListener, NativeMouseInputListener {


  private long time = 0;

  public void listen() throws NativeHookException {
    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(this);
    GlobalScreen.addNativeMouseListener(this);
    GlobalScreen.addNativeMouseMotionListener(this);

  }

  public void unListen() throws NativeHookException {
    GlobalScreen.removeNativeKeyListener(this);
    GlobalScreen.removeNativeMouseListener(this);
    GlobalScreen.removeNativeMouseMotionListener(this);
    GlobalScreen.unregisterNativeHook();
  }

  public boolean isRegistered() {
    return GlobalScreen.isNativeHookRegistered();
  }

  public long getTime() {
    return time;
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    updateTime();
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    updateTime();
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    updateTime();
  }

  @Override
  public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    updateTime();
  }

  @Override
  public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
    updateTime();
  }

  @Override
  public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
    updateTime();
  }

  @Override
  public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
    updateTime();
  }

  @Override
  public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
    updateTime();
  }

  private void updateTime() {
    time = System.currentTimeMillis();
  }}
