package visitors.typechecking;

import static visitors.typechecking.PrimtType.*;

import environments.EnvironmentException;
import environments.GenEnvironment;
import parser.ast.*;
import visitors.Visitor;

public class TypeCheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Ident ident, Exp exp) {
		Type found = env.lookup(ident);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitDecStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	@Override
	public Type visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		BOOL.checkEqual(exp.accept(this));
		thenBlock.accept(this);
		if (elseBlock == null)
			return null;
		elseBlock.accept(this);
		return null;
	}

	@Override
	public Type visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// static semantics of expressions; a type is returned by the visitor

	@Override
	public Type visitAdd(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitIntLiteral(int value) {
		return INT;
	}

	@Override
	public Type visitMul(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitSign(Exp exp) {
		return INT.checkEqual(exp.accept(this));
	}

	@Override
	public Type visitIdent(Ident id) {
		return env.lookup(id);
	}

	@Override
	public Type visitNot(Exp exp) {
		return BOOL.checkEqual(exp.accept(this));
	}

	@Override
	public Type visitAnd(Exp left, Exp right) {
		checkBinOp(left, right, BOOL);
		return BOOL;
	}

	@Override
	public Type visitBoolLiteral(boolean value) {
		return BOOL;
	}

	@Override
	public Type visitEq(Exp left, Exp right) {
		left.accept(this).checkEqual(right.accept(this));
		return BOOL;
	}

	@Override
	public Type visitPairLit(Exp left, Exp right) {
		return new PairType(left.accept(this), right.accept(this));
	}

	@Override
	public Type visitFst(Exp exp) {
		return exp.accept(this).getFstPairType();
	}

	@Override
	public Type visitSnd(Exp exp) {
		return exp.accept(this).getSndPairType();
	}
	
	//L++
	@Override
	public Type visitConcat(Exp left,Exp right) {
		checkBinOp(left, right,left.accept(this));
		return new StringType();
	}

	
	@Override
	public Type visitStringLiteral(String value) {
		return new StringType(value);
	}
	
	@Override
	public Type visitWhileStmt(Exp exp, Block whileBlock) {
		BOOL.checkEqual(exp.accept(this));
		whileBlock.accept(this);
		return null;
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq rest) {
		return first.accept(this).checkEqual(rest.accept(this));
	}

	@Override
	public Type visitSingleExp(Exp single) {
		return single.accept(this);
	}

	@Override
	public Type visitSetLiteral(ExpSeq expSeq) {
		return new SetType(expSeq.accept(this));
	}
	
	@Override
	public Type visitIn(Exp element,Exp set) {
		//BAM!
		Type elemType = element.accept(this);
		Type setElemType = new SetType(elemType);
		setElemType.checkEqual(set.accept(this));
		return BOOL;
	}
	
	@Override
	public Type visitCardinality(Exp exp) {
		//COUNTABLE = SET OR STRING
		exp.accept(this).checkIsCountable();
		return INT;
	}

	@Override
	public Type visitUnion(Exp left, Exp right) {
		left.accept(this).checkIsSetType();
		right.accept(this).checkIsSetType();
		checkBinOp(left, right,left.accept(this));
		return left.accept(this);
	}

	@Override
	public Type visitIntersect(Exp left, Exp right) {
		left.accept(this).checkIsSetType();
		right.accept(this).checkIsSetType();
		checkBinOp(left, right,left.accept(this));
		return left.accept(this);
	}
}
