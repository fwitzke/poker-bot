package br.poker.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import br.poker.bot.input.image.ImageUtil;
import br.poker.bot.input.image.operations.GrayscaleOp;
import br.poker.bot.input.image.operations.ThresholdGrayscaleOp;

public class ImageCheck {
    private JFrame frame;

    private JPanel panelSelectImage;
    private JButton btnProcessImage, btnSelectImage;
    private JFileChooser imageFileChooser;
    private String selectedImageFileName;
    private JSlider thresholdSlider;

    private JPanel panelResultImage;

    public ImageCheck() {
        init();
    }

    public void init() {
        frame = new JFrame();

        //Create Select image panel
        panelSelectImage = new JPanel();
        panelSelectImage.setLayout(new BoxLayout(panelSelectImage, BoxLayout.X_AXIS));

        panelSelectImage.setBorder(BorderFactory.createTitledBorder("Image Selection"));

        btnSelectImage = new JButton("Select Image");
        btnProcessImage = new JButton("Process");
        imageFileChooser = new JFileChooser(new File("D:\\Dev_\\Netbeans Projects\\JPokerBot\\images"));

        btnSelectImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int res = imageFileChooser.showOpenDialog(frame);
                if(res == JFileChooser.APPROVE_OPTION) {
                    selectedImageFileName = imageFileChooser.getSelectedFile().getPath();
                    Logger.debug("Selected image " + selectedImageFileName);
                }
            }
        });


        btnProcessImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processImage();
            }
        });

        thresholdSlider = new JSlider(0, 255);
        thresholdSlider.setValue(100);
        thresholdSlider.setMajorTickSpacing(15);
        thresholdSlider.setMinorTickSpacing(5);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);

        //add components to panels and frame
        panelSelectImage.add(btnSelectImage);
        panelSelectImage.add(thresholdSlider);
        panelSelectImage.add(btnProcessImage);
        
        //Create Result Image panel
        panelResultImage = new JPanel();
        panelResultImage.setBorder(BorderFactory.createTitledBorder("Result Image"));
        
        //Add to Frame
        frame.getContentPane().add(panelSelectImage, BorderLayout.NORTH);
        frame.getContentPane().add(panelResultImage, BorderLayout.CENTER);
        frame.setSize(800, 700);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void processImage() {
        if(selectedImageFileName == null || "".equals(selectedImageFileName)) {
            System.err.println("No image selected");
            return;
        }

        int threshold = thresholdSlider.getValue();
        Logger.debug("Processing image with threshold " + threshold);

        
        BufferedImage resultImage = null;
        BufferedImage selectedImage = ImageUtil.loadImage(selectedImageFileName);

        GrayscaleOp op = new GrayscaleOp();
        BufferedImage grayScaleImg = op.process(selectedImage);

        ThresholdGrayscaleOp op2 = new ThresholdGrayscaleOp();
        op2.setThreshold(threshold);
        resultImage = op2.process(grayScaleImg);

        //update UI
        panelResultImage.removeAll();

        Icon imageIcon = new ImageIcon(resultImage);
        JLabel imageLabel = new JLabel(imageIcon);
        panelResultImage.add(imageLabel);
        panelResultImage.updateUI();
        
    }

    public static void main(String args[]) {
        new ImageCheck();
    }
}
