package org.gnu.sqlloader;

import org.apache.log4j.Logger;

/**
 * App static main()
 *
 */
public class App 
{
	
	final static Logger logger = Logger.getLogger(App.class);
	
    public static void main( String[] args ) throws Exception
    {
    	String strCommandLineArguments = new String("");
    	for (int i=0;i<args.length;i++) {
    		strCommandLineArguments = new String(strCommandLineArguments.concat(" ").concat(args[i]));
    	}
    	logger.info("main( String[] args ):" + strCommandLineArguments);
    	
		// Parser Command Line Arguments ...
		CliArgsParser cliArgsParser = new CliArgsParser(args);
		
		// Create SqlLoader instance ...
		SqlLoader sqlLoader = new SqlLoader(cliArgsParser);

		// Execute SqlLoader ...
		sqlLoader.executeSqlLoader();

    }
}
