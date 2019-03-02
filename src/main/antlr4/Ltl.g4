grammar Ltl;

expression
    : '(' expression ')'                 # parenthesis
    | '!' expression                     # negation
    | 'X' expression                     # next
    | 'F' expression                     # future
    | 'G' expression                     # globally
    | lhs=expression 'U' rhs=expression  # until
    | lhs=expression 'R' rhs=expression  # release
    | lhs=expression '&&' rhs=expression # conjunction
    | lhs=expression '||' rhs=expression # disjunction
    | lhs=expression '->' rhs=expression # implication
    | ID                                 # variable
    | BOOLEAN                            # boolean
    ;

BOOLEAN : 'true' | 'false';
NUM : [0-9]+;
ID : [a-zA-Z][_a-zA-Z0-9]*;
WS : [ \t\r\n]+ -> skip;