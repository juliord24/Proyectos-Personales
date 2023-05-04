import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class Contador {
    JPanel mainPanel;
    private JLabel timerLabel;

    private int segundosRestantes;
    private Timer timer;
    private Preferences preferences = Preferences.userRoot();
    public void empezarContador(){
        int intervalo = 1000; // en milisegundos
        segundosRestantes = preferences.getInt("tiempoTrabajo", 0) * 60; // 25 minutos en segundos
        timer = new Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundosRestantes--;
                int horas = segundosRestantes / 3600;
                int minutos = (segundosRestantes % 3600) / 60;
                int segundos = segundosRestantes % 60;
                if (segundosRestantes < 0) {
                    timer.stop();
                    System.out.println(1);
                } else {
                    timerLabel.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));
                }
            }
        });
        timer.start();
    }

    public void cambiarContador(){
        int intervalo = 1000; // en milisegundos
        segundosRestantes = preferences.getInt("tiempoDescanso", 0) * 60; // 25 minutos en segundos
        timer = new Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundosRestantes--;
                int horas = segundosRestantes / 3600;
                int minutos = (segundosRestantes % 3600) / 60;
                int segundos = segundosRestantes % 60;
                if (segundosRestantes < 0) {
                    timer.stop();
                    System.out.println(1);
                } else {
                    timerLabel.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));
                    timerLabel.repaint();
                }
            }
        });
        timer.start();
    }
}
