package inc.pabacus.TaskMetrics.api.listener;

import org.jnativehook.NativeHookException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NativeKeyAndMouseListenerTest {

  private NativeKeyAndMouseListener nativeKeyAndMouseListener;

  @Before
  public void init() {
    nativeKeyAndMouseListener = new NativeKeyAndMouseListener();
  }

  @Test
  public void testRegisteredBeforeListening() {
    boolean registeredBeforeListening = nativeKeyAndMouseListener.isRegistered();
    assertFalse(registeredBeforeListening);
  }

  @Test
  public void testIsListening() throws NativeHookException {
    nativeKeyAndMouseListener.listen();
    boolean registeredAfterListening = nativeKeyAndMouseListener.isRegistered();
    assertTrue(registeredAfterListening);
    nativeKeyAndMouseListener.unListen();
  }

  @Test
  public void testIsNotListening() throws NativeHookException {
    nativeKeyAndMouseListener.listen();
    nativeKeyAndMouseListener.unListen();
    boolean registeredAfterUnListening = nativeKeyAndMouseListener.isRegistered();
    assertFalse(registeredAfterUnListening);
  }
}
