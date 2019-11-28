package org.gnu.sqlloader;

/**
 * App static main()
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	
		// Parser Command Line Arguments ...
		CliArgsParser cliArgsParser = new CliArgsParser(args);
		
		// Create SqlLoader instance ...
		SqlLoader sqlLoader = new SqlLoader(cliArgsParser);

		// Execute SqlLoader ...
		sqlLoader.executeSqlLoader();

    }
}
