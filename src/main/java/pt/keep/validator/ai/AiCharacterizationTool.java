package pt.keep.validator.ai;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import pt.keep.validator.ai.results.Result;
import pt.keep.validator.ai.utils.CommandLine;

public class AiCharacterizationTool 
{
	private String version = "1.0";
	
	
	public String getVersion(){
		return version;
	}
	public boolean isUniconvertoInstalled(){
		boolean installed=false;
		try{
			String execOut = CommandLine.exec(Arrays.asList("uniconvertor"),null);
			return true;
		}catch(IOException e){
		}
		return installed;
	}
	
	public boolean isImageMagickInstalled(){
		boolean installed=false;
		try{
			String execOut = CommandLine.exec(Arrays.asList("identify"),null);
			return true;
		}catch(IOException e){
		}
		return installed;
	}
	
	
	public String run(File f) throws IOException, JAXBException{
		if(isUniconvertoInstalled() && isImageMagickInstalled()){
			List<String> command = new ArrayList<String>(Arrays.asList("identify", "-verbose",f.getPath()));
			String identifyOutput = CommandLine.exec(command, null);
			Result res = new Result();
			res.setValid(false);
			res.setFeatures(new Hashtable<String, String>());
			try{
				
				Map<String,String> properties = extractMetadata(identifyOutput);
				
				if(properties.containsKey("Format") && (properties.get("Format").equalsIgnoreCase("PDF (Portable Document Format)") || properties.get("Format").equalsIgnoreCase("PS (PostScript)"))){
					res.setValid(true);
				}
				
				res.setFeatures(properties);
			}catch(Exception e){
				
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			JAXBContext jaxbContext = JAXBContext.newInstance(Result.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(res, bos);
			return bos.toString("UTF-8");
		}else{
			return null;
		}
	}
	
	private Map<String, String> extractMetadata(String identifyOutput) {
		
		Map<String,String> metadata = new TreeMap<String, String>();
		try{
			String last2="",last4="",last6="";
			
			for(String s : identifyOutput.split("\n")){
				if(s.contains(":")){
					String key = s.substring(0,s.indexOf(":")).trim().replace(" ", "_");
					String value = s.substring(s.indexOf(":")+1).trim();
					if(s.contains(": ")){
						key = s.substring(0,s.indexOf(": ")).trim().replace(" ", "_");
						value = s.substring(s.indexOf(": ")+1).trim();
					}
					if(key.contains(":")){
						String[] tokens = key.split(":");
						String newKey="";
						
						for(int i=0;i<tokens.length;i++){
							newKey+=(i==0)?tokens[i]:tokens[i].substring(0, 1).toUpperCase() + tokens[i].substring(1); ;
						}
						key=newKey;
					}
					if(s.startsWith("      ")){
						last6=key;
						key=last2+"."+last4+"."+key;
						if(!value.equals("")){
							metadata.put(key, value);
						}
					}else if(s.startsWith("    ")){
						last4=key;
						key=last2+"."+key;
						if(!value.equals("")){
							metadata.put(key, value);
						}
					}else if(s.startsWith("  ")){
						last2=key;
						if(!value.equals("")){
							metadata.put(key, value);
						}
					}
				}
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return metadata;
	}

	private void printHelp(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar [jar file]", opts);
	}
	
	private void printVersion() {
		System.out.println(version);
	}
	
    public static void main( String[] args )
    {

    	try {
    		AiCharacterizationTool act = new AiCharacterizationTool();
			Options options = new Options();
			options.addOption("f", true, "file to analyze");
			options.addOption("v", false, "print this tool version");
			options.addOption("h", false, "print this message");

			CommandLineParser parser = new GnuParser();
			org.apache.commons.cli.CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) {
				act.printHelp(options);
				System.exit(0);
			}
			
			if (cmd.hasOption("v")) {
				act.printVersion();
				System.exit(0);
			}

			if (!cmd.hasOption("f")) {
				act.printHelp(options);
				System.exit(0);
			}

			File f = new File(cmd.getOptionValue("f"));
			if (!f.exists()) {
				System.out.println("File doesn't exist");
				System.exit(0);
			}
			
			String res = act.run(f);
			if(res!=null){
				System.out.println(res);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		
    }
}
