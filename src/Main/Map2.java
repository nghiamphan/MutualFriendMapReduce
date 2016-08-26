package Main;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{
	
	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		
		String[] line = value.toString().split("\t");
		
		String[] couple = line[0].split(",");
		Text fromUser = new Text(couple[0]);
		Text toUser = new Text(couple[1]);
		
		output.collect(fromUser, new Text(toUser + "{" + line[1] + "}"));
		output.collect(toUser, new Text(fromUser + "{" + line[1] + "}"));
	}
}
