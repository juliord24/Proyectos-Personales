
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.prefs.Preferences;

public class Temporizador extends JDialog implements Serializable {

    public static String preferenceKey;
    private JPanel mainPanel;
    private JLabel timerLabel;
    private JLabel switcherLabel;
    private JButton saltarButton;
    private JButton buttonOK;
    private int segundosRestantes;
    private Timer timer;
    private Preferences preferences = Preferences.userRoot();

    private int bloques;
    private int numBloques = 1;
    public void empezarContador(String preferenceKey){
        if (preferenceKey.equals("tiempoTrabajo")) {
            switcherLabel.setIcon(new ImageIcon("media/tareaGrande.png"));
            switcherLabel.setText("<html><head></head><body>Trabajando...    " +  "<br>Bloque nº " + numBloques +"</body></html>");
        } else{
            switcherLabel.setText("<html><head></head><body>Descansando...    " +  "<br>Bloque nº " + numBloques +"</body></html>");
            switcherLabel.setIcon(new ImageIcon("media/descansoGrande.png"));
        }
        int intervalo = 1000; // en milisegundos
        segundosRestantes = preferences.getInt(preferenceKey, 0) * 60; // 25 minutos en segundos
        timer = new Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundosRestantes--;
                int horas = segundosRestantes / 3600;
                int minutos = (segundosRestantes % 3600) / 60;
                int segundos = segundosRestantes % 60;
                if (segundosRestantes < 0) {
                    timer.stop();
                    if(bloques > 0 && preferenceKey.equals("tiempoTrabajo")){
                        if(numBloques > 4){
                            bloques--;
                            numBloques++;
                            //Temporizador.preferenceKey = "tiempoDescanso1";
                            sonido.play();
                            empezarContador("tiempoDescanso1");
                            App.getDialog().setIconImage(new ImageIcon("media/descanso.png").getImage());
                        } else {
                            bloques--;
                            numBloques++;
                            //Temporizador.preferenceKey = "tiempoDescanso";
                            sonido.play();
                            empezarContador("tiempoDescanso");
                            App.getDialog().setIconImage(new ImageIcon("media/descanso.png").getImage());
                        }

                    } else if (bloques >= 1 && (preferenceKey.equals("tiempoDescanso") || preferenceKey.equals("tiempoDescanso1"))) {
                        if(bloques == 1 ){
                            App.getDialog().dispose();
                        }
                        //Temporizador.preferenceKey = "tiempoTrabajo";
                        sonido.play();
                        empezarContador("tiempoTrabajo");
                        App.getDialog().setIconImage(new ImageIcon("media/tarea.png").getImage());
                    }
                } else {
                    timerLabel.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));
                }
            }
        });
        timer.start();
    }
    SClip sonido = new SClip("media/sonido.wav");
    public Temporizador() {
        setContentPane(mainPanel);
        setModal(true);
        bloques = preferences.getInt("bloques", 0);
        saltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                if(bloques > 0 && preferenceKey.equals("tiempoTrabajo")){
                    if(numBloques > 4){
                        sonido.play();
                        empezarContador("tiempoDescanso1");
                        Temporizador.preferenceKey = "tiempoDescanso1";
                    } else {
                        sonido.play();
                        empezarContador("tiempoDescanso");
                        Temporizador.preferenceKey = "tiempoDescanso";
                    }
                    App.getDialog().setIconImage(new ImageIcon("media/descanso.png").getImage());
                    switcherLabel.setText("<html><head></head><body>Descansando...    " +  "<br>Bloque nº " + numBloques +"</body></html>");
                    switcherLabel.setIcon(new ImageIcon("media/descansoGrande.png"));

                } else if (bloques >= 1 && (preferenceKey.equals("tiempoDescanso") || preferenceKey.equals("tiempoDescanso1"))) {
                    if(bloques == 1){
                        App.getDialog().dispose();
                    }
                    bloques--;
                    numBloques++;
                    sonido.play();
                    empezarContador("tiempoTrabajo");
                    Temporizador.preferenceKey = "tiempoTrabajo";
                    App.getDialog().setIconImage(new ImageIcon("media/tarea.png").getImage());
                    switcherLabel.setIcon(new ImageIcon("media/tareaGrande.png"));
                    switcherLabel.setText("<html><head></head><body>Trabajando...    " +  "<br>Bloque nº " + numBloques +"</body></html>");
                }
            }
        });
    }

    public int getBloques() {
        return bloques;
    }
}
