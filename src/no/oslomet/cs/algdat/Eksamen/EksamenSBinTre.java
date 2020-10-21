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

        Node<T> current = rot, next = null;
        int compare = 0;

        while (current != null) {
            next = current;
            compare = comp.compare(verdi, current.verdi);
            current = (compare < 0) ? current.venstre : current.høyre;
        }

        current = new Node<T>(verdi, next);

        if (next == null)
            rot = current;
        else if(compare < 0)
            next.venstre = current;
        else
            next.høyre = current;
        antall++;
        endringer++;
        return true;
    }

    public boolean fjern(T verdi) {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) rot = b;
            else if (p == q.venstre) q.venstre = b;
            else q.høyre = b;
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        int amount = 0;



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
            Node node = q.peek();
            q.poll();

            if (node.venstre != null) {
                q.add(node.venstre);
            } if (node.høyre != null) {
                q.add(node.høyre);
            }
        }
        antall--;
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
        Node<T> temp = p.forelder;
        if (p.forelder == null) return null;

        if (temp.høyre == p || temp.høyre == null) return temp;

        temp = temp.høyre;
        while (temp.venstre != null)
            temp = temp.venstre;

        return temp;

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

    private Node<T> findmin(Node<T> node) {
        Node<T> min = node;
        Node<T> next = min.høyre;
        int compare = 0;

        while (min != null) {
            compare = comp.compare(min.verdi, next.verdi);
            if (compare < 0 ) {
                min = next;
            }

            next = (compare < 0) ? next.venstre : next.høyre;
        }

        return node;
    }


} // ObligSBinTre
