package br.unifei.imc.schema_organizer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassPojo implements Comparable<ClassPojo> {

	private String projectName;
	private String className;
	private String schema;
	private int uac;
	private int loc;

	@Override
	public int compareTo(ClassPojo o) {
		if(uac > o.getUac())
			return -1;
		else if(uac < o.getUac())
			return 1;
		return 0;
	}
}
