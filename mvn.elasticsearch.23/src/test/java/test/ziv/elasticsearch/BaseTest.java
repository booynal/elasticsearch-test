package test.ziv.elasticsearch;

import java.io.PrintStream;

import org.junit.BeforeClass;

public class BaseTest {

	@BeforeClass
	public static void setUpSystemOut() {
		System.setOut(new PrintStream(System.out) {

			private String getFormatedString(Object x) {
				StackTraceElement[] stackTrace = new Exception().getStackTrace();
				int lineNumber = -1;
				String fileName = null;
				int depth = 2;
				for (int i = depth; -1 == lineNumber; i++) {
					lineNumber = stackTrace[i].getLineNumber();
					fileName = stackTrace[i].getFileName();
				}
				return "(" + fileName + ":" + lineNumber + ")\t" + x;
			}

			@Override
			public void println(String x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(boolean x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(int x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(long x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(double x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(Object x) {
				super.println(getFormatedString(x));
			}
		});
	}

}
