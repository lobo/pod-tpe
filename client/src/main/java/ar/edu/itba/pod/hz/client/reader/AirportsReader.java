package ar.edu.itba.pod.hz.client.reader;

import ar.edu.itba.pod.hz.model.AirportData;
import com.hazelcast.core.IMap;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AirportsReader {

	/** https://super-csv.github.io/super-csv/examples_partial_reading.html */
	public static void partialReadWithCsvBeanReader(final IMap<String, AirportData> airportsMap, String filename) throws Exception {

		ICsvBeanReader beanReader = null;
		try {
			final FileInputStream fileInputStream = new FileInputStream(filename);
			final Reader inputStreamReader = new InputStreamReader(fileInputStream);
			beanReader = new CsvBeanReader(inputStreamReader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

			beanReader.getHeader(true); // skip past the header (we're defining our own)

			// only map the needed columns - setting header elements to null means those columns are ignored
			final String[] header = new String[] {
					null,
					"oaci",
					"iata",
					null,
					"denomination",
					null, null, null,
					null, null, null,
					null, null, null,
					null, null, null,
					null, null, null,
					null,
					"province",
					null
			};

			// no processing required for ignored columns
			final CellProcessor[] processors = new CellProcessor[] {
					null,
					new ConvertNullTo(""),
					new ConvertNullTo(""),
					null,
					new ConvertNullTo(""),
					null, null, null,
					null, null, null,
					null, null, null,
					null, null, null,
					null, null, null,
					null,
					new ConvertNullTo(""),
					null
			};

			AirportData airportData;
			while( (airportData = beanReader.read(AirportData.class, header, processors)) != null ) {
				if(!"".equals(airportData.getOaci()))
					airportsMap.set(airportData.getOaci(), airportData);
			}

		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
}
