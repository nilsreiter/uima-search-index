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

	public T getFirst() {
		return findings.get(0);
	}

	public T getLast() {
		return findings.get(findings.size() - 1);
	}

	public int getBegin() {
		return getFirst().getBegin();
	}

	public int getEnd() {
		return getLast().getEnd();
	}

}
