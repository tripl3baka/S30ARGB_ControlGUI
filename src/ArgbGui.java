
import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.util.EventListener;

public class ArgbGui extends JFrame {
    private JPanel mainPanel;
    private JPanel Strip;
    private JSlider ARGB_R_Slider;
    private JSlider ARGB_G_Slider;
    private JSlider ARGB_B_Slider;
    private JSlider ARGB_Brightness_Slider;
    private JTextField Brightness_ARGB;
    private JSlider EK_Brightness_Slider;
    private JTextField Brightness_EK;
    private JSlider EK_G_Slider;
    private JSlider EK_R_Slider;
    private JSlider EK_B_Slider;
    private JComboBox<Integer> EffectComboBox;
    private JButton ARGB_Color_Picker;
    private JButton EK_Color_Picker;
    private JCheckBox connectedCheckBox;
    private JComboBox<String> PortSelectBox;
    private JComboBox EffectBox;


    private void initialValues(Data data) {
        ARGB_Brightness_Slider.setValue(data.brightness_ARGB);
        ARGB_R_Slider.setValue(data.ARGB_R);
        ARGB_G_Slider.setValue(data.ARGB_G);
        ARGB_B_Slider.setValue(data.ARGB_B);
        EK_Brightness_Slider.setValue(data.brightness_EK);
        EK_R_Slider.setValue(data.EK_R);
        EK_G_Slider.setValue(data.EK_G);
        EK_B_Slider.setValue(data.EK_B);

        Brightness_ARGB.setText(String.valueOf(data.brightness_ARGB));
        Brightness_EK.setText(String.valueOf(data.brightness_EK));


    }

    public ArgbGui(String title, Data data) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null); //Centrowanie okienka

        connectedCheckBox.setFocusable(false); //Wyłączenie klikalności checkboxa stanu
        EventListener[] listeners = connectedCheckBox.getListeners(MouseListener.class);
        for (EventListener eventListener : listeners) {
            connectedCheckBox.removeMouseListener((MouseListener) eventListener);
        }


        SerialPort[] portList = SerialPort.getCommPorts();
        for (SerialPort serialPort : portList) {
            if (!serialPort.getSystemPortName().equals("")) {
                PortSelectBox.addItem(serialPort.getSystemPortName());
            }
        }

        initialValues(data); //ustawienie wartości domyślnych

        switch(data.Effect){
            case 0:
                EffectBox.addItem("Sparkle");
        }




        this.addWindowListener(new WindowAdapter() { // Akcja na zamykanie okna - zapis stanu do EEPROM
            @Override
            public void windowClosing(WindowEvent e) {

                data.sendWriteClose();
                e.getWindow().dispose();
            }
        });

        Brightness_ARGB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int brightness = 0;
                try {
                    brightness = Math.min(100, Math.max(0, Integer.parseInt(Brightness_ARGB.getText())));
                } catch (NumberFormatException ignored) {
                }

                Brightness_ARGB.setText(String.valueOf(brightness));
                ARGB_Brightness_Slider.setValue(brightness);

                data.brightness_ARGB = brightness;
                data.send();
            }
        });
        Brightness_EK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int brightness = 0;
                try {
                    brightness = Math.min(100, Math.max(0, Integer.parseInt(Brightness_EK.getText())));
                } catch (NumberFormatException ignored) {
                }

                Brightness_EK.setText(String.valueOf(brightness));
                EK_Brightness_Slider.setValue(brightness);

                data.brightness_EK = brightness;
                data.send();
            }
        });
        ARGB_Brightness_Slider.addChangeListener(new ChangeListener() { //TODO
            @Override

            public void stateChanged(ChangeEvent e) {
                int brightness;
                if (ARGB_Brightness_Slider.getValueIsAdjusting()) {
                    brightness = ARGB_Brightness_Slider.getValue();
                    Brightness_ARGB.setText(String.valueOf(brightness));
                    data.brightness_ARGB = brightness;
                    data.send();

                    System.out.println(brightness);
                    //ARGB_Brightness_Slider.setValue(brightness);

                }
            }

        });

        ARGB_R_Slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int red_multiplier;
                if (ARGB_R_Slider.getValueIsAdjusting()) {
                    red_multiplier = ARGB_R_Slider.getValue();
                    data.ARGB_R = red_multiplier;
                    data.send();
                }
            }
        });
        ARGB_G_Slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int green_multiplier;
                if (ARGB_G_Slider.getValueIsAdjusting()) {
                    green_multiplier = ARGB_G_Slider.getValue();
                    data.ARGB_G = green_multiplier;
                    data.send();
                }

            }
        });
        ARGB_B_Slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int blue_multiplier;
                if (ARGB_B_Slider.getValueIsAdjusting()) {
                    blue_multiplier = ARGB_B_Slider.getValue();
                    data.ARGB_B = blue_multiplier;
                    data.send();
                }

            }
        });
        PortSelectBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected_port = PortSelectBox.getSelectedItem().toString();
                data.setPort(selected_port);

                if (data.checkPort()) { //Wyświetlenie komunikacji z portem COM na checkboxie
                    connectedCheckBox.setSelected(true);
                    data.read();
                }

                System.out.println(selected_port);
            }

        });
    }
}


