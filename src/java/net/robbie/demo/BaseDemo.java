package net.robbie.demo;

public class BaseDemo {

	public static void main(String[] args) {
		String a = "11";
		String b = "11";
		
		String c = new String("22");
		String d = new String("22");
		System.out.println(a == b);
		System.out.println(c == d);
	}

}
