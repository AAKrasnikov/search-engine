public class SearchIndex {
    private int page_id;
    private int lemma_id;
    private float rank;

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public int getLemma_id() {
        return lemma_id;
    }

    public void setLemma_id(int lemma_id) {
        this.lemma_id = lemma_id;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }
}
