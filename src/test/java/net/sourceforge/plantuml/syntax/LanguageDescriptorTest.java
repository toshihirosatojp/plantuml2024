package net.sourceforge.plantuml.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

@Execution(ExecutionMode.SAME_THREAD)
@ResourceLock(value = "java.lang.System.err", mode = ResourceAccessMode.READ_WRITE)
class LanguageDescriptorTest {

	@Test
	@StdIo
	void testGetObfuscate(StdErr err) {
		new LanguageDescriptor().getObfuscate();
		// Lazy test to check that there are no warning
		assertEquals("", err.capturedString());
	}

}
