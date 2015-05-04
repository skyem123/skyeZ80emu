package uk.co.skyem.projects.Z80emu.util;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;

// Made by Vic Nightfall

public class Console {

	private static JFrame consoleFrame;
	private static JScrollPane scrollPane;
	private static JTextPane consoleArea;
	private static JTextField consoleInput;

	private static PrintStream writer;
	private static boolean initialized = false;
	private static InputStream stdin = System.in;

	public static void init(String title) {

		if (initialized) {
			System.err.println("Console already started! Aborting!");
			System.exit(-1);
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		initialized = true;
		try {
			consoleFrame = new JFrame(title);
			consoleFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			consoleFrame.getContentPane().setPreferredSize(new Dimension(600, 300));

			consoleFrame.add(scrollPane = new JScrollPane(consoleArea = new JTextPane()));
			consoleFrame.add(consoleInput = new JTextField(), BorderLayout.SOUTH);

			consoleArea.setEditable(false);
			consoleArea.setFocusable(true);

			consoleInput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

			System.setOut(new ConsoleOutputStream(System.out, Color.black));
			System.setErr(new ConsoleOutputStream(System.err, Color.red));

			PipedInputStream pin = new PipedInputStream();
			writer = new PrintStream(new PipedOutputStream(pin));
			System.setIn(pin);

			Thread inPipe = new Thread(() -> {
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(stdin);
				while (true) {
					writer.println(scanner.nextLine());
					writer.flush();
				}
			});
			inPipe.setDaemon(true);
			inPipe.start();

			consoleInput.addActionListener((ActionEvent action) -> {
				String input = consoleInput.getText();
				consoleInput.setText("");
				writer.println(input);
				writer.flush();
			});

			consoleArea.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					writer.print(e.getKeyChar());
					writer.flush();
				}
			});

			consoleFrame.pack();
			consoleFrame.setLocationRelativeTo(null);
			consoleFrame.setVisible(true);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void append(String s, Color color) {
		AttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground((MutableAttributeSet) set, color);
		StyleConstants.setFontFamily((MutableAttributeSet) set, Font.MONOSPACED);
		try {
			consoleArea.getDocument().insertString(consoleArea.getDocument().getLength(), s, set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	private static class ConsoleOutputStream extends PrintStream {

		private final Color color;

		public ConsoleOutputStream(PrintStream parent, Color color) {
			super(parent);
			this.color = color;
		}

		private String getTrace() {
			StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
			String name = traceElement.getFileName();
			if (name == null) {
				name = traceElement.getClassName();
				name = name.substring(name.lastIndexOf('.', name.length()) + 1);
			}
			return "[" + name + "@" + traceElement.getLineNumber() + "]: ";
		}

		@Override
		public void println(boolean x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(char x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(int x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(long x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(float x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(double x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(char[] x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(String x) {
			super.println(getTrace() + x);
		}

		@Override
		public void println(Object x) {
			super.println(getTrace() + x);
		}

		@Override
		public void write(byte[] buf, int off, int len) {
			super.write(buf, off, len);
			Console.append(new String(buf, off, len), color);
		}

		@Override
		public void write(int b) {
			super.write(b);
			Console.append(String.valueOf(b), color);
		}

		@Override
		public void write(byte[] b) throws IOException {
			super.write(b);
			Console.append(new String(b), color);
		}
	}

}
