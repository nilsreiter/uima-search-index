package de.unistuttgart.ims.uimautil.search;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.jcas.tcas.Annotation;

public class Finding<T extends Annotation> {
	List<T> findings = new LinkedList<T>();

	public Finding(T anno) {
		findings.add(anno);
	}

	public Finding() {
	}

	public List<T> getFindings() {
		return findings;
	}

	public void setFindings(List<T> findings) {
		this.findings = findings;
	}

}
