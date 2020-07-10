package Models;

public class Music {
    private int Id;
    private String name;
    private String downloadLink;
    private boolean isAnalysed;
    private int rank;

    public Music(int id, String name, String downloadLink, boolean isAnalysed, int rank) {
        this.Id = id;
        this.name = name;
        this.downloadLink = downloadLink;
        this.isAnalysed = isAnalysed;
        this.rank = rank;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public boolean isAnalysed() {
        return isAnalysed;
    }

    public void setAnalysed(boolean analysed) {
        isAnalysed = analysed;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
