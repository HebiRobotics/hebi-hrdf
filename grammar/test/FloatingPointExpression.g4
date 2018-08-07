grammar FloatingPointExpression;

expression

  : arithmeticExpression EOF;

primaryExpression

  : constant

  | '(' arithmeticExpression ')';

unaryOperator

  : '+' | '-';

unaryExpression

  : primaryExpression

  | unaryOperator unaryExpression;

multiplicativeExpression

  : unaryExpression

  | multiplicativeExpression '*' unaryExpression

  | multiplicativeExpression '/' unaryExpression;

additiveExpression

  : multiplicativeExpression

  | additiveExpression '+' multiplicativeExpression

  | additiveExpression '-' multiplicativeExpression;

arithmeticExpression

  : additiveExpression;

argumentExpressionList

  : arithmeticExpression (',' arithmeticExpression)*;

constant

  : decimalFloatingConstant

  | symbolicConstant;

decimalFloatingConstant

  : DecimalFloatingConstant;

symbolicConstant

  : 'pi';

DecimalFloatingConstant

  : FractionalConstant ExponentPart?

  | DigitSequence ExponentPart?;

DigitSequence

  : [0-9]+;

WhiteSpace

  : [ \r\n\t] + -> skip;

fragment FractionalConstant

  : DigitSequence? '.' DigitSequence

  | DigitSequence '.';

fragment ExponentPart

  : 'e' ('+' | '-')? DigitSequence

  | 'E' ('+' | '-')? DigitSequence;

// Handle characters which failed to match any other token
ErrorCharacter : . ;
