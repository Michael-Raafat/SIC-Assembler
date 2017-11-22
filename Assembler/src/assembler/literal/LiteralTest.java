package assembler.literal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import assembler.instruction.factory.imp.InstructionFactory;

public class LiteralTest {

	@Test
	public void test() {
		InstructionFactory insFac = new InstructionFactory(null, null, null);
		ILiteral test = new Literal("=C'fasfww'",insFac);
		assertEquals("666173667777",test.getOpCode());
		assertEquals((Integer)6,test.getSize());
		test = new Literal("=X'fasfww'",insFac);
		assertEquals("fasfww",test.getOpCode());
		assertEquals((Integer)6,test.getSize());
		test = new Literal("=W'120'",insFac);
		assertEquals("78",test.getOpCode());
		assertEquals((Integer)3,test.getSize());
		test = new Literal("='120'",insFac);
		assertEquals("78",test.getOpCode());
		assertEquals((Integer)3,test.getSize());
		test = new Literal("='-120'",insFac);
		assertEquals("ffff88",test.getOpCode());
		assertEquals((Integer)3,test.getSize());
	}

}
