package pt.keep.validator.results;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pt.keep.validator.utils.jaxb.MapAdapter;

@XmlRootElement(name = "aiCharacterizationResult")
public class Result {
	private boolean valid;
	private Map<String,String> features;
	
	
	@XmlElement
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@XmlElement
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, String> getFeatures() {
		return features;
	}
	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}
	
	
	
}
