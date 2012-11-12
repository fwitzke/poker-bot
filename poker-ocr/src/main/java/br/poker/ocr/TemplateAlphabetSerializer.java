package br.poker.ocr;

import static br.poker.bot.input.image.ImageUtil.loadImage;
import static br.poker.bot.input.image.ImageUtil.writeImage;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.operations.ImageCompareOp;
import br.poker.util.Logger;
import br.poker.util.PNGFilter;

public class TemplateAlphabetSerializer {
	private final String NAME_COLON = "_colon";
	private final String NAME_SLASH_INVERTED = "_slash_inv";
	private final String NAME_SLASH = "_slash";
	private final String NAME_STAR = "_star";
	private final String NAME_QUESTION_MARK = "_question_mark";

	public void save(TemplateAlphabet alphabet, File folder) {
		Logger.log("Storing TemplateAlphabet on folder " + folder.getName());

		if (folder.exists() && folder.isDirectory()) {
			Logger.log("Found folder " + folder.getName());
			for (File f : folder.listFiles()) {
				Logger.log("- Deleting file " + f.getName());
				f.delete();
			}
		} else {
			Logger.log("Creating folder " + folder.getName());
			folder.mkdir();
		}

		for (int tIndex = 0; tIndex < alphabet.getTemplateCharacters().size(); tIndex++) {
			TemplateCharacter tc = alphabet.getTemplateCharacters().get(tIndex);
			String imageName = tc.getValue();

			// is upper case?
			if (imageName.equals(imageName.toUpperCase())) {
				imageName = imageName.toLowerCase() + "+";
			}

			if (imageName.startsWith(":")) {
				imageName = NAME_COLON;
			} else if (imageName.startsWith("?")) {
				imageName = NAME_QUESTION_MARK;
			} else if (imageName.startsWith("/")) {
				imageName = NAME_SLASH;
			} else if (imageName.startsWith("\\")) {
				imageName = NAME_SLASH_INVERTED;
			} else if (imageName.startsWith("*")) {
				imageName = NAME_STAR;
			}

			// CHECK this: when storing append a template number to enable
			// multiple templates per character
			imageName = folder.getPath() + File.separator + imageName
					+ "_template" + tIndex + ".png";

			Logger.log("Writing image to file: " + imageName);

			writeImage(imageName, tc.getTemplate(), "png");
		}

		try {
			File propertiesFile = new File(folder.getPath() + File.separator + "font.properties");
			if (!propertiesFile.exists())
				propertiesFile.createNewFile();

			FileWriter fw = new FileWriter(propertiesFile);
			BufferedWriter bw = new BufferedWriter(fw);

			AlphabetProperties properties = alphabet.getProperties();
			bw.write("threshold=" + properties.getThreshold() + "\n");
			bw.write("isLetterBlack=" + properties.isLetterBlack());

			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TemplateAlphabet load(File imageFolder) {
		TemplateAlphabet alphabet = new TemplateAlphabet(new AlphabetProperties(0, true));
		if (imageFolder != null) {
			File[] allImages = imageFolder.listFiles(new PNGFilter());

			// load images
			for (File f : allImages) {
				String val = f.getName();

				String letter = val.substring(0, val.indexOf("_template"));
				if (val.contains("+")) {
					letter = letter.toUpperCase().replaceAll("\\+", "");
				}

				if (val.contains(NAME_COLON)) {
					letter = ":";
				} else if (val.contains(NAME_QUESTION_MARK)) {
					letter = "?";
				} else if (val.contains(NAME_SLASH)) {
					letter = "/";
				} else if (val.contains(NAME_SLASH_INVERTED)) {
					letter = "\\";
				} else if (val.contains(NAME_STAR)) {
					letter = "*";
				}

				TemplateCharacter tc = new TemplateCharacter(letter, loadImage(f));
				alphabet.addTemplateCharacter(tc);
			}

			try {
				// get font properties
				File properties = new File(imageFolder.getPath()
						+ File.separator + "font.properties");
				if (properties.exists()) {
					FileReader fr = new FileReader(properties);
					BufferedReader br = new BufferedReader(fr);

					while (br.ready()) {
						String line = br.readLine();
						int eqindex = line.indexOf("=");

						AlphabetProperties letterProperties = alphabet.getProperties();
						if (line.contains("threshold")) {
							letterProperties.setThreshold(parseInt(line.substring(eqindex + 1)));
						} else if (line.contains("isLetterBlack")) {
							letterProperties.setLetterBlack(parseBoolean(line.substring(eqindex + 1)));
						}
					}
					br.close();
				} else {
					Logger.error("[WARNING] - FONT " + imageFolder.getName()
							+ " HAS NO PROPERTIES FILE");
				}
			} catch (IOException e) {
				Logger.error("[ERROR] - NOT ABLE TO READ PROPERTIES FILE");
				e.printStackTrace();
			}
		}
		return alphabet;
	}

	public String saveUnknown(ImageSegment unknownWord, File folder) {
		if(!folder.exists()) {
			Logger.error(String.format("Folder %s doesn't exist. Skipping save unknown...", folder.getPath()));
			return "";
		}
		String extension = ".png";
		int i=0;
		String fileName = getUnknownFileName(folder, "unknown", extension);
		File file = new File(fileName);
		while(file.exists()) {
			fileName = getUnknownFileName(folder, "unknown_" + i, extension);
			file = new File(fileName);
			i++;
		}
		
		boolean exists = doesImageExistInFolder(unknownWord.getImage(), folder);
		if(!exists)
			writeImage(fileName, unknownWord.getImage(), "png");
		return fileName;
	}

	private boolean doesImageExistInFolder(BufferedImage unknownWord, File folder) {
		for(File file : folder.listFiles(new PNGFilter())) {
			BufferedImage loaded = loadImage(file);
			if(new ImageCompareOp().equals(loaded, unknownWord)) {
				return true;
			}
		}
		return false;
	}

	private String getUnknownFileName(File folder, String name, String extension) {
		return folder.getPath() + File.separator + name + extension;
	}
}
