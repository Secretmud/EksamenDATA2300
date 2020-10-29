package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }


    /*
    * boolean leggInn(T verdi)
    *
    * We use a loop until we hit a null pointer, then we compare the values at the next pointer and use our compare
    * variable to check if we want to place the new node to the left or right of our current position.
    *
    * We then increase the antall variable so that we at all times can know how many nodes exists in our tree.
    *
    * @return boolean, we don't really use the return value. So beyond actual debugging I find this to be useless.
    * */
    public boolean leggInn(T verdi) {
        if (!verdi.equals(null)) {
            Node<T> current = rot, parent = null;
            int compare;
            boolean min = true;
            while (current != null) {
                parent = current;
                compare = comp.compare(verdi, current.verdi);
                current = (compare < 0) ? current.venstre : current.høyre;
                min = compare < 0;
            }
            current = new Node<>(verdi, parent);

            if (rot == null)
                rot = current;
            else if(min) {
                assert parent != null;
                parent.venstre = current;
            }
            else
                parent.høyre = current;
            antall++;
            endringer++;
            return true;
        } else return false;
    }


    /*
    * fjern(verdi)
    *
    * Firstly we iterate through the tree until we find the correct node.
    *
    * We then check if the node has 0, 1 or 2 children.
    *
    * Using a switch statement we go into the correct sub-method that handles the deletion of nodes.
    * */

    public boolean fjern(T verdi) {
        Node<T> current = rot;
        int children;
        while (current.verdi != verdi) {
            if (comp.compare(verdi, current.verdi) < 0)
                current = current.venstre;
            else
                current = current.høyre;
            if (current == null)
                return false;
        }
        if (current.venstre == null && current.høyre == null) children = 0;
        else if (current.høyre == null || current.venstre == null) children = 1;
        else children = 2;
        switch (children) {
            case 0:
                removeLeaf(current);
                return true;
            case 1:
                removeNodeWithOneChild(current);
                return true;
            case 2:
                removeNodeWithTwoChildren(current);
                return true;
            default:
                return false;
        }
    }

    /*
    * removeNodeWithTwoChildren(current)
    *
    * Using findmin(current) we get the node with the lowest value on the right side of the node tree.
    * we then append the left and right side of the parent node to the new tree.
    *
    * */

    private void removeNodeWithTwoChildren(Node<T> current) {
        Node<T> replacement = findmin(current);
        Node<T> left = current.venstre;
        Node<T> right = current.høyre;
        current = replacement;
        current.venstre = left;
        current.høyre = right;
        endringer++;
        antall--;
    }

    /*
    * removeNodeWithOneChild(current)
    *
    * I use two ternary operators here to figure out which, side I have to work with. This saves up a lot of code lines.
    *
    * */

    private void removeNodeWithOneChild(Node<T> current) {
        int cmp = (current.venstre != null) ? -1 : 1;
        Node<T> child = (cmp < 0) ? current.venstre : current.høyre;
        if (current == rot) {
            rot = child;
            child.forelder = null;
        } else {
            Node<T> parent = current.forelder;
            if (parent.venstre == current) {
                parent.venstre = child;
            } else {
                parent.høyre = child;
            }
            child.forelder = parent;
        }
        endringer++;
        antall--;
    }
    /*
     * removeLeaf(current)
     *
     * This is the simplest type of deletion in a binary tree. We're able to get a way with a small if-else if-else query
     * and a compare statement to check which of the parents pointers should be set to null.
     *
     * */
    private void removeLeaf(Node<T> current) {
        Node<T> parent = current.forelder;
        if (current == rot)
            rot = null;
        else if (comp.compare(current.verdi, parent.verdi) < 0)
            parent.venstre = null;
        else
            parent.høyre = null;
        endringer++;
        antall--;
    }

    /*
    * findmin(node)
    *
    * find the smallest node on the left side of the node given to the method.
    * */
    private Node<T> findmin(Node<T> node) {
        Node<T> current = node.høyre;
        Node<T> parent = node.forelder;

        if (current.venstre == null) {
            endringer++;
            parent.høyre = current.høyre;
            return current;
        }

        while (current.venstre != null)
            current = current.venstre;

        parent = current.forelder;

        parent.venstre = current.høyre;
        endringer++;
        return current;

    }


    /*
    * fjernAlle(verdi)
    *
    * Remove every instance of a value using fjern(verdi).
    *
    * By utilizing the fjern(verdi) method I can reuse code that I had already written. This saves a lot of time.
    *
    * */
    public int fjernAlle(T verdi) {
        if (rot == null) return 0;
        if (rot.høyre == null && rot.venstre == null) {
            rot = null;
            return 1;
        }
        int amount = 0;
        int count = antall(verdi);
        for (int i = 0; i < count; i++) {
            fjern(verdi);
            amount++;
        }

        return amount;
    }
    /*
    * antall(T verdi)
    *
    * count the amount of times a certain value is found in the binary tree.
    * Use the comparator to see which branch you should go to next.
    *
    *
    * */
    public int antall(T verdi) {

        int amount = 0;
        int compare;

        Node<T> current = rot;

        while (current != null) {
            compare = comp.compare(verdi, current.verdi);
            if (current.verdi.equals(verdi)) {
                amount++;
            }

            current = (compare < 0) ? current.venstre : current.høyre;
        }


        return amount;
    }

    /*
    * nullstill()
    *
    * I use a queue to add nodes to a LinkedList, I then use a while loop to remove every node.
    *
    * I then stop with setting rot = null.
    *
    * */
    public void nullstill() {
        if (rot == null)
            return;

        ArrayDeque<Node<T>> que = new ArrayDeque<>();
        que.add(rot);
        while (!que.isEmpty())
        {
            endringer++;
            antall--;
            Node<T> node = que.peek();
            que.poll();

            if (node.venstre != null) {
                que.add(node.venstre);
            } if (node.høyre != null) {
                que.add(node.høyre);
            }
        }

        rot = null;
    }

    /*
    * førstePostorden(p)
    *
    * Find the node that is one the farside of the left tree. This might not be the leftmost node.
    *
    * */
    private static <T> Node<T> førstePostorden(Node<T> p) {
        Node<T> temp = p;
        if (p.venstre == null && p.høyre == null) return p;
        while (true) {
            if (temp.venstre != null) temp = temp.venstre;
            else if (temp.høyre != null) temp = temp.høyre;
            else return temp;
        }
    }

    /*
    * nestePosorden()
    *
    * Find the successor of the current node, if the node.foreldre have a right node. then we have to iterate
    * through a while-loop to find the next postorden node. If there is nothing to the right or the parent.høyre is
    * the previous element, then we simply return the parent.
    *
    * */

    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> parent = p.forelder;
        if (parent == null) return null;

        if (parent.høyre != null && parent.høyre != p) {
            Node<T> current = parent.høyre;
            while (true) {
                if (current.venstre != null) current = current.venstre;
                else if (current.høyre != null) current = current.høyre;
                else break;
            }
            return current;
        } else {
            return parent;
        }
    }

    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> f = førstePostorden(rot);

        while (f != null) {
            oppgave.utførOppgave(f.verdi);
            f = nestePostorden(f);
        }

    }

    /*
    * posordenRecursive(oppgave)
    *
    * Uses the private recursive method with the same name, this executes oppgave.utførOppgave(p.verdi) in the correct postorder
    * format.
    *
    * If we placed the oppgave.utførOppgave() at the start we would have iterated preorder, at the center it would be
    * inorder, but since it's at the end we do it postorder.
    *
    * */
    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p.venstre != null) postordenRecursive(p.venstre, oppgave);
        if (p.høyre != null) postordenRecursive(p.høyre, oppgave);
        oppgave.utførOppgave(p.verdi);
    }

    /*
    * serialize()
    *
    * Return an ArrayList<T> that has all of the data we need using ArrayDeque and a size variable which keeps ut within a while loop until it reaches 0
    *
    * The entire building block is placed within a while that is running our que isn't empty.
    * */

    public ArrayList<T> serialize() {
        ArrayList<T> ret = new ArrayList<>();
        ArrayDeque<Node<T>> que = new ArrayDeque<>();

        que.add(rot);
        while (!que.isEmpty()) {

            int nc = que.size();

            while (nc > 0) {
                Node<T> n = que.peek();
                assert n != null;
                ret.add(n.verdi);
                que.remove();
                if (n.venstre != null)
                    que.add(n.venstre);
                if (n.høyre != null)
                    que.add(n.høyre);
                nc--;
            }
        }
        return ret;
    }
    /*
    * deserialize(data, c)
    *
    * We create a new binary tree object. and then loop over the arraylist data and add everything to the binarytree using the leggInn() from
    * task 1.
    * */
    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre<K> ret = new EksamenSBinTre<>(c);

        for (K i: data) {
            ret.leggInn(i);
        }

        return ret;
    }



} // ObligSBinTre
