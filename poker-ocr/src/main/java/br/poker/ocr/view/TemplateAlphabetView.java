package br.poker.ocr.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.ImageUtil;
import br.poker.bot.input.image.LetterExtractor;
import br.poker.ocr.TemplateAlphabet;
import br.poker.ocr.TemplateCharacter;
import br.poker.util.Logger;
import static br.poker.ocr.TemplateAlphabet.fromFile;

public class TemplateAlphabetView implements ActionListener {
	private static final String ACTION_SELECT_ALPHABET = "SELECT_ALPHABET";
	private static final String ACTION_EXTRACT_LETTERS_FROM_IMAGE = "EXTRACT_LETTERS_FROM_IMAGE";
	private static final String ACTION_SELECT_INPUT_IMAGE = "SELECT_INPUT_IMAGE";
	private final int TEXT_FIELD_DEFAULT_SIZE = 35;
	
	private JFrame view;
	private JPanel panelAlphabet, panelSelectedLetter;
	private JPanel panelAlphabetContainer;
	private JPanel panelInputTesting;
	private JPanel panelImage;
	private JFileChooser alphabetChooser, inputChooser;
	private JTextField txtSelectedInputFile;
	private BufferedImage selectedImageInput;
	
	private TemplateAlphabet alphabet;
	private JTextField txtSelectedFile;
	
	
	public void show() {
		view = new JFrame("Alphabet Edit");
		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		view.getContentPane().add(getAlphabetChooserPanel(), BorderLayout.NORTH);
		view.getContentPane().add(getAlphabetContainerPanel(), BorderLayout.CENTER);
		view.getContentPane().add(getInputTestingPanel(), BorderLayout.SOUTH);
		
		view.pack();
		view.setVisible(true);
	}

	/*
	 *  Alphabet Chooser Panel
	 */
	private JPanel getAlphabetChooserPanel() {
		JPanel panelAlphabetChooser = new JPanel();
		panelAlphabetChooser.setLayout(new BoxLayout(panelAlphabetChooser, BoxLayout.X_AXIS));
		panelAlphabetChooser.setBorder(BorderFactory.createTitledBorder("Alphabet Selection"));
		
		alphabetChooser = new JFileChooser(new File(".\\images"));
		alphabetChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		txtSelectedFile = new JTextField(TEXT_FIELD_DEFAULT_SIZE);
		JLabel labelSelectedFile = new JLabel("Selected Alphabet: ");
		
		JButton buttonChooseAlphabet = new JButton("Select ...");
		buttonChooseAlphabet.setActionCommand(ACTION_SELECT_ALPHABET);
		buttonChooseAlphabet.addActionListener(this);

		panelAlphabetChooser.add(labelSelectedFile);
		panelAlphabetChooser.add(txtSelectedFile );
		panelAlphabetChooser.add(buttonChooseAlphabet);
		return panelAlphabetChooser;
	}

	private void loadAlphabet(File alphabetFile) {
		alphabet = fromFile(alphabetFile);
		
		view.remove(panelAlphabetContainer);
		view.getContentPane().add(getAlphabetContainerPanel(), BorderLayout.CENTER);
		view.pack();
		view.repaint();
	}

	
	/*
	 * Alphabet Overview Panel
	 */
	private JPanel getAlphabetContainerPanel() {
		panelAlphabetContainer = new JPanel();
		
		panelAlphabetContainer.setLayout(new BorderLayout());
		panelAlphabetContainer.add(getAlphabetPanel(), BorderLayout.NORTH);
		
		return panelAlphabetContainer;
	}
	
