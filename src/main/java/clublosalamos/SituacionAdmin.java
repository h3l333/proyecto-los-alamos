package clublosalamos;

public abstract class SituacionAdmin {
    private final String descSituacionAdmin;
    
    public SituacionAdmin(String descSituacionAdmin) {
        this.descSituacionAdmin = descSituacionAdmin;
    }

    public String getSituacionAdmin() {
        return descSituacionAdmin;
    }
}
