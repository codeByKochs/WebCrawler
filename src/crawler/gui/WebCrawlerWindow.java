package crawler.gui;

import crawler.Listener.ExportButtonListener;
import crawler.Listener.SearchButtonListener;
import crawler.logic.ThreadManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebCrawlerWindow extends JFrame {

    private int width;
    private int height;
    private ThreadManager manager;

    public WebCrawlerWindow(ThreadManager manager){
        this.manager = manager;
        this.width = 600;
        this.height = 325;
        run();
    }

    public void run() {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        createComponents(this);
        pack();
        setVisible(true);
    }

    private void createComponents(Container container){

        //set up container
        container.setPreferredSize(new Dimension(width, height));

        // set up constraints for container components
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(1,1,1,1);

        //set up first row
        JLabel urlCaptionLabel = new JLabel("Start URL:");
        c.gridx = 0;
        c.gridy = 0;
        container.add(urlCaptionLabel, c);

        JTextField urlTextField = new JTextField();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        container.add(urlTextField, c);

        JButton searchButton = new JButton("Parse");
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        container.add(searchButton, c);

        //set up second row
        JLabel workerCaptionLabel = new JLabel("Workers:");
        c.gridx = 0;
        c.gridy = 1;
        container.add(workerCaptionLabel, c);


        JTextField workerTextField = new JTextField();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 4;
        container.add(workerTextField, c);


        //set up third row
        JLabel depthCaptionLabel = new JLabel("Maximum depth:");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        container.add(depthCaptionLabel, c);

        JTextField depthTextField = new JTextField();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 3;
        container.add(depthTextField, c);


        JCheckBox depthCheckBox = new JCheckBox("Enabled");
        c.gridx = 4;
        c.gridy = 2;
        c.gridwidth = 1;
        container.add(depthCheckBox, c);

        //set up fourth row
        JLabel timeCaptionLabel = new JLabel("Time limit:");
        c.gridx = 0;
        c.gridy = 3;
        container.add(timeCaptionLabel, c);

        JTextField timeTextField = new JTextField();
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        container.add(timeTextField, c);

        JLabel timeUnitCaptionLabel = new JLabel("seconds");
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        container.add(timeUnitCaptionLabel, c);

        JCheckBox timeCheckBox = new JCheckBox("Enabled");
        c.gridx = 4;
        c.gridy = 3;
        container.add(timeCheckBox, c);

        //set up fifth row
        JLabel elapsedTimeCaptionLabel = new JLabel("Elapsed Time:");
        c.gridx = 0;
        c.gridy = 4;
        container.add(elapsedTimeCaptionLabel, c);

        JLabel elapsedTimeLabel = new JLabel("0:00");
        c.gridx = 1;
        c.gridy = 4;
        container.add(elapsedTimeLabel, c);

        //set up sixth row
        JLabel pageCountCaptionLabel = new JLabel("Parsed Pages:");
        c.gridx = 0;
        c.gridy = 5;
        container.add(pageCountCaptionLabel, c);

        JLabel parsedPagesCountLabel = new JLabel("0");
        c.gridx = 1;
        c.gridy = 5;
        container.add(parsedPagesCountLabel, c);

        //set up seventh row
        JLabel dummyLabel = new JLabel();
        c.gridx = 0;
        c.gridy = 6;
        container.add(dummyLabel, c);

        JLabel messageLabel = new JLabel();
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 6;
        container.add(messageLabel, c);

        //set up eight row
        JLabel exportCaptionLabel = new JLabel("Export:");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        container.add(exportCaptionLabel, c);

        JTextField exportUrlTextField = new JTextField();
        String desktopPath = System.getenv("homepath");
        exportUrlTextField.setText("C:"+desktopPath+"\\Desktop\\Results.txt");
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 3;
        container.add(exportUrlTextField, c);

        JButton exportButton = new JButton("Export");
        c.gridx = 4;
        c.gridy = 7;
        c.gridwidth = 1;
        container.add(exportButton, c);

        //set up swing timer to update elapsed time JTextField
        javax.swing.Timer elapsedTimeTimer = new javax.swing.Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int seconds = manager.getElapsedTime();
                int minutes = 0;
                StringBuilder stringBuilder = new StringBuilder();

                if (seconds > 59){
                    minutes++;
                    seconds = 0;
                }

                String secondsString = "";
                if (seconds < 10){
                    secondsString = "0"+seconds;
                }
                else {
                    secondsString = ""+seconds;
                }

                stringBuilder.setLength(0);
                stringBuilder.append(minutes+":"+secondsString);

                elapsedTimeLabel.setText(stringBuilder.toString());
            }
        });
        elapsedTimeTimer.start();

        //set up swing timer to update parsed pages JTextField
        javax.swing.Timer parsedPagesTimer = new javax.swing.Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                parsedPagesCountLabel.setText(Integer.toString(manager.getParsedPagesCount()));
            }
        });
        parsedPagesTimer.start();

        //grouping checkboxes
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(depthCheckBox);
        buttonGroup.add(timeCheckBox);

        //add messageLabel to ThreadManager
        manager.setMessageLabel(messageLabel);

        //adding listeners
        searchButton.addActionListener(new SearchButtonListener(manager, urlTextField, workerTextField, depthTextField,
                depthCheckBox, timeTextField, timeCheckBox));

        exportButton.addActionListener(new ExportButtonListener(manager, exportUrlTextField, messageLabel));
    }
}