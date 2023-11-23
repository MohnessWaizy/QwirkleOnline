package org.ConfigManager;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class ConfigManagerTest {

	@Test
	public void testConfigManagerSavesFile() {
//		Configuration conf = new Configuration(0, 0, 0, 0, 0, null, 0, null, 0, 0);
//		assertTrue(ConfigManager.saveConfig(new File("config"+File.separator+"config.json"), conf));
	}

	@Test
	public void testConfigManagerLoadsFile() {
//		Configuration conf = new Configuration(0, 0, 0, 0, 0, null, 0, null, 0, 0);
//		File file = new File("config"+File.separator+"config.json");
//		ConfigManager.saveConfig(file, conf);
//		Configuration confLoaded = ConfigManager.loadConfig(file);
//		assertTrue(confLoaded.getColorShapeCount() == conf.getColorShapeCount()
//				&& confLoaded.getColorShapeCount() == conf.getColorShapeCount()
//				&& confLoaded.getTileCount() == conf.getTileCount()
//				&& confLoaded.getMaxHandTiles() == conf.getMaxHandTiles()
//				&& confLoaded.getTurnTime() == conf.getTurnTime()
//				&& confLoaded.getTimeVisualization() == conf.getTimeVisualization()
//				&& confLoaded.getWrongMove() == conf.getWrongMove()
//				&& confLoaded.getWrongMovePenalty() == conf.getWrongMovePenalty()
//				&& confLoaded.getSlowMove() == conf.getSlowMove()
//				&& confLoaded.getMaxPlayerNumber() == conf.getMaxPlayerNumber());
	}

	@Test
	public void fileCouldNotBeRead() {
		assertNull(ConfigManager.loadConfig(new File("config"+File.separator+"filename.json")));
	}

}
