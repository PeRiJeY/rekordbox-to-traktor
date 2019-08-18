/**
 * 
 */
package es.german.djtools.rekordboxtotraktor;

import java.io.IOException;
import java.util.Map;

import org.dom4j.DocumentException;

import es.german.djtools.rekordboxtotraktor.rekordbox.RBParser;
import es.german.djtools.rekordboxtotraktor.rekordbox.Track;
import es.german.djtools.rekordboxtotraktor.traktor.TraktorParser;

/**
 * @author Germán
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		
		long timeInit = System.currentTimeMillis();
		RBParser rbParser = new RBParser();
		Map<String, Track> rbTracks = rbParser.parser("examples/ExportEnXML_01062019.xml");
		
		TraktorParser traktorParser = new TraktorParser();
		traktorParser.parser("examples/$COLLECTION.nml", rbTracks);
		
		long timeFinish = System.currentTimeMillis();
	}
	
}
