package chawla.sanil.doctactproj.Homes.PatientFull;

import com.google.firebase.firestore.Exclude;

public class DocId {
    @Exclude
    public String DocId;
    public <T extends DocId> T withId(@android.support.annotation.NonNull final String id){
        this.DocId=id;
        return (T) this;
    }
}
