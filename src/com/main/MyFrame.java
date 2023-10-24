package com.main;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFrame implements ActionListener, WeatherDataCallback {
    JFrame frame;
    RoundPanels panelMain, panel1, panel2;
    JTextField cityTf;
    JLabel lblcity;
    JButton button;
    StringBuffer receivedData;
    BufferedReader reader;
    String resultInString;
    Image close, minimize;
    ImageIcon closeI, minimizeI;
    JLabel lblc, lblm;
    private String APIKEY;
    private Thread thread;
    WeatherData wd;
    // lable for adding component to the remaining two panels
    JLabel cityLabel, currentTemp, currentTempDisplay;


    MyFrame() {

        frame = new JFrame("Weather By MT");
        frame.requestFocus(); // we don't need to click on the frame focuse it
        receivedData = new StringBuffer();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setShape(new RoundRectangle2D.Double(0.0, 0.0, 1280.0, 720.0, 20, 20));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
//        frame.getContentPane().setBackground(new Color(0x212125));
        frame.getContentPane().setBackground(new Color(0x8C8C91));
        frame.setLayout(null);

        try {


            close = new ImageIcon(ClassLoader.getSystemResource("com/images/close.png")).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT);
            closeI = new ImageIcon(close);
            lblc = new JLabel(closeI);
            lblc.setBackground(frame.getBackground());
            lblc.setBounds(8, 8, 15, 15);
            lblc.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }
            });


            minimize = new ImageIcon(ClassLoader.getSystemResource("com/images/minimize.png")).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT);
            minimizeI = new ImageIcon(minimize);
            lblm = new JLabel(minimizeI);
            lblm.setBackground(frame.getBackground());
            lblm.setBounds(28, 8, 15, 15);
            lblm.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.setExtendedState(Frame.ICONIFIED); // minimized when the label is pressed
                }
            });

            frame.add(lblc);
            frame.add(lblm);
        } catch (Exception eI) {
            System.out.println("some error occurred. ");
        }


        // -----------------------------------------Panel Section--------------------------------------------------------------------------------------------d
        panelMain = new RoundPanels(30, 30, new Color(0x737373), new Color(0x7A7D7E));
        panelMain.setBounds(50, 40, 370, 640);
        panelMain.setLayout(null);

        panel1 = new RoundPanels(30, 30, new Color(0xFF737373, true), new Color(0xFF7A7D7E, true));
        panel1.setBounds(450, 40, 370, 640);
        panel1.setLayout(null);

        panel2 = new RoundPanels(30, 30, new Color(0x737373), new Color(0x7A7D7E));
        panel2.setBounds(850, 40, 370, 640);
        panel2.setLayout(null);
        // -----------------------------------------Panel Section--------------------------------------------------------------------------------------------d

        lblcity = new JLabel("Enter a City Name");
        lblcity.setBounds(30, 200, 310, 30);
        lblcity.setFont(new Font("System", Font.BOLD, 20));
        lblcity.setForeground(Color.white);
        lblcity.setBackground(new Color(0, 0, 0, 0)); // transparent
        panelMain.add(lblcity);

        cityTf = new JTextField();
        cityTf.setBounds(30, 250, 310, 30);
        cityTf.setBorder(BorderFactory.createEmptyBorder());
        cityTf.setFont(new Font("System", Font.BOLD, 20));
        cityTf.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
        cityTf.setForeground(Color.BLACK);
