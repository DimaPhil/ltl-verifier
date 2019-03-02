grammar Ltl;

expression
    : '(' expression ')'
    | '!' expression
    | 'X' expression
    | 'F' expression
    | 'G' expression
    | lhs=expression 'U' rhs=expression
    | lhs=expression 'R' rhs=expression
    | lhs=expression '&&' rhs=expression
    | lhs=expression '||' rhs=expression
    | lhs=expression '->' rhs=expression
    | ID
    | BOOLEAN
    ;

BOOLEAN : 'true' | 'false';
NUM : [0-9]+;
ID : [a-zA-Z][_a-zA-Z0-9]*;
WS : [ \t\r\n]+ -> skip;