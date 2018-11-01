package ar.edu.itba.pod.hz.client.reader;

import ar.edu.itba.pod.hz.model.AirportData;
import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.core.IMap;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class MovementsReader {

	/** https://super-csv.github.io/super-csv/examples_partial_reading.html */
	public static void partialReadWithCsvBeanReader(final IMap<Integer, MovementData> movementsMap, String filename) throws Exception {

		ICsvBeanReader beanReader = null;
		try {
			final FileInputStream fileInputStream = new FileInputStream(filename);
			final Reader inputStreamReader = new InputStreamReader(fileInputStream);
			beanReader = new CsvBeanReader(inputStreamReader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

			beanReader.getHeader(true); // skip past the header (we're defining our own)

			// only map the needed columns - setting header elements to null means those columns are ignored
			final String[] header = new String[] {
					null,
					null,
					null,
					"clasification",
					"type",
					"originOACI",
					"destOACI",
					null,
					null,
					null
			};

			// no processing required for ignored columns
			final CellProcessor[] processors = new CellProcessor[]{
					null,
					null,
					null,
					new ConvertNullTo(""),
					new ConvertNullTo(""),
					new ConvertNullTo(""),
					new ConvertNullTo(""),
					null,
					null,
					null
			};
			HashMap<Integer,MovementData> mapCache=new HashMap<>();
			MovementData movementData;
			int MAXSIZE=500000;
			int n=0;


			while( (movementData = beanReader.read(MovementData.class, header, processors)) != null ) {
				//movementsMap.put(beanReader.getRowNumber(), movementData);
				mapCache.put(beanReader.getRowNumber(), movementData);
//				System.out.println("Putting: " + movementData);
				n++;
				if(n>MAXSIZE){
					movementsMap.putAll(mapCache);
					mapCache=new HashMap<>();
					n=0;
				}
			}
			movementsMap.putAll(mapCache);

		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
}
