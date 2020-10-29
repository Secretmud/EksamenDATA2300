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
    * if verdi == null, then we want to return false, as a null value isn't allowed.
    * through the function we create a node current and a node next which we use to check our values.
    *
    * We use a loop until we hit a null pointer, then we compare the values at the next pointer and use our compare
    * variable to check if we want to place the new node to the left or right of our current position.
    *
    * We then increase the antall variable so that we at all times can know how many nodes exists in our tree.
    *
    * @return boolean
    * */
    public boolean leggInn(T verdi) {
        if (verdi.equals(null))
            return false;

        Node<T> current = rot, parent = null;
        int compare = 0;
        boolean min = true;
        while (current != null) {
            parent = current;
            compare = comp.compare(verdi, current.verdi);
            current = (compare < 0) ? current.venstre : current.høyre;
            min = (compare < 0) ? true : false;
        }
        current = new Node<T>(verdi, parent);

        if (rot == null)
            rot = current;
        else if(min)
            parent.venstre = current;
        else
            parent.høyre = current;
        antall++;
        endringer++;
        return true;

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
    * remove_two_children(current)
    *
    *
    *
    * */

    private void removeNodeWithTwoChildren(Node<T> current) {
        Node<T> replacement = findmin(current);
        Node<T> left = current.venstre;
        Node<T> right = current.høyre;
        current = replacement;
        current.venstre = left;
        current.høyre = right;
        antall--;
    }

    private void removeNodeWithOneChild(Node<T> current) {
        int cmp = (current.venstre != null) ? -1 : 1;
        Node<T> child = (cmp < 0) ? current.venstre : current.høyre;
        if (current == rot) {
            rot = child;
            child.forelder = null;
        } else if (current != rot) {
            Node<T> parent = current.forelder;
            if (parent.venstre == current) {
                parent.venstre = child;
            } else {
                parent.høyre = child;
            }
            child.forelder = parent;
        }
        antall--;
    }

    private void removeLeaf(Node<T> current) {
        Node<T> parent = current.forelder;
        if (current == rot)
            rot = null;
        else if (comp.compare(current.verdi, parent.verdi) < 0)
            parent.venstre = null;
        else
            parent.høyre = null;
        antall--;
    }

    private Node<T> findmin(Node<T> node) {
        Node<T> current = node.høyre;
        Node<T> parent = node.forelder;

        if (current.venstre == null) {
            parent.høyre = current.høyre;
            return current;
        }

        while (current.venstre != null)
            current = current.venstre;

        parent = current.forelder;

        parent.venstre = (current.høyre != null) ? current.høyre : null;
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
        if (verdi.equals(null))
            return 0;

        int amount = 0;
        int compare = 0;

        Node<T> h = rot;

        while (h != null) {
            compare = comp.compare(verdi, h.verdi);
            if (h.verdi.equals(verdi)) {
                amount++;
            }

            h = (compare < 0) ? h.venstre : h.høyre;
        }


        return amount;
    }

    /*
    * nullstill() uses the private nullstill(Node<T> p) function to remove everything within the tree.
    * */
    public void nullstill() {
        // Base Case
        if (rot == null)
            return;

        // Create an empty queue for level order traversal
        Queue<Node> q = new LinkedList<Node>();

        // Do level order traversal starting from root
        q.add(rot);
        while (!q.isEmpty())
        {
            antall--;
            Node node = q.peek();
            q.poll();

            if (node.venstre != null) {
                q.add(node.venstre);
            } if (node.høyre != null) {
                q.add(node.høyre);
            }
        }

        rot = null;
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {
        Node<T> temp = p;
        if (p.venstre == null && p.høyre == null) return p;
        while (true) {
            if (temp.venstre != null) temp = temp.venstre;
            else if (temp.høyre != null) temp = temp.høyre;
            else return temp;
        }
    }

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

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p.venstre != null) postordenRecursive(p.venstre, oppgave);
        if (p.høyre != null) postordenRecursive(p.høyre, oppgave);
        oppgave.utførOppgave(p.verdi);
    }

    public ArrayList<T> serialize() {
        ArrayList<T> ret = new ArrayList<>();
        ArrayDeque<Node<T>> que = new ArrayDeque<>();

        que.add(rot);
        while (!que.isEmpty()) {

            int nc = que.size();
            if (nc == 0) break;

            while (nc > 0) {
                Node<T> n = que.peek();
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

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre<K> ret = new EksamenSBinTre<>(c);

        for (K i: data) {
            ret.leggInn(i);
        }

        return ret;
    }



} // ObligSBinTre
