package visitors;

import parser.ast.Block;
import parser.ast.Exp;
import parser.ast.ExpSeq;
import parser.ast.Ident;
import parser.ast.Stmt;
import parser.ast.StmtSeq;

public interface Visitor<T> {
	T visitAdd(Exp left, Exp right);

	T visitAssignStmt(Ident ident, Exp exp);

	T visitIntLiteral(int value);
	
	T visitEq(Exp left, Exp right);

	T visitMoreStmt(Stmt first, StmtSeq rest);

	T visitMul(Exp left, Exp right);

	T visitPrintStmt(Exp exp);

	T visitProg(StmtSeq stmtSeq);

	T visitSign(Exp exp);

	T visitIdent(Ident id); // the only corner case ...

	T visitSingleStmt(Stmt stmt);

	T visitDecStmt(Ident ident, Exp exp);

	T visitNot(Exp exp);

	T visitAnd(Exp left, Exp right);

	T visitBoolLiteral(boolean value);

	T visitIfStmt(Exp exp, Block thenBlock, Block elseBlock);

	T visitBlock(StmtSeq stmtSeq);

	T visitPairLit(Exp left, Exp right);

	T visitFst(Exp exp);

	T visitSnd(Exp exp);
	
	//L++
	
	T visitConcat(Exp left, Exp right);

	T visitStringLiteral(String value);

	T visitWhileStmt(Exp exp, Block whileBlock);

	T visitMoreExp(Exp first, ExpSeq rest);

	T visitSingleExp(Exp single);
	
	T visitSetLiteral(ExpSeq expSeq);

	T visitIn(Exp left, Exp right);

	T visitCardinality(Exp exp);

	T visitUnion(Exp left, Exp right);

	T visitIntersect(Exp left, Exp right);
}
