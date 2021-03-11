package br.inatel.cdg.schema_organizer;

public class ClassPojo implements Comparable<ClassPojo> {
	
	private String className;
	private int loc;
	private int uac;
	private String projectName;
	private String schema;
	
	public ClassPojo(String projectName, String className, String schema, int uac, int loc) {
		this.className = className;
		this.loc = loc;
		this.uac = uac;
		this.schema = schema;
		this.projectName = projectName;
	}

	public String getClassName() {
		return className;
	}
	public int getLoc() {
		return loc;
	}
	public int getUac() {
		return uac;
	}
	public String getProjectName() {
		return projectName;
	}
	public String getSchema() {
		return schema;
	}
//	@Override
//	public int compareTo(ClassPojo o) {
//		return -1 * (o.getSchema().compareTo(this.schema));
//	}
	
	@Override
	public int compareTo(ClassPojo o) {
		if(uac > o.getUac())
			return -1;
		else if(uac < o.getUac())
			return 1;
		return 0;
	}
}
