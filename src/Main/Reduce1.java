package Main;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class Reduce1 extends MapReduceBase 
	implements Reducer<Text, Text, Text, Text>{

	@Override
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter report)
			throws IOException {
		
		String[] str0 = values.next().toString().split(",");
		String[] str1 = values.next().toString().split(",");
		
		String mutualFriends = "";
		Integer numMutual = 0;
		for (String x:str0) {
			for (String y:str1) {
				if (x.equals(y)) {
					mutualFriends += x + ",";
					numMutual ++;
				}
			}
		}
		
		if (numMutual > 0) {
			mutualFriends = mutualFriends.substring(0, mutualFriends.length()-1);
			String outString = numMutual.toString() + ":" + mutualFriends;
			output.collect(key, new Text(outString));
		}	
	}
}