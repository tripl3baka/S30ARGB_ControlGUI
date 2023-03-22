import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

public class Data {
    public int brightness_ARGB;
    public int ARGB_R;
    public int ARGB_G;
    public int ARGB_B;
    public int Effect;
    public int brightness_EK;
    public int EK_R;
    public int EK_G;
    public int EK_B;


    static SerialPort commPort;

    //Ustawianie parametrów portu szeregowego
    public void setPort(String selected_port) {
        if(commPort != null) {
            commPort.closePort();
        }

        commPort = SerialPort.getCommPort(selected_port);
        commPort.setComPortParameters(9600, 8, 1, 0);
        commPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
    }

    //Sprawdzanie dostępności portu szeregowego
    public boolean checkPort() {
        if (commPort != null && commPort.openPort() ) {
            return true;
        }
            return false;
    }

    public void read() { //TODO
        if (checkPort()) {
            PrintWriter output = new PrintWriter(commPort.getOutputStream());

            output.print(2137 + " "); //wysłanie do arduino informacji o odczycie z eeprom
            output.flush();
            //delay
            //potem odczyty do zmiennych z javy
            //TODO
        }
    }

    private int getPercentage(int input) {
        return (input * 255) / 100;
    }

    public void sendWriteClose() {
        if(checkPort()) {
            send(1);
            commPort.closePort();
        }
    }

    public void send() {
        send(0);
    }

    private void send(int COMMAND) {

        if (checkPort()) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ignored) {}

            PrintWriter output = new PrintWriter(commPort.getOutputStream());

            output.print((COMMAND) + " ");
            output.print((getPercentage(brightness_ARGB)) + " ");
            output.print((ARGB_R) + " ");
            output.print((ARGB_G) + " ");
            output.print((ARGB_B) + " ");
            output.print((Effect) + " ");
            output.print((getPercentage(brightness_EK)) + " ");
            output.print((EK_R) + " ");
            output.print((EK_G) + " ");
            output.print(EK_B);
            output.flush();
        }

    }

}

