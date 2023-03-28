import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * The Main class implements an application that reads lines from the standard input
 * and prints them to the standard output.
 */
public class Main {
/**
 * Iterate through each line of input.
 */
public static void main(String[] args) throws IOException {
	InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
	BufferedReader in = new BufferedReader(reader);
	String line;
	while ((line = in.readLine()) != null) {
		System.out.println(line);
	}
	System.out.print("Enter the first string: ");
	StringBuilder str1 = new StringBuilder(in.readLine().toLowerCase());

// read the second string
	System.out.print("Enter the second string: ");
	StringBuilder str2 = new StringBuilder(in.readLine().toLowerCase());

// create arrays to store the count of alphabets in both strings
	int arr1=str1.length();
	int arr2=str2.length();
	if (arr1 == arr2) {
		System.out.println("The strings are anagrams");
	} else {
		System.out.println("The strings are not anagrams");
	}
}
}