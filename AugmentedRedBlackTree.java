import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AugmentedRedBlackTree<I> {

	private final int RED = 0;
	private final int BLACK = 1;
	int count = 0;
	private final Node nil = new Node(-1); // -1 in kaynaðý burasý

	private class Node {

		int key = -1, color = BLACK;
		Node left = nil, right = nil, parent = nil;
		I id;
		String date = "";
		int aug = 0;

		Node(int key, I id, String date) {
			this.key = key;
			this.id = id;
			this.date = date;
		}

		Node(int aug) {
			this.aug = aug;
		}
	}

	private Node root = nil;

	public void printTree(Node node) {
		if (node == nil) {
			return;
		}
		printTree(node.left);
		// System.out.print(((node.color == RED) ? "Color: Red " : "Color: Black ") +
		// "Key: " + "\t" + node.key // ilk hali
		// + " Parent: " + node.parent.key + " ID: " + node.id + " Date: " + node.date +
		// "\n");
		System.out
				.print(((node.color == RED) ? "Color: Red " : "Color: Black ") + "Key: " + "\t" + node.key + " Parent: "
						+ node.parent.key + "\t ID: " + node.id + " Date: " + node.date + " " + "\t" + node.aug + "\n");
		printTree(node.right);
	}

	private Node findNode(Node findNode, Node node) {
		if (root == nil) {
			return null;
		}

		if (findNode.key < node.key) {
			if (node.left != nil) {
				return findNode(findNode, node.left);
			}
		} else if (findNode.key > node.key) {
			if (node.right != nil) {
				return findNode(findNode, node.right);
			}
		} else if (findNode.key == node.key) {
			return node;
		}
		return null;
	}

	private void insert(Node node) { // private --> protected
		Node temp = root;
		if (root == nil) {
			root = node;
			node.color = BLACK;
			node.parent = nil;
		} else {
			node.color = RED;
			while (true) {
				if (node.key < temp.key) {
					if (temp.left == nil) {
						temp.left = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.left;
					}
				} else if (node.key >= temp.key) {
					if (temp.right == nil) {
						temp.right = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.right;
					}
				}
			}
			fixTree(node);
		}
	}

	// Takes as argument the newly inserted node
	private void fixTree(Node node) {
		while (node.parent.color == RED) {
			Node uncle = nil;
			if (node.parent == node.parent.parent.left) {
				uncle = node.parent.parent.right;

				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.right) {
					// Double rotation needed
					node = node.parent;
					rotateLeft(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateRight(node.parent.parent);
			} else {
				uncle = node.parent.parent.left;
				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.left) {
					// Double rotation needed
					node = node.parent;
					rotateRight(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateLeft(node.parent.parent);
			}
		}
		root.color = BLACK;
	}

	void rotateLeft(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.right;
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent;
			node.parent = node.right;
			if (node.right.left != nil) {
				node.right.left.parent = node;
			}
			node.right = node.right.left;
			node.parent.left = node;
		} else {// Need to rotate root
			Node right = root.right;
			root.right = right.left;
			right.left.parent = root;
			root.parent = right;
			right.left = root;
			right.parent = nil;
			root = right;
		}
	}

	void rotateRight(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			node.left.parent = node.parent;
			node.parent = node.left;
			if (node.left.right != nil) {
				node.left.right.parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {// Need to rotate root
			Node left = root.left;
			root.left = root.left.right;
			left.right.parent = root;
			root.parent = left;
			left.right = root;
			left.parent = nil;
			root = left;
		}
	}

	void deleteTree() {
		root = nil;
	}

	void transplant(Node target, Node with) {
		if (target.parent == nil) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	boolean delete(Node z) {
		if ((z = findNode(z, root)) == null)
			return false;
		Node x;
		Node y = z; // temporary reference y
		int y_original_color = y.color;

		if (z.left == nil) {
			x = z.right;
			transplant(z, z.right);
		} else if (z.right == nil) {
			x = z.left;
			transplant(z, z.left);
		} else {
			y = treeMinimum(z.right);
			y_original_color = y.color;
			x = y.right;
			if (y.parent == z)
				x.parent = y;
			else {
				transplant(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}
			transplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		if (y_original_color == BLACK)
			deleteFixup(x);
		return true;
	}

	void inOrder(Node x) {

		if (x == null) {
			return;
		} else {
			inOrder(x.left);

			System.out.print(((x.key == -1) ? "" : x.key + " "));

			System.out.print(((x.key == -1) ? "" : x.date + " "));

			System.out.print(((x.key == -1) ? "" : x.id + " "));
			System.out.println();
			inOrder(x.right);

		}

	}

	int splitter(String z) { // yaþ hesaplar ve yaþ gününü verir
		String[] date = z.split("/");
		int age = (Integer.parseInt(date[0]) * -2 + Integer.parseInt(date[1]) * -35 + Integer.parseInt(date[2]) * -701);
		return age;

	}

	int getNumSmaller1(Node x, int age) { // doðum tarihine bakarak kýyaslama

		if (x == null) {
			return 0;
		} else {
			getNumSmaller1(x.left, age);
			if (x.key < age) {
				count++;
				System.out.println(((x.key == -1) ? "" : x.date + " "));
				getNumSmaller1(x.right, age);
			}

		}
		return count;
	}

	int getNumSmaller1111(Node x, int age) { // aug için

		if (x == null) {
			return 0;
		} else {
			getNumSmaller1111(x.left, age);
			if (x.key < age) {
				count++;
				// System.out.println(((x.key == -1) ? "" : x.date + " "));
				getNumSmaller1111(x.right, age);
			}

		}
		return count;
	}

	int getNumSmaller100(Node x, int age) { // ID verilen tarihi yapýyor getNumsmaller1 ile karýþtýrmayýn

		if (x == null) {
			return 0;
		} else {
			getNumSmaller100(x.left, age);
			if (x.key < age) {
				count++;

				// System.out.println(((x.key == -1) ? "" : x.date + " "));

			}
			// System.out.println(((x.key == -1) ? "" : x.date + " "));
			getNumSmaller100(x.right, age);

		}
		return count;
	}

	void tarihGonderen(Node x, I id) {
		if (x == nil) {
			return;
		} else {
			tarihGonderen(x.left, id);
			if (x.id.equals(id)) {
				// System.out.println(x.date);

				System.out.println("--------->\t" + getNumSmaller1(root, splitter(x.date)));
			}
			tarihGonderen(x.right, id);

		}

	}

	void tarihGonderen2(Node x, I id) {
		if (x == nil) {
			return;
		} else {
			tarihGonderen2(x.left, id);
			if (x.id.equals(id)) {
				// System.out.println(x.date);

				x.aug = getNumSmaller1111(root, splitter(x.date));
				count = 0;
				System.out.println(
						"--------->\t" + x.id + " " + x.date + " ----> " + getNumSmaller1(root, splitter(x.date)));

			}
			tarihGonderen2(x.right, id);

		}

	}

	void tarihGonderen222(Node x, I id) {
		if (x == nil) {
			return;
		} else {
			tarihGonderen222(x.left, id);
			if (x.id.equals(id)) {
				// System.out.println(x.date);

				x.aug = getNumSmaller1111(root, splitter(x.date));
				count = 0;
				// System.out.println(
				// "--------->\t" + x.id + " " + x.date + " ----> " + getNumSmaller1(root,
				// splitter(x.date)));

			}
			tarihGonderen222(x.right, id);

		}

	}

	void dateSender2(Node x, int key, int now) { // key ile doðum tarihi bulan fonk. (minumum)
		int result;
		if (x == null) {
			return;
		} else {
			dateSender2(x.left, key, now);
			if (x.key == key) {
				String[] yil = x.date.split("/");
				result = now - Integer.parseInt(yil[2]);
				System.out
						.println(x.date + "----> " + "The minimum age " + result + " years old with " + "ID: " + x.id);

			}

			dateSender2(x.right, key, now);

		}

	}

	void dateSender(Node x, int key, int now) { // key ile doðum tarihi bulan fonk. (maximum)
		int result;
		if (x == null) {
			return;
		} else {
			dateSender(x.left, key, now);
			if (x.key == key) {
				String[] yil = x.date.split("/");
				result = now - Integer.parseInt(yil[2]);
				System.out
						.println(x.date + "----> " + "The maxiumum age " + result + " years old with " + "ID: " + x.id);

			}

			dateSender(x.right, key, now);

		}

	}

	void findDate(Node x, int id) { // find date with ID
		if (x == nil) {
			return;
		}

		else {
			findDate(x.left, id);
			if (x.id.equals(id)) {
				System.out.print("The result of GETNUMSMALLER2 for the node with id " + x.id + " is ");
				System.out.println(getNumSmaller100(x, splitter(x.date)));

			}
			// System.out.print(((x.key == -1) ? "" : x.key + " "));
			findDate(x.right, id);
			// System.out.print("The result of GETNUMSMALLER2 for the node with id " + x.id
			// + " is ");
			// System.out.println(getNumSmaller100(x, splitter(x.date)));
		}

	}

	int getNumSmaller2(Node x, int age) { // doðum tarihine bakarak kýyaslama

		if (x == null) {
			return 0;
		} else {
			getNumSmaller1(x.left, age);
			if (x.key < age) {
				count++;
				System.out.println(((x.key == -1) ? "" : x.date + " "));
				getNumSmaller1(x.right, age);
			}

		}
		return count;
	}

	int size(Node x) {
		if (x == nil)
			return 0;
		else
			return (size(x.left) + 1 + size(x.right));
	}

	int getMin(Node x) {
		if (x == null) {
			return Integer.MAX_VALUE;
		}
		int result = Integer.MAX_VALUE;
		if (x.key != -1) {
			result = x.key;
		}

		int min = getMin(x.left);

		if (min < result) {
			result = min;
		}

		return result;

	}

	int getMax(Node x) {
		if (x == null) {
			return Integer.MIN_VALUE;
		}

		int result = Integer.MIN_VALUE;
		if (x.key != -1) {
			result = x.key;
		}
		int max = getMax(x.right);

		if (max > result) {
			result = max;
		}

		return result;

	}

	void deleteFixup(Node x) {
		while (x != root && x.color == BLACK) {
			if (x == x.parent.left) {
				Node w = x.parent.right;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateLeft(x.parent);
					w = x.parent.right;
				}
				if (w.left.color == BLACK && w.right.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.right.color == BLACK) {
					w.left.color = BLACK;
					w.color = RED;
					rotateRight(w);
					w = x.parent.right;

				}
				if (w.right.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.right.color = BLACK;
					rotateLeft(x.parent);
					x = root;
				}
			} else {
				Node w = x.parent.left;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateRight(x.parent);
					w = x.parent.left;
				}
				if (w.right.color == BLACK && w.left.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.left.color == BLACK) {
					w.right.color = BLACK;
					w.color = RED;
					rotateLeft(w);
					w = x.parent.left;
				}
				if (w.left.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.left.color = BLACK;
					rotateRight(x.parent);
					x = root;
				}
			}
		}
		x.color = BLACK;
	}

	Node treeMinimum(Node subTreeRoot) {
		while (subTreeRoot.left != nil) {
			subTreeRoot = subTreeRoot.left;
		}
		return subTreeRoot;
	}

	public void reading() throws FileNotFoundException {
		Scanner file = new Scanner(new File("C:\\Users\\asus\\Desktop\\people.txt"));
		String line;
		String[] parse = null;
		String[] date = null;
		int age = 0;
		// int [] agee = new int [100];
		Node node;
		while (file.hasNextLine()) {
			line = file.nextLine();
			parse = line.split(",");
			for (int i = 1; i < parse.length; i = i + 2) {
				// System.out.println(parse[i]);
				date = parse[i].split("/");
				for (int j = 0; j < 1; j++) {
					// age = age + dayCalculator(Integer.parseInt(date[j]), Integer.parseInt(date[j
					// + 1]),
					// Integer.parseInt(date[j + 2]));
					age = (Integer.parseInt(date[j]) * -2 + Integer.parseInt(date[j + 1]) * -35
							+ Integer.parseInt(date[j + 2]) * -701);
					// System.out.println(date[j] + "-" + date[j + 1] + "-" + date[j + 2]);
					// System.out.println(age);
					// agee[i]=Integer.parseInt(parse[i-1]);
					// System.out.println(agee[i]);
					node = new Node(age, (I) parse[j], parse[i]);
					insert(node);

					// System.out.println(age);
					// System.out.println(Integer.parseInt(date[j]));
				}
				// pht.put(converter(parse[i].toLowerCase(),pht.capacity), parse[i]);
				// node = new Node((int)parse[i])
				// insert(parse[i]);
			}

		}
	}

	void augControl() throws FileNotFoundException {
		Scanner file = new Scanner(new File("C:\\Users\\asus\\Desktop\\people.txt"));
		String line;
		String[] parse = null;
		String[] date = null;

		while (file.hasNextLine()) {
			line = file.nextLine();
			parse = line.split(",");
			for (int i = 0; i < parse.length; i = i + 2) {
				// System.out.println(parse[i]);
				count = 0;
				tarihGonderen222(root, (I) (parse[i]));

			}

		}
	}

	public void consoleUI() throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		reading();
		augControl();

		// System.out.println(dayCalculator(20,1,1995)-dayCalculator(16,2,1998));

		while (true) {
			System.out.println("\n1.- Add items\n" + "2.- Print tree\n" + "3.- inOrder\n" + "4.- GetMax\n"
					+ "5.- GetMin\n" + "6.- How many numbers\n" + "7.- GETNUMSMALLER1\n" + "8.- GETNUMSMALLER2\n"
			/* "9.- AugmentedRedBlackTree\n" */);

			int choice = scan.nextInt();
			int dogumYili;
			int item;
			I id;
			//Boolean control = true;
			String newItem; //
			Node node;
			switch (choice) {
			case 1:
				System.out.println("Please enter an ID and a date like NNNN,dd/mm/yyyy");
				newItem = scan.next();
				String[] z = newItem.split(",");
				id = (I) (z[0]);
				item = splitter(z[1]); // // string date --> int age
				node = new Node(item, id, z[1]); // key, id, date
				insert(node);
				count = 0;
				tarihGonderen222(node, id);
				augControl();
				/*
				 * while (control) { node = new Node(item, id, z[1]); // key, id, date
				 * insert(node); count = 0; tarihGonderen222(node,id); newItem = scan.next(); }
				 */
				// printTree(root);
				break;

			case 2:
				printTree(root);
				break;

			case 3:
				inOrder(root);
				// System.out.println("al sana inorder");
				break;
			case 4:
				System.out.println("Please enter a year without day and month: ");
				dogumYili = scan.nextInt();
				// System.out.println(getMax(root) + " al sana max");
				dateSender(root, getMax(root), dogumYili); // root,key
				break;
			case 5:
				System.out.println("Please enter a year without day and month: ");
				dogumYili = scan.nextInt();
				// System.out.print("8.- GetMin");
				// System.out.println(getMin(root) + " al sana min");
				dateSender2(root, getMin(root), dogumYili); // root ,key
				// System.out.println(treeMinimum(root));
				break;
			case 6:
				// inOrder2(root);
				// System.out.println(numberSize(root) + "al sana kaç tane sayý");
				System.out.println("The number of all people is " + size(root) + ".");
				break;
			case 7:
				// getNumSmaller1
				System.out.println("Please enter a date like dd/mm/yyyy:");
				String dateInput = scan.next();
				count = 0;
				System.out.println("The result of GETNUMSMALLER1 for the node with birthdate " + dateInput + " is "
						+ getNumSmaller1(root, splitter(dateInput)));
				// System.out.println(splitter(dateInput)); // key kontrol
				// getNumSmaller1(root, splitter(dateInput));
				break;
			case 8:
				// getNumSmaller2
				count = 0;
				System.out.println("Please enter an ID: ");
				I in = (I) scan.next();
				// findDate(root, dateInput2);
				System.out.println("The result of GETNUMSMALLER2 for the node with birthdate " + in + ": ");
				tarihGonderen(root, in);
				// System.out.println("The result of GETNUMSMALLER2 for the node with birthdate
				// " + id + ": ");

				// getNumSmaller2(root,dateSender(root ,dateInput2));

				// System.out.println("The result of GETNUMSMALLER2 for the node with birthdate
				// "+dateInput2+" is "+getNumSmaller1(root,);
				// System.out.println(splitter(dateInput)); // key kontrol
				// getNumSmaller1(root, splitter(dateInput));
				break;
			/*
			 * case 9: // System.out.println("The result of GETNUMSMALLER2 for the node with
			 * birthdate // " + dateInput2 + ": "); // tarihGonderen(root, augReading()); //
			 * augReading(); Scanner file = new Scanner(new
			 * File("C:\\Users\\asus\\Desktop\\people.txt")); String line; String[] parse =
			 * null; String[] date = null;
			 * 
			 * while (file.hasNextLine()) { line = file.nextLine(); parse = line.split(",");
			 * for (int i = 0; i < parse.length; i = i + 2) { //
			 * System.out.println(parse[i]); count = 0; tarihGonderen2(root, (I)(parse[i]));
			 * 
			 * }
			 * 
			 * }
			 */
			}

		}
	}
}
