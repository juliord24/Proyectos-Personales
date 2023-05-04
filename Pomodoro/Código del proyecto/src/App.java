import javax.sql.rowset.spi.SyncResolver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class App implements Serializable {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JButton settingsButton;
    private JPanel historialPanel;
    private JPanel inicioPanel;
    private JTextField tareaTextField;
    private JButton playButton;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JScrollPane scrollPanel;

    private int contador;

    private static Temporizador dialog;

    private static ArrayList<JLabel> jlabels;

    private static ObjectOutputStream oos;

    private static ObjectInputStream ois;

    Preferences preferences;
    public App() {
        
        if(!new File("data/data.dat").exists()){
            tabbedPane.setEnabledAt(1, false);
        }
        preferences = Preferences.userRoot();
        jlabels = new ArrayList<>();
        jlabels.add(label1); jlabels.add(label2); jlabels.add(label3); jlabels.add(label4);
        if(new File("data/data.dat").exists()){
            try {
                ois = new ObjectInputStream(new FileInputStream(new File("data/data.dat")));
                ArrayList<JLabel> jlabels1;
                jlabels1 = (ArrayList<JLabel>) ois.readObject();
                for (int i = 0; i < jlabels.size(); i++) {
                    jlabels.get(i).setText(jlabels1.get(i).getText());
                    jlabels.get(i).setBorder(jlabels1.get(i).getBorder());
                }
                ois.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame settingsFrame = new JFrame("Settings");
                settingsFrame.setContentPane(new Settings(settingsFrame).mainPanel);
                settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                settingsFrame.setSize(500, 300);
                settingsFrame.setLocationRelativeTo(null);
                settingsFrame.setVisible(true);
                settingsFrame.setIconImage(new ImageIcon("media/settingsIcon.png").getImage());
            }
        });
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog = new Temporizador();
                dialog.setTitle(tareaTextField.getText());
                dialog.setIconImage(new ImageIcon("media/tarea.png").getImage());
                dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dialog.setSize(550, 350);
                dialog.setLocationRelativeTo(null);
                if(dialog.getBloques() > 0){
                    Temporizador.preferenceKey = "tiempoTrabajo";
                    dialog.empezarContador("tiempoTrabajo");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    dialog.setVisible(true);
                }

                contador = preferences.getInt("contador", 0);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        tabbedPane.setEnabledAt(1, true);
                        super.windowClosed(e);
                        jlabels.get(contador).setText("<html><head></head><body> " + dialog.getTitle() + "<br>Bloques: " + preferences.getInt("bloques", 0)
                                + "<br>Tiempo trabajo: " + preferences.getInt("tiempoTrabajo", 0) + "<br>Tiempo descanso: "
                                + preferences.getInt("tiempoDescanso", 0) + "<br>Tiempo descanso > 4 bloque: "
                                + preferences.getInt("tiempoDescanso1", 0) + "</body></html>");
                        jlabels.get(contador).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        contador++;
                        if(contador == jlabels.size()){
                            contador = 0;
                        }
                        preferences.putInt("contador", contador);
                    }
                });

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 408);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("media/tomate.png").getImage());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosing(e);
                try {
                    oos = new ObjectOutputStream(new FileOutputStream(new File("data/data.dat")));
                    oos.writeObject(jlabels);
                    oos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        });
    }


    private void createUIComponents() {
        settingsButton = new JButton();
        settingsButton.setIcon(new ImageIcon("media/settingsIcon.png"));
        playButton = new JButton();
        playButton.setIcon(new ImageIcon("media/play.png"));
    }

    public static Temporizador getDialog() {
        return dialog;
    }

}
