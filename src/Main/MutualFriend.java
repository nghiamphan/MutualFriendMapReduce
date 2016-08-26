package Main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*

Input: a file whose each line has the format:
x	a,b,c,...
a	x,y...
...
where a,b,c,... are friends of x

Map1: From the above input line, we output the following:
a,x		a,b,c...
b,x		a,b,c...
c,x		a,b,c...
a,x		x,b...
a,b		x,b...	
...

Reduce1:
We sort the output keys of map1 (alphabetically) and keep them as output keys for reduce1.
For the values, we only keep the intersection of the values which has the same key,
and also count the number of intersections, so we have:
a,x		1:b
...
which means a and x has 1 mutual friend, who is b.

Map2:
a	x{1:b}
x	a{1:b}
a	m{2:l,n} 	# for example
...
which means for "a" has a mutual friend with "x" (b), and two mutual friends with "m" (l,n).

Reduce2:
a	x{1:b};m{2:l,n}
...
 */

public class MutualFriend extends Configured implements Tool {
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new MutualFriend(), args);
		System.exit(res);
	}
	
	public int run(String[] args) throws Exception {
		JobConf conf1 = new JobConf(MutualFriend.class);
		conf1.setJobName("job1");
		
		conf1.setOutputKeyClass(Text.class);
		conf1.setOutputValueClass(Text.class);
		
		conf1.setMapperClass(Map1.class);
		conf1.setReducerClass(Reduce1.class);
		
		conf1.setInputFormat(TextInputFormat.class);
		conf1.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(conf1, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf1, new Path(args[1]));
		
		JobClient.runJob(conf1);
		
		JobConf conf2 = new JobConf(MutualFriend.class);
		conf2.setJobName("job2");
		
		conf2.setOutputKeyClass(Text.class);
		conf2.setOutputValueClass(Text.class);
		
		conf2.setMapperClass(Map2.class);
		conf2.setReducerClass(Reduce2.class);
		
		conf2.setInputFormat(TextInputFormat.class);
		conf2.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(conf2, new Path(args[1]));
		FileOutputFormat.setOutputPath(conf2, new Path(args[2]));
		
		JobClient.runJob(conf2);
		return 0;
	}
}