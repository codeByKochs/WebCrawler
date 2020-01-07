package crawler.Listener;

import crawler.logic.ThreadManager;
import crawler.logic.Url;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class ExportButtonListener implements ActionListener {

    private JTextField exportUrlTextField;
    private ThreadManager manager;
    private JLabel messageLabel;

    public ExportButtonListener(ThreadManager manager, JTextField exportUrlTextField, JLabel messageLabel){
        this.exportUrlTextField = exportUrlTextField;
        this.manager = manager;
        this.messageLabel = messageLabel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String exportUrl = exportUrlTextField.getText();

        try{
            File file = new File(exportUrl);
            FileWriter writer = new FileWriter(file);

            try{
                Map<Url, String> outputMap = manager.getOutputMap();

                for (Url url : outputMap.keySet()){
                    String urlPath = url.getUrl();
                    String title = url.getTitle();

                    writer.write(urlPath+System.lineSeparator());
                    writer.write(title+System.lineSeparator());
                    writer.write(System.lineSeparator());
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            writer.close();
            messageLabel.setForeground(Color.BLACK);
            messageLabel.setText("Data has been exported");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
