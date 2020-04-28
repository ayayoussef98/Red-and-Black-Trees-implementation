/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package red.blacktrees;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.max;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RedBlacktrees {

   public final int RED = 0;
   public  final int BLACK = 1;
    int DictSize=0;

    private class Node {

        String key = null ;
        int color = BLACK;
        Node left = nil, right = nil, parent = nil;

        Node(String key) {
            this.key = key;
        } 
    }
    
    private final Node nil  = new Node(null); 
    private Node root = nil;
    public void read() throws FileNotFoundException, IOException
	{
           Node node;
	   String word;
           FileReader file= new FileReader("rb.txt");
	   Scanner sc= new Scanner(file);
        while(sc.hasNextLine())
        {
            word=sc.nextLine(); 
            node = new Node(word);
            insert(node); 
        }
       
}
    
 
    private Node findNode(Node findNode, Node node) {
        if (node == nil)
        {
            return null;
        }
        
       int flag= findNode.key.compareToIgnoreCase(node.key);
        
        if (flag < 0) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (flag > 0) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (flag == 0) {
            return node;
        }
        return null;
    }
 public void sizeofDict(Node root)
    {
        if(root != nil)
        {  DictSize++;
           sizeofDict(root.left);
           sizeofDict(root.right);
        }
    }
    
    
    private void insert(Node node) throws IOException {
  
        Node temp = root;
        
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.key.compareToIgnoreCase(temp.key)<0) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key.compareToIgnoreCase(temp.key)>0) {
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
                    node = node.parent;
                    rotateLeft(node);
                } 
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
              
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
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;

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
        } else {
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
            } 
        else
        {
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }
 void replace(Node target, Node n){ 
          if(target.parent == nil){
              root = n;
          }else if(target == target.parent.left){
              target.parent.left = n;
          }else
              target.parent.right = n;
          n.parent = target.parent;
    }
    
    boolean delete(Node node) throws IOException{
        if((node = findNode(node, root))==null)return false;
        Node x;
        Node y = node;
        int yinitialcolor = y.color;
        
        if(node.left == nil){
            x = node.right;  
            replace(node, node.right);  
        }else if(node.right == nil){
            x = node.left;
            replace(node, node.left); 
        }else{
            y = treeMinimum(node.right);
            yinitialcolor = y.color;
            x = y.right;
            if(y.parent == node)
                x.parent = y;
            else{
                replace(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            replace(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color; 
        }
        if(yinitialcolor==BLACK)
            deleteFixup(x);  
       
        return true;
       
    }
    
    void deleteFixup(Node x){
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
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
    Node treeMinimum(Node subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
     public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==RED)?"Color: Red ":"Color: Black ")+"Key: "+node.key+" Parent: "+node.parent.key+"\n");
        printTree(node.right);
    }
     public int printTreeHeight(Node n){
        if(n==nil)
            return -1;
        else return (max(printTreeHeight(n.left),printTreeHeight(n.right))+1);
    }
     
    

    public void mainSwitch() throws IOException {
        Scanner scan = new Scanner(System.in);
        int f;
        while (true) {
            System.out.println("\n1.- Load items\n"
                    + "2.- Delete items\n"
                    + "3.- Check items\n"
                    + "4.- Print tree\n"
                    + "5.- Print Height\n"
                    + "6.- Insert word\n"
                    + "7.-Print dictionary size\n");
            Scanner scanm=new Scanner(System.in);
            int choice = scanm.nextInt();

            String item;
            Node node;
            switch (choice) {
                case 1:
                    root=nil;
                    try {
                        
                    read();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(RedBlacktrees.class.getName()).log(Level.SEVERE, null, ex);
                }  
                    printTree(root);
                    break;
                case 2:
                    Scanner sc=new Scanner(System.in);
                    item = sc.nextLine();
                    
                        node = new Node(item);
                        System.out.print("\nDeleting item " + item);
                        if (delete(node)) {
                            System.out.print(": deleted!\n");
                        } else {
                            System.out.print(": does not exist!\n");
                        }
                        
                    printTree(root);
                     f=printTreeHeight(root);
                    System.out.println("\nTree Height:"+f);
                    DictSize=0;
                      sizeofDict(root);
                    System.out.println("\nSize of Dictionary:"+DictSize+"\n");
                   
                    break;
                case 3:
                    Scanner sca=new Scanner(System.in);
                    item = sca.nextLine();
                        node = new Node(item);
                        if(findNode(node, root) != null)
                        System.out.println("found\n");
                        else
                            System.out.println("not found\n");
                       
                    
                    break;
                case 4:
                    printTree(root);
                    break;
                case 5:
                     f=printTreeHeight(root);
                    System.out.println(f);
                    break;
                case 6:
                    Scanner scanner=new Scanner(System.in);
                    item = scanner.nextLine();
                        node = new Node(item);
                        if(findNode(node, root) != null)
                        System.out.println("This word already exists\n");
                        else
                        {System.out.println("inserting word\n");
                    insert(node);
                    printTree(root);}
                        f=printTreeHeight(root);
                    System.out.println("\nTree Height:"+f+"\n");
                    DictSize=0;
                    sizeofDict(root);
                    System.out.println("Size of Dictionary:"+DictSize+"\n");
                        
                break;
                case 7:
                    DictSize=0;
                    sizeofDict(root);
                    System.out.println(DictSize+"\n");
                    break;
                 
            }
            
        }
    }
    /**
     * @param args the command line arguments
     */
public static void main(String[] args) throws IOException {
      
      RedBlacktrees redblack = new RedBlacktrees();
       redblack.mainSwitch();
    }
    /**
     * @param args the command line arguments
     */
    
}
