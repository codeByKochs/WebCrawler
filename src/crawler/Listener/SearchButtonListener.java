package crawler.Listener;

import crawler.logic.ThreadManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButtonListener implements ActionListener {

    private JTextField urlTextField;
    private ThreadManager manager;
    private JTextField workerTextField;
    private JTextField depthTextField;
    private JTextField timeTextField;
    private JCheckBox depthCheckBox;
    private JCheckBox timeCheckBox;


    public SearchButtonListener(ThreadManager manager, JTextField urlTextField, JTextField workerTextField,
                                JTextField depthTextField, JCheckBox depthCheckBox, JTextField timeTextField,
                                JCheckBox timeCheckBox) {
        this.manager = manager;
        this.urlTextField = urlTextField;
        this.depthTextField = depthTextField;
        this.timeCheckBox = timeCheckBox;
        this.workerTextField = workerTextField;
        this.timeTextField = timeTextField;
        this.depthCheckBox = depthCheckBox;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //TODO change run button to stop button
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    //import data to thread manager
                    importData();
                    //start threads
                    manager.run();
                }
            });
            t1.start();
    }

    private void importData(){
        if (!urlTextField.getText().isBlank() || !urlTextField.getText().isEmpty()){
            manager.setStartingUrl(urlTextField.getText());
        }

        if (!workerTextField.getText().isBlank() || !workerTextField.getText().isEmpty()){
            manager.setNumberOfThreads(Integer.parseInt(workerTextField.getText()));
        }

        if (depthCheckBox.isSelected()){
            manager.setMaximumCrawlingDepth(Integer.parseInt(depthTextField.getText()));
        }

        if (timeCheckBox.isSelected()){
            manager.setTimeLimit(Integer.parseInt(timeTextField.getText()));
        }
    }
}
