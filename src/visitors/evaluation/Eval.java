package visitors.evaluation;

import java.io.PrintWriter;
import java.util.Iterator;

import environments.*;
import parser.ast.*;
import visitors.*;

import static java.util.Objects.requireNonNull;

public class Eval implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();
	private final PrintWriter printWriter;

	public Eval() {
		printWriter = new PrintWriter(System.out, true);
	}

	public Eval(PrintWriter printWriter) {
		this.printWriter = requireNonNull(printWriter);
	}

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
			// possible runtime errors
			// EnvironmentException: undefined variable
		} catch (EnvironmentException e) {
			throw new EvaluatorException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Ident ident, Exp exp) {
		env.update(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		printWriter.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitDecStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		if (exp.accept(this).asBool())
			thenBlock.accept(this);
		else if (elseBlock != null)
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Value visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	@Override
	public Value visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() + right.accept(this).asInt());
	}

	//L++
	@Override
	public Value visitConcat(Exp left, Exp right) {
		return new StringValue(left.accept(this).asString() + right.accept(this).asString());
	}
	
	@Override
	public Value visitIntLiteral(int value) {
		return new IntValue(value);
	}

	//L++
	@Override
	public Value visitStringLiteral(String value) {
		return new StringValue(value);
	}
	
	@Override
	public Value visitMul(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() * right.accept(this).asInt());
	}

	@Override
	public Value visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).asInt());
	}

	@Override
	public Value visitIdent(Ident id) {
		return env.lookup(id);
	}

	@Override
	public Value visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).asBool());
	}

	@Override
	public Value visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).asBool() && right.accept(this).asBool());
	}

	@Override
	public Value visitBoolLiteral(boolean value) {
		return new BoolValue(value);
	}

	@Override
	public Value visitEq(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public Value visitPairLit(Exp left, Exp right) {
		return new PairValue(left.accept(this), right.accept(this));
	}

	@Override
	public Value visitFst(Exp exp) {
		return exp.accept(this).asPair().getFstVal();
	}

	@Override
	public Value visitSnd(Exp exp) {
		return exp.accept(this).asPair().getSndVal();
	}
	//L++
	@Override
	public Value visitWhileStmt(Exp exp, Block whileBlock) {
		while (exp.accept(this).asBool()) {
			whileBlock.accept(this);
		}
		return null;
	}

	@Override
	public Value visitSingleExp(Exp single) {
		return new SetValue().add(single.accept(this));
	}

	@Override
	public Value visitSetLiteral(ExpSeq expSeq) {
		return expSeq.accept(this);
	}

	@Override
	public Value visitMoreExp(Exp first, ExpSeq rest) {
	    SetValue fst = rest.accept(this).asSet();
		return fst.add(first.accept(this));
	}
	
	@Override
	public Value visitIn(Exp element, Exp set) {
		Iterator<Value> it=set.accept(this).asSet().iterator();
		while (it.hasNext())
			if(it.next().equals((element.accept(this))))
	    		return new BoolValue(true);
	    return new BoolValue(false);
	}
	
	@Override
	public Value visitCardinality(Exp exp) {
		int res=0;
		try {
			res=exp.accept(this).asString().length();
		} catch (EvaluatorException e) {
			res = exp.accept(this).asSet().dim();
			}
		return new IntValue(res);
	}

	@Override
	public Value visitUnion(Exp left, Exp right) {
		return new SetValue(left.accept(this).asSet()).add(right.accept(this).asSet());
	}

	@Override
	public Value visitIntersect(Exp left, Exp right) {
		return new SetValue(left.accept(this).asSet()).intersect(right.accept(this).asSet());
	}
}
