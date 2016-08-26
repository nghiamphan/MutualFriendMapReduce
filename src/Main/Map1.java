package Main;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class Map1 extends MapReduceBase 
	implements Mapper<LongWritable, Text, Text, Text>{
	
	private Text couple = new Text();
	private Text mutualFriends = new Text();
	
	public void map(LongWritable key, Text value,
			OutputCollector<Text, Text> output,
			Reporter reporter) throws IOException {
		
		String[] line = value.toString().split("\t");
		
		if (line.length > 1) {
			String fromUser = line[0];
			
			String[] friends = line[1].split(",");
			for (String friend:friends) {
				String toUser = friend;
				if (fromUser.compareTo(toUser) < 0) {
					couple = new Text(fromUser + "," + toUser);
				}
				else {
					couple = new Text(toUser + "," + fromUser);
				}				
				mutualFriends = new Text(line[1]);
				output.collect(couple, mutualFriends);
			}
		}
	}
}