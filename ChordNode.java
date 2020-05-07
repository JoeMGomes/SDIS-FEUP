public class ChordNode {

    private ChordNode[] finger;
    private ChordNode predecessor;
    private ChordNode successor;
    private static int mbits;
    private int key;

    public ChordNode() {

    }

    public void join(ChordNode n1) {
        this.predecessor = null;
        //TCP MESSAGE
        this.successor = n1.findSuccessor(this.getkey());
    }

    public void stabilize() {
        //TCP MESSAGE
        ChordNode x = successor.getPredecessor();

        if (x.getkey() > this.getkey() || x.getkey() < successor.getkey()) {
            successor = x;
        }
        //TCP MESSAGE
        successor.notifyNode(this);
    }

    public void notifyNode(ChordNode n1) {
        //TCP MESSAGE
        int n1Key = n1.getkey();

        if(this.predecessor == null || n1Key > predecessor.getkey() || n1Key < this.getkey()){
            this.predecessor = n1;
        }
    }

    public void fixFingers(){
        //TODO Dunno what is "next"

    }

    // TODO: Really an in id? Ver melhor
    public ChordNode findSuccessor(int id) {

        if (id > this.key || id <= successor.getkey()) {
            return successor;
        } else {
            ChordNode n1 = this.closestPrecedingNode(id);
            //TCP MESSAGE
            return n1.findSuccessor(id);
        }
    }

    private ChordNode closestPrecedingNode(int id) {

        for (int i = mbits; i > 0; i--) {
            // TODO: verificar se é >= ou só >
            if (finger[i].getkey() > this.getkey() || finger[i].getkey() < id)
                return finger[i];
        }

        return this;
    }

    // Getters and setters bellow this line
    public ChordNode[] getFingerTable() {
        return this.finger;
    }

    public void setFingerTable(ChordNode[] finger) {
        this.finger = finger;
    }

    public ChordNode getPredecessor() {
        return this.predecessor;
    }

    public void setPredecessor(ChordNode predecessor) {
        this.predecessor = predecessor;
    }

    public int getkey() {
        return this.key;
    }

    public void setkey(int key) {
        this.key = key;
    }
}