import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test extends RedBlackTree{

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		RedBlackTree<Object> rbt = new RedBlackTree<Object>();
		//rbt.consoleUI();
		AugmentedRedBlackTree<Object> arbt = new AugmentedRedBlackTree<Object>();
		//arbt.consoleUI();
		
		while(true) {
			System.out.println("Red-Black Tree --> 1 ");
			System.out.println("Augmented Red-Black Tree --> 2 ");
			int choice = scan.nextInt();
			if(choice == 1){
				rbt.consoleUI();
			}
			else if(choice == 2) {
				arbt.consoleUI();
				//System.out.println("aug daha hazýl deðil");
			}
			
		}
		
		
	}

}
