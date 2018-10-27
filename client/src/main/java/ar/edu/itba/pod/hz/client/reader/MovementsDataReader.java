package ar.edu.itba.pod.hz.client.reader;

import ar.edu.itba.pod.hz.model.AirportData;
import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.core.IMap;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MovementsDataReader {

	private static CellProcessor[] getProcessors() {
		return new CellProcessor[] {
				new NotNull(new NotNull()), // clasification
				new NotNull(new NotNull()), // type
				new NotNull(new NotNull()), // originOACI
				new NotNull(new NotNull()) // destOACI
		};
	}

	public static void readDataSet(final IMap<Integer, MovementData> theIMap, String fileNameIn) throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			final FileInputStream is = new FileInputStream(fileNameIn);
			final Reader aReader = new InputStreamReader(is);
			beanReader = new CsvBeanReader(aReader, CsvPreference.EXCEL_PREFERENCE);

			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			MovementData movementData;
			while ((movementData = beanReader.read(MovementData.class, header, processors)) != null) {
				int row_id = beanReader.getRowNumber();
				theIMap.set(row_id, movementData);
			}
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}
}
