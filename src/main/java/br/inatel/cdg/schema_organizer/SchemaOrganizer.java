package br.inatel.cdg.schema_organizer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVWriter;

import br.inpe.cap.asniffer.ASniffer;
import br.inpe.cap.asniffer.ASniffer2;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.json.JSONReport;

public class SchemaOrganizer {
	
	private static List<ClassPojo> classesPojo = new ArrayList<ClassPojo>();
	private static Map<String,Integer> schemasUACCOunter = new HashMap<String, Integer>();
	
	public static void main(String args[]) {
		
		String path = "";
		path = args[0];
		
		Set<String> schemas = new HashSet<String>();
		
		File[] directories = new File(path).listFiles(File::isDirectory);
//		
//		for (File dir : directories) {
//			System.out.println("Collecting schemas for projet: " + dir.getAbsolutePath());
//			ASniffer2 runner = new ASniffer2();
//			schemas.addAll(runner.getSchemas(dir));
//			for (String schema : schemas) {
//				System.out.println(schema);
//			}
//		}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
		for (File dir : directories) {
			System.out.println("Collecting for projet: " + dir.getAbsolutePath());
			ASniffer runner = new ASniffer(dir.getAbsolutePath(), path, new JSONReport());
			AMReport report = runner.collectSingle();
			extractSchema(report);
			//generateCSV(path, classesPojo);
			//classesPojo = new ArrayList<ClassPojo>();
		}
	
		generateClassPojo(null, schemasUACCOunter);
		generateCSV(path, classesPojo);
	}

	private static void extractSchema(AMReport report) {
			
		System.out.println(report.getProjectName());
		
		for (PackageModel package_ : report.getPackages()) {
			for (ClassModel classResult : package_.getResults()) {

				Set<String> annotationsCounter = new HashSet<String>();
				classResult.getAnnotationSchemasMap().forEach((sName,schema)->{
					String annotation = sName.substring(0, sName.lastIndexOf("-"));
					if(!annotationsCounter.contains(annotation)) {
						annotationsCounter.add(annotation);
						if(!schemasUACCOunter.containsKey(schema))
							schemasUACCOunter.put(schema, 1);
						else
							schemasUACCOunter.computeIfPresent(schema, (k,v)->v+1);
					}
				});
			}
		}
	}

	private static void generateClassPojo(AMReport report, Map<String, Integer> schemasUACCOunter2) {
		schemasUACCOunter2.forEach((schema,uac) -> {
			ClassPojo pojo = new ClassPojo(null, 
										   "",
										   schema,
										   uac,
					                       0);
			classesPojo.add(pojo);
		});
	}

	private static void generateCSV(String path, List<ClassPojo> classesPojo) {
		Collections.sort(classesPojo);
		
		File file = new File(path + File.separator + "results.csv");
		FileWriter outputfile;
		try {
			outputfile = new FileWriter(file,true);
			
			CSVWriter writer = new CSVWriter(outputfile);
			String[] header = {"Project", "Class", "Schema", "UAC", "LOC"};
			writer.writeNext(header);
			
			for (ClassPojo classPojo : classesPojo) {
				String[] data = {"",
								 "",
								 classPojo.getSchema(),
								 Integer.toString(classPojo.getUac()),
								 ""};
				writer.writeNext(data);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
