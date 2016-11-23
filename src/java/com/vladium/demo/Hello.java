package com.vladium.demo;

// ----------------------------------------------------------------------------
/**
 * A simple class illustrating dangers of incorrect cross-compilation. Compile
 * is in J2SDK 1.4 using default javac options and try running the resulting
 * class in any earlier J2SDK: you will see a NoSuchMethodError coming from line
 * 20. The article explains how to correctly compile this class using J2SDK
 * 1.4+.
 * <P>
 * 
 * Usage: java Hello "some string"
 * 
 * @author (C) <a
 *         href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad
 *         Roubtsov</a>, 2003
 */
public class Hello {
	public static void main(final String[] args) {
		// this code is Java 1.0-compliant, right?...

		final StringBuffer greeting = new StringBuffer("hello, ");
		final StringBuffer who = new StringBuffer(args[0]).append("!");
		greeting.append(who);

		System.out.println(greeting);
	}

} // end of class
// ----------------------------------------------------------------------------