//        cityTf.setOpaque(false);
        cityTf.setCaretColor(Color.BLACK);


        button = new JButton("Search");
        button.setBounds(180, 360, 100, 40);
        button.setFocusable(false);
        button.setFont(new Font("System", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0x262F34));
        button.addActionListener(this);


        // ------------------------------- Loading Api key -----------------------------------------------------------

        String inputFile = "P:\\Programming Practice and New learning\\Java Projects\\MyOwn\\Weather App\\src\\com\\main\\api.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String data;
            while ((data = reader.readLine()) != null) {
                APIKEY = data;
            }

        } catch (Exception e) {
            System.out.println("An Error occurred");
        }

        System.out.println(APIKEY); // working test ...


        frame.add(button);
        panelMain.add(cityTf);
        frame.add(panelMain);
        frame.add(panel1);
        frame.add(panel2);
        frame.setVisible(true);

    }

    @Override
    public void onWeatherDataReceived(String resultInString) { // this method ensure that the new weather instace is created when the thread completes
        wd = new WeatherData(resultInString);  // --------------------------------------------> continue from here
        arrangeReceivedData();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String str = cityTf.getText();
        if (!str.isEmpty()) {
            // ------------- Running data requests in new thread -------------------------------
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line;
                    String cityName = cityTf.getText();
                    String apiKey = APIKEY; // Make sure you have obtained the APIKEY earlier in your code
                    String baseUrl = "http://api.openweathermap.org/data/2.5/weather";

                    try {
                        String urlString = baseUrl + "?q=" + cityName + "&appid=" + apiKey;
                        URL url = new URL(urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // setup request
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(10000);
                        connection.setReadTimeout(10000);
                        int status = connection.getResponseCode(); // to check weather the connection is sucessful or not
                        System.out.println(status); // if responsed is 200 then it's a good sign of sucessful connection
                        // Clear the receivedData before each new request
                        receivedData.setLength(0);


                        // let's store the received data
                        if (status > 299) { // checking that there has no error occured
                            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                            while ((line = reader.readLine()) != null) {

                                receivedData.append(line);
                            }
                            JOptionPane.showMessageDialog(null, receivedData, "Error", JOptionPane.ERROR_MESSAGE);
                            reader.close();
                        } else { // run if connection is successful
                            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            while ((line = reader.readLine()) != null) {
                                receivedData.append(line);
                            }
                        }
                        System.out.println(receivedData.toString()); // check purpose
                        connection.disconnect(); // closing the connection
                        // ---------------------------------- ------------------------------------------------------------------
                        // let's convert the received data to string file

                        resultInString = receivedData.toString();

                        // let's create a file to store all the content
                        try {
                            FileWriter writer = new FileWriter("data.json");
//                            writer.write(String.valueOf(receivedData));
                            writer.write(resultInString);
//                            writer.write(String.valueOf(resultInString));
                            writer.close();
                        } catch (Exception eee) {
                            eee.printStackTrace();
                        }


                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                     onWeatherDataReceived(resultInString); // this is the method of the call back interface so if get called as last statementof the thread so that i will call the method when all the req data is recieved
                    /*
                    as we have implemented as call back interface so we muse overdrider the method but the method is that we have override is teh last statemet of the thred so i get called when the thread is bout to completes and in this way we get out data righ
                    Yes, your explanation makes sense. You've correctly identified the key aspects of how the callback interface works in this context:
                    The callback interface defines a method (onWeatherDataReceived in this case) that must be overridden by the class implementing the interface (MyFrame).
                    The method you've overridden (onWeatherDataReceived) is called at a specific point in your code, which is when the API request thread is about to complete its task and has received the data. This ensures that the callback method is executed when the data is available and ready to be processed.
                    By using the callback mechanism, you've established a way for the API request thread to notify MyFrame (or any other class implementing the interface) that the data retrieval process is complete, and it's time to work with the received data.
                    In summary, your explanation accurately describes how the callback interface allows you to coordinate the timing of method execution between different parts of your code, ensuring that data processing occurs when the data is available. This is a common and effective pattern for handling asynchronous operations like network requests.
                     */

                }
            });
            thread.start();


        } else {
            JOptionPane.showMessageDialog(null, "Please Enter a city name", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Method to arrange all component's in the panels and then we call this method inside the callback method to ensure the three must be some data before the we fill the data
    public void arrangeReceivedData() {
        cityLabel = new JLabel(wd.getCityName());
        cityLabel.setFont(new Font("System", Font.BOLD, 25));
        cityLabel.setForeground(Color.WHITE);
        cityLabel.setBackground(Color.BLACK);

        // Calculate the label's width and height based on its text content and font
        FontMetrics metrics = cityLabel.getFontMetrics(cityLabel.getFont());
        int textWidth = metrics.stringWidth(cityLabel.getText());
        int textHeight = metrics.getHeight();
        // Calculate the x-coordinate to center the label within panel1
        int xCoordinate = (panel1.getWidth() - textWidth) / 2;
        cityLabel.setBounds(xCoordinate, 30, textWidth, textHeight);

        currentTemp = new JLabel("Current Temperature");
        currentTemp.setForeground(Color.white);
        currentTemp.setBackground(new Color(122, 125, 126));
        currentTemp.setBounds(30, 60, 120, 30);

        currentTempDisplay = new JLabel();
        currentTempDisplay.setText(wd.getCurrentTemp1() + ""); // converting to string using concatenation
//        currentTempDisplay.setText(Integer.toString(wd.getHumidity())); // converting to string using
        currentTempDisplay.setFont(new Font("system", Font.BOLD, 25));
        currentTempDisplay.setBounds(30, 90, 100, 30);

        panel1.add(currentTempDisplay);
        panel1.add(currentTemp);
        panel1.add(cityLabel);
        // image defree c
        Image celcius = new ImageIcon(ClassLoader.getSystemResource("com/images/celsius.png")).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(celcius);
        Image degree = new ImageIcon(ClassLoader.getSystemResource("com/images/celsius.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(degree);
        JLabel lblImage = new JLabel(icon);
        lblImage.setBounds(50, 87, 30, 30);
        panel1.add(lblImage);

        // let's add some of the other component
        panel1.repaint();

        // Repaint panel1 to ensure the changes are visible


    }

    public void animateComponents() { // to animate the lables etc just by incresing their last rgb value so the thay appear to be loading

    }


}