	private JPanel getAlphabetPanel() {
		panelAlphabet = new JPanel();
		panelAlphabet.setLayout(new BoxLayout(panelAlphabet, BoxLayout.Y_AXIS));
		panelAlphabet.setBorder(BorderFactory.createTitledBorder("Overview"));
		
		if(alphabet != null) {
			Logger.log("Creating alphabet panel...");
			
			List<String> missingLetters = alphabet.getUnknownCharacters();
			panelAlphabet.add(getLettersPanel(TemplateAlphabet.lowerCaseLetters(), missingLetters));
			panelAlphabet.add(getLettersPanel(TemplateAlphabet.upperCaseLetters(), missingLetters));
			panelAlphabet.add(getLettersPanel(TemplateAlphabet.numbers(), 		   missingLetters));
			panelAlphabet.add(getLettersPanel(TemplateAlphabet.specials(), 		   missingLetters));
		}
		
		return panelAlphabet;
	}
	
	
	private JPanel getLettersPanel(List<String> listOfLetters, List<String> missingOnes) {
		JPanel panelLetters = new JPanel();
		panelLetters.setLayout(new BoxLayout(panelLetters, BoxLayout.X_AXIS));
		for(final String character:listOfLetters) {
			JButton buttonLetter = new JButton(character);
			
			if(missingOnes.contains(character)) {
				buttonLetter.setBackground(Color.GRAY);
			} else {
				buttonLetter.setBackground(Color.GREEN);
			}
			
			buttonLetter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					updateSelecteLetterPanel(character, alphabet.getTemplatesByLetter(character));
				}

			});
			
			panelLetters.add(buttonLetter);
		}
		return panelLetters;
	}
	
	
	/*
	 * Alphabet Selected Letters Panel
	 */
	private void updateSelecteLetterPanel(String character, List<TemplateCharacter> templatesByLetter) {
		if(panelSelectedLetter!=null) 
			panelAlphabetContainer.remove(panelSelectedLetter);
		panelAlphabetContainer.add(getSelectedLetterPanel(character, templatesByLetter), BorderLayout.CENTER);

		view.pack();
		view.repaint();
	}
	
	private JPanel getSelectedLetterPanel(String character, List<TemplateCharacter> templatesByLetter) {
		panelSelectedLetter = new JPanel();
		panelSelectedLetter.setBorder(BorderFactory.createTitledBorder("Templates for '" + character + "'"));
		panelSelectedLetter.setLayout(new BoxLayout(panelSelectedLetter, BoxLayout.X_AXIS));
		
		if(templatesByLetter.isEmpty()) {
			panelSelectedLetter.add(new JLabel("No templates available"));
		} else {
			for(TemplateCharacter template:templatesByLetter) {
				BufferedImage image = template.getTemplate();
				ImageIcon imageIcon = new ImageIcon(scaledImage(image, 5));
				JLabel iconLabel = new JLabel(imageIcon);
				
				panelSelectedLetter.add(iconLabel);
				panelSelectedLetter.add(new JLabel("   "));
			}
		}
		
		return panelSelectedLetter;
	}

	private Image scaledImage(BufferedImage image, int scale) {
		int width  = image.getWidth() * scale;
		int height = image.getHeight() * scale;

		Image scaledTemplate = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return scaledTemplate;
	}
	
	
	/*
	 * Inputs Testing
	 */
	private JPanel getInputTestingPanel() {
		panelInputTesting = new JPanel();
		panelInputTesting.setBorder(BorderFactory.createTitledBorder("Input Testing"));
		panelInputTesting.setLayout(new BorderLayout());
		
		inputChooser = new JFileChooser(".\\images");
		txtSelectedInputFile = new JTextField(TEXT_FIELD_DEFAULT_SIZE);
		
		JPanel panelSelectInput = new JPanel();
		
		JButton buttonExtractLetters = new JButton("Extract");
		buttonExtractLetters.addActionListener(this);
		buttonExtractLetters.setActionCommand(ACTION_EXTRACT_LETTERS_FROM_IMAGE);
		
		JButton buttonSelectInput = new JButton("Select ...");
		buttonSelectInput.addActionListener(this);
		buttonSelectInput.setActionCommand(ACTION_SELECT_INPUT_IMAGE);
		
		panelSelectInput.setLayout(new BoxLayout(panelSelectInput, BoxLayout.X_AXIS));
		panelSelectInput.add(new JLabel("Selected Input:"));
		panelSelectInput.add(txtSelectedInputFile);
		panelSelectInput.add(buttonSelectInput);
		panelSelectInput.add(buttonExtractLetters);
		
		panelInputTesting.add(panelSelectInput, BorderLayout.NORTH);
		
		return panelInputTesting;
	}
	
	
	private void loadSelectedInput(File input) {
		if(panelImage != null)
			panelInputTesting.remove(panelImage);
		
		selectedImageInput = ImageUtil.loadImage(input);
		
		panelImage = new JPanel();
		panelImage.add(new JLabel(new ImageIcon(selectedImageInput)));
		panelInputTesting.add(panelImage, BorderLayout.CENTER);
		
		view.pack();
		view.repaint();
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		if(ACTION_SELECT_INPUT_IMAGE.equals(event.getActionCommand())) {
			selectInputImage();
		} else if(ACTION_EXTRACT_LETTERS_FROM_IMAGE.equals(event.getActionCommand())) {
			extractLettersFromImage();
		} else if(ACTION_SELECT_ALPHABET.equals(event.getActionCommand())) {
			selectAlphabetInput();
		}
	}

	private void selectAlphabetInput() {
		alphabetChooser.showDialog(view, "Select");
		File selectedFile = alphabetChooser.getSelectedFile();
		if(selectedFile != null) {
			Logger.log("Selected file " + selectedFile);
			txtSelectedFile.setText(selectedFile.getAbsolutePath());
			
			loadAlphabet(selectedFile);
		}
	}

	private void extractLettersFromImage() {
		Logger.log("Processing input image...");
		
		if(alphabet != null && selectedImageInput != null) {
			ImageSegment[] lettersFromImage = LetterExtractor.getLettersFromImage(new ImageSegment(selectedImageInput),
					alphabet.getProperties());
			
			Logger.log("Found " + lettersFromImage.length + " letters.");
			
			
			JPanel panelResult = new JPanel();
			panelResult.setLayout(new BoxLayout(panelResult, BoxLayout.X_AXIS));
			for(ImageSegment image:lettersFromImage) {
				if(image != null)
					panelResult.add(new JLabel(new ImageIcon(scaledImage(image.getImage(), 5))));
				else
					Logger.debug("Null image o_O");
			}
			panelInputTesting.add(panelResult, BorderLayout.SOUTH);
			
		} else {
			Logger.error("Alphabet or InputImage not selected");
		}
	}

	private void selectInputImage() {
		inputChooser.showDialog(view, "Select");
		File selectedFile = inputChooser.getSelectedFile();
		if(selectedFile != null) {
			Logger.log("Selected input: " + selectedFile);
			txtSelectedInputFile.setText(selectedFile.getAbsolutePath());
			loadSelectedInput(selectedFile);
		}
	}
	
	/*
	 **************************
	 ********* Main ***********
	 **************************
	 */
	public static void main(String args[]) {
		TemplateAlphabetView view = new TemplateAlphabetView();
		view.show();
	}
}
