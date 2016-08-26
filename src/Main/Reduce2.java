package Main;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class Reduce2 extends MapReduceBase implements Reducer<Text, Text, Text, Text>{

	@Override
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

		String outString ="";
		while (values.hasNext()) {
			outString += values.next() + ";";
		}
		
		output.collect(key, new Text(outString));
	}

}
