package pack6;

import java.io.*;
import java.util.*;

public class compile12 
{
	private static final String EMPTY_STR = "";
	private static final String SPACE_STR = " ";
	private static final String OUTPUT_DIR = "D:\\compiler";
	private static final String DIR_SEP = System.getProperty("file.separator");
	private static final String TEMP_FILE = OUTPUT_DIR + DIR_SEP + "temp.txt";
	private static final String JAVAC_LOC = "javac";
	private static final String JAVA_LOC = "java" + SPACE_STR + "-cp";
	private static final String CLASS_FILE_EXTN = ".class";

	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter path of the .java file to run: ");
		String input = "d:\\compiler\\temp.java";//scan.nextLine();

		File inputFile = new File(input);
		if (!inputFile.exists() || inputFile.isDirectory()) 
		{
			System.out.println("Input file not found.");
		}
		String output = TEMP_FILE;
		File outputFile = new File(output);
		compile12 comObj = new compile12();
		System.out.println("Compiling code.....");
		String compileCom = JAVAC_LOC + SPACE_STR + input;
		comObj.executeCom(compileCom, input, output);
		if (outputFile.length() == 0)                    //  check logic.... 
		{
			System.out.println("Compilation successful.");
		} 
		else
		{
			System.out.println("Compilation failed with error msg: ");

			System.exit(0);
		}

		List<File> classFileList = comObj.getClassFileList(OUTPUT_DIR);
		if (classFileList.size() != 1) 
		{
			System.out.println("More than one class file exists. Cannot proceed");
			comObj.purgeClassFiles(classFileList);
			System.exit(0);
		}
		System.out.println("Running class file..... ");
		String classFile = classFileList.get(0).getName();
		String className = classFile.replace(CLASS_FILE_EXTN, EMPTY_STR);
		String runCom = JAVA_LOC + SPACE_STR + OUTPUT_DIR + SPACE_STR
				+ className;
		comObj.executeCom(runCom, input, output);
		System.out.println("Output: ");
		comObj.displayFile(output);

		comObj.purgeClassFiles(classFileList);
	}

	public void executeCom(String command, String input, String output) 
	{
		Runtime rt = Runtime.getRuntime();
		Process pr = null;
		BufferedReader stdInput = null;
		BufferedReader stdError = null;
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		String line;
		try {
			pr = rt.exec(command);
			stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			fWriter = new FileWriter(output);
			writer = new BufferedWriter(fWriter);
			while ((line = stdInput.readLine()) != null) 
			{
				line = line.trim();
				if (!EMPTY_STR.equals(line)) 
				{
					writer.write(line.trim());
					writer.newLine();
				}
			}
			while ((line = stdError.readLine()) != null) 
			{
				if (!EMPTY_STR.equals(line)) {
					writer.write(line.trim());
					writer.newLine();
				}
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(stdError);
			close(stdInput);
			pr.destroy();
			close(writer);
			close(fWriter);
		}
	}

	public static void close(java.io.InputStream o) 
	{
		if (o != null) {
			try {
				o.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(java.io.OutputStream o) {
		if (o != null) {
			try {
				o.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(java.io.Reader o) {
		if (o != null) {
			try {
				o.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(java.io.Writer o) {
		if (o != null) {
			try {
				o.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private List<File> getClassFileList(String outputDir) {
		File outputDirFile = new File(outputDir);
		File[] fileList = outputDirFile.listFiles();
		List<File> classFileList = new ArrayList<File>();
		for (File file : fileList) {
			if (file.getName().contains(CLASS_FILE_EXTN)) {
				classFileList.add(file);
			}
		}
		return classFileList;
	}

	private void purgeClassFiles(List<File> classFileList) 
	{
		for (File file : classFileList) 
		{
			file.delete();
		}
	}

	public void displayFile(String file) {
		FileReader fReader = null;
		BufferedReader reader = null;
		try {
			fReader = new FileReader(file);
			reader = new BufferedReader(fReader);
			String line = reader.readLine();
			while (null != line) 
			{
				System.out.println(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(reader);
			close(fReader);
		}
	}
}