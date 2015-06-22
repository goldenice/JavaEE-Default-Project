package commons.entity;

import java.util.Date;

public interface SoftDeletable {

    public Date getDeletedOn();
    public void setDeletedOn(Date date);

}
