package de.unistuttgart.ims.uimautil.search;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.jcas.tcas.Annotation;

public class Finding {
	List<Annotation> findings = new LinkedList<Annotation>();

	public Finding(Annotation anno) {
		findings.add(anno);
	}

	public Finding() {
	}

	public List<Annotation> getFindings() {
		return findings;
	}

	public void setFindings(List<Annotation> findings) {
		this.findings = findings;
	}

}
