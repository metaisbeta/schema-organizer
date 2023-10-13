package br.unifei.imc.schema_organizer;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class SchemaDiff {

	public static void main(String[] args) {
		
		
		String path = args[0];
		String csv1 = path + "/results-no-java-v1.csv";
		String csv2= path + "/results-no-java-v2.csv";
		System.out.println(path);
		
		//Read the CSV
		Reader readerCsv1, readerCsv2;
		try {
			readerCsv1 = Files.newBufferedReader(Paths.get(csv1));
			CSVReader csvReader1 = new CSVReaderBuilder(readerCsv1).withSkipLines(1).build();
			
			readerCsv2 = Files.newBufferedReader(Paths.get(csv2));
			CSVReader csvReader2 = new CSVReaderBuilder(readerCsv2).withSkipLines(1).build();
			
			List<String[]> records1 = csvReader1.readAll();
			List<String[]> records2 = csvReader2.readAll();
			
			
			List<String> schemasCsv1 = new ArrayList<String>();
			List<String> schemasCsv2 = new ArrayList<String>();
			
			for (String[] record1 : records1) {
				schemasCsv1.add(record1[0]);
			}
			
			for (String[] record2 : records2) {
				schemasCsv2.add(record2[0]);
			}
			
//			Collections.sort(schemasCsv1);
//			Collections.sort(schemasCsv2);
//			
			List<String> schemaTop501 = schemasCsv1.subList(0, 30);
			List<String> schemaTop502 = schemasCsv2.subList(0, 30);
			
			int cont = 0;
			
			
			for (String schema : schemaTop501) {
				if(schemaTop502.contains(schema))
					cont++;
			}
			
			
			System.out.println("Numero de schemas iguais: " + cont);
			
			
			readerCsv2.close();
			readerCsv1.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		}
		

	}

}
