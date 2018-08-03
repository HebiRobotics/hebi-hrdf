java -Xmx500M -cp "/usr/local/lib/antlr-4.7.1-complete.jar:$CLASSPATH" org.antlr.v4.Tool FloatingPointExpression.g4 
CLASSPATH=".:/usr/local/lib/antlr-4.7.1-complete.jar" javac FloatingPointExpression*.java
CLASSPATH=".:/usr/local/lib/antlr-4.7.1-complete.jar" javac GrammarTestRunner.java -Xlint:deprecation
