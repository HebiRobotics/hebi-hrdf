import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.FileReader;


public class GrammarTestRunner {

  GrammarTestRunner() {
  }

  // Track the number of tests run, and the success rate.
  int error_count = 0;
  int test_count = 0;

  // Set to true for verbose diagnostic output.
  boolean debug = false;

  // Ensures an expression that is supposed to parse correctly does.
  void testGood(String expression) {
    if (!parseFloatingPointExpression(expression, debug)) {
      error_count++;
      System.out.println("Error! " + expression + " should succeed, but has a problem when parsing.");
    }
    test_count++;
  }

  // Ensures an expression that is _not_ supposed to parse correctly does not.
  void testBad(String expression) {
    if (parseFloatingPointExpression(expression, debug)) {
      error_count++;
      System.out.println("Error! " + expression + " should fail, but parses without issue.");
    }
    test_count++;
  }

  // Tries to parse with the "expression" rule, returning "False" if the parsing
  // fails, and "True" if it succeeds.
  static boolean parseFloatingPointExpression(String expr, boolean debug) {
    CharStream stream = CharStreams.fromString(expr);

    FloatingPointExpressionLexer lex = new FloatingPointExpressionLexer(stream);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    FloatingPointExpressionParser parser = new FloatingPointExpressionParser(tokens);
    // This prints diagnostic info about the parsing errors on the command line,
    // which is not what we usually want, especially for cases we expect to fail
    if (!debug)
      parser.removeErrorListeners();
    parser.setErrorHandler(new BailErrorStrategy());

    try {
      if (debug)
        System.out.println("Parsing test expression: " + expr);
      FloatingPointExpressionParser.ExpressionContext tree = parser.expression();
      if (debug)
        System.out.println("Parsed without exception");
      return true;
    } catch (ParseCancellationException ex) { //RecognitionException | NullPointerException | StringIndexOutOfBoundsException | RasterFormatException ex) {
      if (debug)
        System.out.println("Caught exception during parse");
      return false;
    }
  }

  // Run through all the tests in a file.  Tests in the file are indicated by
  // "# Good" (and then two lines with the source expression and the expected
  // result) and "# Bad" (and then one line with the source expression which
  // should not parse).
  void run(String filename) throws java.io.IOException {

    System.out.println("Reading test cases from " + filename);

    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line;
    while ((line = br.readLine()) != null) {
      // Process the line:
      if (line.equals("# Good")) {
        // Get the expression and check it
        line = br.readLine();
        testGood(line);
        // Skip/throw away correct result -- we're not computing the result
        // here, just parsing the expression
        line = br.readLine();
      } else if (line.equals("# Bad")) {
        // Get the expression and check it
        line = br.readLine();
        testBad(line);
      }
    }
  }



  public static void main(String[] args) {
    System.out.println("-- Checking floating point formula test cases for valid grammar. --");
    System.out.println("");

    for (int i = 0; i < args.length; ++i)
    {
 
      GrammarTestRunner runner = new GrammarTestRunner();

      // Set this for diagnostic output:
      //runner.debug = true;
   
      // Read through everything in the test file.
      String test_file = args[i];

      try {
        runner.run(test_file);
        System.out.println("Number of tests run: " + Integer.toString(runner.test_count));
        System.out.println("Number of errors: " + Integer.toString(runner.error_count));
      } catch (java.io.IOException ex) {
        System.out.println("IO Exception! Check that " + test_file + "exists and is readable!");
      }

    }

  }
}
