package graph;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Coloring {
	private final static int RED = 1;
	private final static int BLACK = 2;
	private final static int EMPTY = 0;
	static int[] color;
	private final static String FILE_NAME = "largegraph2";//change name of file and run
	static boolean TWO_COLORABLE = true;
	private static int SIZE;
	private static LinkedList<Integer> oddPath = new LinkedList<Integer>();

	public static void DFS(LinkedList<Integer>[] list) throws IOException {
		boolean[] visited = new boolean[list.length];

		Stack<Integer> stack = new Stack<Integer>();
		int x = 0, w = 0, y = 0;
		stack.push(1);
		visited[1] = true;
		while (!stack.isEmpty()) {
			int v = stack.pop();
			visited[v] = true;
			color(0, v, list);

			if (list[v] != null) {
				while (list[v].size() > x) {
					if (!visited[list[v].get(x)])
						stack.push(list[v].get(x));
					color(v, list[v].get(x), list);
					x++;
				}
				x = 0;
			}
			w = v;
			y++;
		}
		pritnToFile();

	}

	private static void pritnToFile() throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME + " OUTPUT"));
		out.println("2-Colorable: " + TWO_COLORABLE);
		if (TWO_COLORABLE) {
			printColors(out);
		} else {
			printColors(out);
		}
		out.close();
	}

	private static void printColors(PrintWriter out) {
		for (int i = 0; i < SIZE; i++) {
			if (color[i] == RED)
				out.println(i + " " + "RED");
			else if (color[i] == BLACK)
				out.println(i + " " + "BLACK");
		}
		printPath(out);
	}

	private static void printPath(PrintWriter out) {
		if(oddPath.size()>0)
		out.println("------OddCycle------");
		for (int y = 0; y < oddPath.size(); y++) {
			out.println(oddPath.get(y));
		}
	}

	private static void color(int w, int v, LinkedList<Integer>[] list) {

		if (color[v] == EMPTY) {
			if (w == 0)
				color[v] = RED;

			if (color[w] == RED)
				color[v] = BLACK;
			else
				color[v] = RED;

		} else {
			if (color[w] == RED && color[v] == RED) {
				// error
				oddPath(v, list);
				TWO_COLORABLE = false;

			}
			if (color[w] == BLACK && color[v] == BLACK) {
				// error
				oddPath(v, list);
				TWO_COLORABLE = false;
			}
		}
	}

	private static void oddPath(int v, LinkedList<Integer>[] list) {
		for (int i = 0; i < list[v].size(); i++) {
			if (!oddPath.contains(list[v].get(i)))
				oddPath.add(list[v].get(i));
		}

	}

	public static void main(String args[]) {

		System.out.println(System.currentTimeMillis());
		File file = new File(FILE_NAME);
		FileInputStream fis = null;
		BufferedInputStream bis = null;// used for faster reading
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			int size = Integer.parseInt(dis.readLine() + 1);
			LinkedList<Integer>[] table = new LinkedList[size];
			SIZE = size;
			color = new int[SIZE];
		

			while (dis.available() != 0) {
				String[] line = dis.readLine().split(" ");
				if (line.length > 1) {
					int first = Integer.parseInt(line[0]);
					int second = Integer.parseInt(line[1]);

					if (table[first] == null) {
						table[first] = new LinkedList<Integer>();
					}
					if (table[second] == null) {
						table[second] = new LinkedList<Integer>();
					}
					table[first].add(second);
					table[second].add(first);
				}
			}
			DFS(table);
			fis.close();
			bis.close();
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done"); // message when done
		System.out.println(System.currentTimeMillis());

	}
}
