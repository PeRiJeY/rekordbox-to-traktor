/**
 * 
 */
package es.german.djtools.rekordboxtotraktor.traktor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import es.german.djtools.rekordboxtotraktor.rekordbox.RBCue;
import es.german.djtools.rekordboxtotraktor.rekordbox.Track;

/**
 * @author Germán
 *
 */
public class TraktorParser {

	public void parser(String pathFileXML, Map<String, Track> rbTracks) throws DocumentException, IOException {
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(pathFileXML).getFile());
		
		Document d = loadFile(file);
		processEntries(d, rbTracks);
		saveDocument(d, pathFileXML);
		
		// System.out.println(d.asXML());
	}
	
	private void saveDocument(Document d, String pathFileXML) throws IOException {
		// Pretty print the document to System.out
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer;
        
        writer = new XMLWriter(new FileOutputStream("COLLECTION-STARED.nml"), format);
        // writer = new XMLWriter(System.out, format);
        writer.write(d);
	}

	public Document loadFile(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }
	
	public void processEntries(Document document, Map<String, Track> rbTracks) {
		List<Node> list = document.selectNodes("NML/COLLECTION/ENTRY");
		
		int contElements = 0;
		int contElementsUpdates = 0;
		for (Iterator<Node> iter = list.iterator(); iter.hasNext();) {
			contElements++;
			Node node = (Node) iter.next();
			Element location = (Element) node.selectSingleNode("LOCATION");
			String fileName = StringEscapeUtils.unescapeHtml(location.attributeValue("FILE"));
			
			Track tbTrack = rbTracks.get(fileName);
			
			if (tbTrack != null) {
				if (tbTrack.getRating() > 0) {
					contElementsUpdates++;
					Element info = (Element) node.selectSingleNode("INFO");
					info.addAttribute("RANKING", String.valueOf(convertRating(tbTrack.getRating())));
				}
				copyCues((Element) node, tbTrack.getListCues());
				
			}
	    }
		System.out.println("Traktor elements processed: " + contElements);
		System.out.println("Traktor elements updates: " + contElementsUpdates);
		
	
	}
	
	private void copyCues(Element nodeEntry, List<RBCue> rbListCues) {
		if (rbListCues != null && rbListCues.size() > 0) {
			List<Node> lActualCue = nodeEntry.selectNodes("CUE_V2");
			for (Node node : lActualCue) {
				node.detach();
			}
			for (RBCue rbCue : rbListCues) {
				if (rbCue.getPosition() >= 0) { // Filter position -1
					Element traktorCue = nodeEntry.addElement("CUE_V2");
					traktorCue.addAttribute("NAME", "n.n.");
					traktorCue.addAttribute("DISPL_ORDER", "0");
					traktorCue.addAttribute("TYPE", (rbCue.getType() == 0 ? "0" : "5"));
					traktorCue.addAttribute("START", String.valueOf(rbCue.getStart() * 1000f));
					if (rbCue.getType() == 4 && (rbCue.getEnd() - rbCue.getStart()) > 0f) { // Loop
						traktorCue.addAttribute("LEN", String.valueOf((rbCue.getEnd() - rbCue.getStart()) * 1000f));
					} else {
						traktorCue.addAttribute("LEN", "0");
					}
					traktorCue.addAttribute("REPEATS", "-1");				
					traktorCue.addAttribute("HOTCUE", String.valueOf(rbCue.getPosition()));
				}
			}
		}
	}
		
	public String locationToFilename(String location) {
		int posIni = location.lastIndexOf('/');
		return location.substring(posIni >= 0 ? posIni + 1 : 0);
	}
	
	private int convertRating(int rbRating) {
		Map<Integer, Integer> mapRbTraktor = new HashMap<>();
		mapRbTraktor.put(255, 255);
		mapRbTraktor.put(204, 196);
		mapRbTraktor.put(153, 128);
		mapRbTraktor.put(102, 64);
		mapRbTraktor.put(51, 0);
		
		Integer result = mapRbTraktor.get(rbRating);
		return result != null ? result.intValue() : 0;
	}

}
