import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings implements Serializable{
    JPanel mainPanel;
    private JSpinner bloquesSpinner;
    private JSpinner trabajoSpinner;
    private JSpinner descansoSpinner;
    private JButton guardarButton;
    private JButton porDefectoButton;
    private JSpinner descansoSpinner1;

    private int bloques;
    private int tiempoTrabajo;
    private int tiempoDescanso;

    private Preferences preferencias;

    public Settings(JFrame frame){
        preferencias  = Preferences.userRoot();
        bloquesSpinner.setValue(preferencias.getInt("bloques", 4));
        trabajoSpinner.setValue(preferencias.getInt("tiempoTrabajo", 25));
        descansoSpinner.setValue(preferencias.getInt("tiempoDescanso", 5));
        descansoSpinner1.setValue(preferencias.getInt("tiempoDescanso1", 15));
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preferencias.putInt("bloques", (int) bloquesSpinner.getValue());
                preferencias.putInt("tiempoTrabajo", (int) trabajoSpinner.getValue());
                preferencias.putInt("tiempoDescanso", (int) descansoSpinner.getValue());
                preferencias.putInt("tiempoDescanso1", (int) descansoSpinner1.getValue());
                try {
                    preferencias.flush();
                } catch (BackingStoreException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });
        porDefectoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bloquesSpinner.setValue(4);
                trabajoSpinner.setValue(25);
                descansoSpinner.setValue(5);
                descansoSpinner1.setValue(15);

            }
        });
    }

}
