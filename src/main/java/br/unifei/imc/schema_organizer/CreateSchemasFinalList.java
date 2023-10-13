package br.unifei.imc.schema_organizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class CreateSchemasFinalList {

	public static void main(String[] args) {
		
		List<ClassPojo> classPojos = new ArrayList<ClassPojo>();
		List<ClassPojo> temp = new ArrayList<ClassPojo>();
		
		Map<String, Integer> schemaMap = new HashMap<String, Integer>();
		
		String path = args[0];
		System.out.println(path);
		int threshold = 0;
		String path2 = args[0] + "/resultsFinal.csv";
		
		//Read the CSV
		Reader reader;
		try {
			reader = Files.newBufferedReader(Paths.get(path + "/results.csv"));
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			
			List<String[]> records = csvReader.readAll();
			System.out.println(records);
			int cont = 0;
			String schemaAnt = "";
			
			for (String[] record : records) {
				String schema = record[2];
				System.out.println("Found Schema: " + schema);
				if(!schema.contains("java"))
					continue;
				
				if(!schemaMap.containsKey(schema)) 
					schemaMap.put(schema, Integer.parseInt(record[3]));
				else {
					//schemaMap.computeIfPresent(schema, (k,v) -> Integer.parseInt(record[3]) +  v);
					schemaMap.computeIfPresent(schema, (k,v) -> {
						if(Integer.parseInt(record[3]) > v) 
							return Integer.parseInt(record[3]);
						return v;
					});
				}

//				if(schemaAnt.equals(schema)) {
//					cont++;
//				}else {
//					if(cont >= threshold) {
//						temp.sort(Comparator.comparing(ClassPojo::getUac, Collections.reverseOrder()).
//														thenComparing(ClassPojo::getLoc,Collections.reverseOrder()));
//						classPojos.add(temp.get(0));
//						classPojos.add(temp.get(1));
//					}
//					cont = 1;
//					temp.clear();
//					schemaAnt = schema;
//				}
//				ClassPojo schemaCounter = new ClassPojo
//						(record[0],
//					     record[1],
//					     schema,
//						 new Integer(record[3]),
//						 new Integer(record[4]));
//				temp.add(schemaCounter);

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		}
		
		//convert map to list
		schemaMap.forEach((k,v) -> {
			ClassPojo clazz = new ClassPojo("", "", k, v, 0);
			classPojos.add(clazz);
		});
		
		//Sort by UAC
		Collections.sort(classPojos);
		
		File file = new File(path2);
		FileWriter outputfile;
		try {
			outputfile = new FileWriter(file);
			
			CSVWriter writer = new CSVWriter(outputfile);
//			String[] header = {"Project", "Class", "Schema", "UAC", "LOC"};
//			writer.writeNext(header);
//			
//			for (ClassPojo classCounter : classPojos) {
//				String[] data = {classCounter.getProjectName(),
//								 classCounter.getClassName(),
//								 classCounter.getSchema(),
//								 Integer.toString(classCounter.getUac()),
//								 Integer.toString(classCounter.getLoc())};
//				writer.writeNext(data); 
//			}
			
			String[] header = {"Schema", "UAC"};
			writer.writeNext(header);
			
			for (ClassPojo clazz : classPojos) {
				String[] data = {clazz.getSchema(),Integer.toString(clazz.getUac())};
				writer.writeNext(data);
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
