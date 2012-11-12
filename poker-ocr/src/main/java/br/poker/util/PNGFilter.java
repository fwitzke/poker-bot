package br.poker.util;

import java.io.File;
import java.io.FileFilter;

public class PNGFilter implements FileFilter {
	@Override
	public boolean accept(File pathname) {
		return pathname.getName().contains("png");
	}
}