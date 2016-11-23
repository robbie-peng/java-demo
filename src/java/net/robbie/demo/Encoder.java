package net.robbie.demo;

import java.util.Base64;

public class Encoder {

	public static void main(String[] args) {
		String s = "Q139qlhjqJGgigLhklsjlqljlookktkjq";
		String t = "2016051712345678123456";
		String result = new String(Base64.getEncoder().encode(t.getBytes()));
		System.out.println(t);
		System.out.println(result);
	}
}
